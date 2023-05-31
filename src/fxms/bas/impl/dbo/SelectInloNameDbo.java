package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CF_INLO", comment = "설치위치테이블")
@FxIndex(name = "FX_CF_INLO__PK", type = INDEX_TYPE.PK, columns = { "INLO_NO" })
public class SelectInloNameDbo {

	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", sequence = "FX_SEQ_INLONO")
	private int inloNo;

	@FxColumn(name = "INLO_NAME", size = 100, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "INLO_ALL_NAME", size = 200, nullable = true, comment = "설치위치전체명")
	private String inloAllName;
}
