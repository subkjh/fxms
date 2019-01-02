package com.fxms.ui.bas.mo;

import com.fxms.ui.bas.property.MoIpAddressable;

public class NeIfMo extends Mo implements MoIpAddressable {

	public static final String MO_CLASS = "IF";

	private int ifIndex;

	private String ifName;

	private String ifAlias;

	private String ifDescr;

	private long ifSpeedQos;

	private long ifSpeed;

	private int ifType;

	private long ifLastChange;

	private int ifStatusAdmin;

	private int ifStatusOper;

	private String ifNetmask;

	private String bwCode;

	private String bwCodeQos;

	private String ipAddress;

	private String macAddress;

	private boolean bit64Yn = true;

	private String cardId;

	private int portNo = 0;

	private boolean flowRecvYn = false;

	private String flowString;

	private int statusIcmp = -1;

	private long statusIcmpChgDate;

	private String chassisId;

	public int getIfIndex() {
		return ifIndex;
	}

	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	public String getIfAlias() {
		return ifAlias;
	}

	public void setIfAlias(String ifAlias) {
		this.ifAlias = ifAlias;
	}

	public String getIfDescr() {
		return ifDescr;
	}

	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	public long getIfSpeedQos() {
		return ifSpeedQos;
	}

	public void setIfSpeedQos(long ifSpeedQos) {
		this.ifSpeedQos = ifSpeedQos;
	}

	public long getIfSpeed() {
		return ifSpeed;
	}

	public void setIfSpeed(long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}

	public int getIfType() {
		return ifType;
	}

	public void setIfType(int ifType) {
		this.ifType = ifType;
	}

	public long getIfLastChange() {
		return ifLastChange;
	}

	public void setIfLastChange(long ifLastChange) {
		this.ifLastChange = ifLastChange;
	}

	public int getIfStatusAdmin() {
		return ifStatusAdmin;
	}

	public void setIfStatusAdmin(int ifStatusAdmin) {
		this.ifStatusAdmin = ifStatusAdmin;
	}

	public int getIfStatusOper() {
		return ifStatusOper;
	}

	public void setIfStatusOper(int ifStatusOper) {
		this.ifStatusOper = ifStatusOper;
	}

	public String getIfNetmask() {
		return ifNetmask;
	}

	public void setIfNetmask(String ifNetmask) {
		this.ifNetmask = ifNetmask;
	}

	public String getBwCode() {
		return bwCode;
	}

	public void setBwCode(String bwCode) {
		this.bwCode = bwCode;
	}

	public String getBwCodeQos() {
		return bwCodeQos;
	}

	public void setBwCodeQos(String bwCodeQos) {
		this.bwCodeQos = bwCodeQos;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public boolean isBit64Yn() {
		return bit64Yn;
	}

	public void setBit64Yn(boolean bit64Yn) {
		this.bit64Yn = bit64Yn;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public boolean isFlowRecvYn() {
		return flowRecvYn;
	}

	public void setFlowRecvYn(boolean flowRecvYn) {
		this.flowRecvYn = flowRecvYn;
	}

	public String getFlowString() {
		return flowString;
	}

	public void setFlowString(String flowString) {
		this.flowString = flowString;
	}

	public int getStatusIcmp() {
		return statusIcmp;
	}

	public void setStatusIcmp(int statusIcmp) {
		this.statusIcmp = statusIcmp;
	}

	public long getStatusIcmpChgDate() {
		return statusIcmpChgDate;
	}

	public void setStatusIcmpChgDate(long statusIcmpChgDate) {
		this.statusIcmpChgDate = statusIcmpChgDate;
	}

	public String getChassisId() {
		return chassisId;
	}

	public void setChassisId(String chassisId) {
		this.chassisId = chassisId;
	}

	@Override
	public String getRemarks() {
		return "type=" + ifType + ", admin=" + ifStatusAdmin + ", oper=" + ifStatusOper;
	}

}
