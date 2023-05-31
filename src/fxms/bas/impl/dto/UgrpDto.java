package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자신규요청테이블
 * @author fxms auto
 * @date 20230509
 */

public class UgrpDto {

	@FxAttr(description = "사용자그룹번호", example = "1000")
	private Integer ugrpNo;

	@FxAttr(description = "사용자그룹명", example = "guest")
	private String ugrpName;

	public Integer getUgrpNo() {
		return ugrpNo;
	}

	public String getUgrpName() {
		return ugrpName;
	}

}
