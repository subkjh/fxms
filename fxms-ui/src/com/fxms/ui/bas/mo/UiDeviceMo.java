package com.fxms.ui.bas.mo;

public class UiDeviceMo extends Mo {

	public static final String MO_CLASS = "DEVICE";

	private String deviceType;

	private String deviceIpaddr;

	private long gwMoNo = 0;

	private String deviceIdInGw;

	private double lastCtrlVal;

	private long lastCtrlDate;

	/**
	 * 장치유형
	 * 
	 * @return 장치유형
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * 장치유형
	 * 
	 * @param deviceType
	 *            장치유형
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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
	 * GW관리번호
	 * 
	 * @return GW관리번호
	 */
	public long getGwMoNo() {
		return gwMoNo;
	}

	/**
	 * GW관리번호
	 * 
	 * @param gwMoNo
	 *            GW관리번호
	 */
	public void setGwMoNo(long gwMoNo) {
		this.gwMoNo = gwMoNo;
	}

	/**
	 * GW용 장치ID
	 * 
	 * @return GW용 장치ID
	 */
	public String getDeviceIdInGw() {
		return deviceIdInGw;
	}

	/**
	 * GW용 장치ID
	 * 
	 * @param deviceIdInGw
	 *            GW용 장치ID
	 */
	public void setDeviceIdInGw(String deviceIdInGw) {
		this.deviceIdInGw = deviceIdInGw;
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
