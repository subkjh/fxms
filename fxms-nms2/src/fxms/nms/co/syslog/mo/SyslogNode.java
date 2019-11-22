package fxms.nms.co.syslog.mo;

import fxms.nms.mo.NmsNode;

public interface SyslogNode extends NmsNode {

	/**
	 * 
	 * @return 트랩수신여부
	 */
	public boolean isSyslogRecv();

}
