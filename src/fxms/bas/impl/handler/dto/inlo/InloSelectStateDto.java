package fxms.bas.impl.handler.dto.inlo;

import fxms.bas.fxo.FxAttr;

public class InloSelectStateDto {

	@FxAttr(description = "설치위치분류코드", example = "PLANT")
	private String inloClCd;

	@FxAttr(description = "MO클래스", example = "SENSOR")
	private String moClass;

	public String getInloClCd() {
		return inloClCd;
	}

	public String getMoClass() {
		return moClass;
	}

}
