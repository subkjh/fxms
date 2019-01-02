package com.fxms.nms.dbo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.27 16:09
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_MO_NE", comment = "NE테이블")
@FxIndex(name = "FN_MO_NE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FN_MO_NE__FK_MO", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FN_MO_NE extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1394573620205885211L;

	public FN_MO_NE() {
	}

	@FxColumn(name = "IP_ADDRESS", size = 39, nullable = true, comment = "IP주소")
	private String ipAddress;

	@FxColumn(name = "IP_ADDRESS2", size = 39, nullable = true, comment = "IP주소2")
	private String ipAddress2;

	@FxColumn(name = "MODEL_NO", size = 9, nullable = true, comment = "장비모델코드 ", defValue = "-1")
	private int modelNo = -1;

	@FxColumn(name = "CATEGORY_CD", size = 10, nullable = true, comment = "카테고리코드", defValue = "'ETC'")
	private String categoryCd = "ETC";

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "컨테이너 설치위치번호", defValue = "-1")
	private int inloNo = -1;

	@FxColumn(name = "INLO_MEMO", size = 200, nullable = true, comment = "설치위치메모")
	private String inloMemo;

	@FxColumn(name = "NET_TYPE", size = 30, nullable = true, comment = "망유형", defValue = "'IP'")
	private String netType = "IP";

	@FxColumn(name = "CONTACT_NAME", size = 100, nullable = true, comment = "연락처(담당자명)")
	private String contactName;

	@FxColumn(name = "CONTACT_TELNO", size = 100, nullable = true, comment = "연락처(전화번호)")
	private String contactTelno;

	@FxColumn(name = "NODE_MEMO", size = 200, nullable = true, comment = "노드메모장")
	private String nodeMemo;

	@FxColumn(name = "NODE_ID_EXTRA", size = 50, nullable = true, comment = "외부노드ID")
	private String nodeIdExtra;

	@FxColumn(name = "INSTALL_YMD", size = 8, nullable = true, comment = "설치일자")
	private int installYmd;

	@FxColumn(name = "SYSLOG_RECV_YN", size = 1, nullable = true, comment = "SYSLOG수신여부(Y|N)", defValue = "'Y'")
	private boolean syslogRecvYn = true;

	@FxColumn(name = "TRAP_RECV_YN", size = 1, nullable = true, comment = "TRAP수신여부(Y|N)", defValue = "'Y'")
	private boolean trapRecvYn = true;

	@FxColumn(name = "PING_POLL_YN", size = 1, nullable = true, comment = "PING 확인여부(Y|N)", defValue = "'Y'")
	private boolean pingPollYn = true;

	@FxColumn(name = "SNMPPING_POLL_YN", size = 1, nullable = true, comment = "SNMPPING 확인여부(Y|N)", defValue = "'Y'")
	private boolean snmppingPollYn = true;

	@FxColumn(name = "CONF_AUTO_SYNC_YN", size = 1, nullable = true, comment = "구성자동갱신여부", defValue = "'Y'")
	private boolean confAutoSyncYn = true;

	@FxColumn(name = "BGP_RECV_YN", size = 1, nullable = true, comment = "BGP수신여부", defValue = "'N'")
	private boolean bgpRecvYn = false;

	@FxColumn(name = "FLOW_RECV_YN", size = 1, nullable = true, comment = "FLOW수신여부", defValue = "'N'")
	private boolean flowRecvYn = false;

	@FxColumn(name = "POLL_CYCLE_SNMP", size = 9, nullable = true, comment = "SNMP폴링주기(초)", defValue = "0")
	private int pollCycleSnmp = 0;

	@FxColumn(name = "POLL_CYCLE_PING", size = 9, nullable = true, comment = "PING폴링주기(초)", defValue = "0")
	private int pollCyclePing = 0;

	@FxColumn(name = "SNMP_STRING", size = 200, nullable = true, comment = "SNMP 정보")
	private String snmpString;

	@FxColumn(name = "TELNET_STRING", size = 200, nullable = true, comment = "TELNET 정보")
	private String telnetString;

	@FxColumn(name = "BGP_STRING", size = 200, nullable = true, comment = "BGP 정보")
	private String bgpString;

	@FxColumn(name = "FLOW_STRING", size = 200, nullable = true, comment = "FLOW 정보")
	private String flowString;

	@FxColumn(name = "MS_IPADDR", size = 39, nullable = true, comment = "관리서버IP주소")
	private String msIpaddr;

	@FxColumn(name = "USER_DATA", size = 1000, nullable = true, comment = "기타데이터")
	private String userData;

	/**
	 * IP주소
	 * 
	 * @return IP주소
	 */
	public String getIpAddress() {
		return ipAddress;
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
	 * @return IP주소2
	 */
	public String getIpAddress2() {
		return ipAddress2;
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
	 * @return 장비모델코드
	 */
	public int getModelNo() {
		return modelNo;
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
	 * 카테고리코드
	 * 
	 * @return 카테고리코드
	 */
	public String getCategoryCd() {
		return categoryCd;
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
	 * 컨테이너 설치위치번호
	 * 
	 * @return 컨테이너 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
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
	 * 설치위치메모
	 * 
	 * @return 설치위치메모
	 */
	public String getInloMemo() {
		return inloMemo;
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
	 * 망유형
	 * 
	 * @return 망유형
	 */
	public String getNetType() {
		return netType;
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
	 * 연락처(담당자명)
	 * 
	 * @return 연락처(담당자명)
	 */
	public String getContactName() {
		return contactName;
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
	 * @return 연락처(전화번호)
	 */
	public String getContactTelno() {
		return contactTelno;
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
	 * 노드메모장
	 * 
	 * @return 노드메모장
	 */
	public String getNodeMemo() {
		return nodeMemo;
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
	 * 외부노드ID
	 * 
	 * @return 외부노드ID
	 */
	public String getNodeIdExtra() {
		return nodeIdExtra;
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
	 * 설치일자
	 * 
	 * @return 설치일자
	 */
	public int getInstallYmd() {
		return installYmd;
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
	 * SYSLOG수신여부(Y|N)
	 * 
	 * @return SYSLOG수신여부(Y|N)
	 */
	public boolean isSyslogRecvYn() {
		return syslogRecvYn;
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
	 * TRAP수신여부(Y|N)
	 * 
	 * @return TRAP수신여부(Y|N)
	 */
	public boolean isTrapRecvYn() {
		return trapRecvYn;
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
	 * PING 확인여부(Y|N)
	 * 
	 * @return PING 확인여부(Y|N)
	 */
	public boolean isPingPollYn() {
		return pingPollYn;
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
	 * SNMPPING 확인여부(Y|N)
	 * 
	 * @return SNMPPING 확인여부(Y|N)
	 */
	public boolean isSnmppingPollYn() {
		return snmppingPollYn;
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
	 * 구성자동갱신여부
	 * 
	 * @return 구성자동갱신여부
	 */
	public boolean isConfAutoSyncYn() {
		return confAutoSyncYn;
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
	 * BGP수신여부
	 * 
	 * @return BGP수신여부
	 */
	public boolean isBgpRecvYn() {
		return bgpRecvYn;
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
	 * FLOW수신여부
	 * 
	 * @return FLOW수신여부
	 */
	public boolean isFlowRecvYn() {
		return flowRecvYn;
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
	 * SNMP폴링주기(초)
	 * 
	 * @return SNMP폴링주기(초)
	 */
	public int getPollCycleSnmp() {
		return pollCycleSnmp;
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
	 * PING폴링주기(초)
	 * 
	 * @return PING폴링주기(초)
	 */
	public int getPollCyclePing() {
		return pollCyclePing;
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
	 * SNMP 정보
	 * 
	 * @return SNMP 정보
	 */
	public String getSnmpString() {
		return snmpString;
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
	 * TELNET 정보
	 * 
	 * @return TELNET 정보
	 */
	public String getTelnetString() {
		return telnetString;
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
	 * BGP 정보
	 * 
	 * @return BGP 정보
	 */
	public String getBgpString() {
		return bgpString;
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
	 * FLOW 정보
	 * 
	 * @return FLOW 정보
	 */
	public String getFlowString() {
		return flowString;
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
	 * 관리서버IP주소
	 * 
	 * @return 관리서버IP주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
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
	 * 기타데이터
	 * 
	 * @return 기타데이터
	 */
	public String getUserData() {
		return userData;
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
