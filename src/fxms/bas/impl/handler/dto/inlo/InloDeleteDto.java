package fxms.bas.impl.handler.dto.inlo;

import fxms.bas.fxo.FxAttr;

public class InloDeleteDto extends InloDto {

	@FxAttr(description = "설치위치명", example = "OO공장")
	private String inloName;

	public String getInloName() {
		return inloName;
	}

}
