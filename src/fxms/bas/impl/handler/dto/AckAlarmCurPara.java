package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;
import subkjh.bas.co.utils.DateUtil;

public class AckAlarmCurPara {

	@FxAttr(description = "알람번호", example = "123456")
	private long alarmNo;

	@FxAttr(description = "확인시간", example = "20200101100000", required = false)
	private long ackDtm;

	public long getAlarmNo() {
		return alarmNo;
	}

	public long getAckDtm() {
		return ackDtm;
	}

	public AckAlarmCurPara() {
		ackDtm = DateUtil.getDtm();
	}

}
