package subkjh.bas.net.soproth;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.SoprothListener;

/**
 * SOcketPROcessTHread <br>
 * 
 * @author subkjh
 * 
 */
public abstract class Soproth implements Runnable {

	private static long soprothNoNext = 0;

	private synchronized static long getNextSoprothNo() {
		return ++soprothNoNext;
	}

	/** 지금까지 처리한 바이트 누적 수 */
	protected long countProcBytes;
	/** 시간표시 HH:mm:ss */
	protected final SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");
	/** 로거 */
	protected Logger logger = Logger.logger;
	/** 지금까지 받은 바이트 누적 수 */
	private long countRecvBytes;
	/** 큐에 들어온 내용을 처리할지 말지 여부. falase이면 처리 */
	private boolean doNothing;
	private String host;
	private boolean isContinue;
	private long mstimeRecvLast;
	private long mstimeSendLast;
	private long mstimeStart;
	private int port;
	private int portLocal;
	/** 받은 패킷이 쌓이는 큐 */
	private LinkedBlockingQueue<byte[]> queueRecvBytes;
	/** 몇초 동안 입력된 내용이 없을 경우 notifyNoData()를 호출합니다. */
	private int secNoData = -1;
	/** 사용되는 소켓 채널 */
	private SocketChannel socketChannel = null;
	/** 구분할 수 있는 ID */
	private String soprothId;
	protected SoprothListener soprothListener;
	/** 패킷처리자 고유번호 */
	private long soprothNo = 0;
	private Thread thread = null;

	/**
	 * 
	 */
	public Soproth() {
		logger = Logger.logger;
		queueRecvBytes = new LinkedBlockingQueue<byte[]>();
		soprothNo = getNextSoprothNo();

		isContinue = true;

		countRecvBytes = 0;
		countProcBytes = 0;
	}

	/**
	 * 스레드만 종료합니다.
	 */
	public void doNothing(String msg) {
		logger.info(msg);
		doNothing = true;
	}

	/**
	 * 
	 * @return 지금까지 처리한 바이트수
	 */
	public long getCountProcBytes() {
		return countProcBytes;
	}

	/**
	 * 
	 * @return 지금까지 받은 누적 바이트수
	 */
	public long getCountRecvBytes() {
		return countRecvBytes;
	}

	/**
	 * 
	 * @return 연결된 서버 주소
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 
	 * @return 연결된 서버의 포트
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @return 나의 포트
	 */
	public int getPortLocal() {
		return portLocal;
	}

	/**
	 * 
	 * @return 받은 바이트열을 가지고 있는 큐
	 */
	public LinkedBlockingQueue<byte[]> getQueueRecvBytes() {
		return queueRecvBytes;
	}

	/**
	 * 
	 * @return 채널
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * ID를 리턴합니다.
	 * 
	 * @return ID
	 */
	public String getSoprothId() {
		return soprothId != null ? soprothId : host + ":" + port;
	}

	/**
	 * 
	 * @return 리슨너
	 */
	public SoprothListener getSoprothListener() {
		return soprothListener;
	}

	/**
	 * 
	 * @return 패킷처리자 고유번호
	 */
	public long getSoprothNo() {
		return soprothNo;
	}

	/**
	 * 
	 * @return 실행시간, 최종받은시간, 최종보낸시간을 제공
	 */
	public String getStateTime() {
		return "C=" + HHMMSS.format(new Date(mstimeStart)) //
				+ "|S=" + HHMMSS.format(new Date(mstimeSendLast)) //
				+ "|R=" + HHMMSS.format(new Date(mstimeRecvLast));
	}

	/**
	 * 
	 * @return 특별히 남길 로그
	 * @since 2013.05.15
	 */
	public String getMsgLog() {
		return "";
	}

