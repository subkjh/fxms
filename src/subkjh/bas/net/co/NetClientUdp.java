package subkjh.bas.net.co;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.net.co.pdumaker.NetPduMaker;
import subkjh.bas.net.co.vo.NetPdu;
import subkjh.bas.net.co.vo.RecvBytes;

/**
 * 
 * 
 * @author subkjh
 * 
 * @param <PDU>
 */
public class NetClientUdp<PDU extends NetPdu> extends NetClient<PDU> {

	private DatagramChannel channel;

	/**
	 * 
	 * @param pduMaker
	 */
	public NetClientUdp(String name, NetPduMaker<PDU> pduMaker) {
		super(name, pduMaker);
	}

	@Override
	protected void _connect(String host, int port, int timeoutMs) throws Exception {

		SocketAddress sa = new InetSocketAddress(host, port);

		selector = Selector.open();

		channel = DatagramChannel.open();

		if (timeoutMs > 0) {
			channel.socket().setSoTimeout(timeoutMs);
		}

		channel.connect(sa);
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);

	}

	@Override
	protected int _send(byte[] bytes) throws Exception {
		int len = 0;
		int totalLen = 0;
		int offset;

		ByteBuffer byteBuffer = null;

		if (bytes == null || bytes.length == 0)
			return 0;

		byteBuffer = ByteBuffer.wrap(bytes, 0, bytes.length);

		offset = 0;

		while (offset < bytes.length) {

			byteBuffer.position(offset);

			if (channel == null)
				throw new Exception("channel is null");

			len = channel.write(byteBuffer);

			if (len < 0)
				throw new Exception(Lang.get("쓰기를 하지 못하였습니다."));

			if (len == 0) {
				Thread.yield();
				continue;
			}

			totalLen += len;
			offset += len;

			if (totalLen >= bytes.length)
				break;

		}

		Thread.yield();

		if (byteBuffer != null)
			byteBuffer.clear();
		byteBuffer = null;

		return totalLen;

	}

	@Override
	protected RecvBytes read(SelectionKey key) throws Exception {

		try {

			ByteBuffer readBuffer = ByteBuffer.allocate(4096);
			readBuffer.clear();

			int numRead = ((DatagramChannel) key.channel()).read(readBuffer);
			if (numRead > 0) {

				RecvBytes ret = new RecvBytes();
				ret.setKey(getName());

				byte bytes[] = new byte[numRead];
				readBuffer.rewind();
				readBuffer.get(bytes);

				ret.setBytes(bytes);

				return ret;
			}

			if (numRead == -1) {
				key.cancel();
				close();
				return null;
			}

		} catch (Exception e) {
			logger.error(e);
			close();
		}

		return null;

	}

	@Override
	protected void closeChannel() {
		if (channel != null) {
			try {
				channel.close();
				logger.debug("{}", channel);
			} catch (IOException e) {
			}
			channel = null;
		}
	}
}
