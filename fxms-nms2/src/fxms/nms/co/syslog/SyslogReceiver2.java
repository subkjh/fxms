package fxms.nms.co.syslog;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.Logger;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.FxThread;
import fxms.nms.api.SyslogApi;
import fxms.nms.co.syslog.vo.SyslogVo;

public class SyslogReceiver2 extends FxThread implements FxServiceMember {

	private DatagramChannel channel = null;
	private int port;
	private LinkedBlockingQueue<SyslogVo> queue;
	private long recvCount = 0;
	private final int buffer = 4096;

	/**
	 * SYSLOG 서버
	 * 
	 * @param syslogProc
	 *            받은 로그를 처리하는 객체
	 */
	public SyslogReceiver2() {
		queue = new LinkedBlockingQueue<SyslogVo>();
	}

	@Override
	public void startMember() throws Exception {

		port = getFxPara().getInt("port", 514);
		int thrSize = getFxPara().getInt("thread-size", 3);
		int bufsize = getFxPara().getInt("buffer-size", 100000);

		SyslogThread th;
		for (int i = 0; i < thrSize; i++) {
			th = new SyslogThread(queue);
			th.setName(getName() + "-Thr#" + (i + 1));
			th.start();
		}

		// udp 포트 사용 (내부소켓 생성)
		InetSocketAddress inet = new InetSocketAddress(port);

		// 블로킹 모드 지정(true-블로킹, false-비블로킹 Default:true)
		try {
			channel = DatagramChannel.open(); // channel open
			channel.socket().setReceiveBufferSize(bufsize);
			channel.socket().bind(inet); // binding
			channel.configureBlocking(true); // non-blocking i/o모드 설정
		} catch (Exception e) {
			throw e;
		}

		start();
	}

	@Override
	protected void doInit() throws Exception {
	}

	@Override
	protected void doWork() {

		SyslogVo vo;
		ByteBuffer buff = ByteBuffer.allocateDirect(buffer);
		byte[] rd = new byte[buffer]; // 버퍼
		int len;
		String logMsg;

		try {

			while (isContinue()) {

				buff.clear();

				SocketAddress addr = null;
				try {

					addr = channel.receive(buff);

					if (addr != null) {

						buff.flip(); // ByteBuffer를 읽기 가능한 상태로 만듬
						len = buff.limit();

						// Arrays.fill( rd , ( byte )0 );
						// rd = new byte[buff.limit()];
						buff.get(rd, 0, len);

						logMsg = new String(rd, 0, len);

						vo = new SyslogVo(((InetSocketAddress) addr).getAddress().getHostAddress(), logMsg);

						queue.put(vo);

						recvCount++;

						Logger.logger.trace("{} {} {} {}", recvCount, queue.size(), vo.getIpAddress(), vo.getMsg());

						if (queue.size() > 100) {
							SyslogApi.getApi().checkQueueSize(getName(), queue.size());
						}

					}
				} catch (Exception e) {
					Logger.logger.error(e);
				}

			}

		} catch (Exception e) {
			System.err.println(e);
		} finally {
			// 서버를 닫는다.
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					Logger.logger.error(e);
				}
			}
		}

	}

}
