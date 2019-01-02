package fxms.bas.mo.exception;

public class MoNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1016320254283658403L;

	public MoNotFoundException() {

	}

	public MoNotFoundException(long moNo) {
		super("mo-no=" + moNo);
	}

	public MoNotFoundException(String msg) {
		super(msg);
	}
}
