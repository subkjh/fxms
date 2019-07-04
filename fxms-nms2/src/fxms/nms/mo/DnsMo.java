package fxms.nms.mo;

import fxms.bas.impl.mo.FxMo;

public class DnsMo extends FxMo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7811050639939173835L;

	private String server;
	
	private String domain;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
