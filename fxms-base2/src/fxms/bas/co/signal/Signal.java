package fxms.bas.co.signal;

import fxms.bas.co.noti.TargetFxEvent;

public class Signal extends TargetFxEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1603298193356548852L;

	public Signal() {

	}

	public Signal(String type, String target) {
		super(type, target);
	}
}
