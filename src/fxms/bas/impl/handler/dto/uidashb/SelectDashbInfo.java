package fxms.bas.impl.handler.dto.uidashb;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "VIRTUAL_TABLE", comment = "가상테이블")
public class SelectDashbInfo {

	@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호")
	private Integer dashbNo;

	public Integer getDashbNo() {
		return dashbNo;
	}
	
}
