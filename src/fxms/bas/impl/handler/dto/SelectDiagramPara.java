package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_UI_DIAG_BAS", comment = "다이아그램기본테이블")
public class SelectDiagramPara {

	@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호", sequence = "FX_SEQ_DIAG_NO")
	private int diagNo;
}
