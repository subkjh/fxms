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

@FxTable(name = "FB_BATCH_IN", comment = "배치원자래주입테이블")
@FxIndex(name = "FB_BATCH_IN__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID", "N_DAYS", "MTRL_CODE" })
public class FB_BATCH_IN implements FX_COMMON {

	public FB_BATCH_IN() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "N_DAYS", size = 5, comment = "차수")
	private int nDays;

	@FxColumn(name = "MTRL_CODE", size = 20, comment = "원자재코드")
	private String mtrlCode;

	@FxColumn(name = "MTRL_AMT", size = 19, comment = "원자재량", defValue = "0")
	private double mtrlAmt = 0;

	@FxColumn(name = "MTRL_UCST", size = 19, comment = "원자재단가", defValue = "1")
	private double mtrlUcst = 1;

	@FxColumn(name = "IN_MEMO", size = 200, nullable = true, comment = "투입메모")
	private String inMemo;

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
	 * 원자재량
	 * 
	 * @return 원자재량
	 */
	public double getMtrlAmt() {
		return mtrlAmt;
	}

	/**
	 * 원자재량
	 * 
	 * @param mtrlAmt
	 *            원자재량
	 */
	public void setMtrlAmt(double mtrlAmt) {
		this.mtrlAmt = mtrlAmt;
	}

	/**
	 * 원자재단가
	 * 
	 * @return 원자재단가
	 */
	public double getMtrlUcst() {
		return mtrlUcst;
	}

	/**
	 * 원자재단가
	 * 
	 * @param mtrlUcst
	 *            원자재단가
	 */
	public void setMtrlUcst(double mtrlUcst) {
		this.mtrlUcst = mtrlUcst;
	}

	/**
	 * 투입메모
	 * 
	 * @return 투입메모
	 */
	public String getInMemo() {
		return inMemo;
	}

	/**
	 * 투입메모
	 * 
	 * @param inMemo
	 *            투입메모
	 */
	public void setInMemo(String inMemo) {
		this.inMemo = inMemo;
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
