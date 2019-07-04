package fxms.nms.co.snmp.mib;

public class SNMPV2_MIB {

	/**
	 * Object sysDescr <br>
	 * OID 1.3.6.1.2.1.1.1 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "A textual description of the entity. This value
	 * should include the full name and version identification of the system's
	 * hardware type, software operating-system, and networking software."
	 */

	public final String sysDescr = ".1.3.6.1.2.1.1.1.0";

	/**
	 * Object sysObjectID <br>
	 * OID 1.3.6.1.2.1.1.2 <br>
	 * Type OBJECT IDENTIFIER <br>
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The vendor's authoritative identification of the
	 * network management subsystem contained in the entity. This value is
	 * allocated within the SMI enterprises subtree (1.3.6.1.4.1) and provides
	 * an easy and unambiguous means for determining `what kind of box' is being
	 * managed. For example, if vendor `Flintstones, Inc.' was assigned the
	 * subtree 1.3.6.1.4.1.424242, it could assign the identifier
	 * 1.3.6.1.4.1.424242.1.1 to its `Fred Router'."
	 */
	public final String sysObjectID = ".1.3.6.1.2.1.1.2.0";

	/**
	 * Object sysUpTime <br>
	 * OID 1.3.6.1.2.1.1.3 <br>
	 * Type TimeTicks <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The time (in hundredths of a second) since the
	 * network management portion of the system was last re-initialized."
	 */
	public final String sysUpTime = ".1.3.6.1.2.1.1.3.0";

	/**
	 * Object sysContact <br>
	 * OID 1.3.6.1.2.1.1.4 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-write <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The textual identification of the contact person for
	 * this managed node, together with information on how to contact this
	 * person. If no contact information is known, the value is the zero-length
	 * string."
	 */
	public final String sysContact = ".1.3.6.1.2.1.1.4.0";

	/**
	 * Object sysName <br>
	 * OID 1.3.6.1.2.1.1.5 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-write <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "An administratively-assigned name for this managed
	 * node. By convention, this is the node's fully-qualified domain name. If
	 * the name is unknown, the value is the zero-length string."
	 */
	public final String sysName = ".1.3.6.1.2.1.1.5.0";

	/**
	 * Object sysLocation <br>
	 * OID 1.3.6.1.2.1.1.6 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-write <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The physical location of this node (e.g., 'telephone
	 * closet, 3rd floor'). If the location is unknown, the value is the
	 * zero-length string."
	 */
	public final String sysLocation = ".1.3.6.1.2.1.1.6.0";

	/**
	 * Object sysServices <br>
	 * OID 1.3.6.1.2.1.1.7 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * Range 0 - 127 <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "A value which indicates the set of services that this
	 * entity may potentially offer. The value is a sum. This sum initially
	 * takes the value zero. Then, for each layer, L, in the range 1 through 7,
	 * that this node performs transactions for, 2 raised to (L - 1) is added to
	 * the sum. For example, a node which performs only routing functions would
	 * have a value of 4 (2^(3-1)). In contrast, a node which is a host offering
	 * application services would have a value of 72 (2^(4-1) + 2^(7-1)). Note
	 * that in the context of the Internet suite of protocols, values should be
	 * calculated accordingly:
	 * 
	 * layer functionality 1 physical (e.g., repeaters) 2 datalink/subnetwork
	 * (e.g., bridges) 3 internet (e.g., supports the IP) 4 end-to-end (e.g.,
	 * supports the TCP) 7 applications (e.g., supports the SMTP)
	 * 
	 * For systems including OSI protocols, layers 5 and 6 may also be counted."
	 */
	public final String sysServices = ".1.3.6.1.2.1.1.7.0";

	/**
	 * Object coldStart <br>
	 * OID 1.3.6.1.6.3.1.1.5.1<br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "A coldStart trap signifies that the SNMP entity,
	 * supporting a notification originator application, is reinitializing
	 * itself and that its configuration may have been altered."
	 */
	public final String coldStart = ".1.3.6.1.6.3.1.1.5.1";

	/**
	 * bject warmStart <br>
	 * OID 1.3.6.1.6.3.1.1.5.2<br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images<br>
	 * Description "A warmStart trap signifies that the SNMP entity, supporting
	 * a notification originator application, is reinitializing itself such that
	 * its configuration is unaltered."
	 */
	public final String warmStart = ".1.3.6.1.6.3.1.1.5.2";

	/**
	 * Object authenticationFailure <br>
	 * OID 1.3.6.1.6.3.1.1.5.5 <br>
	 * Status current <br>
	 * MIB SNMPv2-MIB ; - View Supporting Images this link will generate a new
	 * window Description "An authenticationFailure trap signifies that the SNMP
	 * entity has received a protocol message that is not properly
	 * authenticated. While all implementations of SNMP entities MAY be capable
	 * of generating this trap, the snmpEnableAuthenTraps object indicates
	 * whether this trap will be generated."
	 */
	public final String authenticationFailure = ".1.3.6.1.6.3.1.1.5.5";
	
	public final String egpNeighborLoss = ".1.3.6.1.6.3.1.1.5.6";


}
