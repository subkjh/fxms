package fxms.bas.co.noti;

import java.io.Serializable;

import fxms.bas.fxo.FxObject;

public class FxEventImpl implements FxObject, Serializable, FxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377602869223261093L;

	private String eventType;

	private STATUS status = STATUS.raw;

	public FxEventImpl() {
		eventType = getClass().getSimpleName();
	}

	public String getEventType() {
		return eventType;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getSimpleName());
		sb.append("(");
		sb.append(status);
		sb.append(" ");
		if (getEventType() != null) {
			sb.append(getEventType());
		} else {
			sb.append(getClass().getSimpleName());
		}
		sb.append(")");

		return sb.toString();
	}
}
