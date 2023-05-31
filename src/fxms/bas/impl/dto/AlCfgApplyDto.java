package fxms.bas.impl.dto;

import fxms.bas.fxo.FxAttr;

public class AlCfgApplyDto {

	@FxAttr(description = "경보조건번호")
	private int alarmCfgNo;

	@FxAttr(description = "사용여부", example = "Y")
	private boolean useYn = true;

}
