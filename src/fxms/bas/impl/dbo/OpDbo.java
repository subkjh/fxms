package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_CD_OP", comment = "코드(운용)테이블")
@FxIndex(name = "FX_CD_OP__PK", type = INDEX_TYPE.PK, columns = { "OP_ID" })
public class OpDbo {

	@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
	private String opId;

	@FxColumn(name = "OP_NAME", size = 100, nullable = true, comment = "기능명")
	private String opName;

	@FxColumn(name = "OP_JAVAL_CLASS", size = 200, nullable = true, comment = "기능자바클래스")
	private String opJavalClass;

	public String getOpJavalClass() {
		return opJavalClass;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpJavalClass(String opJavalClass) {
		this.opJavalClass = opJavalClass;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	
}
