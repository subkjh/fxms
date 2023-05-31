package fxms.bas.impl.handler.dto;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DIAG_NODE", comment = "다이아그램NODE테이블")
@FxIndex(name = "FX_UI_DIAG_NODE__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO", "DIAG_NODE_NO"})
public class DeleteDiagramNodePara {


	@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호")
	private int diagNo;


	@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
	private int diagNodeNo;

}
