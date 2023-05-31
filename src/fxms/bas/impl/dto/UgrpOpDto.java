package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class UgrpOpDto {

	@FxAttr(description = "사용자그룹번호")
	private int ugrpNo;

	@FxAttr(description = "기능ID")
	private String opId;

	@FxAttr(description = "사용여부", example = "Y")
	private String useYn;

	public int getUgrpNo() {
		return ugrpNo;
	}

	public String getOpId() {
		return opId;
	}

	public String getUseYn() {
		return useYn;
	}
	
	

}
