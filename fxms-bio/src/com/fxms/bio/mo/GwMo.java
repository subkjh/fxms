package com.fxms.bio.mo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;
import subkjh.bas.user.User;

/**
 * @since 2017.09.28 16:05
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_GW", comment = "GW테이블")
@FxIndex(name = "FX_MO_GW__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_GW__FK_INLO", type = INDEX_TYPE.FK, columns = {
		"INLO_NO" }, fkTable = "FX_CF_INLO", fkColumn = "INLO_NO")
@FxIndex(name = "FX_MO_GW__FK_MO", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")

public class GwMo extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1489868043550705595L;

	public static final String MO_CLASS = "GW";
	
	public static void set(GwMo mo, String moName, String gwIpaddr, int gwPort, String gwType) {
		mo.setAlarmCfgNo(0);
		mo.setGwIpaddr(gwIpaddr);
		mo.setGwPort(gwPort);
		mo.setGwType(gwType);
		mo.setInloMemo("TEST");
		mo.setMngYn(true);
		mo.setMoAname(moName);
		mo.setMoName(moName);
		mo.setRegUserNo(User.USER_NO_SYSTEM);
	}

	public GwMo() {
		setMoClass(MO_CLASS);
	}
	
	@Override
	public String toString() {
		return super.toString() + "TYPE(" + gwType + ")";
	}

	@FxColumn(name = "GW_TYPE", size = 50, comment = "GW 종류")
	private String gwType;

	@FxColumn(name = "GW_IPADDR", size = 39, nullable = true, comment = "GW IP주소")
	private String gwIpaddr;

	@FxColumn(name = "GW_PORT", size = 5, nullable = true, comment = "GW 포트")
	private int gwPort;
	
	@FxColumn(name = "GW_VER", size = 20, nullable = true, comment = "GW 버전")
	private String gwVer;
	
	@FxColumn(name = "MANAGER_IPADDR", size = 39, nullable = true, comment = "매니저 IP주소")
	private String managerIpaddr;

	@FxColumn(name = "MANAGER_PORT", size = 5, nullable = true, comment = "매니저 포트")
	private int managerPort;
	
	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", defValue = "0")
	private int inloNo = 0;

	@FxColumn(name = "INLO_MEMO", size = 200, nullable = true, comment = "설치위치메모")
	private String inloMemo;

	/**
	 * GW 종류
	 * 
	 * @return GW 종류
	 */
	public String getGwType() {
		return gwType;
	}

	/**
	 * GW 종류
	 * 
	 * @param gwType
	 *            GW 종류
	 */
	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	/**
	 * GW IP주소
	 * 
	 * @return GW IP주소
	 */
	public String getGwIpaddr() {
		return gwIpaddr;
	}

	/**
	 * GW IP주소
	 * 
	 * @param gwIpaddr
	 *            GW IP주소
	 */
	public void setGwIpaddr(String gwIpaddr) {
		this.gwIpaddr = gwIpaddr;
	}

	/**
	 * GW 포트
	 * 
	 * @return GW 포트
	 */
	public int getGwPort() {
		return gwPort;
	}

	/**
	 * GW 포트
	 * 
	 * @param gwPort
	 *            GW 포트
	 */
	public void setGwPort(int gwPort) {
		this.gwPort = gwPort;
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

	public String getGwVer() {
		return gwVer;
	}

	public void setGwVer(String gwVer) {
		this.gwVer = gwVer;
	}

	public String getManagerIpaddr() {
		return managerIpaddr;
	}

	public void setManagerIpaddr(String managerIpaddr) {
		this.managerIpaddr = managerIpaddr;
	}

	public int getManagerPort() {
		return managerPort;
	}

	public void setManagerPort(int managerPort) {
		this.managerPort = managerPort;
	}
	
	
}
