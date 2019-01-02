package fxms.bas.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_DEVICE", comment = "장치테이블")
@FxIndex(name = "FX_MO_DEVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_DEVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FX_MO_DEVICE implements Serializable {

	public FX_MO_DEVICE() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "장치관리번호")
	private long moNo;

	@FxColumn(name = "DEVICE_ID", size = 50, comment = "장치ID")
	private String deviceId;

	@FxColumn(name = "DEVICE_NAME", size = 200, comment = "장치명")
	private String deviceName;

	@FxColumn(name = "DEVICE_IPADDR", size = 39, nullable = true, comment = "IP주소")
	private String deviceIpaddr;

	@FxColumn(name = "MODEL_NO", size = 9, nullable = true, comment = "모델번호", defValue = "0")
	private int modelNo = 0;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "INLO_MEMO", size = 200, nullable = true, comment = "설치위치메모")
	private String inloMemo;

	@FxColumn(name = "UGRP_NO", size = 9, nullable = true, comment = "운용자그룹번호", defValue = "0")
	private int ugrpNo = 0;

	@FxColumn(name = "MS_IPADDR", size = 39, nullable = true, comment = "관리서버주소")
	private String msIpaddr;

	@FxColumn(name = "LAST_CTRL_VAL", size = 19, nullable = true, comment = "최종제어값", defValue = "0")
	private double lastCtrlVal = 0;

	@FxColumn(name = "LAST_CTRL_DATE", size = 14, nullable = true, comment = "최종제어값적용일시", defValue = "0")
	private long lastCtrlDate = 0;

	/**
	 * 장치관리번호
	 * 
	 * @return 장치관리번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * 장치관리번호
	 * 
	 * @param moNo
	 *            장치관리번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 장치ID
	 * 
	 * @return 장치ID
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * 장치ID
	 * 
	 * @param deviceId
	 *            장치ID
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 장치명
	 * 
	 * @return 장치명
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * 장치명
	 * 
	 * @param deviceName
	 *            장치명
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * IP주소
	 * 
	 * @return IP주소
	 */
	public String getDeviceIpaddr() {
		return deviceIpaddr;
	}

	/**
	 * IP주소
	 * 
	 * @param deviceIpaddr
	 *            IP주소
	 */
	public void setDeviceIpaddr(String deviceIpaddr) {
		this.deviceIpaddr = deviceIpaddr;
	}

	/**
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo
	 *            모델번호
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo
	 *            설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 설치위치메모
	 * 
	 * @return 설치위치메모
	 */
	public String getInloMemo() {
		return inloMemo;
	}

	/**
	 * 설치위치메모
	 * 
	 * @param inloMemo
	 *            설치위치메모
	 */
	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 관리서버주소
	 * 
	 * @return 관리서버주소
	 */
	public String getMsIpaddr() {
		return msIpaddr;
	}

	/**
	 * 관리서버주소
	 * 
	 * @param msIpaddr
	 *            관리서버주소
	 */
	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	/**
	 * 최종제어값
	 * 
	 * @return 최종제어값
	 */
	public double getLastCtrlVal() {
		return lastCtrlVal;
	}

	/**
	 * 최종제어값
	 * 
	 * @param lastCtrlVal
	 *            최종제어값
	 */
	public void setLastCtrlVal(double lastCtrlVal) {
		this.lastCtrlVal = lastCtrlVal;
	}

	/**
	 * 최종제어값적용일시
	 * 
	 * @return 최종제어값적용일시
	 */
	public long getLastCtrlDate() {
		return lastCtrlDate;
	}

	/**
	 * 최종제어값적용일시
	 * 
	 * @param lastCtrlDate
	 *            최종제어값적용일시
	 */
	public void setLastCtrlDate(long lastCtrlDate) {
		this.lastCtrlDate = lastCtrlDate;
	}
}
