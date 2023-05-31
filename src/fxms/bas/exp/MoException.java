package fxms.bas.exp;

public class MoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546719234530111076L;

	private final long moNo;

	public MoException(long moNo, String msg) {
		super(msg + "(" + moNo + ")");

		this.moNo = moNo;
	}

	public long getMoNo() {
		return moNo;
	}

}
