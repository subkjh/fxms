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

@FxTable(name = "FX_UI_BASIC", comment = "화면기본테이블")
@FxIndex(name = "FY_UI_BASIC__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "UI_GROUP_NO", "UI_NO" })
public class FX_UI_BASIC {

	public FX_UI_BASIC() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "UI_GROUP_NO", size = 5, comment = "화면그룹번호")
	private int uiGroupNo;

	@FxColumn(name = "UI_NO", size = 5, comment = "화면ID")
	private int uiNo;

	@FxColumn(name = "UI_TITLE", size = 100, comment = "화면타이틀")
	private String uiTitle;

	@FxColumn(name = "UI_STYLE", size = 20, comment = "화면스타일")
	private String uiStyle;

	@FxColumn(name = "UI_X", size = 9, nullable = true, comment = "화면X좌표")
	private int uiX;

	@FxColumn(name = "UI_Y", size = 9, nullable = true, comment = "화면Y좌표")
	private int uiY;

	@FxColumn(name = "UI_WIDTH", size = 9, nullable = true, comment = "화면폭")
	private int uiWidth;

	@FxColumn(name = "UI_HEIGHT", size = 9, nullable = true, comment = "화면높이")
	private int uiHeight;

	@FxColumn(name = "OP_NO", size = 9, comment = "운용번호")
	private int opNo;

	@FxColumn(name = "VISIBLE_YN", size = 1, comment = "표시여부", defValue = "'Y'")
	private boolean visibleYn = true;

	@FxColumn(name = "SEQ_BY", size = 5, nullable = true, comment = "정렬순서")
	private int seqBy;

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
	public int getUiNo() {
		return uiNo;
	}

	/**
	 * 화면ID
	 * 
	 * @param uiNo
	 *            화면ID
	 */
	public void setUiNo(int uiNo) {
		this.uiNo = uiNo;
	}

	/**
	 * 화면타이틀
	 * 
	 * @return 화면타이틀
	 */
	public String getUiTitle() {
		return uiTitle;
	}

	/**
	 * 화면타이틀
	 * 
	 * @param uiTitle
	 *            화면타이틀
	 */
	public void setUiTitle(String uiTitle) {
		this.uiTitle = uiTitle;
	}

	/**
	 * 화면스타일
	 * 
	 * @return 화면스타일
	 */
	public String getUiStyle() {
		return uiStyle;
	}

	/**
	 * 화면스타일
	 * 
	 * @param uiStyle
	 *            화면스타일
	 */
	public void setUiStyle(String uiStyle) {
		this.uiStyle = uiStyle;
	}

	/**
	 * 화면X좌표
	 * 
	 * @return 화면X좌표
	 */
	public int getUiX() {
		return uiX;
	}

	/**
	 * 화면X좌표
	 * 
	 * @param uiX
	 *            화면X좌표
	 */
	public void setUiX(int uiX) {
		this.uiX = uiX;
	}

	/**
	 * 화면Y좌표
	 * 
	 * @return 화면Y좌표
	 */
	public int getUiY() {
		return uiY;
	}

	/**
	 * 화면Y좌표
	 * 
	 * @param uiY
	 *            화면Y좌표
	 */
	public void setUiY(int uiY) {
		this.uiY = uiY;
	}

	/**
	 * 화면폭
	 * 
	 * @return 화면폭
	 */
	public int getUiWidth() {
		return uiWidth;
	}

	/**
	 * 화면폭
	 * 
	 * @param uiWidth
	 *            화면폭
	 */
	public void setUiWidth(int uiWidth) {
		this.uiWidth = uiWidth;
	}

	/**
	 * 화면높이
	 * 
	 * @return 화면높이
	 */
	public int getUiHeight() {
		return uiHeight;
	}

	/**
	 * 화면높이
	 * 
	 * @param uiHeight
	 *            화면높이
	 */
	public void setUiHeight(int uiHeight) {
		this.uiHeight = uiHeight;
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
}
