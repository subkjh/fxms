package com.fxms.nms.mo.property;

public interface MoSnmppable {

	/**
	 * 
	 * @return 정상적인 SNMP인지
	 */
	public boolean isSnmp();

	/**
	 * SNMP 정보를 조회합니다.
	 * 
	 * @return SNMP 정보
	 */
	public SnmpPass getSnmpPass();

	public String getIpAddress();

	public long getMoNo();

}
