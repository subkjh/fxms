package com.daims.dfc.filter.config.std.beans;

import java.io.Serializable;

/**
 * 
 * @author subkjh
 * @since 3.0
 */
public class DfcBridge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971658463563435696L;

	private long moNoNode;
	private transient long moNoIf;
	private String ipAddress;
	private int ifIndex;

	private transient int ifIndexPeer;
	private String macAddressPeer;
	private transient String ipAddressPeer;
	private transient long moNoIfPeer;
	private transient long moNoNodePeer;

	private long hstimeSync;
	private transient boolean ignore;

	public long getHstimeSync() {
		return hstimeSync;
	}

	public int getIfIndex() {
		return ifIndex;
	}

	public int getIfIndexPeer() {
		return ifIndexPeer;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getIpAddressPeer() {
		return ipAddressPeer;
	}

	public String getMacAddressPeer() {
		return macAddressPeer;
	}

	public long getMoNoIf() {
		return moNoIf;
	}

	public long getMoNoIfPeer() {
		return moNoIfPeer;
	}

	public long getMoNoNode() {
		return moNoNode;
	}

	public long getMoNoNodePeer() {
		return moNoNodePeer;
	}

	public String getStartId() {
		return ipAddress + "." + ifIndex;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setHstimeSync(long hstimeSync) {
		this.hstimeSync = hstimeSync;
	}

	public void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}

	public void setIfIndexPeer(int ifIndexPeer) {
		this.ifIndexPeer = ifIndexPeer;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setIpAddressPeer(String ipAddressPeer) {
		this.ipAddressPeer = ipAddressPeer;
	}

	public void setMacAddressPeer(String macAddressPeer) {
		this.macAddressPeer = macAddressPeer;
	}

	public void setMoNoIf(long moNoIf) {
		this.moNoIf = moNoIf;
	}

	public void setMoNoIfPeer(long moNoIfPeer) {
		this.moNoIfPeer = moNoIfPeer;
	}

	public void setMoNoNode(long moNoNode) {
		this.moNoNode = moNoNode;
	}

	public void setMoNoNodePeer(long moNoNodePeer) {
		this.moNoNodePeer = moNoNodePeer;
	}

	@Override
	public String toString() {
		return "DfcBridge(" + ipAddress + "|" + ifIndex + "=" + macAddressPeer + "|" + ipAddressPeer + "|" + ifIndexPeer + ")";
	}

}
