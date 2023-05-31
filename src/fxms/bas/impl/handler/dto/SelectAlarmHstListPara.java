package fxms.bas.impl.handler.dto;

import fxms.bas.fxo.FxAttr;

public class SelectAlarmHstListPara {

	@FxAttr(description = "관리대상번호", example = "123456", required = false)
	private Long moNo;

	@FxAttr(description = "경보코드번호", example = "1000", required = false)
	private Integer alcdNo;

	@FxAttr(description = "설치위치번호", example = "1000", required = false)
	private Integer inloNo;

	@FxAttr(description = "모델번호", example = "1000", required = false)
	private Integer modelNo;

	@FxAttr(description = "MO클래스", example = "1000", required = false)
	private String moClass;

	@FxAttr(description = "조회시작발생일시", example = "20220101100000")
	private long startOccurDtm;

	@FxAttr(description = "조회종료발생일시", example = "20220101100000")
	private long endOccurDtm;

	public Integer getAlcdNo() {
		return alcdNo;
	}

	public Integer getInloNo() {
		return inloNo;
	}

	public Integer getModelNo() {
		return modelNo;
	}

	public String getMoClass() {
		return moClass;
	}

	public long getStartOccurDtm() {
		return startOccurDtm;
	}

	public long getEndOccurDtm() {
		return endOccurDtm;
	}
}
