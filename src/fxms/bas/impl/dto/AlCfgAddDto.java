package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgAddDto {

	@FxAttr(description = "경보조건명")
	private String alarmCfgName;

	@FxAttr(description = "경보조건설명")
	private String alarmCfgDesc;

	@FxAttr(description = "MO클래스", required = false, example = "MO")
	private String moClass = "MO";

	@FxAttr(description = "수집MO유형", required = false)
	private String moType;

	@FxAttr(description = "기본경보조건여부", required = false, example = "N")
	private String basAlarmCfgYn = "N";

	@FxAttr(description = "사용여부", required = false, example = "N")
	private String useYn = "N";

	@FxAttr(description = "설치위치번호", required = false)
	private Integer inloNo = 0;

}