	/**
	 * 
	 * @return 처리하는 스레드가 살아있으면 true 그렇치 않으면 false
	 */
	public boolean isAlive() {
		return thread == null ? false : thread.isAlive();
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public long putReceivedBytes(byte bytes[]) {

		try {
			countRecvBytes += bytes.length;
			queueRecvBytes.put(bytes);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return countRecvBytes;
	}

	/**
	 * 큐에서 받은 바이트를 처리하기 전에 호출하는 메소드입니다.
	 * 
	 * @throws Exception
	 */
	protected void doSomething() throws Exception {

	}

	@Override
	public void run() {

		mstimeStart = System.currentTimeMillis();

		logger.info("Started.");

		if (soprothListener != null)
			soprothListener.onSoproth(SOPROTH_STATE.Started, this);

		byte bytes[];

		notifyThreadState(true);

		while (isContinue) {

			if (socketChannel == null || socketChannel.isOpen() == false)
				break;

			try {
				doSomething();
			} catch (Exception e) {
				logger.error(e);
			}

			if (doNothing) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				continue;
			}

			try {
				bytes = queueRecvBytes.poll(50, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				if (isContinue == false)
					break;
				bytes = null;
			}

			if (bytes != null) {

				if (doNothing) {
					queueRecvBytes.add(bytes);
					continue;
				}

				mstimeRecvLast = System.currentTimeMillis();

				try {
					process(bytes);
				} catch (InterruptedException e) {
				} catch (Exception e) {
					logger.error(e);
					break;
				}
			} else {
				if (secNoData > 0 && ((System.currentTimeMillis() - mstimeRecvLast) / 1000L) > secNoData) {
					notifyNoData();
				}
			}

		}

		notifyThreadState(false);
		stopSoproth("Thread stopped");
		logger.debug("Thread stopped");
	}

	/**
	 * 내 쓰레드 시작 여부를 알립니다.
	 * 
	 * @param run
	 *            true이면 실행됨, false이며 종료됨
	 */
	protected void notifyThreadState(boolean run) {

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public synchronized void send(byte bytes[]) throws Exception {
		mstimeSendLast = System.currentTimeMillis();
		int len = 0;
		int totalLen = 0;
		int offset;

		ByteBuffer byteBuffer = null;

		if (bytes != null && bytes.length > 0) {

			byteBuffer = ByteBuffer.wrap(bytes, 0, bytes.length);

			offset = 0;

			while (offset < bytes.length) {

				byteBuffer.position(offset);

				if (socketChannel == null)
					throw new Exception("Connection Closed");

				len = socketChannel.write(byteBuffer);

				if (len < 0)
					throw new Exception("Sent less than 0");

				if (len == 0) {
					socketChannel.socket().getOutputStream().flush();
					Thread.yield();
					continue;
				}

				totalLen += len;
				offset += len;

				if (totalLen >= bytes.length)
					break;

			}

			socketChannel.socket().getOutputStream().flush();
			Thread.yield();
		}

		if (byteBuffer != null)
			byteBuffer.clear();
		byteBuffer = null;

	}

	public void setPortLocal(int portLocal) {
		this.portLocal = portLocal;
	}

	public void setSecNoData(int secNoData) {
		this.secNoData = secNoData;
	}

	public void setSoprothId(String soprothId) {

		if (logger.isTrace()) {
			logger.trace("(" + this.soprothId + ") >> (" + soprothId + ")");
		}

		this.soprothId = soprothId;
		if (thread != null)
			thread.setName(soprothId);
	}

	public void setSoprothListener(SoprothListener soprothListener) {
		this.soprothListener = soprothListener;
	}

	/**
	 * 소프로스를 시작합니다.
	 * 
	 * @param soprothId
	 *            ID
	 * @param socketChannel
	 *            채널
	 * @param _logger
	 *            로거
	 * @return 실행여부
	 */
	public boolean startSoproth(String soprothId, SocketChannel socketChannel) {

		Socket socket = socketChannel.socket();
		host = socket.getInetAddress().getHostAddress();
		port = socket.getPort();
		this.socketChannel = socketChannel;

		logger.debug(host + ":" + port);

		if (soprothId != null)
			setSoprothId(soprothId);
		String threadName = getSoprothId();

		thread = new Thread(this);
		thread.setName(threadName);
		thread.start();

		try {
			initProcess();
		} catch (Exception e) {
			logger.error(e);
			stopSoproth(e.getMessage());
			return false;
		}

		return true;

	}

	/**
	 * 작업을 종료합니다.
	 */
	public synchronized void stopSoproth(String msg) {

		isContinue = false;

		// 큐에 있는 내용을 모두 처리할때까지 기다립니다.
		try {
			if (queueRecvBytes.size() > 0)
				Thread.sleep(1000);
		} catch (InterruptedException e1) {
		}

		if (socketChannel != null && socketChannel.isOpen()) {

			logger.info("finished [" + getSoprothId() + "] msg [" + msg + "]");

			try {
				socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (soprothListener != null)
				soprothListener.onSoproth(SOPROTH_STATE.Finished, this);

		}

		socketChannel = null;

	}

	@Override
	public String toString() {
		return getSoprothNo() + "|" + getSoprothId() + "|" + getClass().getSimpleName();
	}

	/**
	 * 큐에 있는 받은 바이트배열들을 모두 제거합니다.
	 */
	protected void clearQueueRecvBytes() {
		byte bytes[];

		while (true) {
			bytes = queueRecvBytes.poll();
			if (bytes == null)
				break;
		}
	}

	/**
	 * 
	 * @return 나의 IP주소
	 */
	protected String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * 소켓이 연결 후 호출되는 메소드입니다.
	 */
	protected abstract void initProcess() throws Exception;

	/**
	 * 다음 바이트 목록을 큐에서 가져옵니다.
	 * 
	 * @param timeout
	 *            타임아웃(초)
	 * @return 다음 byte
	 * @throws TimeoutException
	 */
	protected byte[] next(int timeout) throws TimeoutException {
		byte bytes[] = null;
		try {
			bytes = queueRecvBytes.poll(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}

		if (bytes == null)
			throw new TimeoutException(Lang.get("자료 읽는 중 타임아웃이 발생하였습니다."));
		return bytes;
	}

	/**
	 * 지정된 시간 동안 데이터가 없을 경우 호출됨<br>
	 * secNoData 변수 값을 참조합니다.
	 */
	protected void notifyNoData() {

	}

	/**
	 * 입력된 Byte를 처리하는 곳입니다.
	 * 
	 * @param bytes
	 * @throws Exception
	 *             예외가 발생되면 Socket을 닫고 종료합니다.
	 */
	protected abstract void process(byte bytes[]) throws Exception;

	protected byte[] mergeNext(byte bytesPrev[]) throws Exception {

		byte bytesTotal[];
		byte bytesNext[] = null;

		try {
			bytesNext = next(3);
		} catch (TimeoutException e1) {
			throw new Exception("No more bytes. current size " + bytesPrev.length + " (" + new String(bytesPrev) + ")");
		}

		bytesTotal = new byte[bytesPrev.length + bytesNext.length];
		System.arraycopy(bytesPrev, 0, bytesTotal, 0, bytesPrev.length);
		System.arraycopy(bytesNext, 0, bytesTotal, bytesPrev.length, bytesNext.length);

		return bytesTotal;

	}
}
