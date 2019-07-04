package fxms.nms.co.snmp.mib;

public class HpMIB {

	/**
	 * Gauge Average number of jobs in the last 1 <br>
	 * Average number of jobs in the last 1 minute * 100.
	 */
	public final String computerSystemAvgJobs1 = ".1.3.6.1.4.1.11.2.3.1.1.3.0";
	/**
	 * Gauge Average number of jobs in the last 15 <br>
	 * Average number of jobs in the last 15 minutes * 100.
	 */
	public final String computerSystemAvgJobs15 = ".1.3.6.1.4.1.11.2.3.1.1.5.0";
	/**
	 * Gauge Average number of jobs in the last 5 <br>
	 * Average number of jobs in the last 5 minutes * 100.
	 */
	public final String computerSystemAvgJobs5 = ".1.3.6.1.4.1.11.2.3.1.1.4.0";
	/** Gauge Free memory. SunOS - not implemented. */
	public final String computerSystemFreeMemory = ".1.3.6.1.4.1.11.2.3.1.1.7.0";
	/** Gauge Currently free swap space. SunOS not implemented. */
	public final String computerSystemFreeSwap = ".1.3.6.1.4.1.11.2.3.1.1.12.0";
	/** INTEGER Physical memory. SunOS - not implemented. */
	public final String computerSystemPhysMemory = ".1.3.6.1.4.1.11.2.3.1.1.8.0";
	/** fileSystem OBJECT IDENTIFIER */
	public final String fileSystem = ".1.3.6.1.4.1.11.2.3.1.2";
	/** INTEGER Free blocks avail to non-superuser. */
	public final String fileSystemBavail = ".1.3.6.1.4.1.11.2.3.1.2.2.1.6";
	/** INTEGER Free blocks in file system. */
	public final String fileSystemBfree = ".1.3.6.1.4.1.11.2.3.1.2.2.1.5";
	/** INTEGER Total blocks in file system. */
	public final String fileSystemBlock = ".1.3.6.1.4.1.11.2.3.1.2.2.1.4";
	/** INTEGER Fundamental file system block size. */
	public final String fileSystemBsize = ".1.3.6.1.4.1.11.2.3.1.2.2.1.7";
	/** DisplayString File system path prefix. */
	public final String fileSystemDir = ".1.3.6.1.4.1.11.2.3.1.2.2.1.10";
	/**
	 * FileSystemEntry Each entry contains objects for a particular Each entry
	 * contains objects for a particular file system.
	 */
	public final String fileSystemEntry = ".1.3.6.1.4.1.11.2.3.1.2.2.1";
	/** INTEGER Free file nodes in file system. */
	public final String fileSystemFfree = ".1.3.6.1.4.1.11.2.3.1.2.2.1.9";

	/** INTEGER Total file nodes in file system. */
	public final String fileSystemFiles = ".1.3.6.1.4.1.11.2.3.1.2.2.1.8";

	/** INTEGER First file system ID. */
	public final String fileSystemID1 = ".1.3.6.1.4.1.11.2.3.1.2.2.1.1";

	/** INTEGER Second file system ID. */
	public final String fileSystemID2 = ".1.3.6.1.4.1.11.2.3.1.2.2.1.2";

	/** fileSystemMounted Gauge The number of file systems mounted. */
	public final String fileSystemMounted = ".1.3.6.1.4.1.11.2.3.1.2.1";
	/** DisplayString Name of mounted file system. */
	public final String fileSystemName = ".1.3.6.1.4.1.11.2.3.1.2.2.1.3";
	/** Table File system table. */
	public final String fileSystemTable = ".1.3.6.1.4.1.11.2.3.1.2.2";

	// .1.3.6.1.4.1.11.2.3.1.4.2.1.22

	/** INTEGER Address of process (in memory). */
	public final String processAddr = ".1.3.6.1.4.1.11.2.3.1.4.2.1.13";
	/** DisplayString Command the process is running. */
	public final String processCmd = ".1.3.6.1.4.1.11.2.3.1.4.2.1.22";
	/** Gauge Processor utilization for scheduling. */
	public final String processCPU = ".1.3.6.1.4.1.11.2.3.1.4.2.1.14";
	/** Counter Ticks of cpu time. */
	public final String processCPUticks = ".1.3.6.1.4.1.11.2.3.1.4.2.1.24";

