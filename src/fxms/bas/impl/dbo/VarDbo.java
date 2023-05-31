package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * System Variabble
 * 
 * @author subkjh
 *
 */
@FxTable(name = "FX_CF_VAR", comment = "코드(시간)테이블")
@FxIndex(name = "FX_CF_VAR__PK", type = INDEX_TYPE.PK, columns = { "VAR_NAME" })
public class VarDbo {

	@FxColumn(name = "VAR_NAME", size = 50, comment = "변수명")
	private String varName;

	@FxColumn(name = "VAR_VAL", size = 100, nullable = true, comment = "변수설정값")
	private String varVal;

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarVal() {
		return varVal;
	}

	public void setVarVal(String varVal) {
		this.varVal = varVal;
	}

}
