package fxms.bas.exp;

public class FxException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2898251064336335920L;

	private final String exceptionName;

	public FxException(String exceptionName, String message) {
		super(message);

		this.exceptionName = exceptionName;
	}

	public String getExceptionName() {
		return exceptionName;
	}

}
