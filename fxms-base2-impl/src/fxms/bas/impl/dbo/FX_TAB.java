package fxms.bas.impl.dbo;

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

@FxTable(name = "FX_TAB", comment = "FX테이블")
@FxIndex(name = "FX_TABLE__PK", type = INDEX_TYPE.PK, columns = { "TAB_NAME" })
public class FX_TAB {

	public FX_TAB() {
	}

	@FxColumn(name = "TAB_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
	private String tabName;

	@FxColumn(name = "TAB_COMMENT", size = 200, comment = "테이블코멘트")
	private String tabComment;

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
	 * 테이블코멘트
	 * 
	 * @return 테이블코멘트
	 */
	public String getTabComment() {
		return tabComment;
	}

	/**
	 * 테이블코멘트
	 * 
	 * @param tabComment
	 *            테이블코멘트
	 */
	public void setTabComment(String tabComment) {
		this.tabComment = tabComment;
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
