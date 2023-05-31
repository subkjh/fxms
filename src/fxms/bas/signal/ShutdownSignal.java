package fxms.bas.signal;

/**
 * Shutdown me
 * 
 * @author subkjh
 *
 */
public class ShutdownSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7447299028633364805L;

	private String reason;

	public ShutdownSignal(String reason) {
		this.reason = reason;
	}

	public String toString() {
		return "SIGNAL-SHUTDOWN '" + reason + "'";
	}
}
