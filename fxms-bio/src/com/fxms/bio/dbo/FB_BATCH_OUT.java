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

@FxTable(name = "FB_BATCH_OUT", comment = "배치생산내역테이블")
@FxIndex(name = "FB_BATCH_OUT__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID", "N_DAYS", "PRDC_CODE" })
public class FB_BATCH_OUT implements FX_COMMON {

	public FB_BATCH_OUT() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "N_DAYS", size = 5, comment = "차수")
	private int nDays;

	@FxColumn(name = "PRDC_CODE", size = 20, comment = "생산품코드")
	private String prdcCode;

	@FxColumn(name = "OUT_AMT", size = 19, comment = "생산량", defValue = "0")
	private double outAmt = 0;

	@FxColumn(name = "OUT_MEMO", size = 200, nullable = true, comment = "생산메모")
	private String outMemo;

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
	 * 차수
	 * 
	 * @return 차수
	 */
	public int getNDays() {
		return nDays;
	}

	/**
	 * 차수
	 * 
	 * @param nDays
	 *            차수
	 */
	public void setNDays(int nDays) {
		this.nDays = nDays;
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
	 * 생산량
	 * 
	 * @return 생산량
	 */
	public double getOutAmt() {
		return outAmt;
	}

	/**
	 * 생산량
	 * 
	 * @param outAmt
	 *            생산량
	 */
	public void setOutAmt(double outAmt) {
		this.outAmt = outAmt;
	}

	/**
	 * 생산메모
	 * 
	 * @return 생산메모
	 */
	public String getOutMemo() {
		return outMemo;
	}

	/**
	 * 생산메모
	 * 
	 * @param outMemo
	 *            생산메모
	 */
	public void setOutMemo(String outMemo) {
		this.outMemo = outMemo;
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
