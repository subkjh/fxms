package com.daims.dfc.mib;

/**
 * OID : 1.3.6.1.2.1.6<br>
 * 
 * @author subkjh
 * 
 */
public class TCP_MIB {

	public class Conn {
		public String localIp;
		public String localPort;
		public String remoteIp;
		public String remotePort;
	}

	/**
	 * The local IP address for this TCP connection. In the case of a connection
	 * in the listen state which is willing to accept connections for any IP
	 * interface associated with the node, the value 0.0.0.0 is used.
	 */
	public final String tcpConnLocalAddress = ".1.3.6.1.2.1.6.13.1.2";

	/** The local port number for this TCP connection. */
	public final String tcpConnLocalPort = ".1.3.6.1.2.1.6.13.1.3";

	/** The remote IP address for this TCP connection */
	public final String tcpConnRemAddress = ".1.3.6.1.2.1.6.13.1.4";

	/** The remote port number for this TCP connection */
	public final String tcpConnRemPort = ".1.3.6.1.2.1.6.13.1.5";

	/**
	 * The state of this TCP connection.
	 * 
	 * The only value which may be set by a management station is deleteTCB(12).
	 * Accordingly, it is appropriate for an agent to return a `badValue'
	 * response if a management station attempts to set this object to any other
	 * value.
	 * 
	 * If a management station sets this object to the value deleteTCB(12), then
	 * this has the effect of deleting the TCB (as defined in RFC 793) of the
	 * corresponding connection on the managed node, resulting in immediate
	 * termination of the connection.
	 * 
	 * As an implementation-specific option, a RST segment may be sent from the
	 * managed node to the other TCP endpoint (note however that RST segments
	 * are not sent reliably). <br>
	 * <br>
	 * INTEGER { closed ( 1 ) , listen ( 2 ) , synSent ( 3 ) , synReceived ( 4 )
	 * , established ( 5 ) , finWait1 ( 6 ) , finWait2 ( 7 ) , closeWait ( 8 ) ,
	 * lastAck ( 9 ) , closing ( 10 ) , timeWait ( 11 ) , deleteTCB ( 12 ) }
	 */
	public final String tcpConnState = ".1.3.6.1.2.1.6.13.1.1";

	public final int tcpConnState_closed = 1;
	public final int tcpConnState_listen = 2;
	public final int tcpConnState_synSent = 3;
	public final int tcpConnState_synReceived = 4;
	public final int tcpConnState_established = 5;
	public final int tcpConnState_finWait1 = 6;
	public final int tcpConnState_finWait2 = 7;
	public final int tcpConnState_closeWait = 8;
	public final int tcpConnState_lastAck = 9;
	public final int tcpConnState_closing = 10;
	public final int tcpConnState_timeWait = 11;
	public final int tcpConnState_deleteTCB = 12;

	public Conn parse ( String s ) {

		String ss[] = s.split("\\.");

		Conn conn = new Conn();

		conn.localIp = ss[0] + "." + ss[1] + "." + ss[2] + "." + ss[3];
		conn.localPort = ss[4];
		conn.remoteIp = ss[5] + "." + ss[6] + "." + ss[7] + "." + ss[8];
		conn.remotePort = ss[9];

		return conn;
	}

}
