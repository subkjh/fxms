package fxms.bas.event;

public class TargetFxEvent extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961522301976301793L;
	private String target;
	private String eventType;

	public TargetFxEvent() {

	}
	
	public TargetFxEvent(String type, String target) {
		setEventType(type);
		setStatus(STATUS.added);
		this.target = target;
	}

	public String getEventType() {
		return eventType;
	}

	public String getTarget() {
		return target;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {

		return super.toString() + target;
	}

}
