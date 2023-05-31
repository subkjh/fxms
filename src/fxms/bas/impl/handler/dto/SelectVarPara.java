package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_CF_VAR", comment = "코드(시간)테이블")
public class SelectVarPara {

	@FxColumn(name = "VAR_GRP_NAME", size = 50, comment = "변수그룹명")
	private String varGrpName;

	@FxColumn(name = "VAR_NAME", size = 50, nullable = true, comment = "변수명")
	private String varName;
}
