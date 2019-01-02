package com.fxms.nms.snmp.mib;

public class HOST_RESOURCES_MIB {

	/**
	 * Object hrSystemUptime <br>
	 * OID 1.3.6.1.2.1.25.1.1 <br>
	 * Type TimeTicks <br>
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The amount
	 * of time since this host was last initialized. Note that this is different
	 * from sysUpTime in the SNMPv2-MIB [RFC1907] because sysUpTime is the
	 * uptime of the network management portion of the system."
	 */
	public final String hrSystemUptime = ".1.3.6.1.2.1.25.1.1.0";

	/**
	 * Object hrSystemDate <br>
	 * OID 1.3.6.1.2.1.25.1.2 <br>
	 * Type DateAndTime <br>
	 * 
	 * Permission read-write <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description The host's
	 * notion of the local date and time of day.
	 */
	public final String hrSystemDate = ".1.3.6.1.2.1.25.1.2.0";

	/**
	 * Object hrSystemNumUsers <br>
	 * OID 1.3.6.1.2.1.25.1.5 <br>
	 * Type Gauge32 <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The number
	 * of user sessions for which this host is storing state information. A
	 * session is a collection of processes requiring a single act of user
	 * authentication and possibly subject to collective job control."
	 */
	public final String hrSystemNumUsers = ".1.3.6.1.2.1.25.1.5.0";
	/**
	 * Object hrSystemProcesses<br>
	 * OID 1.3.6.1.2.1.25.1.6 <br>
	 * Type Gauge32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The number
	 * of process contexts currently loaded or running on this system."
	 */
	public final String hrSystemProcesses = ".1.3.6.1.2.1.25.1.6.0";

	/**
	 * Object hrDeviceIndex <br>
	 * OID 1.3.6.1.2.1.25.3.2.1.1 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * Range 1 - 2147483647<br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "A unique
	 * value for each device contained by the host. The value for each device
	 * must remain constant at least from one re-initialization of the agent to
	 * the next re-initialization."
	 */
	public final String hrDeviceIndex = ".1.3.6.1.2.1.25.3.2.1.1";

	/**
	 * Object hrDeviceType <br>
	 * OID 1.3.6.1.2.1.25.3.2.1.2<br>
	 * Type AutonomousType <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "An
	 * indication of the type of device.
	 * 
	 * If this value is `hrDeviceProcessor { hrDeviceTypes 3 }' then an entry
	 * exists in the hrProcessorTable which corresponds to this device.
	 * 
	 * If this value is `hrDeviceNetwork { hrDeviceTypes 4 }', then an entry
	 * exists in the hrNetworkTable which corresponds to this device.
	 * 
	 * If this value is `hrDevicePrinter { hrDeviceTypes 5 }', then an entry
	 * exists in the hrPrinterTable which corresponds to this device.
	 * 
	 * If this value is `hrDeviceDiskStorage { hrDeviceTypes 6 }', then an entry
	 * exists in the hrDiskStorageTable which corresponds to this device."
	 * 
	 * 
	 */
	public final String hrDeviceType = ".1.3.6.1.2.1.25.3.2.1.2";

	/**
	 * Object hrDeviceDescr <br>
	 * OID 1.3.6.1.2.1.25.3.2.1.3 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "A textual
	 * description of this device, including the device's manufacturer and
	 * revision, and optionally, its serial number."
	 */
	public final String hrDeviceDescr = ".1.3.6.1.2.1.25.3.2.1.3";

	/**
	 * Object hrDeviceID <br>
	 * OID 1.3.6.1.2.1.25.3.2.1.4<br>
	 * Type ProductID <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description The product
	 * ID for this device.
	 */
	public final String hrDeviceID = ".1.3.6.1.2.1.25.3.2.1.4";

