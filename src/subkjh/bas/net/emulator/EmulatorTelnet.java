package subkjh.bas.net.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import subkjh.bas.net.co.vo.NetListener;

public class EmulatorTelnet extends Emulator {

	public static final String TELNET_NET_STATE_MsgRecv = "TelnetMsgRecv";

	/** Telnet 접속 Control 상수값 ( 253, 0xfd ) */
	public static final byte DO = (byte) 253;
	/** Telnet 접속 Control 상수값 ( 254, 0xfe ) */
	public static final byte DONT = (byte) 254;
	/** Telnet 접속 Control 상수값 ( 255, 0xff ) */
	public static final byte IAC = (byte) 255;
	/** Telnet 접속 Control 상수값 ( 251, 0xfb ) */
	public static final byte WILL = (byte) 251;
	/** Telnet 접속 Control 상수값 ( 252, 0xfc ) */
	public static final byte WONT = (byte) 252;

	private Socket socket;
	/** 로그인 문구를 기다렸다가 계정을 보낼지 여부 */
	private boolean waitLoginString = false;
	
	private NetListener listener;

	public static void main(String[] args) throws Exception {
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

	public EmulatorTelnet() {
	}
	
	public EmulatorTelnet(NetListener listener) {
		this.listener = listener;
	}

	/**
	 * 로그인 문구를 기다렸다가 계정을 보낼지 여부를 설정합니다.
	 * 
	 * @param waitLoginString
	 *            true이면 기다림.
	 */
	public void setWaitLoginString(boolean waitLoginString) {
		this.waitLoginString = waitLoginString;
	}

	@Override
	protected void doConnect(String host, int port, String userId, String password) throws Exception {

		InetSocketAddress isa = new InetSocketAddress(host, port);
		socket = new Socket();
		socket.connect(isa, getTimeoutMsConnection());
		socket.setSoTimeout(getTimeoutMsRead());

		logger.debug("host={}, port={} connection ok", host, port);

		byte bytes[] = negotiation();

		logger.debug("negotiation ok - [" + new String(bytes) + "]");

		startReader(bytes);

		doLogin(userId, password);

	}

	protected void doLogin(String userId, String password) throws Exception {

		// negotiation 과정에서 login 문자를 먼저 읽어 버림.
		if (waitLoginString) {
			if (waitfor(getStrLoginIndi()) == null) {
				throw new Exception("Can not input user id - " + getString(getStrLoginIndi()));
			}
		}

		// 2013.08.05 by subkjh : ID는 없고 바로 암호인 경우가 있음
		if (userId != null && userId.trim().length() > 0) {
			if (cmdln(userId, getStrPassIndi()) == null) {
				throw new Exception("Can not input password - " + getString(getStrPassIndi()));
			}
		}

		String ret = cmdln(password);
		if (ret == null) {
			throw new Exception("login fail");
		}

		if (listener != null) {
			listener.onNetState(NetListener.tcpConnected, "");
		}
		
		logger.debug("login ok", ret);
	}

	@Override
	protected void _disconnect() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error(e);
			}
			socket = null;
		}

	}

	@Override
	protected InputStream getInputStream() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected OutputStream getOutputStream() {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 5초이내에 처리가 안되면 과정을 무시합니다.
	 * 
	 * @return
	 * @throws IOException
	 */
	private byte[] negotiation() throws IOException {

		byte[] buff = new byte[3];
		byte recvCh[] = new byte[3];
		int len;
		long ptime = System.currentTimeMillis();

		InputStream inputStream = getInputStream();
		OutputStream outputStream = getOutputStream();

		while (true) {
			if (inputStream.available() >= 1) {

				recvCh[0] = (byte) inputStream.read();

				// 시작이 IAC가 아니면 return함.
				if (recvCh[0] != IAC) {
					len = inputStream.available();

					byte bytes[] = new byte[len + 1];
					bytes[0] = recvCh[0];
					inputStream.read(bytes, 1, len);

					return bytes;
				}

				recvCh[1] = (byte) inputStream.read();

				if (recvCh[1] == DO || recvCh[1] == DONT) {
					recvCh[2] = (byte) inputStream.read();
					buff[0] = IAC;
					buff[1] = WONT;
					buff[2] = recvCh[2];
					// System.out.println(String.format("%x %x %x", recvCh[0],
					// recvCh[1], recvCh[2]));
					outputStream.write(buff);
				} else if (recvCh[1] == WILL) {
					recvCh[2] = (byte) inputStream.read();
					buff[0] = IAC;
					buff[1] = DONT;
					buff[2] = recvCh[2];
					// System.out.println(String.format("%x %x %x", recvCh[0],
					// recvCh[1], recvCh[2]));
					outputStream.write(buff);
				}
			}

			// 2013.08.08 by subkjh : added
			else {
				if (System.currentTimeMillis() > ptime + 5000)
					return new byte[0];
				Thread.yield();
			}
		}
	}

}
