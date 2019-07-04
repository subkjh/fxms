package subkjh.bas.co.user.exception;

public class OpDenyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2635595237069031425L;

	public OpDenyException(String userName, int opNo) {
		super("USER(" + userName + ")OP-NO(" + opNo + ")");
	}
}
