package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DIAG_NODE", comment = "다이아그램NODE테이블")
@FxIndex(name = "FX_UI_DIAG_NODE__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO"})
public class DeleteDiagramNodeDbo {
	@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호")
	private int diagNo;
}
