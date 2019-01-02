package com.daims.dfc.mib;

public class F5_BIGIP_LOCAL_MIB {

	/**
	 * Virtual Server 명 <br>
	 * .1.3.6.1.4.1.3375.2.2.10.1.2.1.1
	 */
	public final String ltmVirtualServName = ".1.3.6.1.4.1.3375.2.2.10.1.2.1.1";
	/**
	 * Virtual Server IP <br>
	 * .1.3.6.1.4.1.3375.2.2.10.1.2.1.3
	 */
	public final String ltmVirtualServAddr = ".1.3.6.1.4.1.3375.2.2.10.1.2.1.3";
	/**
	 * Virtual Server 포트 <br>
	 * .1.3.6.1.4.1.3375.2.2.10.1.2.1.6
	 */
	public final String ltmVirtualServPort = ".1.3.6.1.4.1.3375.2.2.10.1.2.1.6";
	/**
	 * Virtual 서버에 할당된 pool <br>
	 * .1.3.6.1.4.1.3375.2.2.10.1.2.1.19
	 */
	public final String ltmVirtualServDefaultPool = ".1.3.6.1.4.1.3375.2.2.10.1.2.1.19";
	/**
	 * Virtual 서버 설정 상태 <br>
	 * .1.3.6.1.4.1.3375.2.2.10.13.2.1.3<br>
	 * 
	 * 1: none(0) <br>
	 * 2: enabled(1) <br>
	 * 3: disabled(2) <br>
	 * 4: disabledbyparent(3) <br>
	 */
	public final String ltmVsStatusEnabledState = ".1.3.6.1.4.1.3375.2.2.10.13.2.1.3";

	/**
	 * Pool 명 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.1.2.1.1
	 */
	public final String ltmPoolName = ".1.3.6.1.4.1.3375.2.2.5.1.2.1.1";
	/**
	 * LB(Load Balance) 방법 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.1.2.1.2 <br>
	 * 
	 * roundRobin(0) <br>
	 * ratioMember(1) <br>
	 * leastConnMember(2) <br>
	 * observedMember(3) <br>
	 * predictiveMember(4) <br>
	 * ratioNodeAddress(5) <br>
	 * leastConnNodeAddress(6) <br>
	 * fastestNodeAddress(7) <br>
	 * observedNodeAddress(8) <br>
	 * predictiveNodeAddress(9) <br>
	 * dynamicRatio(10) <br>
	 * fastestAppResponse(11) <br>
	 * leastSessions(12) <br>
	 * dynamicRatioMember(13) <br>
	 * l3Addr(14) <br>
	 * weightedLeastConnMember(15) <br>
	 * weightedLeastConnNodeAddr(16) <br>
	 * ratioSession(17)
	 */
	public final String ltmPoolLbMode = ".1.3.6.1.4.1.3375.2.2.5.1.2.1.2";

	/**
	 * 서버가 속한 Pool <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.1
	 */
	public final String ltmPoolMbrStatusPoolName = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.1";
	/**
	 * IP 타입 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.4.3.1.2<br>
	 */
	public final String ltmPoolMemberStatAddrType = ".1.3.6.1.4.1.3375.2.2.5.4.3.1.2";
	/**
	 * 서버 IP <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.3
	 */
	public final String ltmPoolMbrStatusAddr = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.3";

	/**
	 * 서버 포트 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.4
	 */
	public final String ltmPoolMbrStatusPort = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.4";
	/**
	 * 서버 설정 상태 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.3 <br>
	 * 1: none(0) <br>
	 * 2: enabled(1) <br>
	 * 3: disabled(2) <br>
	 * 4: disabledbyparent(3)
	 */
	public final String ltmPoolMbrStatusEnabledState = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.6";
	/**
	 * 서버명 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.3
	 */
	public final String ltmPoolMbrStatusNodeName = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.9";

	/**
	 * Real IP + Port 정보를 파싱하여 가져올 수 있는 값 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.6.2.1.5 <br>
	 * ltmPoolMbrStatusPoolName, ltmPoolMbrStatusAddrType, ltmPoolMbrStatusAddr,
	 * ltmPoolMbrStatusPort <br>
	 */
	public final String ltmPoolMbrStatusAvailState = ".1.3.6.1.4.1.3375.2.2.5.6.2.1.5";
	/**
	 * Pool 세션 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.2.3.1.8
	 */
	public final String ltmPoolStatServerCurConns = ".1.3.6.1.4.1.3375.2.2.5.2.3.1.8";
	/**
	 * PoolMember 현재 세션 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.4.3.1.11
	 */
	public final String ltmPoolMemberStatServerCurConns = ".1.3.6.1.4.1.3375.2.2.5.4.3.1.11";

	/**
	 * Pool 누적 세션 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.2.3.1.7
	 */
	public final String ltmPoolStatServerTotConns = ".1.3.6.1.4.1.3375.2.2.5.2.3.1.7";

	/**
	 * PoolMember 누적 세션 <br>
	 * .1.3.6.1.4.1.3375.2.2.5.4.3.1.10
	 */
	public final String ltmPoolMemberStatServerTotConns = ".1.3.6.1.4.1.3375.2.2.5.4.3.1.10";

}
