package com.daims.dfc.mib;

/**
 * OID : .1.3.6.1.3.61.xxx
 * 
 * @author subkjh
 * 
 */
public class PIM_MIB {
	/**
	 * The IP address of the PIM interface.
	 */
	public final String pimInterfaceAddress = ".1.3.6.1.3.61.1.1.2.1.2";
	
	
	/**
	 * The network mask for the IP address of the PIM interface.
	 */
	public final String pimInterfaceNetMask = ".1.3.6.1.3.61.1.1.2.1.3";

	/**
	 * PIM 모드
	 * 
	 * 단위 : 1:dense, 2:sparse, 3:sparseDense
	 * The configured mode of this PIM interface.  A value of sparseDense is only valid for PIMv1.
	 * 
	 */
	public final String pimInterfaceMode =  ".1.3.6.1.3.61.1.1.2.1.4";
	
	
	/**
	 * PIM DR Address
	 * The Designated Router on this PIM interface.  For point-to-point interfaces, this object has the value 0.0.0.0
	 */
	public final String pimInterfaceDR = ".1.3.6.1.3.61.1.1.2.1.5";
	
	
	/**
	 * PIM 상태
	 * 단위 : 1:active, 2:notInService, 3:notReady, 4:createAndGo, 5:createAndWait, 6:destroy
	 * The status of this entry.  Creating the entry enables PIM on the interface; destroying the entry disables PIM on the interface
	 */
	public final String pimInterfaceStatus = ".1.3.6.1.3.61.1.1.2.1.7";
	
	
	/**
	 * ifIndex. neighbor address를 알 수 있음
	 * The value of ifIndex for the interface used to reach this PIM neighbor.
	 */
	public final String pimNeighborIfIndex = ".1.3.6.1.3.61.1.1.3.1.2";
	
	
	/**
	 * Up Time
	 * The time since this PIM neighbor (last) became a neighbor of the local router.
	 */
	public final String pimNeighborUpTime = ".1.3.6.1.3.61.1.1.3.1.3";
	
	
	/**
	 * Expire Time
	 * The minimum time remaining before this PIM neighbor will be aged out.
	 */
	public final String pimNeighborExpiryTime = ".1.3.6.1.3.61.1.1.3.1.4";
	
	
	/**
	 * neighbor 모드
	 * 단위 : 1:dense, 2:sparse
	 * The active PIM mode of this neighbor.  This object is deprecated for PIMv2 routers since all neighbors on
	 *  the interface must be either dense or sparse as determined by the protocol running on the interface.
	 */
	public final String pimNeighborMode = ".1.3.6.1.3.61.1.1.3.1.5";
}
