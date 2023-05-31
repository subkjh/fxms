package subkjh.bas.net.co;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.net.co.pdumaker.NetPduMaker;
import subkjh.bas.net.co.vo.NetListener;
import subkjh.bas.net.co.vo.NetPdu;
import subkjh.bas.net.co.vo.RecvBytes;

/**
 * 
 * 
 * @author subkjh
 * 
 * @param <PDU>
 */
public class NetClientTcp<PDU extends NetPdu> extends NetClient<PDU> {

	private SocketChannel channel;
	private Boolean keepAlive = null;

	/**
	 * 
	 * @param name
	 * @param pduMaker
	 */
	public NetClientTcp(String name, NetPduMaker<PDU> pduMaker) {
		super(name, pduMaker);
	}

	public NetClientTcp(String name, NetPduMaker<PDU> pduMaker, NetListener listener) {
		super(name, pduMaker, listener);
	}

	public void setKeepAlive(Boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	@Override
	protected void _connect(String host, int port, int timeoutMs) throws ConnectException, Exception {

		selector = Selector.open();
		channel = SocketChannel.open();

		try {
			if (timeoutMs > 100) {
				channel.socket().connect(new InetSocketAddress(host, port), timeoutMs);
			} else {
				channel.connect(new InetSocketAddress(host, port));
			}

			if (keepAlive != null) {
				channel.setOption(StandardSocketOptions.SO_KEEPALIVE, keepAlive);
			}

		} catch (ConnectException e) {
			selector.close();
			selector = null;
			throw e;
		} catch (SocketTimeoutException e) {
			selector.close();
			selector = null;
			throw e;
		} catch (Exception e) {
			selector.close();
			selector = null;
			throw e;
		}

		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_READ);
	}

	@Override
	protected int _send(byte bytes[]) throws Exception {
		int len = 0;
		int totalLen = 0;
		int offset;

		ByteBuffer byteBuffer = null;

		if (bytes != null && bytes.length > 0) {

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
					channel.socket().getOutputStream().flush();
					Thread.yield();
					continue;
				}

				totalLen += len;
				offset += len;

				if (totalLen >= bytes.length)
					break;

			}

			channel.socket().getOutputStream().flush();
			Thread.yield();
		}

		if (byteBuffer != null)
			byteBuffer.clear();
		byteBuffer = null;

		return totalLen;
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

	@Override
	protected RecvBytes read(SelectionKey key) throws Exception {

		try {

			ByteBuffer readBuffer = ByteBuffer.allocate(4096);
			readBuffer.clear();

			int numRead;
			numRead = ((SocketChannel) key.channel()).read(readBuffer);
			if (numRead > 0) {

				RecvBytes ret = new RecvBytes();
				ret.setKey(key.channel());

				byte bytes[] = new byte[numRead];
				readBuffer.rewind();
				readBuffer.get(bytes);

				if (logger.isTrace()) {
					logger.trace("read length=" + numRead);
				}

				ret.setBytes(bytes);
				return ret;

			}
			if (numRead == -1) {
				close();
				return null;
			}
		} catch (ClosedChannelException e) {
			close();
		} catch (Exception e) {
			logger.fail(e.getClass().getSimpleName() + ":" + e.getMessage());
			close();
		}

		return null;

	}

}
