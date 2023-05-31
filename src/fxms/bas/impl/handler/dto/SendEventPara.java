package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class SendEventPara {

	@FxColumn(name = "TYPE", size = 100, comment = "유형")
	private String type;

	@FxColumn(name = "TARGET", size = 100, comment = "타켓")
	private String target;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
