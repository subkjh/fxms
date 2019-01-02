package fxms.bas.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.01.17 09:48
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_MO_ATTR", comment = "MO속성정의테이블")
@FxIndex(name = "FX_CF_MO_ATTR__PK", type = INDEX_TYPE.PK, columns = { "ATTR_CD" })
@FxIndex(name = "FX_CF_MO_ATTR__UK", type = INDEX_TYPE.UK, columns = { "TBL_NM", "COL_NM" })
public class FX_CF_MO_ATTR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 982172016849678132L;

	public FX_CF_MO_ATTR() {
	}

	@FxColumn(name = "ATTR_CD", size = 30, comment = "속성코드")
	private String attrCd;

	@FxColumn(name = "ATTR_NM", size = 50, comment = "속성명")
	private String attrNm;

	@FxColumn(name = "TBL_NM", size = 32, comment = "테이블명")
	private String tblNm;

	@FxColumn(name = "COL_NM", size = 32, comment = "컬럼명")
	private String colNm;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private Number regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Number regDate;

	/**
	 * 속성코드
	 * 
	 * @return 속성코드
	 */
	public String getAttrCd() {
		return attrCd;
	}

	/**
	 * 속성코드
	 * 
	 * @param attrCd
	 *            속성코드
	 */
	public void setAttrCd(String attrCd) {
		this.attrCd = attrCd;
	}

	/**
	 * 속성명
	 * 
	 * @return 속성명
	 */
	public String getAttrNm() {
		return attrNm;
	}

	/**
	 * 속성명
	 * 
	 * @param attrNm
	 *            속성명
	 */
	public void setAttrNm(String attrNm) {
		this.attrNm = attrNm;
	}

	/**
	 * 테이블명
	 * 
	 * @return 테이블명
	 */
	public String getTblNm() {
		return tblNm;
	}

	/**
	 * 테이블명
	 * 
	 * @param tblNm
	 *            테이블명
	 */
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}

	/**
	 * 컬럼명
	 * 
	 * @return 컬럼명
	 */
	public String getColNm() {
		return colNm;
	}

	/**
	 * 컬럼명
	 * 
	 * @param colNm
	 *            컬럼명
	 */
	public void setColNm(String colNm) {
		this.colNm = colNm;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public Number getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(Number regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Number getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(Number regDate) {
		this.regDate = regDate;
	}
}
