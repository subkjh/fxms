package subkjh.bas.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.soproth.SOPROTH_STATE;
import subkjh.bas.net.soproth.Soproth;

public class NioClient implements Runnable, SoprothListener, Loggable {

	private String host;
	private boolean isContinue;
	private Logger logger = Logger.logger;
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;
	private Soproth soproth;
	private Thread thread;
	private long totalRecvDataSize = 0;
	private long mstimeLastRecv;
	private ByteBuffer readBuffer;
	private Boolean keepAlive = null;

	/**
	 * 
	 * @param logger
	 */
	public NioClient() {
		readBuffer = ByteBuffer.allocate(4096);
	}

	public String getHost() {
		return host;
	}

	@Override
	public String getName() {
		return thread == null ? "" : thread.getName();
	}

	public int getPort() {
		return port;
	}

	public Selector getSelector() {
		return selector;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("host=");
		sb.append(host);
		sb.append("port=");
		sb.append(port);
		sb.append("last-read-time=");
		sb.append(mstimeLastRecv);
		sb.append("read-data-size=");
		sb.append(totalRecvDataSize);

		return sb.toString();
	}

	public boolean isAlive() {
		return thread == null ? false : thread.isAlive();
	}

	@Override
	public void onSoproth(SOPROTH_STATE soprothState, Soproth soproth) {
		logger.debug(soproth + " = " + soprothState);
		if (soprothState == SOPROTH_STATE.Finished) {
			this.stopClient(soprothState.name());
		}
	}

	@Override
	public void run() {

		logger.info("Started." + "[" + host + ":" + port + "]");

		SelectionKey key = null;
		// String keyState;
		isContinue = true;

		while (isContinue) {
			try {

				if (soproth == null)
					break;

				selector.select();

				Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {

					key = selectedKeys.next();
					selectedKeys.remove();

					// if (logger.isRaw()) {
					// keyState = "";
					// if (key.isAcceptable())
					// keyState += " isAcceptable";
					// if (key.isConnectable())
					// keyState += " isConnectable";
					// if (key.isReadable())
					// keyState += " isReadable";
					// if (key.isWritable())
					// keyState += " isWritable";
					// if (keyState.length() > 0)
					// logger.raw(keyState.trim());
					// }

					if (key.isValid() == false)
						continue;

					if (key.isReadable()) {
						read(key);
					}

				}

			} catch (java.nio.channels.CancelledKeyException e) {
				// logger.error(e);
				stopClient("key cancelled");
				break;
			} catch (java.nio.channels.ClosedSelectorException e) {
				logger.error(e);
				stopClient("closed selector");
				break;
			} catch (Exception e) {
				logger.error(e);
				break;
			}
		}

		if (soproth != null) {
			soproth.stopSoproth("thread is down");
			soproth = null;
		}

		try {
			if (socketChannel != null) {
				socketChannel.close();
				socketChannel = null;
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

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	/**
	 * 읽기를 시작합니다.<br>
	 * 읽은 내용을 처리하는 Soproth도 여기에서 실행합니다.
	 * 
	 * @param threadName
	 *            이름
	 * @param host
	 *            접속서버주소
	 * @param port
	 *            접속서버포트
	 * @param soproth
	 *            받은 패킷을 처리할 Soproth
	 * @throws Exception
	 */
	public void startClient(String threadName, String host, int port, Soproth soproth) throws Exception {

		if (thread != null)
			throw new Exception(Lang.get("이미 실행중입니다."));

		logger.trace("connecting " + host + ":" + port);

		this.soproth = soproth;
		this.host = host;
		this.port = port;
		this.soproth.setSoprothListener(this);

		selector = Selector.open();

		try {
			socketChannel = SocketChannel.open(new InetSocketAddress(host, port));

			// 2016.06.24 by subkjh
			if (keepAlive != null) {
				socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, keepAlive);
			}

			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {
			logger.error(e);
			selector.close();
			throw e;
		}

		logger.debug("connected " + host + ":" + port);

		thread = new Thread(this);
		if (threadName != null)
			thread.setName(threadName);
		else
			thread.setName(soproth.getSoprothId() == null ? host + ":" + port : soproth.getSoprothId().toString());
		thread.start();

		soproth.startSoproth(null, socketChannel);
	}

	/**
	 * 
	 * @param msg
	 */
	public void stopClient(String msg) {

		if (soproth != null) {
			soproth.stopSoproth(msg);
			soproth = null;
		}

		isContinue = false;

		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	private void read(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = socketChannel.read(readBuffer);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel the selection
			// key and close the channel.
			key.cancel();
			stopClient("read error > " + e.getMessage());
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the same from our
			// end and cancel the channel.
			key.cancel();
			stopClient("read length = -1");
			return;
		}

		if (numRead <= 0)
			return;

		try {

			byte bytes[] = new byte[numRead];
			readBuffer.rewind();
			readBuffer.get(bytes);

			long len = soproth.putReceivedBytes(bytes);

			if (len > 0) {
				totalRecvDataSize += len;
			}

			// if (logger.isRaw()) {
			// logger.raw("read length=" + numRead + "|" + len);
			// }

			mstimeLastRecv = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error(e);
		}

	}

}
