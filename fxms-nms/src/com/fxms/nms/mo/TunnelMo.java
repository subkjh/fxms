package com.fxms.nms.mo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoNeedManager;

public class TunnelMo extends Mo implements MoNeedManager {

	/**
	 * MO분류. TUNNEL
	 */
	public static final String MO_CLASS = "TUNNEL";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7297177128627919200L;

	/** ACTIVE_TIME */
	private long activeTime;

	/** 로컬 IP */
	private String localIp;

	/** 리모트 IP */
	private String remoteIp;

	/** 터널상태 */
	private int statusTunnel;

	/** 터널 INDEX */
	private long tnIndex;

	public TunnelMo() {
	}

	public long getActiveTime() {
		return activeTime;
	}

	public String getLocalIp() {
		return localIp;
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public int getStatusTunnel() {
		return statusTunnel;
	}

	public long getTnIndex() {
		return tnIndex;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public void setStatusTunnel(int statusTunnel) {
		this.statusTunnel = statusTunnel;
	}

	public void setTnIndex(long tnIndex) {
		this.tnIndex = tnIndex;
	}

	@Override
	public String toString() {
		return super.toString() + "|" + tnIndex;
	}

	@Override
	public long getManagerMoNo() {
		return getUpperMoNo();
	}
}