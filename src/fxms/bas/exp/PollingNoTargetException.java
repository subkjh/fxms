package fxms.bas.exp;

/**
 * 타켓이 아님
 * 
 * @author SUBKJH-DEV
 *
 */
public class PollingNoTargetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4894724319996384639L;

	private long moNo;

	public PollingNoTargetException(long moNo, String msg) {
		super("MO_NO=" + moNo + " " + msg);
		this.moNo = moNo;
	}

	public long getMoNo() {
		return moNo;
	}

}
