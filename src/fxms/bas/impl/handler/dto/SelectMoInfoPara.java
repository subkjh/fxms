package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class SelectMoInfoPara {
	
	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private long moNo;
	
}
