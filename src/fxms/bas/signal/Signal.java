package fxms.bas.signal;

import fxms.bas.event.TargetFxEvent;

public class Signal extends TargetFxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1603298193356548852L;

	private String sender;

	public Signal() {

	}

	public Signal(String type, String target) {
		super(type, target);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(getNo()).append(")");
		sb.append(getClass().getSimpleName());
		sb.append("(");
		sb.append(getTarget());
		sb.append(") ");
		sb.append(getStatus());

		return sb.toString();
	}

}
