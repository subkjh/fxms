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

@FxTable(name = "FB_BATCH_DATA", comment = "배치데이터테이블")
@FxIndex(name = "FB_BATCH_DATA__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID", "YYYYMMDD" })
@FxIndex(name = "FB_BATCH_DATA__FK_NO", type = INDEX_TYPE.FK, columns = {
		"BATCH_ID" }, fkTable = "FB_BATCH", fkColumn = "BATCH_ID")
public class FB_BATCH_DATA implements FX_COMMON {

	public FB_BATCH_DATA() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "YYYYMMDD", size = 8, comment = "일자")
	private String yyyymmdd;

	@FxColumn(name = "PBR_MO_NO", size = 19, comment = "PBR 관리번호")
	private long pbrMoNo;

	@FxColumn(name = "N_DAYS", size = 5, comment = "차수")
	private int nDays;

	@FxColumn(name = "BIOMASS", size = 19, nullable = true, comment = "바이오매스")
	private double biomass;

	@FxColumn(name = "N", size = 19, nullable = true, comment = "n")
	private double n;

	@FxColumn(name = "P", size = 19, nullable = true, comment = "p")
	private double p;

	@FxColumn(name = "CELL", size = 19, nullable = true, comment = "cell")
	private double cell;

	@FxColumn(name = "AERATION", size = 19, nullable = true, comment = "aeration")
	private double aeration;

	@FxColumn(name = "CO2", size = 19, nullable = true, comment = "co2")
	private double co2;

	@FxColumn(name = "AXC", size = 19, nullable = true, comment = "axc")
	private double axc;

	@FxColumn(name = "MEMO", size = 200, nullable = true, comment = "MEMO")
	private String memo;

	@FxColumn(name = "PROCESS", size = 10, nullable = true, comment = "process")
	private String process;

	@FxColumn(name = "END_YN", size = 1, nullable = true, comment = "isEnd", defValue = "'N'")
	private boolean endYn = false;

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
	 * 일자
	 * 
	 * @return 일자
	 */
	public String getYyyymmdd() {
		return yyyymmdd;
	}

	/**
	 * 일자
	 * 
	 * @param yyyymmdd
	 *            일자
	 */
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	/**
	 * PBR 관리번호
	 * 
	 * @return PBR 관리번호
	 */
	public long getPbrMoNo() {
		return pbrMoNo;
	}

	/**
	 * PBR 관리번호
	 * 
	 * @param pbrMoNo
	 *            PBR 관리번호
	 */
	public void setPbrMoNo(long pbrMoNo) {
		this.pbrMoNo = pbrMoNo;
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
	 * 바이오매스
	 * 
	 * @return 바이오매스
	 */
	public double getBiomass() {
		return biomass;
	}

	/**
	 * 바이오매스
	 * 
	 * @param biomass
	 *            바이오매스
	 */
	public void setBiomass(double biomass) {
		this.biomass = biomass;
	}

	/**
	 * n
	 * 
	 * @return n
	 */
	public double getN() {
		return n;
	}

	/**
	 * n
	 * 
	 * @param n
	 *            n
	 */
	public void setN(double n) {
		this.n = n;
	}

	/**
	 * p
	 * 
	 * @return p
	 */
	public double getP() {
		return p;
	}

	/**
	 * p
	 * 
	 * @param p
	 *            p
	 */
	public void setP(double p) {
		this.p = p;
	}

	/**
	 * cell
	 * 
	 * @return cell
	 */
	public double getCell() {
		return cell;
	}

	/**
	 * cell
	 * 
	 * @param cell
	 *            cell
	 */
	public void setCell(double cell) {
		this.cell = cell;
	}

	/**
	 * aeration
	 * 
	 * @return aeration
	 */
	public double getAeration() {
		return aeration;
	}

	/**
	 * aeration
	 * 
	 * @param aeration
	 *            aeration
	 */
	public void setAeration(double aeration) {
		this.aeration = aeration;
	}

	/**
	 * co2
	 * 
	 * @return co2
	 */
	public double getCo2() {
		return co2;
	}

	/**
	 * co2
	 * 
	 * @param co2
	 *            co2
	 */
	public void setCo2(double co2) {
		this.co2 = co2;
	}

	/**
	 * axc
	 * 
	 * @return axc
	 */
	public double getAxc() {
		return axc;
	}

	/**
	 * axc
	 * 
	 * @param axc
	 *            axc
	 */
	public void setAxc(double axc) {
		this.axc = axc;
	}

	/**
	 * MEMO
	 * 
	 * @return MEMO
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * MEMO
	 * 
	 * @param memo
	 *            MEMO
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * process
	 * 
	 * @return process
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * process
	 * 
	 * @param process
	 *            process
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * isEnd
	 * 
	 * @return isEnd
	 */
	public boolean isEndYn() {
		return endYn;
	}

	/**
	 * isEnd
	 * 
	 * @param endYn
	 *            isEnd
	 */
	public void setEndYn(boolean endYn) {
		this.endYn = endYn;
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
