package com.fxms.ui.bas.mo;

import com.fxms.ui.bas.property.MoIpAddressable;
import com.fxms.ui.bas.property.MoLocatable;

public class NeMo extends Mo implements MoLocatable, MoIpAddressable {

	public static final String MO_CLASS = "NE";

	private String ipAddress;

	private String ipAddress2;

	private int modelNo = -1;

	private String categoryCd = "ETC";

	private int inloNo = -1;

	private String inloMemo;

	private String netType = "IP";

	private String contactName;

	private String contactTelno;

	private String nodeMemo;

	private String nodeIdExtra;

	private int installYmd;

	private boolean syslogRecvYn = true;

	private boolean trapRecvYn = true;

	private boolean pingPollYn = true;

	private boolean snmppingPollYn = true;

	private boolean confAutoSyncYn = true;

	private boolean bgpRecvYn = false;

	private boolean flowRecvYn = false;

	private int pollCycleSnmp = 0;

	private int pollCyclePing = 0;

	private String snmpString;

	private String telnetString;

	private String bgpString;

	private String flowString;

	private String msIpaddr;

	private String userData;

	/**
	 * BGP 정보
	 * 
	 * @return BGP 정보
	 */
	public String getBgpString() {
		return bgpString;
	}

	/**
	 * 카테고리코드
	 * 
	 * @return 카테고리코드
	 */
	public String getCategoryCd() {
		return categoryCd;
	}

	/**
	 * 연락처(담당자명)
	 * 
	 * @return 연락처(담당자명)
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * 연락처(전화번호)
	 * 
	 * @return 연락처(전화번호)
	 */
	public String getContactTelno() {
		return contactTelno;
	}

	/**
	 * FLOW 정보
	 * 
	 * @return FLOW 정보
	 */
	public String getFlowString() {
		return flowString;
	}

	/**
	 * 설치위치메모
	 * 
	 * @return 설치위치메모
	 */
	public String getInloMemo() {
		return inloMemo;
	}

	/**
	 * 컨테이너 설치위치번호
	 * 
	 * @return 컨테이너 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치일자
	 * 
	 * @return 설치일자
	 */
	public int getInstallYmd() {
		return installYmd;
	}

	/**
	 * IP주소
	 * 
	 * @return IP주소
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * IP주소2
	 * 
	 * @return IP주소2
	 */
	public String getIpAddress2() {
		return ipAddress2;
	}

	/**
	 * 장비모델코드
	 * 
	 * @return 장비모델코드
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 관리서버IP주소
	 * 
	 * @return 관리서버IP주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 망유형
	 * 
	 * @return 망유형
	 */
	public String getNetType() {
		return netType;
	}

	/**
	 * 외부노드ID
	 * 
	 * @return 외부노드ID
	 */
	public String getNodeIdExtra() {
		return nodeIdExtra;
	}

	/**
	 * 노드메모장
	 * 
	 * @return 노드메모장
	 */
	public String getNodeMemo() {
		return nodeMemo;
	}

	/**
	 * PING폴링주기(초)
	 * 
	 * @return PING폴링주기(초)
	 */
	public int getPollCyclePing() {
		return pollCyclePing;
	}

	/**
	 * SNMP폴링주기(초)
	 * 
	 * @return SNMP폴링주기(초)
	 */
	public int getPollCycleSnmp() {
		return pollCycleSnmp;
	}

	/**
	 * SNMP 정보
	 * 
	 * @return SNMP 정보
	 */
	public String getSnmpString() {
		return snmpString;
	}

	/**
	 * TELNET 정보
	 * 
	 * @return TELNET 정보
	 */
	public String getTelnetString() {
		return telnetString;
	}

	/**
	 * 기타데이터
	 * 
	 * @return 기타데이터
	 */
	public String getUserData() {
		return userData;
	}

	/**
	 * BGP수신여부
	 * 
	 * @return BGP수신여부
	 */
	public boolean isBgpRecvYn() {
		return bgpRecvYn;
	}

	/**
	 * 구성자동갱신여부
	 * 
	 * @return 구성자동갱신여부
	 */
	public boolean isConfAutoSyncYn() {
		return confAutoSyncYn;
	}

	/**
	 * FLOW수신여부
	 * 
	 * @return FLOW수신여부
	 */
	public boolean isFlowRecvYn() {
		return flowRecvYn;
	}

	public boolean isLeaf()
	{
		return false;
	}

	/**
	 * PING 확인여부(Y|N)
	 * 
	 * @return PING 확인여부(Y|N)
	 */
	public boolean isPingPollYn() {
		return pingPollYn;
	}

	/**
	 * SNMPPING 확인여부(Y|N)
	 * 
	 * @return SNMPPING 확인여부(Y|N)
	 */
	public boolean isSnmppingPollYn() {
		return snmppingPollYn;
	}

	/**
	 * SYSLOG수신여부(Y|N)
	 * 
	 * @return SYSLOG수신여부(Y|N)
	 */
	public boolean isSyslogRecvYn() {
		return syslogRecvYn;
	}

	/**
	 * TRAP수신여부(Y|N)
	 * 
	 * @return TRAP수신여부(Y|N)
	 */
	public boolean isTrapRecvYn() {
		return trapRecvYn;
	}

