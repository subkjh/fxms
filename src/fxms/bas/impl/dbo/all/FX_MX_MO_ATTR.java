package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.01.18 15:35
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MX_MO_ATTR", comment = "관리대상속성테이블")
@FxIndex(name = "FX_MX_MO_ATTR__PK", type = INDEX_TYPE.PK, columns = { "MO_NO", "ATTR_NAME" })
@FxIndex(name = "FX_MX_MO_ATTR__FK1", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FX_MX_MO_ATTR implements Serializable {

	public FX_MX_MO_ATTR() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
	private Long moNo;

	@FxColumn(name = "ATTR_NAME", size = 100, comment = "속성명")
	private String attrName;

	@FxColumn(name = "ATTR_VALUE", size = 240, comment = "속성값")
	private String attrValue;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public Long getMoNo() {
		return moNo;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo MO번호
	 */
	public void setMoNo(Long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 속성명
	 * 
	 * @return 속성명
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * 속성명
	 * 
	 * @param attrName 속성명
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * 속성값
	 * 
	 * @return 속성값
	 */
	public String getAttrValue() {
		return attrValue;
	}

	/**
	 * 속성값
	 * 
	 * @param attrValue 속성값
	 */
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}
}
