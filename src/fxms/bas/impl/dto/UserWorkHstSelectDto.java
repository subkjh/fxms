package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class UserWorkHstSelectDto {

	@FxAttr(description = "조회시간시작일시")
	private String startStrtDtm;

	@FxAttr(description = "조회시간종료일시")
	private String endStrtDtm;

}
