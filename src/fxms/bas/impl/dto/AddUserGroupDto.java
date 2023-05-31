package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자그룹테이블
 * @author fxms auto
 * @date 20230509
 */

public class AddUserGroupDto {

	@FxAttr(description = "사용자그룹명")
	private String ugrpName;

	@FxAttr(description = "사용자그룹설명", required = false)
	private String ugrpDesc;

	@FxAttr(description = "설치위치번호", required = false, example = "0")
	private int inloNo = 0;

	public String getUgrpName() {
		return ugrpName;
	}	

}
