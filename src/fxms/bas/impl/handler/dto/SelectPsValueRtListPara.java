package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;

public class SelectPsValueRtListPara {

	@FxAttr(description = "관리대상번호", example = "123456")
	private long moNo;

	public long getMoNo() {
		return moNo;
	}

}
