package subkjh.bas.user.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_CD_OP", comment = "코드(운용)테이블")
@FxIndex(name = "FX_CD_OP__PK", type = INDEX_TYPE.PK, columns = { "OP_NO" })
public class OpDbo {

	@FxColumn(name = "OP_NO", size = 9, comment = "기능번호")
	private int opNo;

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

	public int getOpNo() {
		return opNo;
	}

	public void setOpJavalClass(String opJavalClass) {
		this.opJavalClass = opJavalClass;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

}
