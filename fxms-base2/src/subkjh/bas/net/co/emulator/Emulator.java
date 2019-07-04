package subkjh.bas.net.co.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import subkjh.bas.co.log.Logger;

public abstract class Emulator {

	enum OP {
		reset, get, put;
	}

	class Reader extends Thread {

		private StringBuffer sb;
		private boolean isContinue = true;

		public synchronized String buffer(OP op, String... strArr) {

			if (op == OP.reset) {

				if (sb.length() == 0)
					return null;

				String ret = sb.toString();
				sb = new StringBuffer();
				return ret;
			} else if (op == OP.get) {
				return sb.toString();
			} else if (op == OP.put) {
				for (String s : strArr) {
					sb.append(s);
				}
			}

			return null;
		}

		@Override
		public void run() {

			byte[] ba = new byte[4096];
			int len = 0;
			InputStream in = null;
			sb = new StringBuffer();

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
						buffer(OP.put, new String(ba, 0, len));
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

	public String cmd(String cmd, String... waitStrArr) throws Exception {

		reader.buffer(OP.reset);

		byte[] byte_msg = cmd.getBytes();

		OutputStream out = getOutputStream();

		out.write(byte_msg, 0, byte_msg.length);

		out.flush();

		if (waitStrArr.length == 0) {
			return waitforPrompt();
		} else {
			return waitfor(waitStrArr);
		}
	}

	public String cmdln(String cmd, String... waitStrArr) throws Exception {
		return cmd(cmd + "\n", waitStrArr);
	}

	public void connect(String host, int port, String userId, String password) throws Exception {

		try {
			_connect(host, port, userId, password);
		} catch (Exception e) {
			logger.error(e);
			disconnect();
			throw e;
		}

		if (reader == null) {
			reader = new Reader();
			reader.start();
		}

	}

	public void disconnect() {
		if (reader != null) {
			reader.stopReader();
			reader = null;
		}

		_disconnect();

		logger.debug("ok");
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

	protected abstract void _connect(String host, int port, String userId, String password) throws Exception;

	protected abstract void _disconnect();

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

	protected String waitfor(String waitStrArr[]) {

		String s;
		StringBuffer sb = new StringBuffer();
		String result;
		long ptime = System.currentTimeMillis();

		while (true) {
			s = reader.buffer(OP.get);
			if (s == null) {
				if (ptime + timeoutMsRead < System.currentTimeMillis())
					return null;
				continue;
			}
			sb.append(s);
			result = sb.toString().toLowerCase();
			for (String f : waitStrArr) {
				if (result.indexOf(f.toLowerCase()) >= 0) {
					return result;
				}
			}
		}

	}

	protected String waitforPrompt() {

		String s;
		int index = 0;
		int pos;
		StringBuffer sb = new StringBuffer();
		String result;
		boolean isExist = false;
		long ptime = System.currentTimeMillis();
		int lenPrompt = 0;

		while (true) {

			s = reader.buffer(OP.reset);

			if (s == null) {
				if (ptime + timeoutMsRead < System.currentTimeMillis())
					return null;
				if (isExist)
					break;
			} else {
				sb.append(s);
				result = sb.toString();
				for (String prompt : promptArr) {
					index = result.lastIndexOf(prompt);
					if (index >= 0) {
						lenPrompt = prompt.length();
						break;
					}
				}

				pos = result.length() - lenPrompt;

				if (index >= 0 && (index == pos || index == (pos - 1))) {
					isExist = true;
					Thread.yield();
					continue;
				}
			}

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}

		}

		return sb.toString();
	}

}
