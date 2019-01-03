package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.20 13:09
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_PROPERTY", comment = "사용자인자테이블")
@FxIndex(name = "FX_UR_PROPERTY__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "PROPERTY_NAME" })
public class FX_UR_PROPERTY {

	public FX_UR_PROPERTY() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "PROPERTY_NAME", size = 100, comment = "속성명")
	private String propertyName;

	@FxColumn(name = "PROPERTY_VALUE", size = 240, comment = "속성값")
	private String propertyValue;

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
