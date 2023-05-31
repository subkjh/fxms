package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class UserNewApplySelectDto {

	@FxAttr(description = "신청사용자ID", required = false)
	private String applyUserId;

	@FxAttr(description = "신청사용자명", required = false)
	private String applyUserName;

	@FxAttr(description = "조회시작신청일자", example = "20010101")
	private String startApplyDate;

	@FxAttr(description = "조회종료신청일자", example = "20010101")
	private String endApplyDate;

}
