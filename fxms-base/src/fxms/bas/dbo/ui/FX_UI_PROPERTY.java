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

@FxTable(name = "FX_UI_PROPERTY", comment = "화면인자테이블")
@FxIndex(name = "FY_UI_PROPERTY__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "UI_GROUP_NO", "UI_NO",
		"PROPERTY_NAME" })
public class FX_UI_PROPERTY {

	public FX_UI_PROPERTY() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "UI_GROUP_NO", size = 5, comment = "화면그룹번호")
	private int uiGroupNo;

	@FxColumn(name = "UI_NO", size = 5, comment = "화면ID")
	private int uiNo;

	@FxColumn(name = "PROPERTY_NAME", size = 100, comment = "속성명")
	private String propertyName;

	@FxColumn(name = "PROPERTY_VALUE", size = 240, comment = "속성값")
	private String propertyValue;

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
	 * 속성명
	 * 
	 * @return 속성명
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * 속성명
	 * 
	 * @param propertyName
	 *            속성명
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 속성값
	 * 
	 * @return 속성값
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * 속성값
	 * 
	 * @param propertyValue
	 *            속성값
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
}
