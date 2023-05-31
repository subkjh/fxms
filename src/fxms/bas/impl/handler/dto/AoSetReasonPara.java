package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;
import subkjh.bas.co.utils.DateUtil;

public class AoSetReasonPara {


	@FxAttr(description = "알람번호", example = "1000000")
	private long alarmNo;

	@FxAttr(description = "원인등록일시", example = "20200101100000", required = false)
	private long rsnRegDtm;

	@FxAttr(description = "원인번호", example = "1234")
	private int rsnNo;

	@FxAttr(description = "원인명", example = "test")
	private String rsnName;

	@FxAttr(description = "원인메모", example = "test")
	private String rsnMemo;

	public AoSetReasonPara() {
		rsnRegDtm = DateUtil.getDtm();
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public long getRsnRegDtm() {
		return rsnRegDtm;
	}

	public int getRsnNo() {
		return rsnNo;
	}

	public String getRsnName() {
		return rsnName;
	}

	public String getRsnMemo() {
		return rsnMemo;
	}

}