package subkjh.bas.net.co.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.vo.NetListener;

/**
 * Telnet/Ssh 접속 에뮬레이터 공통 부분
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public abstract class Emulator {

	class Reader extends Thread {

		private Object lockObj = new Object();
		private boolean isContinue = true;
		private byte bytes[] = new byte[0];

		public Reader(byte initBytes[]) {
			if (initBytes != null) {
				put(initBytes, 0, initBytes.length);
			}
		}

		public String get() {
			synchronized (lockObj) {

				if (bytes == null || bytes.length == 0) {
					return "";
				}

				try {
					return new String(bytes, charset);
				} catch (UnsupportedEncodingException e) {
					return new String(bytes);
				}
			}
		}

		public void put(byte b[], int offset, int len) {

			synchronized (lockObj) {

				if (listener != null) {
					try {
						listener.onNetState(EmulatorTelnet.TELNET_NET_STATE_MsgRecv, new String(b, offset, len, charset));
					} catch (UnsupportedEncodingException e) {
						listener.onNetState(EmulatorTelnet.TELNET_NET_STATE_MsgRecv, new String(b, offset, len));
					}
					bytes = null;
					return;
				}

				if (bytes == null) {
					bytes = new byte[len];
					System.arraycopy(b, offset, bytes, 0, len);
					return;
				}

				byte tmp[] = new byte[bytes.length + len];
				System.arraycopy(bytes, 0, tmp, 0, bytes.length);
				System.arraycopy(b, offset, tmp, bytes.length, len);

				bytes = tmp;
			}
		}

		public void reset() {
			synchronized (lockObj) {
				bytes = null;
			}
		}

		@Override
		public void run() {

			byte[] ba = new byte[4096];
			int len = 0;
			InputStream in = null;

			while (isContinue) {
				if (getInputStream() != null)
					break;
				Thread.yield();
			}

			in = getInputStream();

			while (isContinue) {

				try {
					if (in.available() == 0) {
						Thread.yield();
						continue;
					}
					len = in.read(ba);
					if (len > 0) {
						put(ba, 0, len);
					}
				} catch (Exception e1) {
					isContinue = false;
					break;
				}

			}

			logger.debug("finished");

		}

		public void stopReader() {
			isContinue = false;
			reader.interrupt();
		}

	}

	/** 연결 TIMEOUT. Milliseconds */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;

	/** 읽기 TIMEOUT. Milliseconds */
	public static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

	public static boolean isPrompt(String result, String[] promptArr) {

		if (result == null || promptArr == null) {
			return false;
		}

		int index = 0;
		int lenPrompt = 0;
		int pos;

		for (String prompt : promptArr) {
			index = result.lastIndexOf(prompt);
			if (index >= 0) {
				lenPrompt = prompt.length();
				break;
			}
		}

		pos = result.length() - lenPrompt;

		return index >= 0 && (index == pos || index == (pos - 1));
	}

	public static void main(String[] args) throws Exception {
		// EmulatorSsh e = new EmulatorSsh();
		// e.connect("167.1.21.31", 22, "nprism30", "nprism03!@");
		// System.out.println("--------------------------------------------------");
		// System.out.println(e.cmdln("ps -ef"));
		// System.out.println("--------------------------------------------------");
		// System.out.println(e.cmdln("nprismstatus"));
		// System.out.println("--------------------------------------------------");
		// e.disconnect();

		EmulatorTelnet e = new EmulatorTelnet();
		e.setPromptArr("$", "#", ">");
		e.connect("167.1.21.95", 23, "admin", "daims!@");
		System.out.println("--------------------------------------------------");
		System.out.println(e.cmdln("en"));
		System.out.println("--------------------------------------------------");
		System.out.println(e.cmdln("terminal length 0"));
		System.out.println("--------------------------------------------------");
		System.out.println(e.cmdln("show running-config"));
		System.out.println("--------------------------------------------------");
		e.disconnect();

	}

	private String charset = "utf-8";

	protected Logger logger = Logger.logger;

	/** 계정 입력 지시자 */
	private String strLoginIndi[] = new String[] { "login:", "username:", "user:" };

	/** 암호 입력 지시자 */
	private String strPassIndi[] = new String[] { "password:" };

	protected Reader reader;
	/** 프롬프트 */
	private String promptArr[] = new String[] { "$", "#", ">" };
	/** 접속 응답 대기 시간 */
	private int timeoutMsConnection = DEFAULT_CONNECTION_TIMEOUT;
	/** 메시지 수신 대기 시간 */
	private int timeoutMsRead = DEFAULT_READ_TIMEOUT;
	private NetListener listener;

	public String cmd(String cmd, String... waitStrArr) throws Exception {

		logger.debug("cmd : {}", cmd);

		reader.reset();

		byte[] byte_msg = cmd.getBytes();

		OutputStream out = getOutputStream();

		out.write(byte_msg, 0, byte_msg.length);

		out.flush();

		if (listener != null) {
			return "";
		}

		if (waitStrArr.length == 0) {
			return waitforPrompt();
		} else {
			return waitfor(waitStrArr);
		}
	}

	public String cmdln(String cmd, String... waitStrArr) throws Exception {
		return cmd(cmd + "\n", waitStrArr);
	}

	/**
	 * 접속 및 로그인
	 * 
	 * @param host
	 * @param port
	 * @param userId
	 * @param password
	 * @throws Exception
	 */
	public void connect(String host, int port, String userId, String password) throws Exception {

		try {
			doConnect(host, port, userId, password);
		} catch (Exception e) {
			logger.error(e);
			disconnect();
			throw e;
		}

	}

	public void connect(String host, int port, String userId, String password, NetListener listener) throws Exception {
		connect(host, port, userId, password);
		this.listener = listener;
	}

	public void disconnect() {
		if (reader != null) {
			reader.stopReader();
			reader = null;
		}

		_disconnect();

		logger.debug("ok");
	}

	public String getCharset() {
		return charset;
	}

	public Logger getLogger() {
		return logger;
	}

	public String[] getStrLoginIndi() {
		return strLoginIndi;
	}

	public String[] getStrPassIndi() {
		return strPassIndi;
	}

	public int getTimeoutMsConnection() {
		return timeoutMsConnection;
	}

	public int getTimeoutMsRead() {
		return timeoutMsRead;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setPromptArr(String... promptArr) {
		this.promptArr = promptArr;
	}

	public void setStrLoginIndi(String[] strLoginIndi) {
		this.strLoginIndi = strLoginIndi;
	}

	public void setStrPassIndi(String[] strPassIndi) {
		this.strPassIndi = strPassIndi;
	}

	public void setTimeoutMsConnection(int timeoutMsConnection) {
		this.timeoutMsConnection = timeoutMsConnection;
	}

	public void setTimeoutMsRead(int timeoutMsRead) {
		this.timeoutMsRead = timeoutMsRead;
	}

	/**
	 * 
	 * @param waitStrArr
	 * @return
	 */
	public String waitfor(String waitStrArr[]) {
		return waitfor0(waitStrArr);
	}

	protected abstract void _disconnect();

	protected abstract void doConnect(String host, int port, String userId, String password) throws Exception;

	/**
	 * 입력 스트림
	 * 
	 * @return 입력 스트림
	 * @throws IOException
	 */
	protected abstract InputStream getInputStream();

	/**
	 * 
	 * @return 출력 스트림
	 * @throws IOException
	 */
	protected abstract OutputStream getOutputStream();

	/**
	 * 
	 * @param strArr
	 * @return
	 */
	protected String getString(String[] strArr) {

		if (strArr == null)
			return "null";
		if (strArr.length == 0)
			return "<empty>";
		StringBuffer sb = new StringBuffer();

		for (String s : strArr) {
			sb.append(",");
			sb.append(s);
		}

		return sb.toString().substring(1);

	}

	protected synchronized void startReader(byte bytes[]) {
		if (reader == null) {
			reader = new Reader(bytes);
			reader.start();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected String waitforPrompt() {
		return waitfor0();
	}

	private String waitfor0(String... waitStrArr) {

		String result;
		String resultPrev = null;
		long ptime = System.currentTimeMillis();

		while (true) {

			result = reader.get();

			if (result != null) {

				if (waitStrArr == null || waitStrArr.length == 0) {
					// 프롬프트로 비교
					if (isPrompt(result, promptArr)) {
						return result;
					}
				} else {
					// 포함여부로 비교
					for (String s : waitStrArr) {
						if (result.indexOf(s) >= 0) {
							return result;
						}
					}
				}

				if (result.equals(resultPrev) == false) {
					resultPrev = result;
					// 받은 시간 리셋
					ptime = System.currentTimeMillis();
				}
			}

			// 데이터가 있던 없든 상관없이 시간이 지나면 리턴한다.
			if (ptime + timeoutMsRead < System.currentTimeMillis())
				return null;

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}

		}
	}
}
