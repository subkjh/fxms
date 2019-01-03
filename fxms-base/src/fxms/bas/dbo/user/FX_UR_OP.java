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

@FxTable(name = "FX_UR_OP", comment = "운용자기능테이블")
@FxIndex(name = "FY_UR_OP__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "OP_NO" })
public class FX_UR_OP {

	public FX_UR_OP() {
	}

	@FxColumn(name = "USER_NO", size = 9, nullable = true, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "OP_NO", size = 9, nullable = true, comment = "운용번호")
	private int opNo;

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
	 * 운용번호
	 * 
	 * @return 운용번호
	 */
	public int getOpNo() {
		return opNo;
	}

	/**
	 * 운용번호
	 * 
	 * @param opNo
	 *            운용번호
	 */
	public void setOpNo(int opNo) {
		this.opNo = opNo;
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
