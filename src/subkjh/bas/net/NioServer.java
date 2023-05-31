package subkjh.bas.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.soproth.SOPROTH_STATE;
import subkjh.bas.net.soproth.Soproth;

/**
 * 
 * @author subkjh
 * 
 * @param <SOPROTH>
 */
public class NioServer<SOPROTH extends Soproth> implements Runnable, SoprothListener, Loggable {

	class SoprothStarter extends Thread {
		private SocketChannel socketChannel;
		private SOPROTH soproth;

		SoprothStarter(SOPROTH soproth, SocketChannel socketChannel) {
			this.soproth = soproth;
			this.socketChannel = socketChannel;
		}

		public void run() {
			setName(socketChannel.toString());
			String soprothId = socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort();
			boolean bret = soproth.startSoproth(soprothId, socketChannel);
			if (bret == false) {
				soproth.stopSoproth("Starting Error");
			}
		}
	}

	public static final SimpleDateFormat MMDDHHMMSS = new SimpleDateFormat("MM.dd HH:mm:ss");

	/** 로거 */
	protected Logger logger = Logger.logger;
	private Class<SOPROTH> classOfSoproth;
	private boolean isContinue;
	private Map<SocketChannel, SOPROTH> mapSoproth;
	private long mstimeStarted;
	private String name;
	private int port;
	/** selector */
	private Selector selector;
	private ServerSocketChannel serverSocketChannel = null;
	private Thread thread;
	/**
	 * 블랙리스트 IP<br>
	 * 이 IP에서 Accept되면 모두 버립니다.
	 */
	private List<String> blackList;
	private int countAcceptMax = 200;
	private ByteBuffer readBuffer;
	private boolean reuseAddr = true;

	/**
	 * 
	 * @param logger
	 */
	public NioServer() {
		mapSoproth = Collections.synchronizedMap(new HashMap<SocketChannel, SOPROTH>());
		blackList = new ArrayList<String>();
		readBuffer = ByteBuffer.allocate(4096);
	}

	public void addBlackList(String ip) {
		if (blackList.contains(ip) == false) {
			blackList.add(ip);
		}
	}

	/**
	 * 입력된 bytes열을 연결된 모든 클라이언트에게 전달합니다.
	 * 
	 * @param bytes
	 *            보낼 바이트열
	 * @throws Exception
	 */
	public void broadcast(final byte bytes[]) throws Exception {

		List<SOPROTH> list = getSoprothList();

		for (final Soproth soproth : list) {

			// 2013.08.28 by subkjh : 스레드를 통해서 보내는 방식으로 변경함.
			new Thread() {
				public void run() {
					try {
						soproth.send(bytes);
					} catch (Exception e) {
						logger.fail(soproth.getSoprothId() + " : " + e.getMessage());
						soproth.stopSoproth("broadcast fail [" + e.getMessage() + "]");
					}
				}
			}.start();
		}
	}

	/**
	 * 
	 * @return 연결된 수
	 */
	public int getCountAccepted() {
		return mapSoproth.size();
	}

	public int getCountAcceptMax() {
		return countAcceptMax;
	}

	/**
	 * 
	 * @return 시작일시
	 */
	public long getMstimeStarted() {
		return mstimeStarted;
	}

	/**
	 * 
	 * @return 바인딩한 포트
	 */
	public int getPort() {
		if (port > 0)
			return port;
		return serverSocketChannel.socket().getLocalPort();
	}

	public Selector getSelector() {
		return selector;
	}

