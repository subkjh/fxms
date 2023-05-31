package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgMemDto {

	@FxAttr(description = "경보조건번호")
	private int alarmCfgNo;

	@FxAttr(description = "경보코드")
	private int alcdNo;

	public AlCfgMemDto() {
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

}