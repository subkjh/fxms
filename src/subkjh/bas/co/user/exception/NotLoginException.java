package subkjh.bas.co.user.exception;

public class NotLoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7734466389483411852L;

	public NotLoginException(String sessionId) {
		super("SESSION-ID(" + sessionId + ")");
	}

}