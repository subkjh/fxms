package subkjh.bas.net.syslog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import subkjh.bas.net.syslog.vo.SyslogListener;
import subkjh.bas.net.syslog.vo.SyslogVo;

/**
 * SYSLOG 서버 쓰레드
 * 
 * @author subkjh
 * 
 */
public class SyslogReceiver extends Thread {

	private DatagramSocket dsocket;
	private long m_count;
	private SyslogListener listener;
	private boolean m_keepGoing;

	/**
	 * SYSLOG 서버
	 * 
	 * @param syslogProc
	 *            받은 로그를 처리하는 객체
	 */
	public SyslogReceiver(SyslogListener listener) {
		setName(getClass().getSimpleName());

		this.listener = listener;
	}

	@Override
	public void run() {

		SyslogVo vo;

		m_keepGoing = true;
		m_count = 0;
		try {
			String host;

			// Create a buffer to read datagrams into. If a packet is larger
			// than this buffer, the excess will simply be
			// discarded!
			byte[] buffer = new byte[2048];

			// Create a packet to receive data into the buffer
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			dsocket.setSoTimeout(3000);
			// Now loop forever, waiting to receive packets and printing them.
			while (m_keepGoing) {
				// Wait to receive a datagram

				try {
					dsocket.receive(packet);
				} catch (SocketTimeoutException e1) {
					continue;
				} catch (Exception e) {
					continue;
				}

				host = packet.getAddress().getHostAddress();
				byte bytes[] = new byte[packet.getLength()];
				System.arraycopy(buffer, 0, bytes, 0, bytes.length);

				m_count = m_count == Long.MAX_VALUE ? 0 : m_count + 1;

				vo = makeData(host, bytes);

				listener.onRecv(vo);

				// Reset the length of the packet before reusing it.
				packet.setLength(buffer.length);
			}

		} catch (Exception e) {
			System.err.println(e);
		} finally {

			// 서버를 닫는다.
			if (dsocket != null) {
				dsocket.close();
			}
		}

	}

	/**
	 * SYSLOG 서버를 실행합니다.
	 * 
	 * @throws SocketException
	 */
	public void startSyslogReceiver(int port) throws SocketException {

		// Create a socket to listen on the port.
		dsocket = new DatagramSocket(port);

		start();
	}

	/**
	 * SYSLOG 서버를 종료합니다.
	 */
	public void stopSyslogReceiver() {
		m_keepGoing = false;
		try {
			this.join();
		} catch (Exception e) {
		}
		System.out.println("CSyslogDaemon(" + m_count + ")");
	}

	private SyslogVo makeData(String host, byte buffer[]) {
		int spos, epos;
		int facility = -1, severity = -1;
		long msTime;
		String buf, ss[];

		// Convert the contents to a string, and display them
		String msg = new String(buffer, 0, buffer.length);

		spos = msg.indexOf("<");
		epos = msg.indexOf(">");

		msTime = System.currentTimeMillis();

		/*
		 * Originally received message <00>... Relayed message <13>TIMESTAMP
		 * HOSTNAME <00>...
		 */
		if (spos == 0 && epos > 0) {
			try {
				severity = Integer.parseInt(msg.substring(spos + 1, epos));
				if (severity == 13) {
					msg = msg.substring(epos + 1);
					spos = msg.indexOf("<");
					epos = msg.indexOf(">");
					if (spos == 0 && epos > 0) {
						severity = Integer.parseInt(msg.substring(spos + 1, epos));
						buf = msg.substring(0, spos - 1);
						ss = buf.split(" ");
						if (ss.length >= 2) {
							try {
								msTime = Long.parseLong(ss[0]) * 1000L;
							} catch (Exception ex) {
							}
							host = ss[1];
						}
					}
				}
				msg = msg.substring(epos + 1);
				facility = severity & 0x3f8;
				severity = severity & 0x07;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return new SyslogVo(facility, severity, host, msTime, msg);

	}

}
