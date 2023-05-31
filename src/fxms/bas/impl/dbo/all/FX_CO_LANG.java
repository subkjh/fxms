package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.04.20 13:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CO_LANG", comment = "공통언어테이블")
@FxIndex(name = "FX_CO_LANG__PK", type = INDEX_TYPE.PK, columns = { "LANG_KEY", "LANG_CD" })
@FxIndex(name = "FX_CO_LANG__KEY", type = INDEX_TYPE.KEY, columns = { "LANG_CD" })
public class FX_CO_LANG {

	public FX_CO_LANG() {
	}

	@FxColumn(name = "LANG_KEY", size = 400, comment = "언어키")
	private String langKey;

	@FxColumn(name = "LANG_CD", size = 10, comment = "언어코드")
	private String langCd;

	@FxColumn(name = "LANG_VALUE", size = 400, comment = "언어값")
	private String langValue;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * 언어키
	 * 
	 * @return 언어키
	 */
	public String getLangKey() {
		return langKey;
	}

	/**
	 * 언어키
	 * 
	 * @param langKey 언어키
	 */
	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	/**
	 * 언어코드
	 * 
	 * @return 언어코드
	 */
	public String getLangCd() {
		return langCd;
	}

	/**
	 * 언어코드
	 * 
	 * @param langCd 언어코드
	 */
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}

	/**
	 * 언어값
	 * 
	 * @return 언어값
	 */
	public String getLangValue() {
		return langValue;
	}

	/**
	 * 언어값
	 * 
	 * @param langValue 언어값
	 */
	public void setLangValue(String langValue) {
		this.langValue = langValue;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
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
