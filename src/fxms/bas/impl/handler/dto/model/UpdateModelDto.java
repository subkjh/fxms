package fxms.bas.impl.handler.dto.model;

import fxms.bas.fxo.FxAttr;

public class UpdateModelDto extends AddModelDto {

	@FxAttr(description = "모델번호", example = "1234")
	private int modelNo;

	public int getModelNo() {
		return modelNo;
	}

	
}
