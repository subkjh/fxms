package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgCopyDto {

	@FxAttr(description = "복사할 알람조건번호")
	private String alarmCfgNo;

	@FxAttr(description = "새로운 알람조건 이름")
	private String newAlarmCfgName;

	public String getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public String getNewAlarmCfgName() {
		return newAlarmCfgName;
	}

	
}
