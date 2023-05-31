package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class CdCodeDto {

	@FxAttr(description = "코드분류")
	private String cdClass;

	@FxAttr(description = "코드")
	private String cdCode;

	public String getCdClass() {
		return cdClass;
	}

	public String getCdCode() {
		return cdCode;
	}

}
