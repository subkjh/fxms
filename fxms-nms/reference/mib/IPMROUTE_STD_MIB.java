package com.daims.dfc.mib;

/**
 * OID : .1.3.6.1.2.1.83.xxx
 * 
 * @author subkjh
 * 
 */
public class IPMROUTE_STD_MIB {

	/**
	 * group ip, source ip를 이용하여 채널과 mapping 가능. 해당 채널이 사용하는 ifIndex. The value
	 * of ifIndex for the interface on which IP datagrams sent by these sources
	 * to this multicast address are received. A value of 0 indicates that
	 * datagrams are not subject to an incoming interface check, but may be
	 * accepted on multiple interfaces (e.g., in CBT).
	 */
	public final String ipMRouteInIfIndex = ".1.3.6.1.2.1.83.1.1.2.1.5";

	/**
	 * 채널 트래픽 (packet) The number of packets which this router has received from
	 * these sources and addressed to this multicast group address.
	 */
	public final String ipMRoutePkts = ".1.3.6.1.2.1.83.1.1.2.1.8";

	/**
	 * 채널 트래픽 (bytes) The number of octets contained in IP datagrams which were
	 * received from these sources and addressed to this multicast group
	 * address, and which were forwarded by this router. This object is a 64-bit
	 * version of ipMRouteOctets.
	 */
	public final String ipMRouteHCOctets = ".1.3.6.1.2.1.83.1.1.2.1.16";

	/**
	 * 인터페이스 Multicast In 트래픽 (bytes) The number of octets of multicast packets
	 * that have arrived on the interface, including framing characters. This
	 * object is a 64-bit version of ipMRouteInterfaceInMcastOctets. It is
	 * similar to ifHCInOctets in the Interfaces MIB, except that only multicast
	 * packets are counted.
	 */
	public final String ipMRouteInterfaceHCInMcastOctets = ".1.3.6.1.2.1.83.1.1.4.1.7";

	/**
	 * 인터페이스 Multicast Out 트래픽 (bytes) The number of octets of multicast packets
	 * that have been sent on the interface. This object is a 64-bit version of
	 * ipMRouteInterfaceOutMcastOctets.
	 */
	public final String ipMRouteInterfaceHCOutMcastOctets = ".1.3.6.1.2.1.83.1.1.4.1.8";
}
