package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class SelectUserDto {

	@FxAttr(description = "사용자그룹번호")
	private Integer ugrpNo;

	@FxAttr(description = "설치위치번호")
	private Integer inloNo;

	@FxAttr(description = "사용자명")
	private String userName;

}
