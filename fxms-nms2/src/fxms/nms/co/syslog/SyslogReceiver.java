package fxms.nms.co.syslog;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.Logger;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.FxThread;
import fxms.nms.api.SyslogApi;
import fxms.nms.co.syslog.vo.SyslogVo;

/**
 * SYSLOG 서버 쓰레드
 * 
 * @author subkjh
 * 
 */
public class SyslogReceiver extends FxThread implements FxServiceMember {

	private DatagramSocket dsocket;
	private int port;
	private LinkedBlockingQueue<SyslogVo> queue;
	private long recvCount = 0;

	/**
	 * SYSLOG 서버
	 * 
	 * @param syslogProc
	 *            받은 로그를 처리하는 객체
	 */
	public SyslogReceiver() {
		queue = new LinkedBlockingQueue<SyslogVo>();
	}

	@Override
	public void startMember() throws Exception {

		port = getFxPara().getInt("port", 514);
		dsocket = new DatagramSocket(port);

		int thrSize = getFxPara().getInt("thread-size", 3);

		SyslogThread th;
		for (int i = 0; i < thrSize; i++) {
			th = new SyslogThread(queue);
			th.setName(getName() + "-Thr#" + (i + 1));
			th.start();
		}

		start();
	}

	@Override
	protected void doInit() throws Exception {
	}

	@Override
	protected void doWork() {

		SyslogVo vo;

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
			while (isContinue()) {
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

				vo = new SyslogVo(host, bytes);

				queue.put(vo);

				recvCount++;

				Logger.logger.trace("{} {} {} {}", recvCount, queue.size(), vo.getIpAddress(), vo.getMsg());

				if (queue.size() > 100) {
					SyslogApi.getApi().checkQueueSize(getName(), queue.size());
				}

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

}
