package com.fxms.nms.mo;

import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoNeedManager;

public class VpnMo extends Mo implements MoNeedManager {

	/** MO분류. VLAN */
	public static final String MO_CLASS = "VPN";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8076916747704090733L;

	private String ipAddress;

	private int statusVpn;

	private int statusVrfPing;

	private String vpnId;

	private String vpnName;

	private String vpnProtocol;

	private String vpnType;

	private String vrfName;

	public VpnMo() {
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getStatusVpn() {
		return statusVpn;
	}

	public int getStatusVrfPing() {
		return statusVrfPing;
	}

	public String getVpnId() {
		return vpnId;
	}

	public String getVpnName() {
		return vpnName;
	}

	public String getVpnProtocol() {
		return vpnProtocol;
	}

	public String getVpnType() {
		return vpnType;
	}

	public String getVrfName() {
		return vrfName;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setStatusVpn(int statusVpn) {
		this.statusVpn = statusVpn;
	}

	public void setStatusVrfPing(int statusVrfPing) {
		this.statusVrfPing = statusVrfPing;
	}

	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public void setVpnProtocol(String vpnProtocol) {
		this.vpnProtocol = vpnProtocol;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public void setVrfName(String vrfName) {
		this.vrfName = vrfName;
	}

	@Override
	public long getManagerMoNo() {
		return getUpperMoNo();
	}

}