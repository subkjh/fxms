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

@FxTable(name = "FX_AM_GROUP_MO", comment = "관리그룹대상MO내역")
@FxIndex(name = "FX_AM_GROUP_MO__PK", type = INDEX_TYPE.PK, columns = { "AM_GROUP_NO", "MO_NO" })
public class FX_AM_GROUP_MO  {

	public FX_AM_GROUP_MO() {
	}

	@FxColumn(name = "AM_GROUP_NO", size = 9, comment = "관리그룹번호")
	private int amGroupNo;

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "MO_DESCR", size = 200, nullable = true, comment = "MO설명")
	private String moDescr;

	@FxColumn(name = "ENABLE_YN", size = 1, nullable = true, comment = "활성화여부", defValue = "'Y'")
	private boolean enableYn = true;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

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
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo
	 *            MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * MO설명
	 * 
	 * @return MO설명
	 */
	public String getMoDescr() {
		return moDescr;
	}

	/**
	 * MO설명
	 * 
	 * @param moDescr
	 *            MO설명
	 */
	public void setMoDescr(String moDescr) {
		this.moDescr = moDescr;
	}

	/**
	 * 활성화여부
	 * 
	 * @return 활성화여부
	 */
	public boolean isEnableYn() {
		return enableYn;
	}

	/**
	 * 활성화여부
	 * 
	 * @param enableYn
	 *            활성화여부
	 */
	public void setEnableYn(boolean enableYn) {
		this.enableYn = enableYn;
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
}
