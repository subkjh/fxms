package fxms.bas.dbo.diag;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.02.27 14:27
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_DI_MAIN", comment = "다이아그램테이블")
@FxIndex(name = "FX_DI_MAIN__PK", type = INDEX_TYPE.PK, columns = { "DIAG_NO" })
public class FX_DI_MAIN {

	public FX_DI_MAIN() {
	}

	public static final String FX_SEQ_DIAG_NO = "FX_SEQ_DIAG_NO";
	@FxColumn(name = "DIAG_NO", size = 9, comment = "챠트명", sequence = "FX_SEQ_DIAG_NO")
	private Integer diagNo;

	@FxColumn(name = "DIAG_TITLE", size = 30, comment = "MO분류 ")
	private String diagTitle;

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private Integer userNo;

	@FxColumn(name = "OP_NO", size = 9, comment = "운용번호")
	private Integer opNo;

	@FxColumn(name = "REG_DATE", size = 14, comment = "등록일시", operator = COLUMN_OP.insert)
	private Long regDate;

	@FxColumn(name = "CHG_DATE", size = 14, comment = "수정일시")
	private Long chgDate;

	/**
	 * 챠트명
	 * 
	 * @return 챠트명
	 */
	public Integer getDiagNo() {
		return diagNo;
	}

	/**
	 * 챠트명
	 * 
	 * @param diagNo
	 *            챠트명
	 */
	public void setDiagNo(Integer diagNo) {
		this.diagNo = diagNo;
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getDiagTitle() {
		return diagTitle;
	}

	/**
	 * MO분류
	 * 
	 * @param diagTitle
	 *            MO분류
	 */
	public void setDiagTitle(String diagTitle) {
		this.diagTitle = diagTitle;
	}

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public Integer getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(Integer userNo) {
		this.userNo = userNo;
	}

	/**
	 * 운용번호
	 * 
	 * @return 운용번호
	 */
	public Integer getOpNo() {
		return opNo;
	}

	/**
	 * 운용번호
	 * 
	 * @param opNo
	 *            운용번호
	 */
	public void setOpNo(Integer opNo) {
		this.opNo = opNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(Long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public Long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(Long chgDate) {
		this.chgDate = chgDate;
	}
}
