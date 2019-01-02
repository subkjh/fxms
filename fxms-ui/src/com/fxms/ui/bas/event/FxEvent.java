package com.fxms.ui.bas.event;

import fxms.client.FxmsClient;

public class FxEvent {

	private final long date;

	private String eventType;

	private String status;

	private String target;

	public FxEvent() {
		date = FxmsClient.getDate(System.currentTimeMillis());
	}

	public long getDate() {
		return date;
	}

	public String getEventType() {
		return eventType;
	}

	public String getStatus() {
		return status;
	}

	public String getTarget() {
		return target;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
