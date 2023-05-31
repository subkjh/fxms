package fxms.bas.exp;

public class AlarmNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6450081261605468517L;

	public AlarmNotFoundException(long alarmNo) {
		super("alarm", alarmNo, null);
	}
}
