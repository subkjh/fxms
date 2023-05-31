package fxms.bas.impl.handler.dto.mo;

import fxms.bas.fxo.FxAttr;

public class MoDto {

	@FxAttr(description = "관리대상번호", example = "1234")
	private Long moNo;

	public Long getMoNo() {
		return moNo;
	}
	
	
}
