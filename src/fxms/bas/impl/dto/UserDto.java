package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자신규요청테이블
 * @author fxms auto
 * @date 20230509
 */

public class UserDto {

	@FxAttr(description = "사용자번호", example = "1000")
	private Integer userNo;

	@FxAttr(description = "사용자ID", example = "hong@test.com")
	private String userId;

	public Integer getUserNo() {
		return userNo;
	}

	public String getUserId() {
		return userId;
	}

}
