package fxms.nms.co.syslog.mo;

public interface SyslogNode {

	/**
	 * 
	 * @return 트랩수신여부
	 */
	public boolean isSyslogRecv();

	/**
	 * 
	 * @return
	 */
	public String getIpAddress();
}
