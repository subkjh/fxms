package fxms.bas.exp;

/**
 * SNMP Counter 값이 reset됨.
 * 
 * @author subkjh
 * 
 */
public class ResetCounterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 803045476884308142L;

	private String target;

	public ResetCounterException(String target) {
		super("TARGET(" + target + ")");
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
