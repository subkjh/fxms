package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_MO", comment = "운용자관리MO테이블")
@FxIndex(name = "FX_UR_MO__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "MO_NO" })
public class FX_UR_MO {

	public FX_UR_MO() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "MO_NO", size = 19, comment = "관리대상번호")
	private long moNo;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운용자번호")
	private int regUserNo;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "REG_MEMO", size = 100, nullable = true, comment = "등록사유")
	private String regMemo;

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 관리대상번호
	 * 
	 * @return 관리대상번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * 관리대상번호
	 * 
	 * @param moNo
	 *            관리대상번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 등록운용자번호
	 * 
	 * @return 등록운용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운용자번호
	 * 
	 * @param regUserNo
	 *            등록운용자번호
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
	 * 등록사유
	 * 
	 * @return 등록사유
	 */
	public String getRegMemo() {
		return regMemo;
	}

	/**
	 * 등록사유
	 * 
	 * @param regMemo
	 *            등록사유
	 */
	public void setRegMemo(String regMemo) {
		this.regMemo = regMemo;
	}
}
