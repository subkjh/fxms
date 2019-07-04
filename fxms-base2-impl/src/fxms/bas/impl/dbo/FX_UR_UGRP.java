package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.23 16:50
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_UGRP", comment = "운용자그룹테이블")
@FxIndex(name = "FX_UR_UGRP__PK", type = INDEX_TYPE.PK, columns = { "UGRP_NO" })
@FxIndex(name = "FX_UR_UGRP__UK_NM", type = INDEX_TYPE.UK, columns = { "UGRP_NAME" })
public class FX_UR_UGRP {

	public FX_UR_UGRP() {
	}

	public static final String FX_SEQ_UGRPNO = "FX_SEQ_UGRPNO";
	@FxColumn(name = "UGRP_NO", size = 9, comment = "운용자그룹번호", sequence = "FX_SEQ_UGRPNO")
	private int ugrpNo;

	@FxColumn(name = "UGRP_NAME", size = 100, comment = "운용자그룹명")
	private String ugrpName;

	@FxColumn(name = "UGRP_DESCR", size = 200, nullable = true, comment = "운용자그룹설명")
	private String ugrpDescr;

	@FxColumn(name = "RESERVED_YN", size = 1, nullable = true, comment = "예약여부")
	private boolean reservedYn;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 운용자그룹명
	 * 
	 * @return 운용자그룹명
	 */
	public String getUgrpName() {
		return ugrpName;
	}

	/**
	 * 운용자그룹명
	 * 
	 * @param ugrpName
	 *            운용자그룹명
	 */
	public void setUgrpName(String ugrpName) {
		this.ugrpName = ugrpName;
	}

	/**
	 * 운용자그룹설명
	 * 
	 * @return 운용자그룹설명
	 */
	public String getUgrpDescr() {
		return ugrpDescr;
	}

	/**
	 * 운용자그룹설명
	 * 
	 * @param ugrpDescr
	 *            운용자그룹설명
	 */
	public void setUgrpDescr(String ugrpDescr) {
		this.ugrpDescr = ugrpDescr;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	public boolean isReservedYn() {
		return reservedYn;
	}

	public void setReservedYn(boolean reservedYn) {
		this.reservedYn = reservedYn;
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
