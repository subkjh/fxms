package fxms.nms.co.vo;

import java.io.Serializable;

import fxms.bas.co.noti.FxEventImpl;

/**
 * Event Log
 * 
 * @author subkjh
 * @since 2012-12-18
 */
public class EventLog extends FxEventImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2766763554295660802L;
	
	/** 경보코드 */
	private long alarmCode = 0;
	/** 경보레벨 */
	private int alarmLevel = 0;
	/** 경보명 */
	private String alarmName;
	/** 경보관리번호 */
	private long alarmNo;
	/** 발생시각 */
	private long hstimeRecv;
	/** IP주소 */
	private String ipAddress;
	/** 로그 그룹 */
	private String logGroup;
	/** SYSLOG */
	private String logMsg;
	/** 모델명 */
	private String modelName;
	/** 모델번호 */
	private int modelNo;
	/** 관리대상명 */
	private String moName;
	/** 관리대상번호 */
	private long moNo;
	/** 인스턴스(발생객체) */
	private String moInstance;

	public EventLog() {

	}

	public long getAlarmCode() {
		return alarmCode;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public long getHstimeRecv() {
		return hstimeRecv;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getLogGroup() {
		return logGroup;
	}

	public String getLogMsg() {
		return logMsg;
	}

	public String getModelName() {
		return modelName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public String getMoName() {
		return moName;
	}

	public long getMoNo() {
		return moNo;
	}

	public void setAlarmCode(long alarmCode) {
		this.alarmCode = alarmCode;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public void setHstimeRecv(long hstimeRecv) {
		this.hstimeRecv = hstimeRecv;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setLogGroup(String logGroup) {
		this.logGroup = logGroup;
	}

	public void setLogMsg(String logMsg) {
		this.logMsg = logMsg;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public void setMoName(String moName) {
		this.moName = moName;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	@Override
	public String toString() {
		return moNo + "|" + moName + "|" + logMsg;
	}

}
