package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DIAG_BAS", comment = "다이아그램기본테이블")
@FxIndex(name = "FX_UI_DIAG_BAS__PK", type = INDEX_TYPE.PK, columns = { "DIAG_NO" })
public class DeleteDiagramPara {

	@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호", sequence = "FX_SEQ_DIAG_NO")
	private int diagNo;

	public int getDiagNo() {
		return diagNo;
	}

}