	public SOPROTH getSoproth(String soprothId) {

		List<SOPROTH> listSoproth = getSoprothList();
		if (listSoproth.size() > 0) {
			for (SOPROTH soproth : listSoproth) {
				if (soproth.getSoprothId() != null && soproth.getSoprothId().equals(soprothId)) {
					return soproth;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return 연결된 Soproth 목록
	 */
	public List<SOPROTH> getSoprothList() {

		List<SOPROTH> listSoproth = new ArrayList<SOPROTH>();

		SocketChannel keyArray[] = mapSoproth.keySet().toArray(new SocketChannel[mapSoproth.size()]);
		SOPROTH soproth;

		for (SocketChannel key : keyArray) {
			soproth = mapSoproth.get(key);
			if (soproth != null) {
				if (soproth.getSocketChannel() != null && soproth.isAlive()) {
					listSoproth.add(soproth);
				}
			}
		}

		return listSoproth;
	}

	/**
	 * 
	 * @return Soproth 상태 목록
	 */
	public List<String> getSOPROTH_STATEList() {

		List<String> listClient = new ArrayList<String>();

		for (Soproth soproth : getSoprothList()) {
			listClient.add(soproth.getStateTime() + "  " + soproth.getSoprothId() + " " + soproth.getMsgLog());
		}

		return listClient;
	}

	// @Override
	// public String getState(int level) {
	// List<String> list = getSOPROTH_STATEList();
	//
	// String msg = port + " " + MMDDHHMMSS.format(new Date(getMstimeStarted()))
	// + " BL[" + blackList.size() + "]"
	// + " Connection[" + getCountAccepted() + "/" + countAcceptMax + "]"
	// + (thread != null ? " thread=" + thread.isAlive() : "");
	//
	// if (list != null && list.size() > 0)
	// msg += "\n" + Const.DISP_STR + Const.getString(list);
	//
	// return msg;
	// }

	public boolean isReuseAddr() {
		return reuseAddr;
	}

	@Override
	public void onSoproth(SOPROTH_STATE soprothState, Soproth soproth) {

		logger.debug("[" + soproth + "] = " + soprothState);

		if (soprothState == SOPROTH_STATE.Finished) {
			try {
				mapSoproth.remove(soproth.getSocketChannel());
			} catch (Exception e) {
			}
		}

	}

	@Override
	public void run() {

		logger.info("Started.");

		SelectionKey key = null;
		// String keyState;
		isContinue = true;

		while (isContinue) {

			try {
				selector.select();

				Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {

					key = selectedKeys.next();
					selectedKeys.remove();

					// if (logger.isTrace()) {
					// keyState = key.toString();
					// if (key.isAcceptable())
					// keyState += " isAcceptable";
					// if (key.isConnectable())
					// keyState += " isConnectable";
					// if (key.isReadable())
					// keyState += " isReadable";
					// if (key.isWritable())
					// keyState += " isWritable";
					// if (keyState.length() > 0)
					// logger.raw(keyState.trim() + ", " + key.isValid());
					// }

					if (key.isValid() == false)
						continue;

					if (key.isReadable()) {
						read(key);
					} else if (key.isAcceptable()) {
						accept(key);
					}
				}
			} catch (java.nio.channels.CancelledKeyException e) {
				stopSoproth(key, "key cancelled");

				// 2014.04.28 by subkjh
				// break;
			} catch (java.nio.channels.ClosedSelectorException e) {
				stopSoproth(key, "closed selector");
				logger.debug("break");
				break;
			} catch (Exception e) {
				logger.error(e);
				logger.debug("break");
				break;
			}

		}

		try {
			if (serverSocketChannel != null) {
				serverSocketChannel.close();
				serverSocketChannel = null;
			}
		} catch (Exception e) {
		}

		try {
			if (selector != null) {
				selector.close();
				selector = null;
			}
		} catch (Exception e) {
		}

		if (logger != null)
			logger.info("finished");

	}

	public void setCountAcceptMax(int countAcceptMax) {
		this.countAcceptMax = countAcceptMax;
	}

	public void setReuseAddr(boolean reuseAddr) {
		this.reuseAddr = reuseAddr;
	}

	@SuppressWarnings("unchecked")
	public void startServer(String name, String host, int port, Class<? extends Soproth> _classOfSoproth)
			throws Exception {

		mstimeStarted = System.currentTimeMillis();
		this.classOfSoproth = (Class<SOPROTH>) _classOfSoproth;
		this.name = name;
		this.port = port;

		classOfSoproth.newInstance(); // 존재 여부 확인용

		logger.debug("binding " + port);

		selector = Selector.open();

		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().setReuseAddress(reuseAddr);
			if (host == null) {
				serverSocketChannel.socket().bind(new InetSocketAddress(port));
			} else {
				serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
			}
		} catch (Exception e) {
			logger.error(e);
			selector.close();
			throw e;
		}

		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		mapSoproth.clear();

		logger.debug("binding ok");

		thread = new Thread(this);
		thread.setName(name);
		thread.start();
	}

	public void stopServer() {
		isContinue = false;
		stopSoprothAll();
		thread.interrupt();
	}

	/**
	 * 연결된 모든 Soproth를 종료합니다.
	 */
	public void stopSoprothAll() {
		for (Soproth soproth : getSoprothList()) {
			soproth.stopSoproth("stopSoprothAll");
		}

		mapSoproth.clear();
	}

	/**
	 * 
	 * @param socketChannel
	 * @return
	 */
	protected SOPROTH getConnectionBean(SocketChannel socketChannel) {
		return mapSoproth.get(socketChannel);
	}

	@SuppressWarnings("unchecked")
	protected SOPROTH makeSoproth() throws Exception {

		Object obj = classOfSoproth.newInstance();
		if (obj instanceof Soproth) {
			SOPROTH soproth = (SOPROTH) obj;
			// soproth.setLogger(logger);
			return soproth;
		}

		throw new Exception(Lang.get("처리 스레드를 생성하지 못하였습니다.") + " (" + classOfSoproth.getName() + ")");
	}

	/**
	 * 
	 * @param key
	 */
	private void accept(SelectionKey key) {
		SocketChannel socketChannel = null;
		try {

			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			socketChannel = ssc.accept();
			if (socketChannel == null)
				return;

			String host = socketChannel.socket().getInetAddress().getHostAddress();
			int port = socketChannel.socket().getPort();

			if (isIp4BlackList(host)) {
				if (logger.isTrace()) {
					logger.fail("[" + host + "] is in the black list");
				}
				try {
					socketChannel.close();
				} catch (Exception e) {
					logger.error(e);
				}
				return;
			}

			if (countAcceptMax > 0 && getCountAccepted() >= countAcceptMax) {
				logger.fail(countAcceptMax + "/" + getCountAccepted() + "[" + host + "] exceed the limit");
				try {
					socketChannel.close();
				} catch (Exception e) {
					logger.error(e);
				}
				return;
			}

			logger.debug("Client Connected {} {}", host, port);

			socketChannel.configureBlocking(false);

			SOPROTH soproth = makeSoproth();
			soproth.setSoprothListener(this);
			soproth.setPortLocal(getPort());

			// 1. 먼저 읽을 수 있도록 설정한 후에
			try {
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				logger.error(e);
				stopSoproth(key, e.getMessage());
			}

			// 목록에 미리 등록합니다.
			mapSoproth.put(socketChannel, (SOPROTH) soproth);

			// 2. 연결되었음을 알립니다.
			new SoprothStarter(soproth, socketChannel).start();

			if (logger.isTrace()) {
				logger.trace(socketChannel.toString());
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * 블랙리스트 IP 여부
	 * 
	 * @param ip
	 *            비교 IP
	 * @return
	 * @since 2014.02.17 by subkjh
	 */
	private boolean isIp4BlackList(String ip) {

		if (blackList == null || blackList.size() == 0)
			return false;

		return blackList.contains(ip);
	}

	private void read(SelectionKey key) {

		try {

			SocketChannel socketChannel = (SocketChannel) key.channel();

			Soproth soproth = mapSoproth.get(socketChannel);
			if (soproth == null) {

				// 2013.08.22 by subkjh
				socketChannel.close();
				return;
			}

			Object soprothId = soproth.getSoprothId();

			// Clear out our read buffer so it's ready for new data
			readBuffer.clear();

			// Attempt to read off the channel
			int numRead;
			try {
				numRead = socketChannel.read(readBuffer);
			} catch (IOException e) {
				// The remote forcibly closed the connection, cancel the
				// selection key and close the channel.
				key.cancel();
				stopSoproth(key, "read error > " + e.getMessage());
				return;
			}

			if (numRead == -1) {
				// Remote entity shut the socket down cleanly. Do the same from
				// our end and cancel the channel.
				stopSoproth(key, "read length = -1");
				key.cancel();
				return;
			}

			if (numRead == 0) {
				return;
			}

			try {

				byte bytes[] = new byte[numRead];
				readBuffer.rewind();
				readBuffer.get(bytes);

				long len = soproth.putReceivedBytes(bytes);
				// if (logger.isRaw()) {
				// logger.raw("[" + soprothId + "] read length=" + numRead + "|"
				// + len);
				// }

			} catch (Exception e) {
				logger.error(e);
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Soproth를 종료하고 맵에서 삭제합니다.
	 * 
	 * @param soproth
	 * @param msg
	 */
	private void stopSoproth(SelectionKey key, String msg) {

		SocketChannel socketChannel = (SocketChannel) key.channel();
		Soproth soproth = mapSoproth.remove(socketChannel);

		if (soproth == null) {
			return;
		}

		soproth.stopSoproth(msg);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

}
