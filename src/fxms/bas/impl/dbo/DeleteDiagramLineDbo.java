package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DIAG_LINE", comment = "다이아그램라인테이블")
@FxIndex(name = "FX_UI_DIAG_LINE__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO"})
public class DeleteDiagramLineDbo {
	@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호")
	private int diagNo;
}
