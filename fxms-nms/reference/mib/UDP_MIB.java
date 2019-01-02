package com.daims.dfc.mib;

/**
 * OID : 1.3.6.1.2.1.7<br>
 * 
 * @author subkjh
 * 
 */
public class UDP_MIB {

	/** The local port number for this UDP listener. */
	public final String udpLocalPort = ".1.3.6.1.2.1.7.5.1.2";

	/**
	 * The local IP address for this UDP listener. In the case of a UDP
	 * listener which is willing to accept datagrams for any IP interface
	 * associated with the node, the value 0.0.0.0 is used.
	 */
	public final String udpLocalAddress = ".1.3.6.1.2.1.7.5.1.1";
	
}
