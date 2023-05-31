package subkjh.bas.co.user.exception;

/**
 * 기능 거부 예외
 * 
 * @author subkjh
 *
 */
public class OpDenyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2635595237069031425L;

	public OpDenyException(String userName, int ugrpNo, String opId) {
		super("USER(" + userName + ")UGRP-NO(" + ugrpNo + ")OP-ID(" + opId + ")");
	}
}
