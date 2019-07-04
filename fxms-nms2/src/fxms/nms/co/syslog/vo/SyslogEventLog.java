package fxms.nms.co.syslog.vo;

import fxms.nms.co.cd.NmsCode;
import fxms.nms.co.vo.EventLog;

/**
 * SYSLOG 이벤트
 * 
 * @author subkjh
 * 
 */
public class SyslogEventLog extends EventLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5763180989200310460L;

	/** syslog priority */
	private int syslogPriority = -1;

	private int syslogFacility = -1;

	public SyslogEventLog() {
		setLogGroup(NmsCode.AlarmGroup.SYSLOG);
	}

	public int getSyslogFacility() {
		return syslogFacility;
	}

	public int getSyslogPriority() {
		return syslogPriority;
	}

	public void setSyslogFacility(int syslogFacility) {
		this.syslogFacility = syslogFacility;
	}

	public void setSyslogPriority(int syslogPriority) {
		this.syslogPriority = syslogPriority;
	}

}
