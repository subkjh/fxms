package com.fxms.nms.mo.pmo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoPollable;

public class IpPmo extends Mo implements MoPollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7655245193005540919L;
	
	private String ipAddress;

	/** 이전 응답시간 평균 */
	private float rtt;

	/** 1:Online, 0:OffLine */
	private int statusIcmp;
	
	private int pollingCycle = 0;

	
	public int getPollingCycle() {
		return pollingCycle;
	}


	public void setPollingCycle(int pollingCycle) {
		this.pollingCycle = pollingCycle;
	}


	public int getStatusIcmp() {
		return statusIcmp;
	}


	public void setStatusIcmp(int statusIcmp) {
		this.statusIcmp = statusIcmp;
	}


	public String getIpAddress() {
		return ipAddress;
	}
	

	public float getRtt() {
		return rtt;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setRtt(float rtt) {
		this.rtt = rtt;
	}

}
