package fxms.bas.impl.vo;

import subkjh.dao.def.FxColumn;

public class PsItemVo {

	@FxColumn(name = "PS_ID", size = 20, comment = "성능ID")
	private String psId;

	@FxColumn(name = "PS_NAME", size = 50, comment = "상태값명")
	private String psName;

	@FxColumn(name = "PS_UNIT", size = 10, nullable = true, comment = "성능단위")
	private String psUnit;

	@FxColumn(name = "MAX_VAL", size = 100, nullable = true, comment = "최대값")
	private double maxVal;

	@FxColumn(name = "MIN_VAL", size = 100, nullable = true, comment = "최소값")
	private double minVal;
}
