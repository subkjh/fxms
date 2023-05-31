package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

/**
 * @description 사용자신규요청테이블
 * @author fxms auto
 * @date 20230509
 */

public class AlarmSelectDto {

	@FxAttr(description = "발생시간(조회범위시작)", example = "20200101000000")
	private Long occurDtmStart;

	@FxAttr(description = "발생시간(조회범위끝)", example = "20200101235959", required = false)
	private Long occurDtmEnd;

	@FxAttr(description = "발생시간(조회범위끝)", example = "20200101235959", required = false)
	private String moClass;

	public Long getOccurDtmStart() {
		return occurDtmStart;
	}

	public Long getOccurDtmEnd() {
		return occurDtmEnd;
	}

	public String getMoClass() {
		return moClass;
	}

	
}
