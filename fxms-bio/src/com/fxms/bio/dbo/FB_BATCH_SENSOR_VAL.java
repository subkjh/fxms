package com.fxms.bio.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.07.19 11:19
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FB_BATCH_SENSOR_VAL", comment = "배치센서값테이블")
@FxIndex(name = "FB_BATCH_SENSOR_VAL__PK", type = INDEX_TYPE.PK, columns = { "BATCH_ID", "N_DAYS", "SENSOR_MO_NO" })
public class FB_BATCH_SENSOR_VAL {

	public FB_BATCH_SENSOR_VAL() {
	}

	@FxColumn(name = "BATCH_ID", size = 10, comment = "배치ID")
	private String batchId;

	@FxColumn(name = "N_DAYS", size = 5, comment = "차수")
	private int nDays;

	@FxColumn(name = "SENSOR_MO_NO", size = 19, comment = "센서MO번호")
	private long sensorMoNo;

	@FxColumn(name = "PS_CODE", size = 20, comment = "상태값번호")
	private String psCode;

	@FxColumn(name = "PS_DATE", size = 14, comment = "상태값일시")
	private long psDate;

	@FxColumn(name = "PS_VAL", size = 19, comment = "상태값", defValue = "0")
	private double psVal = 0;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
	private long regDate;

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
	 * 센서MO번호
	 * 
	 * @return 센서MO번호
	 */
	public long getSensorMoNo() {
		return sensorMoNo;
	}

	/**
	 * 센서MO번호
	 * 
	 * @param sensorMoNo
	 *            센서MO번호
	 */
	public void setSensorMoNo(long sensorMoNo) {
		this.sensorMoNo = sensorMoNo;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCode
	 *            상태값번호
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	/**
	 * 상태값일시
	 * 
	 * @return 상태값일시
	 */
	public long getPsDate() {
		return psDate;
	}

	/**
	 * 상태값일시
	 * 
	 * @param psDate
	 *            상태값일시
	 */
	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	/**
	 * 상태값
	 * 
	 * @return 상태값
	 */
	public double getPsVal() {
		return psVal;
	}

	/**
	 * 상태값
	 * 
	 * @param psVal
	 *            상태값
	 */
	public void setPsVal(double psVal) {
		this.psVal = psVal;
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
}
