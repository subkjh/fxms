package com.daims.dfc.mib;

public class BRIDGE_MIB {
	/**
	 * Object dot1dTpFdbPort <br>
	 * OID 1.3.6.1.2.1.17.4.3.1.2 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status mandatory <br>
	 * MIB BRIDGE-MIB ; - View Supporting Images this link will generate a new
	 * window Description "Either the value '0', or the port number of the port
	 * on which a frame having a source address equal to the value of the
	 * corresponding instance of dot1dTpFdbAddress has been seen. A value of '0'
	 * indicates that the port number has not been learned but that the bridge
	 * does have some forwarding/filtering information about this address (e.g.
	 * in the dot1dStaticTable). Implementors are encouraged to assign the port
	 * value to this object whenever it is learned even for addresses for which
	 * the corresponding value of dot1dTpFdbStatus is not learned(3)."
	 */
	public String dot1dTpFdbPort = ".1.3.6.1.2.1.17.4.3.1.2";

	/**
	 * Object dot1dTpFdbStatus <br>
	 * OID 1.3.6.1.2.1.17.4.3.1.3 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status mandatory <br>
	 * Values 1 : other<br>
	 * 2 : invalid<br>
	 * 3 : learned<br>
	 * 4 : self<br>
	 * 5 : mgmt<br>
	 * 
	 * MIB BRIDGE-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The status of this entry. The meanings of the values
	 * are:
	 * 
	 * other(1) : none of the following. This would include the case where some
	 * other MIB object (not the corresponding instance of dot1dTpFdbPort, nor
	 * an entry in the dot1dStaticTable) is being used to determine if and how
	 * frames addressed to the value of the corresponding instance of
	 * dot1dTpFdbAddress are being forwarded.
	 * 
	 * invalid(2) : this entry is not longer valid (e.g., it was learned but has
	 * since aged-out), but has not yet been flushed from the table.
	 * 
	 * learned(3) : the value of the corresponding instance of dot1dTpFdbPort
	 * was learned, and is being used.
	 * 
	 * self(4) : the value of the corresponding instance of dot1dTpFdbAddress
	 * represents one of the bridge's addresses. The corresponding instance of
	 * dot1dTpFdbPort indicates which of the bridge's ports has this address.
	 * 
	 * mgmt(5) : the value of the corresponding instance of dot1dTpFdbAddress is
	 * also the value of an existing instance of dot1dStaticAddress."
	 */
	public String dot1dTpFdbStatus = ".1.3.6.1.2.1.17.4.3.1.3";
}
