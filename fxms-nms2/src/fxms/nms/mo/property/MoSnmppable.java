package fxms.nms.mo.property;

import fxms.bas.mo.property.Moable;

public interface MoSnmppable extends Moable {

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

	public void setSnmpPass(SnmpPass pass);

	public void setIpAddress(String ipAddress);

}
