package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;

public class ClearAlarmPara {

	@FxAttr(description = "알람번호", example = "123456")
	private long alarmNo;

	@FxAttr(description = "원인메모", example = "테스트 해제")
	private String rsnMemo;

	public long getAlarmNo() {
		return alarmNo;
	}

	public String getRsnMemo() {
		return rsnMemo;
	}

}
