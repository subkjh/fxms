package com.fxms.bio.dbo;

import fxms.bas.dbo.FX_COMMON;
import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.07.19 11:19
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FB_BATCH", comment = "배치테이블")
@FxIndex(name = "FB_BATCH__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID" })
public class FB_BATCH implements FX_COMMON {

	public FB_BATCH() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "PRDC_CODE", size = 20, comment = "대표생산품코드")
	private String prdcCode;

	@FxColumn(name = "SEED_CODE", size = 20, comment = "종균코드")
	private String seedCode;

	@FxColumn(name = "COMPANY_INLO_NO", size = 9, comment = "회사설치위치번호")
	private int companyInloNo;

	@FxColumn(name = "CUR_N_DAYS", size = 5, nullable = true, comment = "현재차수", defValue = "0")
	private int curNDays = 0;

	@FxColumn(name = "BATCH_SRT_DATE", size = 14, comment = "배치시작일시", defValue = "0")
	private long batchSrtDate = 0;

	@FxColumn(name = "BATCH_END_DATE", size = 14, comment = "배치종료일시", defValue = "0")
	private long batchEndDate = 0;

	@FxColumn(name = "END_YN", size = 1, comment = "종료여부", defValue = "'N'")
	private boolean endYn = false;

	@FxColumn(name = "DEL_YN", size = 1, comment = "삭제여부", defValue = "'N'")
	private boolean delYn = false;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0", operator = COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", operator = COLUMN_OP.insert)
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	/**
	 * 배치ID
	 * 
	 * @return 배치ID
	 */
	public String getBatchId() {
		return batchId;
	}

	/**
	 * 배치ID
	 * 
	 * @param batchId
	 *            배치ID
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	/**
	 * 대표생산품코드
	 * 
	 * @return 대표생산품코드
	 */
	public String getPrdcCode() {
		return prdcCode;
	}

	/**
	 * 대표생산품코드
	 * 
	 * @param prdcCode
	 *            대표생산품코드
	 */
	public void setPrdcCode(String prdcCode) {
		this.prdcCode = prdcCode;
	}

	/**
	 * 종균코드
	 * 
	 * @return 종균코드
	 */
	public String getSeedCode() {
		return seedCode;
	}

	/**
	 * 종균코드
	 * 
	 * @param seedCode
	 *            종균코드
	 */
	public void setSeedCode(String seedCode) {
		this.seedCode = seedCode;
	}

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
	 * 현재차수
	 * 
	 * @return 현재차수
	 */
	public int getCurNDays() {
		return curNDays;
	}

	/**
	 * 현재차수
	 * 
	 * @param curNDays
	 *            현재차수
	 */
	public void setCurNDays(int curNDays) {
		this.curNDays = curNDays;
	}

	/**
	 * 배치시작일시
	 * 
	 * @return 배치시작일시
	 */
	public long getBatchSrtDate() {
		return batchSrtDate;
	}

	/**
	 * 배치시작일시
	 * 
	 * @param batchSrtDate
	 *            배치시작일시
	 */
	public void setBatchSrtDate(long batchSrtDate) {
		this.batchSrtDate = batchSrtDate;
	}

	/**
	 * 배치종료일시
	 * 
	 * @return 배치종료일시
	 */
	public long getBatchEndDate() {
		return batchEndDate;
	}

	/**
	 * 배치종료일시
	 * 
	 * @param batchEndDate
	 *            배치종료일시
	 */
	public void setBatchEndDate(long batchEndDate) {
		this.batchEndDate = batchEndDate;
	}

	/**
	 * 종료여부
	 * 
	 * @return 종료여부
	 */
	public boolean isEndYn() {
		return endYn;
	}

	/**
	 * 종료여부
	 * 
	 * @param endYn
	 *            종료여부
	 */
	public void setEndYn(boolean endYn) {
		this.endYn = endYn;
	}

	/**
	 * 삭제여부
	 * 
	 * @return 삭제여부
	 */
	public boolean isDelYn() {
		return delYn;
	}

	/**
	 * 삭제여부
	 * 
	 * @param delYn
	 *            삭제여부
	 */
	public void setDelYn(boolean delYn) {
		this.delYn = delYn;
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
