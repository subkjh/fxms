package com.fxms.bio.mo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoNeedManager;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.user.User;

/**
 * @since 2017.06.13 17:17
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_DEVICE", comment = "장치테이블")
@FxIndex(name = "FX_MO_DEVICE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_DEVICE__FK_MO", type = INDEX_TYPE.FK, columns = {
		"MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")

public class DeviceMo extends Mo implements MoNeedManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -713828256248824714L;

	public static final String MO_CLASS = "DEVICE";

	public DeviceMo() {
		setMoClass(MO_CLASS);
	}

	public static void set(DeviceMo mo, long upperMoNo, long gwMoNo, String deviceIdInGw, String deviceType,
			String deviceIpaddr) {
		mo.setUpperMoNo(upperMoNo);
		mo.setAlarmCfgNo(0);
		mo.setDeviceIdInGw(deviceIdInGw);
		mo.setDeviceIpaddr(deviceIpaddr);
		mo.setDeviceType(deviceType);
		mo.setMoType(deviceType);
		mo.setGwMoNo(gwMoNo);
		mo.setMngYn(true);
		mo.setMoAname(deviceIdInGw);
		mo.setMoName(deviceIdInGw);
		mo.setRegUserNo(User.USER_NO_SYSTEM);
	}

	@FxColumn(name = "DEVICE_TYPE", size = 20, comment = "장치유형")
	private String deviceType;

	@FxColumn(name = "DEVICE_IPADDR", size = 39, nullable = true, comment = "IP주소")
	private String deviceIpaddr;

	@FxColumn(name = "GW_MO_NO", size = 19, comment = "GW관리번호", defValue = "0")
	private long gwMoNo = 0;

	@FxColumn(name = "DEVICE_ID_IN_GW", size = 100, nullable = true, comment = "GW용 장치ID")
	private String deviceIdInGw;

	@FxColumn(name = "LAST_CTRL_VAL", size = 19, nullable = true, comment = "최종제어값")
	private double lastCtrlVal;

	@FxColumn(name = "LAST_CTRL_DATE", size = 14, nullable = true, comment = "최종제어값적용일시")
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
		setMoType(deviceType);
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

	@Override
	public long getManagerMoNo() {
		return getGwMoNo();
	}
}
