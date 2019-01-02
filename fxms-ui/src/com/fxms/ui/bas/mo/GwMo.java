package com.fxms.ui.bas.mo;

public class GwMo extends Mo {

	public static final String MO_CLASS = "GW";


	private String gwType;

	private String gwIpaddr;

	private int gwPort;
	
	private String gwVer;
	
	private String managerIpaddr;

	private int managerPort;
	
	private int inloNo = 0;

	private String inloMemo;

	public String getGwType() {
		return gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getGwIpaddr() {
		return gwIpaddr;
	}

	public void setGwIpaddr(String gwIpaddr) {
		this.gwIpaddr = gwIpaddr;
	}

	public int getGwPort() {
		return gwPort;
	}

	public void setGwPort(int gwPort) {
		this.gwPort = gwPort;
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

	public int getInloNo() {
		return inloNo;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public String getInloMemo() {
		return inloMemo;
	}

	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}
	
	
}
