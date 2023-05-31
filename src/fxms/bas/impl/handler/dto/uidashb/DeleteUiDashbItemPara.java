package fxms.bas.impl.handler.dto.uidashb;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UI_DASHB_ITEM", comment = "UI대시보드항목테이블")
@FxIndex(name = "FX_UI_DASHB_ITEM__PK", type = INDEX_TYPE.PK, columns = {"DASHB_NO", "UI_NO"})

public class DeleteUiDashbItemPara {

	public DeleteUiDashbItemPara() {
	}

	@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호")
	private Integer dashbNo;

	@FxColumn(name = "UI_NO", size = 5, comment = "화면번호")
	private Integer uiNo;

}