	/**
	 * Object hrDeviceStatus <br>
	 * OID 1.3.6.1.2.1.25.3.2.1.5<br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * Values 1 : unknown 2 : running 3 : warning 4 : testing 5 : down<br>
	 * 
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The
	 * current operational state of the device described by this row of the
	 * table. A value unknown(1) indicates that the current state of the device
	 * is unknown. running(2) indicates that the device is up and running and
	 * that no unusual error conditions are known. The warning(3) state
	 * indicates that agent has been informed of an unusual error condition by
	 * the operational software (e.g., a disk device driver) but that the device
	 * is still 'operational'. An example would be a high number of soft errors
	 * on a disk. A value of testing(4), indicates that the device is not
	 * available for use because it is in the testing state. The state of
	 * down(5) is used only when the agent has been informed that the device is
	 * not available for any use."
	 */
	public final String hrDeviceStatus = ".1.3.6.1.2.1.25.3.2.1.5";

	/**
	 * 
	 Object hrDeviceErrors<br>
	 * OID 1.3.6.1.2.1.25.3.2.1.6<br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The number
	 * of errors detected on this device. It should be noted that as this object
	 * has a SYNTAX of Counter32, that it does not have a defined initial value.
	 * However, it is recommended that this object be initialized to zero, even
	 * though management stations must not depend on such an initialization."
	 */
	public final String hrDeviceErrors = ".1.3.6.1.2.1.25.3.2.1.6";

	/**
	 * The (conceptual) table of processors contained by the host.
	 * 
	 * Note that this table is potentially sparse: a (conceptual) entry exists
	 * only if the correspondent value of the hrDeviceType object is
	 * `hrDeviceProcessor'.
	 * 
	 * @author subkjh
	 * 
	 */


	/**
	 * Object hrDeviceProcessor <br>
	 * OID 1.3.6.1.2.1.25.3.1.3 <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-TYPES ; - View Supporting Images Description The
	 * device type identifier used for a CPU.
	 */
	public final String hrDeviceProcessor = ".1.3.6.1.2.1.25.3.1.3";
	

	/** The product ID of the firmware associated with the processor. */
	public final String hrProcessorFrwID = ".1.3.6.1.2.1.25.3.3.1.1";

	/**
	 * The average, over the last minute, of the percentage of time that this
	 * processor was not idle.
	 */
	public final String hrProcessorLoad = ".1.3.6.1.2.1.25.3.3.1.2";
	

	/**
	 * "The (conceptual) table of logical storage areas on the host.
	 * 
	 * An entry shall be placed in the storage table for each logical area of
	 * storage that is allocated and has fixed resource limits. The amount of
	 * storage represented in an entity is the amount actually usable by the
	 * requesting entity, and excludes loss due to formatting or file system
	 * reference information.
	 * 
	 * These entries are associated with logical storage areas, as might be seen
	 * by an application, rather than physical storage entities which are
	 * typically seen by an operating system. Storage such as tapes and floppies
	 * without file systems on them are typically not allocated in chunks by the
	 * operating system to requesting applications, and therefore shouldn't
	 * appear in this table. Examples of valid storage for this table include
	 * disk partitions, file systems, ram (for some architectures this is
	 * further segmented into regular memory, extended memory, and so on),
	 * backing store for virtual memory (`swap space').
	 * 
	 * This table is intended to be a useful diagnostic for `out of memory' and
	 * `out of buffers' types of failures. In addition, it can be a useful
	 * performance monitoring tool for tracking memory, disk, or buffer usage."
	 */
	public final String hrStorageTable = ".1.3.6.1.2.1.25.2.3";

	/**
	 * A (conceptual) entry for one logical storage area on the host. As an
	 * example, an instance of the hrStorageType object might be named
	 * hrStorageType.3
	 */
	public final String hrStorageEntry = ".1.3.6.1.2.1.25.2.3.1";

	/**
	 * Object hrStorageIndex <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.1 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * Range 1 - 2147483647<br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "A unique
	 * value for each logical storage area contained by the host."
	 */
	public final String hrStorageIndex = ".1.3.6.1.2.1.25.2.3.1.1";

