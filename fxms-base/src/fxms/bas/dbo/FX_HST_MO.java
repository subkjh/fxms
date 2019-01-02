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

@FxTable(name = "FX_HST_MO", comment = "M이력테이블")
@FxIndex(name = "FX_HST_MO__KEY", type = INDEX_TYPE.KEY, columns = { "MO_NO" })
public class FX_HST_MO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4295587350100659094L;

	public FX_HST_MO() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "관리대상번호")
	private Number moNo;

	@FxColumn(name = "MO_NAME", size = 200, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_HST_CD", size = 30, comment = "MO변경구분코드")
	private String moHstCd;

	@FxColumn(name = "MO_HST_NM", size = 50, nullable = true, comment = "MO변경구분명")
	private String moHstNm;

	@FxColumn(name = "ATTR_CD", size = 30, nullable = true, comment = "속성코드")
	private String attrCd;

	@FxColumn(name = "ATTR_NM", size = 50, nullable = true, comment = "속성명")
	private String attrNm;

	@FxColumn(name = "ATTR_BF_VAL", size = 100, nullable = true, comment = "이전속성값")
	private String attrBfVal;

	@FxColumn(name = "ATTR_AF_VAL", size = 100, nullable = true, comment = "이후속성값")
	private String attrAfVal;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private Number regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Number regDate;

	/**
	 * 관리대상번호
	 * 
	 * @return 관리대상번호
	 */
	public Number getMoNo() {
		return moNo;
	}

	/**
	 * 관리대상번호
	 * 
	 * @param moNo
	 *            관리대상번호
	 */
	public void setMoNo(Number moNo) {
		this.moNo = moNo;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	/**
	 * MO명
	 * 
	 * @param moName
	 *            MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO변경구분코드
	 * 
	 * @return MO변경구분코드
	 */
	public String getMoHstCd() {
		return moHstCd;
	}

	/**
	 * MO변경구분코드
	 * 
	 * @param moHstCd
	 *            MO변경구분코드
	 */
	public void setMoHstCd(String moHstCd) {
		this.moHstCd = moHstCd;
	}

	/**
	 * MO변경구분명
	 * 
	 * @return MO변경구분명
	 */
	public String getMoHstNm() {
		return moHstNm;
	}

	/**
	 * MO변경구분명
	 * 
	 * @param moHstNm
	 *            MO변경구분명
	 */
	public void setMoHstNm(String moHstNm) {
		this.moHstNm = moHstNm;
	}

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
	 * 이전속성값
	 * 
	 * @return 이전속성값
	 */
	public String getAttrBfVal() {
		return attrBfVal;
	}

	/**
	 * 이전속성값
	 * 
	 * @param attrBfVal
	 *            이전속성값
	 */
	public void setAttrBfVal(String attrBfVal) {
		this.attrBfVal = attrBfVal;
	}

	/**
	 * 이후속성값
	 * 
	 * @return 이후속성값
	 */
	public String getAttrAfVal() {
		return attrAfVal;
	}

	/**
	 * 이후속성값
	 * 
	 * @param attrAfVal
	 *            이후속성값
	 */
	public void setAttrAfVal(String attrAfVal) {
		this.attrAfVal = attrAfVal;
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
