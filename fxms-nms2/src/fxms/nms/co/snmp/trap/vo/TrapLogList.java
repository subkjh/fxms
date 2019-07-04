package fxms.nms.co.snmp.trap.vo;

import java.util.ArrayList;

import fxms.bas.co.noti.FxEvent;

public class TrapLogList extends ArrayList<TrapLog> implements FxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8794673120103315359L;

	private String eventType;

	private STATUS status = STATUS.raw;

	public TrapLogList() {
		eventType = "trap-log-list";
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
