package fxms.bas.impl.handler.dto.mo;

import fxms.bas.fxo.FxAttr;

public class MoSearchStateDto {

	@FxAttr(description = "설치위치번호", example = "1234", required = false)
	private int inloNo = -1;

	@FxAttr(description = "MO분류", example = "SENSOR", required = false)
	private String moClass;

	public int getInloNo() {
		return inloNo;
	}

	public String getMoClass() {
		return moClass;
	}

}
