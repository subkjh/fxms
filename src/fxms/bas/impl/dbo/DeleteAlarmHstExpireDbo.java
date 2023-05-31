package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxTable;

@FxTable(name = "FX_CF_META", comment = "메타테이블")
public class DeleteAlarmHstExpireDbo {

	@FxColumn(name = "META_NAME", size = 30, comment = "메타명")
	private String metaName;

	
	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
	private long regDtm;
}
