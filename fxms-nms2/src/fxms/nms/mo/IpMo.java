package fxms.nms.mo;

import fxms.bas.co.def.ARGS;
import fxms.bas.impl.mo.FxMo;

/**
 * IP 주소 MO
 * 
 * @author subkjh
 * 
 */
public class IpMo extends FxMo {

	/** MO분류. IP */
	public static final String MO_CLASS = "IP";

	/**
	 * 
	 */
	private static final long serialVersionUID = 4556098067419681394L;

	/** 그룹명 */
	private String groupName;
	/** 수정일시 */
	private long hstimeChg;
	/** IP주소 */
	private String ipAddress;
	/** IPV6여부 */
	private boolean ipv6;
	/** 네트워크(C class) - C Class Network 입니다. */
	private String network;
	/** SNMP 발송 그룹 */
	private int smsGroupNo;
	/** 1:Online, 0:OffLine */
	private int statusIcmp;
	/** 운용자그룹번호(RO그룹) */
	private int userGroupNo;
	/** 운용자번호(소유자) */
	private int userNo;
	/** 운용자번호(수정) */
	private int userNoChg;
	/** Ping 관제주기. */
	private int secPollingPing;
	/** 담당서버 */
	private String mgrServer;

	/** 이전 응답시간 평균 */
	private float rtt;

	public IpMo() {

	}

	@ARGS(para = { "MO-NO", "GROUP-NAME", "IP-ADDRESS", "MO-ALIAS", "MO-NAME", "PERF-THR-NO" })
	public IpMo(long moNo, String groupName, String ip, String alias, String moName, int alarmCfgNo) {

		setMoNo(moNo);
		setAlarmCfgNo(alarmCfgNo);
		setGroupName(groupName);
		setIpAddress(ip);
		setMngYn(true);
		setMgrServer(null);
		setMoAname(alias);
		setMoName(moName);
	}

	public String getGroupName() {
		return groupName;
	}

	public long getHstimeChg() {
		return hstimeChg;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getMgrServer() {
		return mgrServer;
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	public String getNetwork() {

		if (isIpv6() == false) {
			if (network == null || network.length() == 0) {
				network = ipAddress.substring(0, ipAddress.lastIndexOf(".")) + ".0";
			}
		}

		return network;
	}

	public float getRtt() {
		return rtt;
	}

	public int getSecPollingPing() {
		return secPollingPing;
	}

	public int getSmsGroupNo() {
		return smsGroupNo;
	}

	public int getStatusIcmp() {
		return statusIcmp;
	}

	public int getUserGroupNo() {
		return userGroupNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public int getUserNoChg() {
		return userNoChg;
	}

	public boolean isIpv6() {
		return ipv6;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setHstimeChg(long hstimeChg) {
		this.hstimeChg = hstimeChg;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setIpv6(boolean ipv6) {
		this.ipv6 = ipv6;
	}

	public void setMgrServer(String mgrServer) {
		this.mgrServer = mgrServer;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public void setRtt(float rtt) {
		this.rtt = rtt;
	}

	public void setSecPollingPing(int secPollingPing) {
		this.secPollingPing = secPollingPing;
	}

	public void setSmsGroupNo(int smsGroupNo) {
		this.smsGroupNo = smsGroupNo;
	}

	public void setStatusIcmp(int statusIcmp) {
		this.statusIcmp = statusIcmp;
	}

	public void setUserGroupNo(int userGroupNo) {
		this.userGroupNo = userGroupNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public void setUserNoChg(int userNoChg) {
		this.userNoChg = userNoChg;
	}

}
