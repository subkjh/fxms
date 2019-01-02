package subkjh.bas.fxdao.vo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.04.06 14:21
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_TAB_IDX", comment = "FX테이블인덱스")
@FxIndex(name = "FX_TAB_IDX__PK", type = INDEX_TYPE.PK, columns = { "TAB_NAME", "IDX_NAME" })
@FxIndex(name = "FX_TAB_IDX__FK_NM", type = INDEX_TYPE.FK, columns = {
		"TAB_NAME" }, fkTable = "FX_TAB", fkColumn = "TAB_NAME")
public class FX_TAB_IDX  {

	public FX_TAB_IDX() {
	}

	@FxColumn(name = "TAB_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
	private String tabName;

	@FxColumn(name = "IDX_NAME", size = 32, operator = COLUMN_OP.insert, comment = "컬럼명")
	private String idxName;

	@FxColumn(name = "IDX_TYPE", size = 3, comment = "컬럼타입")
	private String idxType;

	@FxColumn(name = "COL_NAME_LIST", size = 1000, comment = "컬럼크기")
	private String colNameList;

	@FxColumn(name = "FK_TAB_NAME", size = 20, nullable = true, comment = "NULL가능여부", defValue = "'Y'")
	private String fkTabName = "Y";

	@FxColumn(name = "FK_COL_NAME_LIST", size = 200, nullable = true, comment = "컬럼코멘트")
	private String fkColNameList;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 테이블명
	 * 
	 * @return 테이블명
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * 테이블명
	 * 
	 * @param tabName
	 *            테이블명
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	/**
	 * 컬럼명
	 * 
	 * @return 컬럼명
	 */
	public String getIdxName() {
		return idxName;
	}

	/**
	 * 컬럼명
	 * 
	 * @param idxName
	 *            컬럼명
	 */
	public void setIdxName(String idxName) {
		this.idxName = idxName;
	}

	/**
	 * 컬럼타입
	 * 
	 * @return 컬럼타입
	 */
	public String getIdxType() {
		return idxType;
	}

	/**
	 * 컬럼타입
	 * 
	 * @param idxType
	 *            컬럼타입
	 */
	public void setIdxType(String idxType) {
		this.idxType = idxType;
	}

	/**
	 * 컬럼크기
	 * 
	 * @return 컬럼크기
	 */
	public String getColNameList() {
		return colNameList;
	}

	/**
	 * 컬럼크기
	 * 
	 * @param colNameList
	 *            컬럼크기
	 */
	public void setColNameList(String colNameList) {
		this.colNameList = colNameList;
	}

	/**
	 * NULL가능여부
	 * 
	 * @return NULL가능여부
	 */
	public String getFkTabName() {
		return fkTabName;
	}

	/**
	 * NULL가능여부
	 * 
	 * @param fkTabName
	 *            NULL가능여부
	 */
	public void setFkTabName(String fkTabName) {
		this.fkTabName = fkTabName;
	}

	/**
	 * 컬럼코멘트
	 * 
	 * @return 컬럼코멘트
	 */
	public String getFkColNameList() {
		return fkColNameList;
	}

	/**
	 * 컬럼코멘트
	 * 
	 * @param fxColNameList
	 *            컬럼코멘트
	 */
	public void setFkColNameList(String fkColNameList) {
		this.fkColNameList = fkColNameList;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
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
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}
}
