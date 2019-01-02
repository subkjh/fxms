package com.fxms.nms.mo;

import fxms.bas.mo.Mo;

public class DnsMo extends Mo {
	
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
