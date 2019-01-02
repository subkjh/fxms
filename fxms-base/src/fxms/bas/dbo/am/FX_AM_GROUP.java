package fxms.bas.dbo.am;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.08.10 09:55
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AM_GROUP", comment = "관리그룹테이블")
@FxIndex(name = "FX_AM_GROUP__PK", type = INDEX_TYPE.PK, columns = { "AM_GROUP_NO" })
@FxIndex(name = "FX_AM_GROUP__UK", type = INDEX_TYPE.UK, columns = { "AM_GROUP_NAME" })
public class FX_AM_GROUP {

	public FX_AM_GROUP() {
	}

	public static final String FX_SEQ_AMGROUPNO = "FX_SEQ_AMGROUPNO";
	@FxColumn(name = "AM_GROUP_NO", size = 9, comment = "관리그룹번호", sequence = "FX_SEQ_AMGROUPNO")
	private int amGroupNo;

	@FxColumn(name = "AM_GROUP_NAME", size = 50, comment = "관리그룹명")
	private String amGroupName;

	@FxColumn(name = "AM_GROUP_DESCR", size = 200, nullable = true, comment = "관리그룹설명")
	private String amGroupDescr;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 관리그룹번호
	 * 
	 * @return 관리그룹번호
	 */
	public int getAmGroupNo() {
		return amGroupNo;
	}

	/**
	 * 관리그룹번호
	 * 
	 * @param amGroupNo
	 *            관리그룹번호
	 */
	public void setAmGroupNo(int amGroupNo) {
		this.amGroupNo = amGroupNo;
	}

	/**
	 * 관리그룹명
	 * 
	 * @return 관리그룹명
	 */
	public String getAmGroupName() {
		return amGroupName;
	}

	/**
	 * 관리그룹명
	 * 
	 * @param amGroupName
	 *            관리그룹명
	 */
	public void setAmGroupName(String amGroupName) {
		this.amGroupName = amGroupName;
	}

	/**
	 * 관리그룹설명
	 * 
	 * @return 관리그룹설명
	 */
	public String getAmGroupDescr() {
		return amGroupDescr;
	}

	/**
	 * 관리그룹설명
	 * 
	 * @param amGroupDescr
	 *            관리그룹설명
	 */
	public void setAmGroupDescr(String amGroupDescr) {
		this.amGroupDescr = amGroupDescr;
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
