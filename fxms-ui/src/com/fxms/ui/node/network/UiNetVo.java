package com.fxms.ui.node.network;

import java.util.ArrayList;
import java.util.List;

public class UiNetVo {

	private int netNo;

	private String topologyType;

	private String netName;

	private List<UiNetItemVo> itemList;

	public List<UiNetItemVo> getItemList() {
		
		if (itemList == null) {
			itemList = new ArrayList<UiNetItemVo>();
		}
		return itemList;
	}

	public String getNetName() {
		return netName;
	}

	public int getNetNo() {
		return netNo;
	}

	public String getTopologyType() {
		return topologyType;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	public void setNetNo(int netNo) {
		this.netNo = netNo;
	}

	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}

}