	/**
	 * Object hrStorageType <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.2 <br>
	 * Type AutonomousType <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description The type of
	 * storage represented by this entry.
	 */
	public final String hrStorageType = ".1.3.6.1.2.1.25.2.3.1.2";

	/**
	 * Object hrStorageDescr <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.3 <br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "A
	 * description of the type and instance of the storage described by this
	 * entry."
	 */
	public final String hrStorageDescr = ".1.3.6.1.2.1.25.2.3.1.3";

	/**
	 * Object hrStorageAllocationUnits <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.4 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * Units Bytes <br>
	 * Range 1 - 2147483647<br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The size,
	 * in bytes, of the data objects allocated from this pool. If this entry is
	 * monitoring sectors, blocks, buffers, or packets, for example, this number
	 * will commonly be greater than one. Otherwise this number will typically
	 * be one."
	 */
	public final String hrStorageAllocationUnits = ".1.3.6.1.2.1.25.2.3.1.4";

	/**
	 * Object hrStorageSize <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.5 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-write<br>
	 * Status current <br>
	 * Range 0 - 2147483647<br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The size
	 * of the storage represented by this entry, in units of
	 * hrStorageAllocationUnits. This object is writable to allow remote
	 * configuration of the size of the storage area in those cases where such
	 * an operation makes sense and is possible on the underlying system. For
	 * example, the amount of main memory allocated to a buffer pool might be
	 * modified or the amount of disk space allocated to virtual memory might be
	 * modified."
	 */
	public final String hrStorageSize = ".1.3.6.1.2.1.25.2.3.1.5";

	/**
	 * Object hrStorageUsed <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.6 <br>
	 * Type Integer32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * Range 0 - 2147483647<br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The amount
	 * of the storage represented by this entry that is allocated, in units of
	 * hrStorageAllocationUnits."
	 */
	public final String hrStorageUsed = ".1.3.6.1.2.1.25.2.3.1.6";

	/**
	 * Object hrStorageAllocationFailures <br>
	 * OID 1.3.6.1.2.1.25.2.3.1.7 <br>
	 * Type Counter32 <br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB HOST-RESOURCES-MIB ; - View Supporting Images Description "The number
	 * of requests for storage represented by this entry that could not be
	 * honored due to not enough storage. It should be noted that as this object
	 * has a SYNTAX of Counter32, that it does not have a defined initial value.
	 * However, it is recommended that this object be initialized to zero, even
	 * though management stations must not depend on such an initialization."
	 */
	public final String hrStorageAllocationFailures = ".1.3.6.1.2.1.25.2.3.1.7";
	
//	public enum hrStorageTypes {
//		
//		hrStorageOther(1)	
//		, hrStorageRam(2)
//		, hrStorageVirtualMemory(3)
//		, hrStorageFixedDisk(4)
//		, hrStorageRemovableDisk(5)
//		, hrStorageFloppyDisk(6)
//		, hrStorageCompactDisc(7)
//		, hrStorageRamDisk(8)
//		, hrStorageFlashMemory(9)
//		, hrStorageNetworkDisk(10);
//		
//		private int index;
//		
//		private hrStorageTypes(int index) {
//			this.index = index;
//		}
//		
//		public static String getOid(hrStorageTypes e) {
//			return ".1.3.6.1.2.1.25.2.1." + e.index;
//		}
//		
//		public static hrStorageTypes fromOid(String oid) {
//			for ( hrStorageTypes e : hrStorageTypes.values() ) {
//				if ( oid.equals(getOid(e))) return e;
//			}
//			return null;
//		}
//		
//		public static hrStorageTypes fromIndex(int index) {
//			for ( hrStorageTypes e : hrStorageTypes.values() ) {
//				if ( e.index == index ) return e;
//			}
//			return null;
//		}
//	}

