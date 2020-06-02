package fxms.nms.co.tl1;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.NetClient;
import subkjh.bas.net.co.NetClientTcp;
import subkjh.bas.net.co.vo.NetListener;

public class TL1Client implements Runnable, Loggable, NetListener {

	public static final String TL1_NET_STATE_TL1PduRecv = "TL1PduRecv";

	// public static void main(String[] args) throws Exception {
	//
	// Logger logger = Logger.createLogger(".", "a");
	// logger.setLevel(LOG_LEVEL.trace);
	//
	// NetListener listener = new NetListener() {
	//
	// @Override
	// public void onNetState(String arg0, Object arg1) {
	// }
	//
	// };
	//
	// // String host = "211.197.229.20";
	// // String host = "70.70.250.228";
	// String host = "167.1.21.79";
	// int port = 9032;
	//
	// if (args.length > 0)
	// host = args[0];
	// if (args.length > 1)
	// port = Integer.parseInt(args[1]);
	//
	// final TL1Client tl1 = new TL1Client("test", host, port, logger,
	// listener);
	//
	// tl1.open();
	// byte bytes[] = new byte[1024];
	// int len;
	//
	// String command;
	// while (true) {
	// len = System.in.read(bytes);
	// command = new String(bytes, 0, len);
	// tl1.send(command);
	//
	// }
	// // for (int i = 0; i < 1000; i++) {
	// // Thread.sleep(3000);
	// // tl1.send("RTRV-EQPT", "AdvTL1Sim", "SLOT-1", null, null);
	// // }
	// }

	private NetPduMakerTL1 pduMaker;
	private NetClientTcp<NetPduTL1> netClient;
	private String name;
	private String host;
	private int port;
	private Logger logger;
	private NetListener listener;
	private boolean isConnected = false;
	private Thread thread;
	/** correlation tag */
	private int ctag = 0;
	private long countRecv = 0;
	private String charset = "utf-8";

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public TL1Client(String name, String host, int port, Logger logger, NetListener listener, NetPduMakerTL1 pduMaker) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.logger = logger;
		this.listener = listener;
		this.pduMaker = pduMaker;
	}

	/**
	 * TL1클라이언트를 닫습니다.
	 * 
	 */
	public void close() {

		logger.info("Close TL1Client " + getName());

		if (pduMaker != null) {
			pduMaker.close();
			pduMaker = null;
		}

		if (netClient != null) {
			netClient.close();
			netClient = null;
		}

	}

	/**
	 * 소켓을 닫고 다시 연결하기 위한 메소드
	 */
	public void doReconnect() {
		if (netClient != null) {
			netClient.close();
			netClient = null;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public NetPduMakerTL1 getPduMaker() {
		return pduMaker;
	}

	public NetClient<NetPduTL1> getSocket() {
		return netClient;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return "recv=" + countRecv + (netClient == null ? "" : "|" + netClient.getState(level));
	}

	@Override
	public void onNetState(String state, Object obj) {

		onRecv(state, obj);

		if (NetListener.tcpConnected.equals(state)) {
			isConnected = true;
			logger.debug("{} {}", name, state);
		} else if (NetListener.tcpDisconnected.equals(state)) {
			isConnected = false;
			logger.debug("{} {}", name, state);
		}

	}

	/**
	 * TL1 클라이언트를 실행합니다.
	 */
	public void open() {

		logger.info("Open TL1Client " + getName());

		if (thread == null) {

			pduMaker.setListener(this);
			pduMaker.open();

			thread = new Thread(this);
			thread.setName(getName());
			thread.start();

		}
	}

	@Override
	public void run() {
		NetPduTL1 pdu;

		Thread.yield();

		while (pduMaker != null) {

			try {

				if (isConnected == false) {
					isConnected = connect();
					if (isConnected == false) {
						onRecv(NetListener.tcpConnectFail, null);
						Thread.sleep(5000);
						continue;
					}
				}

				if (netClient == null)
					continue;

				pdu = netClient.getQueuePdu().poll(5, TimeUnit.SECONDS);

				if (pdu == null)
					continue;

				countRecv++;

				onRecv(TL1_NET_STATE_TL1PduRecv, pdu);

			} catch (SocketTimeoutException e) {
				logger.fail(e.getClass().getSimpleName());
			} catch (InterruptedException e) {

			} catch (Exception e) {
				logger.error(e);
			}

		}
	}

	/**
	 * 
	 * @param command
	 * @return
	 * @throws Exception
	 */
	public int send(String command) throws Exception {
		logger.debug("SEND=[{}]", command);
		return netClient.send(command.getBytes(charset));
	}

	/**
	 * 
	 * cmd:tid:aid:ctag:[generalBlock:][paylog:];
	 * 
	 * @param cmd
	 * @param tid
	 * @param aid
	 * @param generalBlock
	 * @param payload
	 * @return correlation tag
	 * @throws Exception
	 */
	public int send(String cmd, String tid, String aid, String datas[]) throws Exception {
		int ctag = getNextCTag();
		StringBuffer sb = new StringBuffer();
		sb.append(cmd);
		sb.append(":").append(tid == null ? "" : tid);
		sb.append(":").append(aid == null ? "" : aid);
		sb.append(":").append(ctag);

		if (datas != null) {
			for (String s : datas) {
				sb.append(":").append(s == null ? "" : s);
			}
		}

		sb.append(";");

		if (netClient == null)
			throw new Exception("Not Connected Yet");

		logger.debug("SEND=[{}]", sb.toString());

		netClient.send(sb.toString().getBytes(charset));

		return ctag;
	}

	/**
	 * 네트워크 상태를 통보합니다.
	 * 
	 * @param state
	 *            상태
	 * @param obj
	 *            그떄의 값
	 */
	protected void onRecv(String state, Object obj) {
		if (listener != null) {
			listener.onNetState(state, obj);
		}
	}

	private boolean connect() throws Exception {

		if (netClient != null) {
			netClient.close();
			netClient = null;
		}

		netClient = new NetClientTcp<NetPduTL1>(name + "#Socket", pduMaker, this);

		try {
			logger.info("connecting... " + host + ":" + port);
			netClient.connect(host, port, 5000);
			logger.info("--- connected " + host + ":" + port + " ---");
			return true;
		} catch (SocketTimeoutException e) {
			logger.fail("timeout error {}:{}", host, port, e.getMessage());
		} catch (ConnectException e) {
			logger.fail("connection error {}:{}", host, port, e.getMessage());
		} catch (Exception e) {
		}

		return false;
	}

	private synchronized int getNextCTag() {

		ctag++;

		if (ctag > 999999) {
			ctag = 1;
		}

		return ctag;
	}

}
