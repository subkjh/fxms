package fxms.bas.impl.co;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * System Variabble
 * 
 * @author subkjh
 *
 */
@FxTable(name = "FX_CF_VAR", comment = "코드(시간)테이블")
@FxIndex(name = "FX_CD_VAR__PK", type = INDEX_TYPE.PK, columns = { "VAR_NAME" })
public class VarDbo {

	@FxColumn(name = "VAR_NAME", size = 50, comment = "변수명")
	private String varName;

	@FxColumn(name = "VAR_VALUE", size = 100, nullable = true, comment = "변수설정값")
	private String varValue;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운용자번호")
	private int chgUserNo;

	public long getChgDate() {
		return chgDate;
	}

	public int getChgUserNo() {
		return chgUserNo;
	}

	public String getVarName() {
		return varName;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

}
