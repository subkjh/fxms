package fxms.nms.co.syslog.vo;

import java.util.ArrayList;

import fxms.bas.co.noti.FxEvent;

public class SyslogEventLogList extends ArrayList<SyslogEventLog> implements FxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5863485672129821444L;

	private String eventType;

	private STATUS status = STATUS.raw;

	public SyslogEventLogList() {
		eventType = "syslog-log-list";
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}
}