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

@FxTable(name = "FB_CD_MTRL", comment = "원자재정의테이블")
@FxIndex(name = "FB_CD_MTRL__PK", type = INDEX_TYPE.PK, columns = { "COMPANY_INLO_NO", "MTRL_CODE" })
public class FB_CD_MTRL implements FX_COMMON {

	public FB_CD_MTRL() {
	}

	@FxColumn(name = "COMPANY_INLO_NO", size = 9, comment = "회사설치위치번호")
	private int companyInloNo;

	@FxColumn(name = "MTRL_CODE", size = 30, comment = "원자재코드")
	private String mtrlCode;

	@FxColumn(name = "MTRL_NAME", size = 50, comment = "원자재명")
	private String mtrlName;

	@FxColumn(name = "MTRL_DESCR", size = 200, nullable = true, comment = "원자재설명")
	private String mtrlDescr;

	@FxColumn(name = "MTRL_UNIT", size = 30, comment = "원자재단위")
	private String mtrlUnit;

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
	 * 원자재코드
	 * 
	 * @return 원자재코드
	 */
	public String getMtrlCode() {
		return mtrlCode;
	}

	/**
	 * 원자재코드
	 * 
	 * @param mtrlCode
	 *            원자재코드
	 */
	public void setMtrlCode(String mtrlCode) {
		this.mtrlCode = mtrlCode;
	}

	/**
	 * 원자재명
	 * 
	 * @return 원자재명
	 */
	public String getMtrlName() {
		return mtrlName;
	}

	/**
	 * 원자재명
	 * 
	 * @param mtrlName
	 *            원자재명
	 */
	public void setMtrlName(String mtrlName) {
		this.mtrlName = mtrlName;
	}

	/**
	 * 원자재설명
	 * 
	 * @return 원자재설명
	 */
	public String getMtrlDescr() {
		return mtrlDescr;
	}

	/**
	 * 원자재설명
	 * 
	 * @param mtrlDescr
	 *            원자재설명
	 */
	public void setMtrlDescr(String mtrlDescr) {
		this.mtrlDescr = mtrlDescr;
	}

	/**
	 * 원자재단위
	 * 
	 * @return 원자재단위
	 */
	public String getMtrlUnit() {
		return mtrlUnit;
	}

	/**
	 * 원자재단위
	 * 
	 * @param mtrlUnit
	 *            원자재단위
	 */
	public void setMtrlUnit(String mtrlUnit) {
		this.mtrlUnit = mtrlUnit;
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
