package com.fxms.ui.bas.code;

public class UiAlarmCfgVo {

	private int alarmCfgNo;

	private String alarmCfgName;

	private String alarmCfgDescr;

	private String moClass;

	private boolean basicCfgYn = false;

	private boolean useYn = false;
	
	public UiAlarmCfgVo() {

	}

	public UiAlarmCfgVo(int alarmCfgNo, String alarmCfgName) {
		this.alarmCfgNo = alarmCfgNo;
		this.alarmCfgName = alarmCfgName;
	}

	public String getAlarmCfgDescr() {
		return alarmCfgDescr;
	}
	
	public String getAlarmCfgName() {
		return alarmCfgName;
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public String getMoClass() {
		return moClass;
	}

	public boolean isBasicCfgYn() {
		return basicCfgYn;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setAlarmCfgDescr(String alarmCfgDescr) {
		this.alarmCfgDescr = alarmCfgDescr;
	}

	public void setAlarmCfgName(String alarmCfgName) {
		this.alarmCfgName = alarmCfgName;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	public void setBasicCfgYn(boolean basicCfgYn) {
		this.basicCfgYn = basicCfgYn;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public String toString() {
		return alarmCfgName;
	}

}
