package subkjh.bas.net.co.vo;

import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 기본 PDU
 * 
 * @author subkjh
 * 
 */
public abstract class NetPdu {

	public enum LogType {
		LENGTH, HEXA, STRING
	}

	/**  */
	public static String characterSet = "UTF-8";

	public static String getString4Key(DatagramChannel channel) {
		DatagramSocket socket = (DatagramSocket) channel.socket();
		StringBuffer sb = new StringBuffer();
		sb.append(socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort() + "-");
		if (socket.getInetAddress() != null) {
			sb.append(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		} else {
			sb.append("*:*");
		}

		return sb.toString();
	}

	/**
	 * 키에 대한 문자열을 제공합니다.
	 * 
	 * @param key
	 *            키
	 * @return 키의 문자열
	 */
	public static String getString4Key(Object key) {

		if (key instanceof InetSocketAddress) {
			InetSocketAddress sa = (InetSocketAddress) key;
			return sa.getAddress().getHostAddress() + ":" + sa.getPort();
		} else if (key instanceof SelectionKey) {
			SelectionKey selectionKey = (SelectionKey) key;

			Object obj = selectionKey.attachment();

			if (selectionKey.channel() instanceof SocketChannel) {
				return getString4Key(((SocketChannel) selectionKey.channel())) + (obj == null ? "" : "-" + obj.toString());
			} else if (selectionKey.channel() instanceof DatagramChannel) {
				return getString4Key(((DatagramChannel) selectionKey.channel())) + (obj == null ? "" : "-" + obj.toString());
			}
		} else if (key instanceof SocketChannel) {
			return getString4Key(((SocketChannel) key));
		} else if (key instanceof DatagramChannel) {
			return getString4Key(((DatagramChannel) key));
		}

		return key == null ? "null" : key.toString();
	}

	public static String getString4Key(SocketChannel channel) {
		StringBuffer sb = null;
		Socket socket = (Socket) ((SocketChannel) channel).socket();
		sb = new StringBuffer();
		sb.append(socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort() + "-");
		if (socket.getInetAddress() != null) {
			sb.append(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		} else {
			sb.append("*:*");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param logType
	 * @param bytes
	 * @return
	 */
	public static String log(LogType logType, byte bytes[]) {

		if (logType == LogType.LENGTH) {
			return bytes.length + "";
		} else if (logType == LogType.STRING) {
			try {
				return bytes.length + " : " + new String(bytes, characterSet);
			} catch (UnsupportedEncodingException e) {
				return bytes.length + " : " + new String(bytes);
			}
		} else
			return bytes.length + " : " + toHexa(bytes);

	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHexa(byte bytes[]) {

		StringBuffer sb = new StringBuffer();
		int i = 0;

		for (byte b : bytes) {
			sb.append(String.format("%02x ", b));
			if (++i == 40) {
				sb.append("\n");
				i = 0;
			}
		}
		return sb.toString();
	}

	/** 상태 소켓 관려 키 */
	private Object key;

	/**
	 * PDU를 바이트배열로 만들어 제공합니다.
	 * 
	 * @return PUD의 바이트배열
	 * @throws Exception
	 */
	public abstract byte[] getBytes() throws Exception;

	/**
	 * 
	 * @return PDU의 키
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * 인간이 알아볼 수 있는 문자열로 PDU를 표현합니다.
	 * 
	 * @return PDU의 문자열 표시 값
	 */
	public abstract String getString();

	/**
	 * PDU의 키를 설정합니다.
	 * 
	 * @param key
	 *            PDU의 키
	 */
	public void setKey(Object key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "[PDU, " + getString4Key(key) + "]";
	}

	public static List<String> split(String s, char... separatorCh) {
		List<String> strList = new ArrayList<String>();
		String item = "";
		char chArray[] = s.toCharArray();
		char ch;
		if (separatorCh.length == 0) {
			separatorCh = new char[] { ' ', '\t', '\n', '\r' };
		}

		AAA: for (int i = 0; i < chArray.length; i++) {
			ch = chArray[i];
			for (char c : separatorCh) {
				if (c == ch) {
					if (item.length() > 0)
						strList.add(item);
					item = "";
					continue AAA;
				}
			}

			item += ch;
		}

		if (item.length() > 0)
			strList.add(item);

		return strList;
	}

}
