package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class UserAccessHstSelectDto {

	@FxAttr(description = "조회시간시작로그인일시")
	private String startLoginDtm;

	@FxAttr(description = "조회시간종료로그인일시")
	private String endLoginDtm;

	public String getStartLoginDtm() {
		return startLoginDtm;
	}

	public String getEndLoginDtm() {
		return endLoginDtm;
	}

}
