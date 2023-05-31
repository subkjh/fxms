package fxms.bas.impl.handler.dto.model;

import fxms.bas.fxo.FxAttr;

public class AddModelDto {

	@FxAttr(description = "모델명", example = "TEST")
	private String modelName;

	@FxAttr(description = "제조사명", example = "TEST")
	private String vendrName;

	@FxAttr(description = "모델분류코드", example = "NONE", required = false)
	private String modelClCd;

	@FxAttr(description = "모델식별값", example = "", required = false)
	private String modelIdfyVal;

	@FxAttr(description = "모델설명", example = "", required = false)
	private String modelDescr;

	@FxAttr(description = "MO클래스", example = "SENSOR")
	private String moClass;

	public String getModelName() {
		return modelName;
	}

	public String getVendrName() {
		return vendrName;
	}

	public String getModelClCd() {
		return modelClCd;
	}

	public String getModelIdfyVal() {
		return modelIdfyVal;
	}

	public String getModelDescr() {
		return modelDescr;
	}

	public String getMoClass() {
		return moClass;
	}

	
}
