package fxms.nms.co.snmp.trap;

public interface TrapNode {

	/**
	 * 
	 * @return 트랩수신여부
	 */
	public boolean isTrapRecv();

	/**
	 * 
	 * @return
	 */
	public String getIpAddress();

}
