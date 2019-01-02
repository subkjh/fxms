package com.daims.dfc.mib;

public class ALTEON_CHEETAH_LAYER4_MIB {
	/**
	 * Virtual IP Index <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.1
	 */
	public final String slbCurCfgVirtServerIndex = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.1";
	/**
	 * Virtual Service Index <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.2
	 */
	public final String slbCurCfgVirtServiceIndex = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.2";

	/**
	 * Virtual Service Info Index <br>
	 * .1.3.6.1.4.1.1872.2.5.4.3.4.1.2
	 */
	public final String slbVirtServicesInfoSvcIndex = ".1.3.6.1.4.1.1872.2.5.4.3.4.1.2";

	/**
	 * Virtual 서버 IP <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.2
	 */
	public final String slbCurCfgVirtServerIpAddress = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.2";

	/**
	 * Virtual 서버 Port <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.3
	 */
	public final String slbCurCfgVirtServiceVirtPort = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.3";

	/**
	 * Virtual 서버 상태<br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.4<br>
	 * enabled(2) disabled(3)
	 */
	public final String slbCurCfgVirtServerState = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.2.1.4";

	/**
	 * Virtual 서비스 소속 그룹 <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.4
	 */
	public final String slbCurCfgVirtServiceRealGroup = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.4";

	/**
	 * Real IP Index <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.1
	 */
	public final String slbCurCfgRealServerIndex = ".1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.1";

	/**
	 * Real 서버 그룹 <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.3.3.1.2
	 */
	public final String slbCurCfgGroupRealServers = ".1.3.6.1.4.1.1872.2.5.4.1.1.3.3.1.2";

	/**
	 * Real 서버 IP <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.2
	 */
	public final String slbCurCfgRealServerIpAddr = ".1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.2";

	/**
	 * Real 서버 Port <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.5
	 */
	public final String slbCurCfgVirtServiceRealPort = ".1.3.6.1.4.1.1872.2.5.4.1.1.4.5.1.5";

	/**
	 * Real 서버 상태<br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.10 <br>
	 * enabled(2) disabled(3)
	 */
	public final String slbCurCfgRealServerState = ".1.3.6.1.4.1.1872.2.5.4.1.1.2.2.1.10";

	/**
	 * Service 상태 <br>
	 * .1.3.6.1.4.1.1872.2.5.4.3.4.1.6 <br>
	 * blocked(1) running(2) failed(3) disabled(4)
	 */
	public final String slbVirtServicesInfoState = ".1.3.6.1.4.1.1872.2.5.4.3.4.1.6";

	/**
	 * Service별 Session <br>
	 * .1.3.6.1.4.1.1872.2.5.4.2.18.1.4 <br>
	 * Indexes : slbStatVirtServerIndex, slbStatVirtServiceIndex,
	 * slbStatRealServerIndex
	 */
	public final String slbStatVirtServiceCurrSessions = ".1.3.6.1.4.1.1872.2.5.4.2.18.1.4";

	/**
	 * Service별 Total Session <br>
	 * .1.3.6.1.4.1.1872.2.5.4.2.18.1.4 <br>
	 * Indexes : slbStatVirtServerIndex, slbStatVirtServiceIndex,
	 * slbStatRealServerIndex
	 */
	public final String slbStatVirtServiceTotalSessions = ".1.3.6.1.4.1.1872.2.5.4.2.18.1.5";

	/**
	 * Group별 session <br>
	 * .1.3.6.1.4.1.1872.2.5.4.2.3.1.2 <br>
	 * Indexes : slbStatGroupIndex
	 */
	public final String slbStatGroupCurrSessions = ".1.3.6.1.4.1.1872.2.5.4.2.3.1.2";

	/**
	 * Group별 Total session <br>
	 * .1.3.6.1.4.1.1872.2.5.4.2.3.1.3 <br>
	 * Indexes : slbStatGroupIndex
	 */
	public final String slbStatGroupTotalSessions = ".1.3.6.1.4.1.1872.2.5.4.2.3.1.3";

	/**
	 * LB(Load Balance) 방법 <br>
	 * .1.3.6.1.4.1.1872.2.5.4.1.1.3.3.1.3 <br>
	 * roundRobin(1)<br>
	 * leastConnections(2)<br>
	 * minMisses(3)<br>
	 * hash(4)<br>
	 * response(5)<br>
	 * bandwidth(6)<br>
	 * phash(7)
	 */
	public final String slbCurCfgGroupMetric = ".1.3.6.1.4.1.1872.2.5.4.1.1.3.3.1.3";

	
}
