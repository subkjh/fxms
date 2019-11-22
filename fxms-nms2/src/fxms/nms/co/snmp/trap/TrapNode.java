package fxms.nms.co.snmp.trap;

import fxms.nms.mo.NmsNode;

public interface TrapNode extends NmsNode {

	/**
	 * 
	 * @return 트랩수신여부
	 */
	public boolean isTrapRecv();


}
