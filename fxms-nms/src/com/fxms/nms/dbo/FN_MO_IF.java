package com.fxms.nms.dbo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.27 16:41
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_MO_IF", comment = "인터페이스테이블")
@FxIndex(name = "FN_MO_IF__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FN_MO_IF__FK_MO", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FN_MO_IF extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2285463344348792715L;

	public FN_MO_IF() {
	}

	@FxColumn(name = "IF_INDEX", size = 9, nullable = true, comment = "인터페이스인덱스")
	private int ifIndex;

	@FxColumn(name = "IF_NAME", size = 255, nullable = true, comment = "IF_NAME")
	private String ifName;

	@FxColumn(name = "IF_ALIAS", size = 255, nullable = true, comment = "IF_ALIAS")
	private String ifAlias;

	@FxColumn(name = "IF_DESCR", size = 255, nullable = true, comment = "IF_DESCR")
	private String ifDescr;

	@FxColumn(name = "IF_SPEED_QOS", size = 15, nullable = true, comment = "IF_SPEED")
	private long ifSpeedQos;

	@FxColumn(name = "IF_SPEED", size = 15, nullable = true, comment = "IF_SPEED")
	private long ifSpeed;

	@FxColumn(name = "IF_TYPE", size = 9, nullable = true, comment = "IF_TYPE")
	private int ifType;

	@FxColumn(name = "IF_LAST_CHANGE", size = 15, nullable = true, comment = "IF_LAST_CHANGE")
	private long ifLastChange;

	@FxColumn(name = "IF_STATUS_ADMIN", size = 5, nullable = true, comment = "관리상태")
	private int ifStatusAdmin;

	@FxColumn(name = "IF_STATUS_OPER", size = 5, nullable = true, comment = "연결상태")
	private int ifStatusOper;

	@FxColumn(name = "IF_NETMASK", size = 39, nullable = true, comment = "IF_NETMASK")
	private String ifNetmask;

	@FxColumn(name = "BW_CODE", size = 20, nullable = true, comment = "대역폭코드 ")
	private String bwCode;

	@FxColumn(name = "BW_CODE_QOS", size = 10, nullable = true, comment = "대역폭코드(QoS)")
	private String bwCodeQos;

	@FxColumn(name = "IP_ADDRESS", size = 39, nullable = true, comment = "IP주소")
	private String ipAddress;

	@FxColumn(name = "MAC_ADDRESS", size = 100, nullable = true, comment = "MAC주소")
	private String macAddress;

	@FxColumn(name = "BIT64_YN", size = 1, nullable = true, comment = "64비트인터페이스여부", defValue = "'Y'")
	private boolean bit64Yn = true;

	@FxColumn(name = "CARD_ID", size = 50, nullable = true, comment = "카드ID")
	private String cardId;

	@FxColumn(name = "PORT_NO", size = 9, nullable = true, comment = "포트번호", defValue = "0")
	private int portNo = 0;

	@FxColumn(name = "FLOW_RECV_YN", size = 1, nullable = true, comment = "FLOW수신여부", defValue = "'N'")
	private boolean flowRecvYn = false;

	@FxColumn(name = "FLOW_STRING", size = 200, nullable = true, comment = "FLOW 정보")
	private String flowString;

	@FxColumn(name = "STATUS_ICMP", size = 5, nullable = true, comment = "ICMP PING 상태", defValue = "-1")
	private int statusIcmp = -1;

	@FxColumn(name = "STATUS_ICMP_CHG_DATE", size = 14, nullable = true, comment = "변경일시(ICMP상태)")
	private long statusIcmpChgDate;

	@FxColumn(name = "CHASSIS_ID", size = 50, nullable = true, comment = "샤시스ID")
	private String chassisId;

	/**
	 * 인터페이스인덱스
	 * 
	 * @return 인터페이스인덱스
	 */
	public int getIfIndex() {
		return ifIndex;
	}

	/**
	 * 인터페이스인덱스
	 * 
	 * @param ifIndex
	 *            인터페이스인덱스
	 */
	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}

	/**
	 * IF_NAME
	 * 
	 * @return IF_NAME
	 */
	public String getIfName() {
		return ifName;
	}

	/**
	 * IF_NAME
	 * 
	 * @param ifName
	 *            IF_NAME
	 */
	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	/**
	 * IF_ALIAS
	 * 
	 * @return IF_ALIAS
	 */
	public String getIfAlias() {
		return ifAlias;
	}

	/**
	 * IF_ALIAS
	 * 
	 * @param ifAlias
	 *            IF_ALIAS
	 */
	public void setIfAlias(String ifAlias) {
		this.ifAlias = ifAlias;
	}

	/**
	 * IF_DESCR
	 * 
	 * @return IF_DESCR
	 */
	public String getIfDescr() {
		return ifDescr;
	}

	/**
	 * IF_DESCR
	 * 
	 * @param ifDescr
	 *            IF_DESCR
	 */
	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	/**
	 * IF_SPEED
	 * 
	 * @return IF_SPEED
	 */
	public long getIfSpeedQos() {
		return ifSpeedQos;
	}

	/**
	 * IF_SPEED
	 * 
	 * @param ifSpeedQos
	 *            IF_SPEED
	 */
	public void setIfSpeedQos(long ifSpeedQos) {
		this.ifSpeedQos = ifSpeedQos;
	}

	/**
	 * IF_SPEED
	 * 
	 * @return IF_SPEED
	 */
	public long getIfSpeed() {
		return ifSpeed;
	}

	/**
	 * IF_SPEED
	 * 
	 * @param ifSpeed
	 *            IF_SPEED
	 */
	public void setIfSpeed(long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	/**
	 * IF_TYPE
	 * 
	 * @return IF_TYPE
	 */
	public int getIfType() {
		return ifType;
	}

	/**
	 * IF_TYPE
	 * 
	 * @param ifType
	 *            IF_TYPE
	 */
	public void setIfType(int ifType) {
		this.ifType = ifType;
	}

	/**
	 * IF_LAST_CHANGE
	 * 
	 * @return IF_LAST_CHANGE
	 */
	public long getIfLastChange() {
		return ifLastChange;
	}

	/**
	 * IF_LAST_CHANGE
	 * 
	 * @param ifLastChange
	 *            IF_LAST_CHANGE
	 */
	public void setIfLastChange(long ifLastChange) {
		this.ifLastChange = ifLastChange;
	}

	/**
	 * 관리상태
	 * 
	 * @return 관리상태
	 */
	public int getIfStatusAdmin() {
		return ifStatusAdmin;
	}

	/**
	 * 관리상태
	 * 
	 * @param ifStatusAdmin
	 *            관리상태
	 */
	public void setIfStatusAdmin(int ifStatusAdmin) {
		this.ifStatusAdmin = ifStatusAdmin;
	}

	/**
	 * 연결상태
	 * 
	 * @return 연결상태
	 */
	public int getIfStatusOper() {
		return ifStatusOper;
	}

	/**
	 * 연결상태
	 * 
	 * @param ifStatusOper
	 *            연결상태
	 */
	public void setIfStatusOper(int ifStatusOper) {
		this.ifStatusOper = ifStatusOper;
	}

	/**
	 * IF_NETMASK
	 * 
	 * @return IF_NETMASK
	 */
	public String getIfNetmask() {
		return ifNetmask;
	}

	/**
	 * IF_NETMASK
	 * 
	 * @param ifNetmask
	 *            IF_NETMASK
	 */
	public void setIfNetmask(String ifNetmask) {
		this.ifNetmask = ifNetmask;
	}

	/**
	 * 대역폭코드
	 * 
	 * @return 대역폭코드
	 */
	public String getBwCode() {
		return bwCode;
	}

	/**
	 * 대역폭코드
	 * 
	 * @param bwCode
	 *            대역폭코드
	 */
	public void setBwCode(String bwCode) {
		this.bwCode = bwCode;
	}

	/**
	 * 대역폭코드(QoS)
	 * 
	 * @return 대역폭코드(QoS)
	 */
	public String getBwCodeQos() {
		return bwCodeQos;
	}

	/**
	 * 대역폭코드(QoS)
	 * 
	 * @param bwCodeQos
	 *            대역폭코드(QoS)
	 */
	public void setBwCodeQos(String bwCodeQos) {
		this.bwCodeQos = bwCodeQos;
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
	 * IP주소
	 * 
	 * @param ipAddress
	 *            IP주소
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * MAC주소
	 * 
	 * @return MAC주소
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * MAC주소
	 * 
	 * @param macAddress
	 *            MAC주소
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/**
	 * 64비트인터페이스여부
	 * 
	 * @return 64비트인터페이스여부
	 */
	public boolean isBit64Yn() {
		return bit64Yn;
	}

	/**
	 * 64비트인터페이스여부
	 * 
	 * @param bit64Yn
	 *            64비트인터페이스여부
	 */
	public void setBit64Yn(boolean bit64Yn) {
		this.bit64Yn = bit64Yn;
	}

	/**
	 * 카드ID
	 * 
	 * @return 카드ID
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 카드ID
	 * 
	 * @param cardId
	 *            카드ID
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 포트번호
	 * 
	 * @return 포트번호
	 */
	public int getPortNo() {
		return portNo;
	}

	/**
	 * 포트번호
	 * 
	 * @param portNo
	 *            포트번호
	 */
	public void setPortNo(int portNo) {
		this.portNo = portNo;
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
	 * ICMP PING 상태
	 * 
	 * @return ICMP PING 상태
	 */
	public int getStatusIcmp() {
		return statusIcmp;
	}

	/**
	 * ICMP PING 상태
	 * 
	 * @param statusIcmp
	 *            ICMP PING 상태
	 */
	public void setStatusIcmp(int statusIcmp) {
		this.statusIcmp = statusIcmp;
	}

	/**
	 * 변경일시(ICMP상태)
	 * 
	 * @return 변경일시(ICMP상태)
	 */
	public long getStatusIcmpChgDate() {
		return statusIcmpChgDate;
	}

	/**
	 * 변경일시(ICMP상태)
	 * 
	 * @param statusIcmpChgDate
	 *            변경일시(ICMP상태)
	 */
	public void setStatusIcmpChgDate(long statusIcmpChgDate) {
		this.statusIcmpChgDate = statusIcmpChgDate;
	}

	public String getChassisId() {
		return chassisId;
	}

	public void setChassisId(String chassisId) {
		this.chassisId = chassisId;
	}

}
