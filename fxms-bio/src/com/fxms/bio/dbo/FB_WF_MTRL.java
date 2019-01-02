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

@FxTable(name = "FB_WF_MTRL", comment = "원자재구입목록테이블")
@FxIndex(name = "FB_WF_MTRL__KEY", type = INDEX_TYPE.KEY, columns = { "COMPANY_INLO_NO", "MTRL_CODE", "START_TIME" })
public class FB_WF_MTRL implements FX_COMMON {

	public FB_WF_MTRL() {
	}

	@FxColumn(name = "COMPANY_INLO_NO", size = 9, comment = "회사설치위치번호")
	private int companyInloNo;

	@FxColumn(name = "MTRL_CODE", size = 30, comment = "원자재코드")
	private String mtrlCode;

	@FxColumn(name = "UNIT_PRICE", size = 19, comment = "구입단가")
	private double unitPrice;

	@FxColumn(name = "IN_AMOUNT", size = 19, comment = "구입량")
	private long inAmount;

	@FxColumn(name = "BUY_USER_NO", size = 9, nullable = true, comment = "구입운용자")
	private int buyUserNo;

	@FxColumn(name = "BUY_DATE", size = 14, nullable = true, comment = "구입일자")
	private long buyDate;

	@FxColumn(name = "START_TIME", size = 14, nullable = true, comment = "사용시작일자")
	private long startTime;

	@FxColumn(name = "STOCK_AMOUNT", size = 19, nullable = true, comment = "재고량")
	private long stockAmount;

	@FxColumn(name = "END_TIME", size = 14, nullable = true, comment = "사용종료일자")
	private long endTime;

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
	 * 구입단가
	 * 
	 * @return 구입단가
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * 구입단가
	 * 
	 * @param unitPrice
	 *            구입단가
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * 구입량
	 * 
	 * @return 구입량
	 */
	public long getInAmount() {
		return inAmount;
	}

	/**
	 * 구입량
	 * 
	 * @param inAmount
	 *            구입량
	 */
	public void setInAmount(long inAmount) {
		this.inAmount = inAmount;
	}

	/**
	 * 구입운용자
	 * 
	 * @return 구입운용자
	 */
	public int getBuyUserNo() {
		return buyUserNo;
	}

	/**
	 * 구입운용자
	 * 
	 * @param buyUserNo
	 *            구입운용자
	 */
	public void setBuyUserNo(int buyUserNo) {
		this.buyUserNo = buyUserNo;
	}

	/**
	 * 구입일자
	 * 
	 * @return 구입일자
	 */
	public long getBuyDate() {
		return buyDate;
	}

	/**
	 * 구입일자
	 * 
	 * @param buyDate
	 *            구입일자
	 */
	public void setBuyDate(long buyDate) {
		this.buyDate = buyDate;
	}

	/**
	 * 사용시작일자
	 * 
	 * @return 사용시작일자
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 사용시작일자
	 * 
	 * @param startTime
	 *            사용시작일자
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 재고량
	 * 
	 * @return 재고량
	 */
	public long getStockAmount() {
		return stockAmount;
	}

	/**
	 * 재고량
	 * 
	 * @param stockAmount
	 *            재고량
	 */
	public void setStockAmount(long stockAmount) {
		this.stockAmount = stockAmount;
	}

	/**
	 * 사용종료일자
	 * 
	 * @return 사용종료일자
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * 사용종료일자
	 * 
	 * @param endTime
	 *            사용종료일자
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
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
