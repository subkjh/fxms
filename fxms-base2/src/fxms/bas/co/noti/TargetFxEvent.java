package fxms.bas.co.noti;

public class TargetFxEvent extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961522301976301793L;
	private String target;

	public TargetFxEvent() {

	}

	public TargetFxEvent(String type, String target) {
		setEventType(type);
		setStatus(STATUS.added);
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
