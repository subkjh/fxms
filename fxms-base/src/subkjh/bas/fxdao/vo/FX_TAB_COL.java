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

@FxTable(name = "FX_TAB_COL", comment = "FX테이블컬럼")
@FxIndex(name = "FX_TAB_COL__PK", type = INDEX_TYPE.PK, columns = { "TAB_NAME", "COL_NAME" })
@FxIndex(name = "FX_TAB_COL__FK_NM", type = INDEX_TYPE.FK, columns = {
		"TAB_NAME" }, fkTable = "FX_TAB", fkColumn = "TAB_NAME")
public class FX_TAB_COL {

	public FX_TAB_COL() {
	}

	@FxColumn(name = "TAB_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
	private String tabName;

	@FxColumn(name = "COL_NAME", size = 32, operator = COLUMN_OP.insert, comment = "컬럼명")
	private String colName;

	@FxColumn(name = "COL_TYPE", size = 10, comment = "컬럼타입")
	private String colType;

	@FxColumn(name = "COL_SIZE", size = 5, comment = "컬럼크기")
	private int colSize;

	@FxColumn(name = "NULLABLE_YN", size = 1, comment = "NULL가능여부", defValue = "'Y'")
	private boolean nullableYn = true;

	@FxColumn(name = "COL_COMMENT", size = 200, comment = "컬럼코멘트")
	private String colComment;

	@FxColumn(name = "DEF_VALUE", size = 100, nullable = true, comment = "디폴트값")
	private String defValue;

	@FxColumn(name = "UPDATEBLE_YN", size = 1, comment = "업데이트가능여부")
	private boolean updatebleYn;

	@FxColumn(name = "SEQ_NAME", size = 32, nullable = true, comment = "시퀀스명")
	private String seqName;

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
	public String getColName() {
		return colName;
	}

	/**
	 * 컬럼명
	 * 
	 * @param colName
	 *            컬럼명
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}

	/**
	 * 컬럼타입
	 * 
	 * @return 컬럼타입
	 */
	public String getColType() {
		return colType;
	}

	/**
	 * 컬럼타입
	 * 
	 * @param colType
	 *            컬럼타입
	 */
	public void setColType(String colType) {
		this.colType = colType;
	}

	/**
	 * 컬럼크기
	 * 
	 * @return 컬럼크기
	 */
	public int getColSize() {
		return colSize;
	}

	/**
	 * 컬럼크기
	 * 
	 * @param colSize
	 *            컬럼크기
	 */
	public void setColSize(int colSize) {
		this.colSize = colSize;
	}

	/**
	 * NULL가능여부
	 * 
	 * @return NULL가능여부
	 */
	public boolean isNullableYn() {
		return nullableYn;
	}

	/**
	 * NULL가능여부
	 * 
	 * @param nullableYn
	 *            NULL가능여부
	 */
	public void setNullableYn(boolean nullableYn) {
		this.nullableYn = nullableYn;
	}

	/**
	 * 컬럼코멘트
	 * 
	 * @return 컬럼코멘트
	 */
	public String getColComment() {
		return colComment;
	}

	/**
	 * 컬럼코멘트
	 * 
	 * @param colComment
	 *            컬럼코멘트
	 */
	public void setColComment(String colComment) {
		this.colComment = colComment;
	}

	/**
	 * 디폴트값
	 * 
	 * @return 디폴트값
	 */
	public String getDefValue() {
		return defValue;
	}

	/**
	 * 디폴트값
	 * 
	 * @param defValue
	 *            디폴트값
	 */
	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	/**
	 * 업데이트가능여부
	 * 
	 * @return 업데이트가능여부
	 */
	public boolean isUpdatebleYn() {
		return updatebleYn;
	}

	/**
	 * 업데이트가능여부
	 * 
	 * @param updatebleYn
	 *            업데이트가능여부
	 */
	public void setUpdatebleYn(boolean updatebleYn) {
		this.updatebleYn = updatebleYn;
	}

	/**
	 * 시퀀스명
	 * 
	 * @return 시퀀스명
	 */
	public String getSeqName() {
		return seqName;
	}

	/**
	 * 시퀀스명
	 * 
	 * @param seqName
	 *            시퀀스명
	 */
	public void setSeqName(String seqName) {
		this.seqName = seqName;
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
