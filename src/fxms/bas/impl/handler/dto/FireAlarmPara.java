package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;

public class FireAlarmPara {

	@FxAttr(description = "관리대상번호", example = "123456")
	private long moNo;

	@FxAttr(description = "경보명", example = "TestAlarm")
	private String alarmName;

	@FxAttr(description = "경보메시지", example = "시험용 경보")
	private String alarmMsg;

	@FxAttr(description = "MO인스턴스", example = "test", required = false)
	private String moInstance;

	@FxAttr(description = "알람키", example = "test", required = false)
	private String alarmKey;

	public long getMoNo() {
		return moNo;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public String getAlarmKey() {
		return alarmKey;
	}

}
