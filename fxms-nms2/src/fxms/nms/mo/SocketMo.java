package fxms.nms.mo;

import fxms.bas.impl.mo.FxMo;

public class SocketMo extends FxMo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3175267848577070784L;

	private String ipAddress;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private int port;

}
