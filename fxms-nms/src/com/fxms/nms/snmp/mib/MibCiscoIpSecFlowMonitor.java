package com.fxms.nms.snmp.mib;

/**
 * 
 * Object ciscoIpSecFlowMonitorMIB <br>
 * OID 1.3.6.1.4.1.9.9.171 <br>
 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
 * Description "This is a MIB Module for monitoring the structures in
 * IPSec-based Virtual Private Networks. The MIB has been designed to be adopted
 * as an IETF standard. Hence Cisco-specific features of IPSec protocol are
 * excluded from this MIB. <br>
 * 
 * Acronyms The following acronyms are used in this document:
 * 
 * IPSec: Secure IP Protocol
 * 
 * VPN: Virtual Private Network
 * 
 * ISAKMP: Internet Security Association and Key Exchange Protocol
 * 
 * IKE: Internet Key Exchange Protocol
 * 
 * SA: Security Association
 * 
 * MM: Main Mode - the process of setting up a Phase 1 SA to secure the
 * exchanges required to setup Phase 2 SAs
 * 
 * QM: Quick Mode - the process of setting up Phase 2 Security Associations
 * using a Phase 1 SA.
 * 
 * 
 * Overview of IPsec MIB
 * 
 * The MIB contains six major groups of objects which are used to manage the
 * IPSec Protocol. These groups include a Levels Group, a Phase-1 Group, a
 * Phase-2 Group, a History Group, a Failure Group and a TRAP Control Group. The
 * following table illustrates the structure of the IPSec MIB.
 * 
 * The Phase 1 group models objects pertaining to IKE negotiations and tunnels.
 * 
 * The Phase 2 group models objects pertaining to IPSec data tunnels.
 * 
 * The History group is to aid applications that do trending analysis.
 * 
 * The Failure group is to enable an operator to do troubleshooting and
 * debugging of the VPN Router. Further, counters are supported to aid Intrusion
 * Detection.
 * 
 * In addition to the five major MIB Groups, there are a number of
 * Notifications. The following table illustrates the name and description of
 * the IPSec TRAPs.
 * 
 * For a detailed discussion, please refer to the IETF draft
 * draft-ietf-ipsec-flow-monitoring-mib-00.txt."
 * 
 * @author subkjh
 * 
 */
public class MibCiscoIpSecFlowMonitor {

	/**
	 * Object cipSecTunOutOctets <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.39 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units Octets <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images <br>
	 * Description "The total number of octets sent by this IPsec Phase-2
	 * Tunnel. This value is accumulated AFTER determining whether or not the
	 * packet should be compressed. See also cipSecTunOutOctWraps for the number
	 * of times this counter has wrapped."
	 */
	public static final String cipSecTunOutOctets = ".1.3.6.1.4.1.9.9.171.1.3.2.1.39";

	/**
	 * Object cipSecTunActiveTime<br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.10 <br>
	 * Type TimeInterval <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description
	 * "The length of time the IPsec Phase-2 Tunnel has been active in hundredths of seconds."
	 * <br>
	 * 
	 */
	public final String cipSecTunActiveTime = ".1.3.6.1.4.1.9.9.171.1.3.2.1.10";

	/**
	 * Object cipSecTunHcInOctets <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.27 <br>
	 * Type Counter64 <br>
	 * Permission read-only <br>
	 * Status current <br>
	 * Units Octets <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images <br>
	 * Description "A high capacity count of the total number of octets received
	 * by this IPsec Phase-2 Tunnel. This value is accumulated BEFORE
	 * determining whether or not the packet should be decompressed." <br>
	 * 
	 */
	public final String cipSecTunHcInOctets = ".1.3.6.1.4.1.9.9.171.1.3.2.1.27";

	/**
	 * Object cipSecTunHcOutOctets <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.40 <br>
	 * Type Counter64 <br>
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description "A high capacity count of the total number of octets sent by
	 * this IPsec Phase-2 Tunnel. This value is accumulated AFTER determining
	 * whether or not the packet should be compressed." <br>
	 * 
	 */
	public final String cipSecTunHcOutOctets = ".1.3.6.1.4.1.9.9.171.1.3.2.1.40";

