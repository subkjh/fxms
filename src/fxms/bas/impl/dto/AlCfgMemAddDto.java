package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgMemAddDto extends AlCfgMemDto {

	public AlCfgMemAddDto() {
	}

	@FxAttr(description = "사용여부", example = "Y", required = false)
	private String useYn = "Y";

	@FxAttr(description = "심각알람비교값", required = false)
	private Float alCriCmprVal;

	@FxAttr(description = "경고알람비교값", required = false)
	private Float alMajCmprVal;

	@FxAttr(description = "관심알람비교값", required = false)
	private Float alMinCmprVal;

	@FxAttr(description = "관심알람비교값", required = false)
	private Float alWarCmprVal;

	@FxAttr(description = "반복횟수", required = false)
	private int reptTimes = 1;

	@FxAttr(description = "선행비교성능ID", required = false)
	private String preCmprPsId;

	@FxAttr(description = "선행비교코드", required = false)
	private String preCmprCd;

	@FxAttr(description = "선행비교값", required = false)
	private double preCmprVal;

	@FxAttr(description = "후속조치코드", required = false)
	private String fpactCd;

	@FxAttr(description = "등록메모")
	private String regMemo;

}