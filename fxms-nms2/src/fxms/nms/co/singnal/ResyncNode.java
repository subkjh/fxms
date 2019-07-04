package fxms.nms.co.singnal;

import fxms.bas.co.signal.Signal;

public class ResyncNode extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 554987503904923295L;

	private long moNo;
	private String ipAddress;
	private String reason;

	public ResyncNode(long moNo, String ipAddress, String reason) {
		this.moNo = moNo;
		this.ipAddress = ipAddress;
		this.reason = reason;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getReason() {
		return reason;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
