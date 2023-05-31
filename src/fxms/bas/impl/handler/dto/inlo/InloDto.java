package fxms.bas.impl.handler.dto.inlo;

import fxms.bas.fxo.FxAttr;

public class InloDto {

	@FxAttr(description = "설치위치번호", example = "1234")
	private Integer inloNo;

	public Integer getInloNo() {
		return inloNo;
	}

}
