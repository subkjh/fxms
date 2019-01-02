package com.fxms.bio.dbo;

import fxms.bas.dbo.FX_COMMON;
import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.07.16 15:37
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FB_CD_PRDC", comment = "생산품정의테이블")
@FxIndex(name = "FB_CD_PRDC__PK", type = INDEX_TYPE.PK, columns = { "COMPANY_INLO_NO", "PRDC_CODE" })
public class FB_CD_PRDC implements FX_COMMON {

	public FB_CD_PRDC() {
	}

	@FxColumn(name = "COMPANY_INLO_NO", size = 9, comment = "회사설치위치번호")
	private int companyInloNo;

	@FxColumn(name = "PRDC_CODE", size = 20, comment = "생산품코드")
	private String prdcCode;

	@FxColumn(name = "PRDC_NM", size = 50, comment = "생산품명")
	private String prdcNm;

	@FxColumn(name = "PRDC_DESCR", size = 200, nullable = true, comment = "생산품설명")
	private String prdcDescr;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0", operator = COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", operator = COLUMN_OP.insert)
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 회사설치위치번호
	 * 
	 * @return 회사설치위치번호
	 */
	public int getCompanyInloNo() {
		return companyInloNo;
	}

	/**
	 * 회사설치위치번호
	 * 
	 * @param companyInloNo
	 *            회사설치위치번호
	 */
	public void setCompanyInloNo(int companyInloNo) {
		this.companyInloNo = companyInloNo;
	}

	/**
	 * 생산품코드
	 * 
	 * @return 생산품코드
	 */
	public String getPrdcCode() {
		return prdcCode;
	}

	/**
	 * 생산품코드
	 * 
	 * @param prdcCode
	 *            생산품코드
	 */
	public void setPrdcCode(String prdcCode) {
		this.prdcCode = prdcCode;
	}

	/**
	 * 생산품명
	 * 
	 * @return 생산품명
	 */
	public String getPrdcNm() {
		return prdcNm;
	}

	/**
	 * 생산품명
	 * 
	 * @param prdcNm
	 *            생산품명
	 */
	public void setPrdcNm(String prdcNm) {
		this.prdcNm = prdcNm;
	}

	/**
	 * 생산품설명
	 * 
	 * @return 생산품설명
	 */
	public String getPrdcDescr() {
		return prdcDescr;
	}

	/**
	 * 생산품설명
	 * 
	 * @param prdcDescr
	 *            생산품설명
	 */
	public void setPrdcDescr(String prdcDescr) {
		this.prdcDescr = prdcDescr;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
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
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}
}
