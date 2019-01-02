package com.fxms.ui;

public enum VAR {
	
	PS_SERVER("ps-server")
	
	, PS_SERVER_PORT("ps-server-port")
	
	, FX_BROADCASTER("fx-broadcaster")
	
	, FX_BROADCASTER_PORT("fx-broadcaster-port")
	
	, FX_ALARM_BROADCASTER("fx-alarm-broadcaster")
	
	, FX_ALARM_BROADCASTER_PORT("fx-alarm-broadcaster-port");
	
	
	private String varName;
	
	private VAR(String varName)
	{
		this.varName = varName;
	}

	public String getVarName() {
		return varName;
	}
	
	

}
