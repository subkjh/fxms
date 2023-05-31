package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgDeleteDto {

	@FxAttr(description = "알람조건번호")
	private int alarmCfgNo;

	@FxAttr(description = "알람조건명")
	private String alarmCfgName;

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	public String getAlarmCfgName() {
		return alarmCfgName;
	}

	public void setAlarmCfgName(String alarmCfgName) {
		this.alarmCfgName = alarmCfgName;
	}

}
