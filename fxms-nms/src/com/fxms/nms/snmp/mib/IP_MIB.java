package com.fxms.nms.snmp.mib;

public class IP_MIB {

	/**
	 * Object ipAdEntAddr <br>
	 * OID 1.3.6.1.2.1.4.20.1.1<br>
	 * Type IpAddress <br>
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * MIB IP-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The IPv4 address to which this entry's addressing
	 * information pertains."
	 */
	public final String ipAdEntAddr = ".1.3.6.1.2.1.4.20.1.1";

	/**
	 * Object ipAdEntIfIndex <br>
	 * OID 1.3.6.1.2.1.4.20.1.2 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * Range 1 - 2147483647 <br>
	 * MIB IP-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The index value which uniquely identifies the
	 * interface to which this entry is applicable. The interface identified by
	 * a particular value of this index is the same interface as identified by
	 * the same value of the IF-MIB's ifIndex."
	 */

	public final String ipAdEntIfIndex = ".1.3.6.1.2.1.4.20.1.2";

	/**
	 * Object ipAdEntNetMask<br>
	 * OID 1.3.6.1.2.1.4.20.1.3 <br>
	 * Type IpAddress <br>
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * MIB IP-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The subnet mask associated with the IPv4 address of
	 * this entry. The value of the mask is an IPv4 address with all the network
	 * bits set to 1 and all the hosts bits set to 0."
	 */
	public final String ipAdEntNetMask = ".1.3.6.1.2.1.4.20.1.3";

	/**
	 * Object ipAdEntBcastAddr <br>
	 * OID 1.3.6.1.2.1.4.20.1.4 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status deprecated <br>
	 * Range 0 - 1 <br>
	 * MIB IP-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The value of the least-significant bit in the IPv4
	 * broadcast address used for sending datagrams on the (logical) interface
	 * associated with the IPv4 address of this entry. For example, when the
	 * Internet standard all-ones broadcast address is used, the value will be
	 * 1. This value applies to both the subnet and network broadcast addresses
	 * used by the entity on this (logical) interface."
	 */
	public final String ipAdEntBcastAddr = ".1.3.6.1.2.1.4.20.1.4";

}