	/**
	 * Object cipSecTunIndex <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.1<br>
	 * Type Integer32 <br>
	 * 
	 * Permission not-accessible<br>
	 * Status current <br>
	 * Range 1 - 2147483647<br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description "The index of the IPsec Phase-2 Tunnel Table. The value of
	 * the index is a number which begins at one and is incremented with each
	 * tunnel that is created. The value of this object will wrap at
	 * 2,147,483,647." <br>
	 * 
	 */
	public final String cipSecTunIndex = ".1.3.6.1.4.1.9.9.171.1.3.2.1.1";

	/**
	 * Object cipSecTunInOctets <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.26 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units Octets <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description "The total number of octets received by this IPsec Phase-2
	 * Tunnel. This value is accumulated BEFORE determining whether or not the
	 * packet should be decompressed. See also cipSecTunInOctWraps for the
	 * number of times this counter has wrapped." <br>
	 */
	public final String cipSecTunInOctets = ".1.3.6.1.4.1.9.9.171.1.3.2.1.26";

	/**
	 * Object cipSecTunInPkts <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.32<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * Units Packets <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description
	 * "The total number of packets received by this IPsec Phase-2 Tunnel." <br>
	 * 
	 */
	public final String cipSecTunInPkts = ".1.3.6.1.4.1.9.9.171.1.3.2.1.32";

	/**
	 * Object cipSecTunLocalAddr<br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.4 <br>
	 * Type IPSIpAddress <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description
	 * "The IP address of the local endpoint for the IPsec Phase-2 Tunnel." <br>
	 */
	public final String cipSecTunLocalAddr = ".1.3.6.1.4.1.9.9.171.1.3.2.1.4";

	/**
	 * Object cipSecTunnelEntry <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1 <br>
	 * Type CipSecTunnelEntry <br>
	 * 
	 * Permission not-accessible <br>
	 * Status current <br>
	 * Index cipSecTunIndex <br>
	 * 
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images <br>
	 * Description
	 * "Each entry contains the attributes associated with an active IPsec Phase-2 Tunnel."
	 * <br>
	 * 
	 */
	public final String cipSecTunnelEntry = ".1.3.6.1.4.1.9.9.171.1.3.2.1";

	/**
	 * Object cipSecTunnelTable<br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2<br>
	 * Type SEQUENCE<br>
	 * Permission not-accessible<br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description "The IPsec Phase-2 Tunnel Table. There is one entry in this
	 * table for each active IPsec Phase-2 Tunnel."<br>
	 * 
	 */
	public final String cipSecTunnelTable = ".1.3.6.1.4.1.9.9.171.1.3.2";

	/**
	 * Object cipSecTunOutPkts <br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.45 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units Packets <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description
	 * "The total number of packets sent by this IPsec Phase-2 Tunnel." <br>
	 * 
	 */
	public final String cipSecTunOutPkts = ".1.3.6.1.4.1.9.9.171.1.3.2.1.45";

	/**
	 * Object cipSecTunRemoteAddr<br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.5 <br>
	 * Type IPSIpAddress <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images Description
	 * "The IP address of the remote endpoint for the IPsec Phase-2 Tunnel." <br>
	 */
	public final String cipSecTunRemoteAddr = ".1.3.6.1.4.1.9.9.171.1.3.2.1.5";

	/**
	 * Object cipSecTunStatus<br>
	 * OID 1.3.6.1.4.1.9.9.171.1.3.2.1.51 <br>
	 * Type TunnelStatus <br>
	 * 1:active<br>
	 * 2:destroy<br>
	 * 
	 * Permission read-write<br>
	 * Status current <br>
	 * MIB CISCO-IPSEC-FLOW-MONITOR-MIB ; - View Supporting Images<br>
	 * Description "The status of the MIB table row.
	 * 
	 * This object can be used to bring the tunnel down by setting value of this
	 * object to destroy(2). When the value is set to destroy(2), the SA bundle
	 * is destroyed and this row is deleted from this table.
	 * 
	 * When this MIB value is queried, the value of active(1) is always
	 * returned, if the instance exists.
	 * 
	 * This object cannot be used to create a MIB table row." <br>
	 * 
	 */
	public final String cipSecTunStatus = ".1.3.6.1.4.1.9.9.171.1.3.2.1.51";

}
