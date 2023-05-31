package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;

public class SelectAlarmPara {

	@FxAttr(description = "알람번호", example = "123456")
	private long alarmNo;

	public long getAlarmNo() {
		return alarmNo;
	}

}
