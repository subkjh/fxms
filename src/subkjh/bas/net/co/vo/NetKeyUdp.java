package subkjh.bas.net.co.vo;

import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * UDP 패킷용 키
 * 
 * @author subkjh
 * 
 */
public class NetKeyUdp {

	private DatagramChannel channel;
	
	private SocketAddress sa;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NetKeyUdp) {
			NetKeyUdp key = (NetKeyUdp) obj;
			return key.getChannel().equals(channel);
		}
		return false;
	}

	public DatagramChannel getChannel() {
		return channel;
	}

	public SocketAddress getSa() {
		return sa;
	}

	@Override
	public int hashCode() {
		return 2;
	}

	public void setChannel(DatagramChannel channel) {
		this.channel = channel;
	}

	public void setSa(SocketAddress sa) {
		this.sa = sa;
	}

	@Override
	public String toString() {
		return NetPdu.getString4Key(sa);
	}

}
