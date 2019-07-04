package fxms.nms.co.snmp.mib;

public class MibCiscoPingTable {
	
	/**
	 * Object ciscoPingAddress <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.3 <br>
	 * Type CiscoNetworkAddress <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "The address of the device to be pinged. An instance of this
	 * object cannot be created until the associated instance of
	 * ciscoPingProtocol is created."
	 */
	public final String ciscoPingAddress = ".1.3.6.1.4.1.9.9.16.1.1.1.3";
	/**
	 * Object ciscoPingAvgRtt <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.12 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units milliseconds <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "The average round trip time of all the packets that have
	 * been sent in this sequence.
	 * 
	 * This object will not be created until the first ping response in a
	 * sequence is received."
	 */
	public final String ciscoPingAvgRtt = ".1.3.6.1.4.1.9.9.16.1.1.1.12";

	/**
	 * Object ciscoPingCompleted <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.14 <br>
	 * Type TruthValue <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description
	 * "Set to true when all the packets in this sequence have been either responded to or timed out."
	 */
	public final String ciscoPingCompleted = ".1.3.6.1.4.1.9.9.16.1.1.1.14";

	/**
	 * Object ciscoPingDelay <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.7 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * Units milliseconds <br>
	 * Range 0 - 3600000 <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "Specifies the minimum amount of time to wait before sending
	 * the next packet in a sequence after receiving a response or declaring a
	 * timeout for a previous packet. The actual delay may be greater due to
	 * internal task scheduling."
	 */
	public final String ciscoPingDelay = ".1.3.6.1.4.1.9.9.16.1.1.1.7";
	/**
	 * Object ciscoPingEntryOwner <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.15 <br>
	 * Type OwnerString <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description The entity that configured this entry.
	 */
	public final String ciscoPingEntryOwner = ".1.3.6.1.4.1.9.9.16.1.1.1.15";

	/**
	 * Object ciscoPingEntryStatus <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.16 <br>
	 * Type RowStatus <br>
	 * 1:active <br>
	 * 2:notInService <br>
	 * 3:notReady <br>
	 * 4:createAndGo <br>
	 * 5:createAndWait <br>
	 * 6:destroy <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "The status of this table entry. Once the entry status is set
	 * to active, the associate entry cannot be modified until the sequence
	 * completes (ciscoPingCompleted is true)."
	 */
	public final String ciscoPingEntryStatus = ".1.3.6.1.4.1.9.9.16.1.1.1.16";

	/**
	 * Object ciscoPingMaxRtt <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.13 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units milliseconds <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "The maximum round trip time of all the packets that have
	 * been sent in this sequence.
	 * 
	 * This object will not be created until the first ping response in a
	 * sequence is received."
	 */
	public final String ciscoPingMaxRtt = ".1.3.6.1.4.1.9.9.16.1.1.1.13";

	/**
	 * Object ciscoPingMinRtt <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.11 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * Units milliseconds <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "The minimum round trip time of all the packets that have
	 * been sent in this sequence.
	 * 
	 * This object will not be created until the first ping response in a
	 * sequence is received."
	 */
	public final String ciscoPingMinRtt = ".1.3.6.1.4.1.9.9.16.1.1.1.11";

	/**
	 * Object ciscoPingPacketCount <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.4 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * Range 1 - 2147483647 <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description
	 * "Specifies the number of ping packets to send to the target in this sequence."
	 */
	public final String ciscoPingPacketCount = ".1.3.6.1.4.1.9.9.16.1.1.1.4";

	/**
	 * Object ciscoPingPacketSize <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.5 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "Specifies the size of ping packets to send to the target in
	 * this sequence. The lower and upper boundaries of this object are
	 * protocol-dependent. An instance of this object cannot be modified unless
	 * the associated instance of ciscoPingProtocol has been created (so as to
	 * allow protocol-specific range checking on the new value)."
	 */
	public final String ciscoPingPacketSize = ".1.3.6.1.4.1.9.9.16.1.1.1.5";

	/**
	 * Object ciscoPingPacketTimeout <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.6 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * Units milliseconds <br>
	 * Range 0 - 3600000 <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "Specifies the amount of time to wait for a response to a
	 * transmitted packet before declaring the packet 'dropped.'"
	 */
	public final String ciscoPingPacketTimeout = ".1.3.6.1.4.1.9.9.16.1.1.1.6";

	/**
	 * Object ciscoPingProtocol <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.2 <br>
	 * Type CiscoNetworkProtocol <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description
	 * "The protocol to use. Once an instance of this object is created, its value can not be changed."
	 */
	public final String ciscoPingProtocol = ".1.3.6.1.4.1.9.9.16.1.1.1.2";

	/**
	 * Object ciscoPingReceivedPackets <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.10 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description
	 * "The number of ping packets that have been received from the target in this sequence."
	 */
	public final String ciscoPingReceivedPackets = ".1.3.6.1.4.1.9.9.16.1.1.1.10";

	/**
	 * Object ciscoPingSentPackets <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.9 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description
	 * "The number of ping packets that have been sent to the target in this sequence."
	 */
	public final String ciscoPingSentPackets = ".1.3.6.1.4.1.9.9.16.1.1.1.9";

	/**
	 * Object ciscoPingSerialNumber <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.1 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission not-accessible<br>
	 * Status current <br>
	 * Range 1 - 2147483647<br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images Description "Object which
	 * specifies a unique entry in the ciscoPingTable. A management station
	 * wishing to initiate a ping operation should use a pseudo-random value for
	 * this object when creating or modifying an instance of a ciscoPingEntry.
	 * The RowStatus semantics of the ciscoPingEntryStatus object will prevent
	 * access conflicts."
	 */
	public final String ciscoPingSerialNumber = ".1.3.6.1.4.1.9.9.16.1.1.1.1";

	/**
	 * Object ciscoPingTrapOnCompletion <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.8 <br>
	 * Type TruthValue <br>
	 * 
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "Specifies whether or not a ciscoPingCompletion trap should
	 * be issued on completion of the sequence of pings. If such a trap is
	 * desired, it is the responsibility of the management entity to ensure that
	 * the SNMP administrative model is configured in such a way as to allow the
	 * trap to be delivered."
	 */
	public final String ciscoPingTrapOnCompletion = ".1.3.6.1.4.1.9.9.16.1.1.1.8";

	/**
	 * Object ciscoPingVrfName <br>
	 * OID 1.3.6.1.4.1.9.9.16.1.1.1.17 <br>
	 * Type OCTET STRING <br>
	 * Permission read-create <br>
	 * Status current <br>
	 * MIB CISCO-PING-MIB ; - View Supporting Images <br>
	 * Description "This field is used to specify the VPN name in which the ping
	 * will be used. For regular ping this field should not be configured. The
	 * agent will use this field to identify the VPN routing Table for this
	 * ping. This is the same ascii string used in the CLI to refer to this VPN.
	 * "
	 */
	public final String ciscoPingVrfName = ".1.3.6.1.4.1.9.9.16.1.1.1.17";

	public MibCiscoPingTable()
	{
		
	}

}
