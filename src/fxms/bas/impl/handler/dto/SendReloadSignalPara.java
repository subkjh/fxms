package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class SendReloadSignalPara {

	@FxColumn(name = "TYPE", size = 100, comment = "유형")
	private String type;

	public String getType() {
		return type;
	}
}