	public final String hrStorageOther         = ".1.3.6.1.2.1.25.2.1.1";
	public final String hrStorageRam           = ".1.3.6.1.2.1.25.2.1.2";
	public final String hrStorageVirtualMemory = ".1.3.6.1.2.1.25.2.1.3";
	public final String hrStorageFixedDisk     = ".1.3.6.1.2.1.25.2.1.4";
	public final String hrStorageRemovableDisk = ".1.3.6.1.2.1.25.2.1.5";
	public final String hrStorageFloppyDisk    = ".1.3.6.1.2.1.25.2.1.6";
	public final String hrStorageCompactDisc   = ".1.3.6.1.2.1.25.2.1.7";
	public final String hrStorageRamDisk       = ".1.3.6.1.2.1.25.2.1.8";
	public final String hrStorageFlashMemory   = ".1.3.6.1.2.1.25.2.1.9";
	public final String hrStorageNetworkDisk   = ".1.3.6.1.2.1.25.2.1.10";


   
   

	/**
	 * A unique value for each piece of software running on the host. Wherever
	 * possible, this should be the system's native, unique identification
	 * number. <br>
	 * <br>
	 * INTEGER ( 1 .. 2147483647 )
	 */
	public final String hrSWRunIndex = ".1.3.6.1.2.1.25.4.2.1.1";

	/**
	 * A textual description of this running piece of software, including the
	 * manufacturer, revision, and the name by which it is commonly known. If
	 * this software was installed locally, this should be the same string as
	 * used in the corresponding hrSWInstalledName. <br>
	 * <br>
	 * InternationalDisplayString ( SIZE ( 0 .. 64 ) )
	 */
	public final String hrSWRunName = ".1.3.6.1.2.1.25.4.2.1.2";

	/**
	 * The product ID of this running piece of software. <br>
	 * <br>
	 * ProductID
	 */
	public final String hrSWRunID = ".1.3.6.1.2.1.25.4.2.1.3";

	/**
	 * A description of the location on long-term storage (e.g. a disk drive)
	 * from which this software was loaded. <br>
	 * <br>
	 * InternationalDisplayString ( SIZE ( 0 .. 128 ) )
	 */
	public final String hrSWRunPath = ".1.3.6.1.2.1.25.4.2.1.4";

	/**
	 * A description of the parameters supplied to this software when it was
	 * initially loaded. <br>
	 * <br>
	 * InternationalDisplayString ( SIZE ( 0 .. 128 ) )
	 */
	public final String hrSWRunParameters = ".1.3.6.1.2.1.25.4.2.1.5";

	/**
	 * The type of this software. <br>
	 * <br>
	 * INTEGER { unknown ( 1 ) , operatingSystem ( 2 ) , deviceDriver ( 3 ) ,
	 * application ( 4 ) }
	 */
	public final String hrSWRunType = ".1.3.6.1.2.1.25.4.2.1.6";

	/**
	 * The status of this running piece of software. Setting this value to
	 * invalid(4) shall cause this software to stop running and to be unloaded. <br>
	 * <br>
	 * INTEGER { running ( 1 ) , runnable ( 2 ) , notRunnable ( 3 ) , invalid (
	 * 4 ) }
	 */
	public final String hrSWRunStatus = ".1.3.6.1.2.1.25.4.2.1.7";

	/**
	 * The number of centi-seconds of the total system's CPU resources consumed
	 * by this process. Note that on a multi-processor system, this value may
	 * increment by more than one centi-second in one centi-second of real (wall
	 * clock) time. <br>
	 * <br>
	 * INTEGER
	 */
	public final String hrSWRunPerfCPU = ".1.3.6.1.2.1.25.5.1.1.1";

	/**
	 * The total amount of real system memory allocated to this process. <br>
	 * <br>
	 * KBytes
	 */
	public final String hrSWRunPerfMem = ".1.3.6.1.2.1.25.5.1.1.2";

}
