package fxms.bas.exp;

/**
 * 조건이 맞니 않는 경우 발생
 * 
 * @author subkjh
 *
 */
public class FxNotMatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3277803854921967012L;

	public FxNotMatchException(String msg) {
		super(msg);
	}

}
