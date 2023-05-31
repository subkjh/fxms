package fxms.bas.impl.handler.dto.uidashb;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DASHB", comment = "UI대시보드테이블")
@FxIndex(name = "FX_UI_DASHB__PK", type = INDEX_TYPE.PK, columns = { "DASHB_NO" })
public class DeleteUiDashbPara {

	public DeleteUiDashbPara() {
	}

	public static final String FX_SEQ_DASHBNO = "FX_SEQ_DASHBNO";
	@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호", defValue = "-1", sequence = "FX_SEQ_DASHBNO")
	private Integer dashbNo = -1;

}