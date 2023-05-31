package fxms.bas.impl.handler.dto.model;

import fxms.bas.fxo.FxAttr;

public class DeleteModelDto {

	@FxAttr(description = "모델번호", example = "123456")
	private int modelNo;

	@FxAttr(description = "모델명", example = "TEST")
	private String modelName;

	public int getModelNo() {
		return modelNo;
	}

	public String getModelName() {
		return modelName;
	}

}