	/**
	 * BGP수신여부
	 * 
	 * @param bgpRecvYn
	 *            BGP수신여부
	 */
	public void setBgpRecvYn(boolean bgpRecvYn) {
		this.bgpRecvYn = bgpRecvYn;
	}

	/**
	 * BGP 정보
	 * 
	 * @param bgpString
	 *            BGP 정보
	 */
	public void setBgpString(String bgpString) {
		this.bgpString = bgpString;
	}

	/**
	 * 카테고리코드
	 * 
	 * @param categoryCd
	 *            카테고리코드
	 */
	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}

	/**
	 * 구성자동갱신여부
	 * 
	 * @param confAutoSyncYn
	 *            구성자동갱신여부
	 */
	public void setConfAutoSyncYn(boolean confAutoSyncYn) {
		this.confAutoSyncYn = confAutoSyncYn;
	}

	/**
	 * 연락처(담당자명)
	 * 
	 * @param contactName
	 *            연락처(담당자명)
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * 연락처(전화번호)
	 * 
	 * @param contactTelno
	 *            연락처(전화번호)
	 */
	public void setContactTelno(String contactTelno) {
		this.contactTelno = contactTelno;
	}

	/**
	 * FLOW수신여부
	 * 
	 * @param flowRecvYn
	 *            FLOW수신여부
	 */
	public void setFlowRecvYn(boolean flowRecvYn) {
		this.flowRecvYn = flowRecvYn;
	}

	/**
	 * FLOW 정보
	 * 
	 * @param flowString
	 *            FLOW 정보
	 */
	public void setFlowString(String flowString) {
		this.flowString = flowString;
	}

	/**
	 * 설치위치메모
	 * 
	 * @param inloMemo
	 *            설치위치메모
	 */
	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}

	/**
	 * 컨테이너 설치위치번호
	 * 
	 * @param inloNo
	 *            컨테이너 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 설치일자
	 * 
	 * @param installYmd
	 *            설치일자
	 */
	public void setInstallYmd(int installYmd) {
		this.installYmd = installYmd;
	}

	/**
	 * IP주소
	 * 
	 * @param ipAddress
	 *            IP주소
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * IP주소2
	 * 
	 * @param ipAddress2
	 *            IP주소2
	 */
	public void setIpAddress2(String ipAddress2) {
		this.ipAddress2 = ipAddress2;
	}

	/**
	 * 장비모델코드
	 * 
	 * @param modelNo
	 *            장비모델코드
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 관리서버IP주소
	 * 
	 * @param msIpaddr
	 *            관리서버IP주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 망유형
	 * 
	 * @param netType
	 *            망유형
	 */
	public void setNetType(String netType) {
		this.netType = netType;
	}

	/**
	 * 외부노드ID
	 * 
	 * @param nodeIdExtra
	 *            외부노드ID
	 */
	public void setNodeIdExtra(String nodeIdExtra) {
		this.nodeIdExtra = nodeIdExtra;
	}

	/**
	 * 노드메모장
	 * 
	 * @param nodeMemo
	 *            노드메모장
	 */
	public void setNodeMemo(String nodeMemo) {
		this.nodeMemo = nodeMemo;
	}

	/**
	 * PING 확인여부(Y|N)
	 * 
	 * @param pingPollYn
	 *            PING 확인여부(Y|N)
	 */
	public void setPingPollYn(boolean pingPollYn) {
		this.pingPollYn = pingPollYn;
	}

	/**
	 * PING폴링주기(초)
	 * 
	 * @param pollCyclePing
	 *            PING폴링주기(초)
	 */
	public void setPollCyclePing(int pollCyclePing) {
		this.pollCyclePing = pollCyclePing;
	}

	/**
	 * SNMP폴링주기(초)
	 * 
	 * @param pollCycleSnmp
	 *            SNMP폴링주기(초)
	 */
	public void setPollCycleSnmp(int pollCycleSnmp) {
		this.pollCycleSnmp = pollCycleSnmp;
	}

	/**
	 * SNMPPING 확인여부(Y|N)
	 * 
	 * @param snmppingPollYn
	 *            SNMPPING 확인여부(Y|N)
	 */
	public void setSnmppingPollYn(boolean snmppingPollYn) {
		this.snmppingPollYn = snmppingPollYn;
	}

	/**
	 * SNMP 정보
	 * 
	 * @param snmpString
	 *            SNMP 정보
	 */
	public void setSnmpString(String snmpString) {
		this.snmpString = snmpString;
	}

	/**
	 * SYSLOG수신여부(Y|N)
	 * 
	 * @param syslogRecvYn
	 *            SYSLOG수신여부(Y|N)
	 */
	public void setSyslogRecvYn(boolean syslogRecvYn) {
		this.syslogRecvYn = syslogRecvYn;
	}

	/**
	 * TELNET 정보
	 * 
	 * @param telnetString
	 *            TELNET 정보
	 */
	public void setTelnetString(String telnetString) {
		this.telnetString = telnetString;
	}

	/**
	 * TRAP수신여부(Y|N)
	 * 
	 * @param trapRecvYn
	 *            TRAP수신여부(Y|N)
	 */
	public void setTrapRecvYn(boolean trapRecvYn) {
		this.trapRecvYn = trapRecvYn;
	}

	/**
	 * 기타데이터
	 * 
	 * @param userData
	 *            기타데이터
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}
}
