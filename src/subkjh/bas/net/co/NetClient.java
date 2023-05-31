package subkjh.bas.net.co;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Loggable;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.pdumaker.NetPduMaker;
import subkjh.bas.net.co.vo.CountComm;
import subkjh.bas.net.co.vo.NetListener;
import subkjh.bas.net.co.vo.NetPdu;
import subkjh.bas.net.co.vo.RecvBytes;

/**
 * 네트워크에서 사용할 클라이언트
 * 
 * @author subkjh
 * 
 * @param <PDU>
 *            PDU 종류
 */
public abstract class NetClient<PDU extends NetPdu> implements Runnable, Loggable, NetSender<PDU> {

	protected Logger logger = Logger.logger;
	protected Selector selector;
	protected NetListener listener;
	private NetPduMaker<PDU> pduMaker;
	private Thread thread;
	private boolean isContinue;
	private String name;
	private CountComm countComm;
	private String host;
	private int port;

	/**
	 * 
	 * @param name
	 * @param pduMaker
	 */
	public NetClient(String name, NetPduMaker<PDU> pduMaker) {
		this.name = name;
		this.pduMaker = pduMaker;
		this.listener = pduMaker.getListener();
		logger = pduMaker.getLogger();
		countComm = new CountComm();
	}

	public NetClient(String name, NetPduMaker<PDU> pduMaker, NetListener listener) {
		this(name, pduMaker);
		this.listener = listener;
	}

	/**
	 * 사용을 종료합니다.
	 */
	public void close() {

		logger.trace("closing");

		isContinue = false;

		if (thread != null) {
			thread.interrupt();
		}

		closeChannel();

		if (selector != null) {
			try {
				selector.close();
				selector = null;
				logger.info("close selector");
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 
	 * @param host
	 *            연결할 호스트
	 * @param port
	 *            연결할 포트
	 * @param timeoutMs
	 *            타임아웃(ms)
	 * @throws Exception
	 */
	public void connect(String host, int port, int timeoutMs) throws ConnectException, SocketTimeoutException, Exception {

		logger.trace("{}:{} connecting...", host, port);

		_connect(host, port, timeoutMs);

		this.host = host;
		this.port = port;

		thread = new Thread(this);
		thread.setName(getName());
		thread.start();

	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return 받은 패킷을 변환한 PDU가 적재되는 큐
	 */
	public LinkedBlockingQueue<PDU> getQueuePdu() {
		return pduMaker.getPduQueue();
	}

	/**
	 * 네트워크 상태를 통보합니다.
	 * 
	 * @param state
	 *            상태
	 * @param obj
	 *            그떄의 값
	 */
	protected void notify(String state, Object obj) {
		if (listener != null) {
			listener.onNetState(state, obj);
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return countComm + "";
	}

	@Override
	public void run() {

		logger.info("start" + " " + host + ":" + port);

		notify(NetListener.tcpConnected, getName());

		isContinue = true;
		Iterator<?> selectedKeys;
		SelectionKey key = null;
		String keyState;
		RecvBytes recvBytes;

		CLOSE: while (isContinue) {

			try {
				selector.select();
				selectedKeys = selector.selectedKeys().iterator();

				while (selectedKeys.hasNext()) {

					key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isReadable()) {
						try {

							recvBytes = read(key);
							if (recvBytes == null)
								continue;

							countComm.addRecv(1);

							pduMaker.putRecvBytes(recvBytes);

						} catch (NotYetConnectedException e) {
						} catch (Exception e) {
							logger.error(e);
							break CLOSE;
						}
					}
				}
			} catch (java.nio.channels.CancelledKeyException e) {
				if (isContinue) {
					logger.error(e);
				}
				break;
			} catch (java.nio.channels.ClosedSelectorException e) {
				if (isContinue) {
					logger.error(e);
				}
				break;
			} catch (Exception e) {
				logger.error(e);
			}
		}

		try {
			close();
		} catch (Exception e) {
			logger.error(e);
		}

		notify(NetListener.tcpDisconnected, getName());

		logger.info("finished " + getState(LOG_LEVEL.trace));
	}

	/**
	 * 입력된 바이트 배열을 보냅니다.
	 * 
	 * @param bytes
	 *            보낼 바이트배열
	 * @return 보낸 수
	 * @throws Exception
	 */
	public int send(byte bytes[]) throws Exception {

		int len = _send(bytes);

		if (len > 0)
			countComm.addSend(1);

		return len;

	}

	@Override
	public int send(PDU pdu) throws Exception {
		return send(pdu.getBytes());
	}

	/**
	 * 서버에 접속합니다.
	 * 
	 * @param host
	 *            접속할 호스트
	 * @param port
	 *            접속할 포트
	 * @param timeoutMs
	 *            타임아웃(ms)
	 * @throws Exception
	 */
	protected abstract void _connect(String host, int port, int timeoutMs) throws Exception;

	/**
	 * 실제 보내는 기능 구현
	 * 
	 * @param bytes
	 *            연결된 서버에 보낼 바이트배열
	 * @return 보낸 바이트 수
	 * @throws Exception
	 */
	protected abstract int _send(byte bytes[]) throws Exception;

	/**
	 * 채널을 닫습니다.
	 */
	protected abstract void closeChannel();

	/**
	 * 셀렉션 키로부터 바이트배열을 읽습니다.
	 * 
	 * @param key
	 *            셀렉션 키
	 * @return 받은 바이트배열
	 * @throws Exception
	 */
	protected abstract RecvBytes read(SelectionKey key) throws Exception;

}
