package fxms.nms.fxactor.ping;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingTimeoutException;
import fxms.nms.NmsCodes;
import fxms.nms.mo.SocketMo;

public class PingTcpActor extends PingFxActor {

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof SocketMo) == false) {
			return null;
		}

		SocketMo node = (SocketMo) mo;

		int status;
		long eTime;
		try {

			eTime = getConnectionTime(node.getIpAddress(), node.getPort());

			status = eTime >= 0 ? 1 : 0;
		} catch (Exception e) {
			status = 0;
		}

		List<PsVo> valueList = new ArrayList<PsVo>();

		valueList.add(new PsVo(node, node.getPort() + "", NmsCodes.PsItem.TcpPortListen, status));

		return valueList;
	}

	long getConnectionTime(String ip, int port) throws Exception {

		long startTime = System.currentTimeMillis();
		Socket socket = new Socket();

		try {
			SocketAddress sockaddr = new InetSocketAddress(ip, port);
			socket.setSoTimeout(5);
			socket.connect(sockaddr, 5000);
			if (socket.isConnected()) {
				long mstime = System.currentTimeMillis() - startTime;
				if (mstime < 0)
					mstime = 1;
				return mstime;
			}
		} catch (Exception e1) {
			Logger.logger.fail(ip + ":" + port + " " + e1.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return -1;

	}

	long getConnectionTime2(String ip, int port) {

		long startTime = System.currentTimeMillis();
		SocketChannel clientSocketChannel = null;

		try {
			InetSocketAddress sockaddr = new InetSocketAddress(ip, port);
			clientSocketChannel = SocketChannel.open();
			clientSocketChannel.configureBlocking(true);

			if (clientSocketChannel.connect(sockaddr)) {
				long mstime = System.currentTimeMillis() - startTime;
				if (mstime < 0)
					mstime = 1;
				return mstime;
			}
		} catch (Exception e1) {
			Logger.logger.fail(e1.getMessage());
		} finally {
			if (clientSocketChannel != null) {
				try {
					// clientSocketChannel.configureBlocking(false);
					// clientSocketChannel.finishConnect();
					clientSocketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return -1;

	}

}