	/**
	 * Counter Total ticks for life of process. SunOS - More... Total ticks for
	 * life of process. SunOS - not implemented.
	 */
	public final String processCPUticksTotal = ".1.3.6.1.4.1.11.2.3.1.4.2.1.25";
	/** Gauge Process data size. */
	public final String processDsize = ".1.3.6.1.4.1.11.2.3.1.4.2.1.5";
	/**
	 * ProcessEntry Each entry contains information about a More... Each entry
	 * contains information about a process running on the system.
	 */
	public final String processEntry = ".1.3.6.1.4.1.11.2.3.1.4.2.1";
	/** OBJECT IDENTIFIER - */
	public final String processes = ".1.3.6.1.4.1.11.2.3.1.4";
	/**
	 * Enum Possible values are:
	 * 
	 * incore, sys, locked, trace, trace2
	 * 
	 * Flags associated with process. SunOS - More... Flags associated with
	 * process. SunOS - values found in /usr/include/sys/proc.h.
	 */
	public final String processFlags = ".1.3.6.1.4.1.11.2.3.1.4.2.1.18";
	/**
	 * INTEGER Fair Share Schedular Group. SunOS - not More... Fair Share
	 * Schedular Group. SunOS - not implemented.
	 */
	public final String processFss = ".1.3.6.1.4.1.11.2.3.1.4.2.1.26";
	/**
	 * INTEGER Index for pstat() requests. SunOS - not More... Index for pstat()
	 * requests. SunOS - not implemented.
	 */
	public final String processIdx = ".1.3.6.1.4.1.11.2.3.1.4.2.1.2";
	/**
	 * INTEGER Process tty major number. SunOS - not More... Process tty major
	 * number. SunOS - not implemented.
	 */
	public final String processMajor = ".1.3.6.1.4.1.11.2.3.1.4.2.1.9";
	/**
	 * INTEGER Process tty minor number. SunOS - not More... Process tty minor
	 * number. SunOS - not implemented.
	 */
	public final String processMinor = ".1.3.6.1.4.1.11.2.3.1.4.2.1.10";
	/** Gauge Process nice value. */
	public final String processNice = ".1.3.6.1.4.1.11.2.3.1.4.2.1.8";
	/** Gauge The number of processes running */
	public final String processNum = ".1.3.6.1.4.1.11.2.3.1.4.1";
	/** Gauge Percent CPU * 100 for this process. */
	public final String processPctCPU = ".1.3.6.1.4.1.11.2.3.1.4.2.1.27";

	/** INTEGER Process group of this process. */
	public final String processPgrp = ".1.3.6.1.4.1.11.2.3.1.4.2.1.11";
	/** INTEGER The process ID (pid). */
	public final String processPID = ".1.3.6.1.4.1.11.2.3.1.4.2.1.1";

	/** INTEGER Parent process ID. */
	public final String processPPID = ".1.3.6.1.4.1.11.2.3.1.4.2.1.4";
	/** INTEGER Process priority. */
	public final String processPrio = ".1.3.6.1.4.1.11.2.3.1.4.2.1.12";
	/**
	 * INTEGER Processor this process last run on. SunOS - More... Processor
	 * this process last run on. SunOS - not implemented.
	 */
	public final String processProcNum = ".1.3.6.1.4.1.11.2.3.1.4.2.1.21";
	/**
	 * Gauge Resident Set Size for process (private More... Resident Set Size
	 * for process (private pages).
	 */
	public final String processRssize = ".1.3.6.1.4.1.11.2.3.1.4.2.1.28";
	/** Gauge Process stack size. */
	public final String processSsize = ".	1.3.6.1.4.1.11.2.3.1.4.2.1.7";
	/** TimeTicks Time Process started. */
	public final String processStart = ".1.3.6.1.4.1.11.2.3.1.4.2.1.17";
	/**
	 * Enum Possible values are:
	 * 
	 * sleep, run, stop, zombie, other, idle
	 * 
	 * The process status. SunOS - sleep(1), More... The process status. SunOS -
	 * sleep(1), wait(2), run(3), idle (4), zombie(5), stop(6)
	 */
	public final String processStatus = ".1.3.6.1.4.1.11.2.3.1.4.2.1.19";

	/** TimeTicks System time spent executing. */
	public final String processStime = ".1.3.6.1.4.1.11.2.3.1.4.2.1.16";
	/** INTEGER saved UID. */
	public final String processSUID = ".1.3.6.1.4.1.11.2.3.1.4.2.1.29";

	/** Table Processes Table. */
	public final String processTable = ".1.3.6.1.4.1.11.2.3.1.4.2";
	/** INTEGER Resident time for scheduling. */
	public final String processTime = ".1.3.6.1.4.1.11.2.3.1.4.2.1.23";
	/** Gauge Process text size. */
	public final String processTsize = ".1.3.6.1.4.1.11.2.3.1.4.2.1.6";
	/** DisplayString Process TTY. SunOS - not implemented. */
	public final String processTTY = ".1.3.6.1.4.1.11.2.3.1.4.2.1.31";
	/** INTEGER Process User ID. */
	public final String processUID = ".1.3.6.1.4.1.11.2.3.1.4.2.1.3";
	/** DisplayString User name. */
	public final String processUname = ".1.3.6.1.4.1.11.2.3.1.4.2.1.30";
	/** TimeTicks User time spent executing. */
	public final String processUtime = ".1.3.6.1.4.1.11.2.3.1.4.2.1.15";
	/**
	 * INTEGER If processStatus is sleep, value sleeping on. More... If
	 * processStatus is sleep, value sleeping on.
	 */
	public final String processWchan = ".1.3.6.1.4.1.11.2.3.1.4.2.1.20";

	public HpMIB() {

	}

	public String[] splitProcessName_Path_Para(String str) {
		int pos = str.indexOf(' ');
		if (pos > 0) {
			int pos2 = str.substring(0, pos).lastIndexOf('/');
			if (pos2 > 0) return new String[] { str.substring(0, pos).substring(pos2 + 1), str.substring(0, pos),
					str.substring(pos) };
			return new String[] { str.substring(0, pos), str.substring(0, pos), str.substring(pos) };
		}

		return new String[] { str, str, "" };
	}
}
