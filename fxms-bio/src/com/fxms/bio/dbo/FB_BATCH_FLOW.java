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

@FxTable(name = "FB_BATCH_FLOW", comment = "배치흐름테이블")
@FxIndex(name = "FB_BATCH_FLOW__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID", "N_DAYS" })
@FxIndex(name = "FB_BATCH_FLOW__FK_NO", type = INDEX_TYPE.FK, columns = {
		"BATCH_ID" }, fkTable = "FB_BATCH", fkColumn = "BATCH_ID")
public class FB_BATCH_FLOW implements FX_COMMON {

	public FB_BATCH_FLOW() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "N_DAYS", size = 5, comment = "차수")
	private int nDays;

	@FxColumn(name = "PBR_MO_NO", size = 19, comment = "PBR 관리번호")
	private long pbrMoNo;

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "래시피관리번호")
	private int alarmCfgNo;

	@FxColumn(name = "DAYS_MEMO", size = 200, nullable = true, comment = "차수메모")
	private String daysMemo;

	@FxColumn(name = "DAYS_START_DATE", size = 14, nullable = true, comment = "차수시작일시")
	private long daysStartDate;

	@FxColumn(name = "DAYS_END_DATE", size = 14, nullable = true, comment = "차수종료일시")
	private long daysEndDate;

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
	 * 래시피관리번호
	 * 
	 * @return 래시피관리번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 래시피관리번호
	 * 
	 * @param alarmCfgNo
	 *            래시피관리번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 차수메모
	 * 
	 * @return 차수메모
	 */
	public String getDaysMemo() {
		return daysMemo;
	}

	/**
	 * 차수메모
	 * 
	 * @param daysMemo
	 *            차수메모
	 */
	public void setDaysMemo(String daysMemo) {
		this.daysMemo = daysMemo;
	}

	/**
	 * 차수시작일시
	 * 
	 * @return 차수시작일시
	 */
	public long getDaysStartDate() {
		return daysStartDate;
	}

	/**
	 * 차수시작일시
	 * 
	 * @param daysStartDate
	 *            차수시작일시
	 */
	public void setDaysStartDate(long daysStartDate) {
		this.daysStartDate = daysStartDate;
	}

	/**
	 * 차수종료일시
	 * 
	 * @return 차수종료일시
	 */
	public long getDaysEndDate() {
		return daysEndDate;
	}

	/**
	 * 차수종료일시
	 * 
	 * @param daysEndDate
	 *            차수종료일시
	 */
	public void setDaysEndDate(long daysEndDate) {
		this.daysEndDate = daysEndDate;
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
