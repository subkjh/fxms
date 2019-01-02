package subkjh.bas.fxdao;


import subkjh.bas.dao.define.COLUMN_TYPE;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_TEST_T1", comment = "관리대상테이블")
@FxIndex(name = "PK", type = INDEX_TYPE.PK, columns = { "moNo" })
@FxIndex(name = "UK", type = INDEX_TYPE.UK, columns = { "upperMoNo", "moClass", "moName" })
public class T1 {

	@FxColumn(name = "KKK", type = COLUMN_TYPE.NUMBER, sequence = "FX_SEQ_MONO", nullable = false, comment = "관리번호")
	private long moNo;

	@FxColumn(nullable = false, comment = "상위관리번호")
	private long upperMoNo;

	@FxColumn(size = 50, nullable = false, comment = "관리대상분류")
	private String moClass;

	@FxColumn(size = 100, nullable = false, comment = "관리대상명")
	private String moName;

}
