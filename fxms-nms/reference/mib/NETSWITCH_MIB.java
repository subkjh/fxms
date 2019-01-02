package com.daims.dfc.mib;

public class NETSWITCH_MIB {
	/**
	 * name hpSwitchCpuStat<br>
	 * Oid 1.3.6.1.4.1.11.2.14.11.5.1.9.6.1<br>
	 * Path <br>
	 * Base-Type integer<br>
	 * Value-Ranges <br>
	 * Access read-only<br>
	 * Status mandatory<br>
	 * Description<br>
	 * The CPU utilization in percent(%).<br>
	 */
	public final String hpSwitchCpuStat = ".1.3.6.1.4.1.11.2.14.11.5.1.9.6.1.0";

	/**
	 * Name hpLocalMemTotalBytes<br>
	 * Oid 1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.5<br>
	 * Path iso . org . dod . internet . private . enterprises . hp . nm . icf .
	 * hpicfObjects . hpicfSwitch . hpSwitch . hpOpSystem . hpMem . hpLocalMem .
	 * hpLocalMemTable . hpLocalMemEntry . hpLocalMemTotalBytes<br>
	 * Base-Type integer<br>
	 * Access read-only<br>
	 * Status mandatory<br>
	 * Description<br>
	 * The number of currently installed bytes.<br>
	 */
	public final String hpLocalMemTotalBytes = ".1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.5";

	/**
	 * Name hpLocalMemFreeBytes<br>
	 * Oid 1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.6<br>
	 * Path iso . org . dod . internet . private . enterprises . hp . nm . icf .
	 * hpicfObjects . hpicfSwitch . hpSwitch . hpOpSystem . hpMem . hpLocalMem .
	 * hpLocalMemTable . hpLocalMemEntry . hpLocalMemFreeBytes<br>
	 * Base-Type integer<br>
	 * Access read-only<br>
	 * Status mandatory<br>
	 * Description<br>
	 * The number of available (unallocated) bytes.<br>
	 */
	public final String hpLocalMemFreeBytes = ".1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.6";

	/**
	 * Name hpLocalMemAllocBytes<br>
	 * Oid 1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.7<br>
	 * Path iso . org . dod . internet . private . enterprises . hp . nm . icf .
	 * hpicfObjects . hpicfSwitch . hpSwitch . hpOpSystem . hpMem . hpLocalMem .
	 * hpLocalMemTable . hpLocalMemEntry . hpLocalMemAllocBytes<br>
	 * Base-Type integer<br>
	 * Access read-only<br>
	 * Status mandatory<br>
	 * Description<br>
	 * The number of currently allocated bytes.<br>
	 */
	public final String hpLocalMemAllocBytes = ".1.3.6.1.4.1.11.2.14.11.5.1.1.2.1.1.1.7";

	/**
	 * Object hh3cEntityExtCpuUsage<br>
	 * OID 1.3.6.1.4.1.25506.2.6.1.1.1.1.6<br>
	 * Description CPU entity usage during the last one minute<br>
	 * Type Column object<br>
	 * Remarks <br>
	 * l This object contains many entities (leaf nodes), the object OID of
	 * which is 1.3.6.1.4.1.25506.2.6.1.1.1.1.6.n. The value of n (hereinafter
	 * referred to as entity number) varies with entities. l The number of a CPU
	 * entity is decided by objects entPhysicalDescr (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.2), entPhysicalClass (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.5), and entPhysicalName (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.7) in file hh3c-entity-ext.mib. l If the value of
	 * an entity is always 0, the entity is not a CPU entity.
	 */
	public final String hh3cEntityExtCpuUsage = ".1.3.6.1.4.1.25506.2.6.1.1.1.1.6";

	/**
	 * hh3cEntityExtMemUsage OBJECT-TYPE<br>
	 * SYNTAX Integer32 (0..100)<br>
	 * MAX-ACCESS read-only<br>
	 * STATUS current<br>
	 * DESCRIPTION "The memory usage for the entity. This object indicates what
	 * percent of memory are used."<br>
	 * ::= { hh3cEntityExtStateEntry 8 }
	 */
	public final String hh3cEntityExtMemUsage = ".1.3.6.1.4.1.25506.2.6.1.1.1.1.8";

	/**
	 * hh3cEntityExtMemSize OBJECT-TYPE<br>
	 * SYNTAX Unsigned32<br>
	 * UNITS "bytes"<br>
	 * MAX-ACCESS read-only<br>
	 * STATUS current<br>
	 * DESCRIPTION "The size of memory for the entity.<br>
	 * 
	 * If the amount of memory exceeds 4,294,967,295 bytes, the value remains
	 * 4,294,967,295 bytes."<br>
	 * ::= { hh3cEntityExtStateEntry 10 }<br>
	 */
	public final String hh3cEntityExtMemSize = ".1.3.6.1.4.1.25506.2.6.1.1.1.1.10";

	public final String hh3cEntityExtTemperature = ".1.3.6.1.4.1.25506.2.6.1.1.1.1.12";

	/**
	 * HP (latest) (hpn) <br>
	 * 1.3.6.1.4.1.11. <br>
	 * 1.3.6.1.4.1.11.2.14. 11.15.2.6.1.1.1.1.6 <br>
	 * hpnicfEntityExtCpuUsage<br>
	 */
	public final String hpnicfEntityExtCpuUsage = ".1.3.6.1.4.1.11.2.14.11.15.2.6.1.1.1.1.6.0";

	/**
	 * Object h3cEntityExtCpuUsage<br>
	 * OID 1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6<br>
	 * Description CPU entity usage during the last one minute<br>
	 * Type Column object<br>
	 * Remarks <br>
	 * l This object contains many entities (leaf nodes), the object OID of
	 * which is 1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6.n. The value of n (hereinafter
	 * referred to as entity number) varies with entities. l The number of a CPU
	 * entity is decided by objects entPhysicalDescr (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.2), entPhysicalClass (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.5), and entPhysicalName (with the OID of
	 * 1.3.6.1.2.1.47.1.1.1.1.7) in file h3c-entity-ext.mib. l If the value of
	 * an entity is always 0, the entity is not a CPU entity.
	 */

	public final String h3cEntityExtCpuUsage = ".1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6";

	/**
	 * 3Com (a3Com) <br>
	 * 1.3.6.1.4.1.43 <br>
	 * .1.3.6.1.4.1.43.45.1.10.2.6.1.1.1.1.6 <br>
	 * h3cEntityExtCpuUsage<br>
	 */

	public final String h3cEntityExtCpuUsage_3Com = "..1.3.6.1.4.1.43.45.1.10.2.6.1.1.1.1.6.0";
}
