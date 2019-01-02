package com.fxms.nms.mo.pmo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoPollable;

public class IpsPmo extends Mo implements MoPollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -690854683532264321L;
	
	private List<IpPmo> ipList;

	private int pollingCycle = 0;

	
	public int getPollingCycle() {
		return pollingCycle;
	}


	public void setPollingCycle(int pollingCycle) {
		this.pollingCycle = pollingCycle;
	}


	public List<IpPmo> getIpList() {
		if (ipList == null) {
			ipList = new ArrayList<IpPmo>();
		}
		return ipList;
	}

}
