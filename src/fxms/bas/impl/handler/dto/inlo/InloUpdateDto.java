package fxms.bas.impl.handler.dto.inlo;

import fxms.bas.fxo.FxAttr;

public class InloUpdateDto extends InloDto {

	@FxAttr(description = "상위설치위치번호", example = "1234")
	private Integer upperInloNo = 0;

	@FxAttr(description = "설치위치명", example = "OO공장")
	private String inloName;

	@FxAttr(description = "설치위치설명", example = "OO공장", required = false)
	private String inloDesc;

	@FxAttr(description = "설치위치분류코드", example = "NONE")
	private String inloClCd = "NONE";

	public Integer getUpperInloNo() {
		return upperInloNo;
	}

	public String getInloName() {
		return inloName;
	}

	public String getInloDesc() {
		return inloDesc;
	}

	public String getInloClCd() {
		return inloClCd;
	}

}
