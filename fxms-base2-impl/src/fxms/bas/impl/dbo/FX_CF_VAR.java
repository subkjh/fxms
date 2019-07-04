package fxms.bas.impl.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_VAR", comment = "코드(시간)테이블")
@FxIndex(name = "FX_CD_VAR__PK", type = INDEX_TYPE.PK, columns = { "VAR_NAME" })
@FxIndex(name = "FX_CD_VAR__UK", type = INDEX_TYPE.UK, columns = { "VAR_ANAME" })
public class FX_CF_VAR implements Serializable {

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운용자번호")
	private int chgUserNo;

	@FxColumn(name = "EDIT_YN", size = 1, nullable = true, comment = "편집여부")
	private boolean editYn = true;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "‘Y’")
	private boolean useYn = true;

	@FxColumn(name = "VAR_ANAME", size = 100, nullable = true, comment = "변수별칭")
	private String varAname;

	@FxColumn(name = "VAR_DESCR", size = 200, nullable = true, comment = "변수설명")
	private String varDescr;

	@FxColumn(name = "VAR_FTYPE", size = 1, nullable = true, comment = "변수유형")
	private String varFtype;

	@FxColumn(name = "VAR_FTYPE_BASE", size = 200, nullable = true, comment = "변수값범위")
	private String varFtypeBase;

	@FxColumn(name = "VAR_GNAME", size = 50, nullable = true, comment = "변수그룹명")
	private String varGname;

	@FxColumn(name = "VAR_MEMO", size = 100, nullable = true, comment = "변수메모사항")
	private String varMemo;

	@FxColumn(name = "VAR_NAME", size = 50, comment = "변수명")
	private String varName;

	@FxColumn(name = "VAR_VALUE", size = 100, nullable = true, comment = "변수설정값")
	private String varValue;

	public FX_CF_VAR() {
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정운용자번호
	 * 
	 * @return 수정운용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 변수별칭
	 * 
	 * @return 변수별칭
	 */
	public String getVarAname() {
		return varAname;
	}

	/**
	 * 변수설명
	 * 
	 * @return 변수설명
	 */
	public String getVarDescr() {
		return varDescr;
	}

	/**
	 * 변수유형
	 * 
	 * @return 변수유형
	 */
	public String getVarFtype() {
		return varFtype;
	}

	/**
	 * 변수값범위
	 * 
	 * @return 변수값범위
	 */
	public String getVarFtypeBase() {
		return varFtypeBase;
	}

	/**
	 * 변수그룹명
	 * 
	 * @return 변수그룹명
	 */
	public String getVarGname() {
		return varGname;
	}

	/**
	 * 변수메모사항
	 * 
	 * @return 변수메모사항
	 */
	public String getVarMemo() {
		return varMemo;
	}

	/**
	 * 변수명
	 * 
	 * @return 변수명
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * 변수설정값
	 * 
	 * @return 변수설정값
	 */
	public String getVarValue() {
		return varValue;
	}

	public boolean isEditYn() {
		return editYn;
	}

	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * 수정운용자번호
	 * 
	 * @param chgUserNo
	 *            수정운용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	public void setEditYn(boolean editYn) {
		this.editYn = editYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	
	/**
	 * 변수별칭
	 * 
	 * @param varAname
	 *            변수별칭
	 */
	public void setVarAname(String varAname) {
		this.varAname = varAname;
	}

	/**
	 * 변수설명
	 * 
	 * @param varDescr
	 *            변수설명
	 */
	public void setVarDescr(String varDescr) {
		this.varDescr = varDescr;
	}

	/**
	 * 변수유형
	 * 
	 * @param varFtype
	 *            변수유형
	 */
	public void setVarFtype(String varFtype) {
		this.varFtype = varFtype;
	}

	/**
	 * 변수값범위
	 * 
	 * @param varFtypeBase
	 *            변수값범위
	 */
	public void setVarFtypeBase(String varFtypeBase) {
		this.varFtypeBase = varFtypeBase;
	}

	/**
	 * 변수그룹명
	 * 
	 * @param varGname
	 *            변수그룹명
	 */
	public void setVarGname(String varGname) {
		this.varGname = varGname;
	}

	/**
	 * 변수메모사항
	 * 
	 * @param varMemo
	 *            변수메모사항
	 */
	public void setVarMemo(String varMemo) {
		this.varMemo = varMemo;
	}

	/**
	 * 변수명
	 * 
	 * @param varName
	 *            변수명
	 */
	public void setVarName(String varName) {
		this.varName = varName;
	}

	/**
	 * 변수설정값
	 * 
	 * @param varValue
	 *            변수설정값
	 */
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}
}
