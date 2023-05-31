package fxms.bas.impl.handler.dto.inlo;

import fxms.bas.fxo.FxAttr;

public class InloClCdDto {

	@FxAttr(description = "설치위치분류코드", example = "PLANT")
	private String inloClCd;

	public String getInloClCd() {
		return inloClCd;
	}

}
