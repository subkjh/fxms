package fxms.bas.impl.handler.dto.mo;

import fxms.bas.fxo.FxAttr;

public class MoSearchCountDto {

	@FxAttr(description = "모델번호", example = "1234", required = false)
	private Integer modelNo;

	@FxAttr(description = "설치위치번호", example = "1234", required = false)
	private Integer inloNo;

	@FxAttr(description = "설치위치구분", example = "PLANT", required = false)
	private String inloClCd;

	@FxAttr(description = "관리구분", example = "VUP", required = false)
	private String mngDiv = "VUP";

	public Integer getModelNo() {
		return modelNo;
	}

	public Integer getInloNo() {
		return inloNo;
	}

	public String getInloClCd() {
		return inloClCd;
	}

	public String getMngDiv() {
		return mngDiv;
	}

}
