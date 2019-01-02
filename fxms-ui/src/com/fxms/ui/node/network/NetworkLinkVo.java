package com.fxms.ui.node.network;

import javafx.scene.Node;

public class NetworkLinkVo {

	private long westNeMoNo;
	private long eastNeMoNo;
	private long westIfMoNo;
	private long eastIfMoNo;

	public NetworkLinkVo(Node westNode, Node eastNode) {
		westNeMoNo = Long.valueOf(westNode.getId());
		eastNeMoNo = Long.valueOf(eastNode.getId());
	}

	public NetworkLinkVo(long westNeMoNo, long eastNeMoNo) {
		this.westNeMoNo = westNeMoNo;
		this.eastNeMoNo = eastNeMoNo;
	}

	public long getEastIfMoNo() {
		return eastIfMoNo;
	}

	public long getEastNeMoNo() {
		return eastNeMoNo;
	}

	public long getWestIfMoNo() {
		return westIfMoNo;
	}

	public long getWestNeMoNo() {
		return westNeMoNo;
	}

	public void setEastIfMoNo(long eastIfMoNo) {
		this.eastIfMoNo = eastIfMoNo;
	}

	public void setWestIfMoNo(long westIfMoNo) {
		this.westIfMoNo = westIfMoNo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(westNeMoNo);

		if (westIfMoNo > 0) {
			sb.append(".");
			sb.append(westIfMoNo);
		}

		sb.append(" >> ");

		sb.append(eastNeMoNo);

		if (eastIfMoNo > 0) {
			sb.append(".");
			sb.append(eastIfMoNo);
		}

		return sb.toString();
	}

}
