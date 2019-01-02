package fxms.bas.exception;

public class ExistChildException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3043366057517500633L;

	public ExistChildException(String childType, Object msg) {
		super("exist children, " + childType + ":" + msg);
	}

}
