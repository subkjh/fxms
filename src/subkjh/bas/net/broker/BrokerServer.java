package subkjh.bas.net.broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;

/**
 * TCP Broker
 * 
 * @author subkjh
 * @since 2009-12-16
 */
public class BrokerServer implements Runnable {

	public static void main(String[] args) {
		int port;
		String targetHost;
		int targetPort;
		String home = null;
		String level = "info";

		port = Integer.parseInt(args[0]);
		targetHost = args[1];
		targetPort = Integer.parseInt(args[2]);
		home = args.length > 3 ? args[3] : null;
		level = args.length > 4 ? args[4] : null;

		Logger.logger = new Logger(home, "broker");
		Logger.logger.setMaxBackupFileCount(3);

		BrokerServer broker = new BrokerServer(null);
		try {
			broker.startBroker(null, port, targetHost, targetPort);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** TcpRelay 번호 */
	private long index = 0;

	/** TcpRelay Lock Object */
	private Object listObj = new Object();

	/** 로거 */
	private Logger logger;

	private String name;

	/** 서버 소켓 */
	private ServerSocket serverSocket;

	/** 기본 연결 호스트 */
	private String targetHost;

	/** 기본 연결 호스트 포트 */
	private int targetPort;

	/** TcpRelay 목록 */
	private List<TcpRelay> tcpRelayList;

	private Thread thread;

	/** 계속 진행 여부 */
	private boolean toContinue;

	public BrokerServer(String name) {
		this.name = (name == null ? getClass().getSimpleName() : name);
		tcpRelayList = new ArrayList<TcpRelay>();
		logger = Logger.logger;
	}

	public Logger getLogger() {
		return logger;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		StringBuffer sb = new StringBuffer();
		List<TcpRelay> tcpRelayList = getTcpRelays();
		if (tcpRelayList == null || tcpRelayList.size() == 0) {
			return "";
		} else {
			for (TcpRelay tcpRelay : tcpRelayList) {
				sb.append(" ");
				sb.append(tcpRelay.getTcpRelayName());
			}
			return sb.substring(1);
		}
	}

	/**
	 * 연결되어 있는 TcpRelay 목록을 제공합니다.
	 * 
	 * @return 연결된 TcpRelay
	 */
	public List<TcpRelay> getTcpRelays() {
		List<TcpRelay> list = new ArrayList<TcpRelay>();
		synchronized (listObj) {
			for (int i = tcpRelayList.size() - 1; i >= 0; i--) {
				if (tcpRelayList.get(i).isAlive())
					list.add(tcpRelayList.get(i));
				else
					tcpRelayList.remove(i);
			}
		}
		return list;
	}

	@Override
	public void run() {

		toContinue = true;
		logger.info("started");
		long ptime = System.currentTimeMillis();

		while (toContinue) {

			try {
				final Socket readSocket = serverSocket.accept();
				if (readSocket == null)
					continue;

				new Thread() {
					public void run() {
						long index = getIndex();
						setName("Temp-" + index);
						Socket sendSocket;
						try {
							sendSocket = init(readSocket);
							TcpRelay relay = new TcpRelay(readSocket, sendSocket, logger);
							relay.startRelay();
							add(relay);
						} catch (Exception e) {
							logger.error(e);
						}

					}
				}.start();
				ptime = System.currentTimeMillis();
			} catch (SocketTimeoutException e) {
				if (System.currentTimeMillis() > ptime + 600 * 1000L) {
					logger.info("SocketTimeoutException");
					ptime = System.currentTimeMillis();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}

		logger.info("finished");

	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * 
	 * @param port
	 * @param targetHost
	 * @param targetPort
	 * @throws Exception
	 */
	public void startBroker(int port, String targetHost, int targetPort) throws Exception {
		startBroker(null, port, targetHost, targetPort);
	}

	/**
	 * TCP 브로커를 시작합니다.
	 * 
	 * @param host
	 *            localhost<br>
	 *            기본적으로 null입니다.
	 * @param port
	 * @param targetHost
	 * @param targetPort
	 * @throws Exception
	 */
	public void startBroker(String host, int port, String targetHost, int targetPort) throws Exception {
		this.targetHost = targetHost;
		this.targetPort = targetPort;

		serverSocket = new ServerSocket();
		serverSocket.setSoTimeout(5000);
		if (host == null) {
			serverSocket.bind(new InetSocketAddress(port));
		} else {
			serverSocket.bind(new InetSocketAddress(host, port));
		}
		
		Logger.logger.info(Logger.makeString(port, targetHost + ":" + targetPort));

		thread = new Thread(this);
		thread.setName(name);
		thread.start();
	}

	/**
	 * 모든 TcpRelay를 제거합니다.
	 */
	public void stopAllRelays() {
		List<TcpRelay> relays = getTcpRelays();
		for (TcpRelay relay : relays) {
			relay.stopRelay();
		}
	}

	/**
	 * 브로커를 종료합니다.
	 */
	public void stopBroker() {
		toContinue = false;
		if (thread != null)
			thread.interrupt();
		stopAllRelays();
	}

	private void add(TcpRelay tcpRelay) {
		synchronized (listObj) {
			tcpRelayList.add(tcpRelay);
		}
	}

	private synchronized long getIndex() {
		return ++index;
	}

	/**
	 * 기본 호스트가 아니고 다른 호스트인 경우 이 메소드를 Override합니다.<br>
	 * 입력된 socket으로부터 주소나 포트를 받을 필요가 있습니다.<br>
	 * 
	 * @param socket
	 *            원
	 * @return 타켓과 연결된 소켓
	 * @throws Exception
	 */
	protected Socket init(Socket socket) throws Exception {
		Socket targetSocket = new Socket();
		targetSocket.connect(new InetSocketAddress(targetHost, targetPort));
		return targetSocket;
	}
}
