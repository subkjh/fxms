package fxms.bas.dbo.ui;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.15 10:17
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UI_GROUP", comment = "화면그룹테이블")
@FxIndex(name = "FY_UI_GROUP__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "UI_GROUP_NO" })
public class FX_UI_GROUP {

	public FX_UI_GROUP() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "UI_GROUP_NO", size = 5, comment = "화면그룹번호")
	private int uiGroupNo;

	@FxColumn(name = "UI_GROUP_NAME", size = 50, comment = "화면ID")
	private String uiGroupName;

	@FxColumn(name = "VISIBLE_YN", size = 1, comment = "표시여부", defValue = "'Y'")
	private boolean visibleYn = true;

	@FxColumn(name = "SEQ_BY", size = 5, nullable = true, comment = "정렬순서")
	private int seqBy;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일자")
	private long chgDate;

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
	 * 화면그룹번호
	 * 
	 * @return 화면그룹번호
	 */
	public int getUiGroupNo() {
		return uiGroupNo;
	}

	/**
	 * 화면그룹번호
	 * 
	 * @param uiGroupNo
	 *            화면그룹번호
	 */
	public void setUiGroupNo(int uiGroupNo) {
		this.uiGroupNo = uiGroupNo;
	}

	/**
	 * 화면ID
	 * 
	 * @return 화면ID
	 */
	public String getUiGroupName() {
		return uiGroupName;
	}

	/**
	 * 화면ID
	 * 
	 * @param uiGroupName
	 *            화면ID
	 */
	public void setUiGroupName(String uiGroupName) {
		this.uiGroupName = uiGroupName;
	}

	/**
	 * 표시여부
	 * 
	 * @return 표시여부
	 */
	public boolean isVisibleYn() {
		return visibleYn;
	}

	/**
	 * 표시여부
	 * 
	 * @param visibleYn
	 *            표시여부
	 */
	public void setVisibleYn(boolean visibleYn) {
		this.visibleYn = visibleYn;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSeqBy() {
		return seqBy;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	/**
	 * 수정일자
	 * 
	 * @return 수정일자
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일자
	 * 
	 * @param chgDate
	 *            수정일자
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}
}
