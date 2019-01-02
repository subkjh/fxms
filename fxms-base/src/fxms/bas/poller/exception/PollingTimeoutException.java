package fxms.bas.poller.exception;

/**
 * 타임아웃
 * 
 * @author SUBKJH-DEV
 *
 */
public class PollingTimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 395031922535096834L;
	
	private long moNo;

	public PollingTimeoutException(long moNo, String msg) {
		super(msg);
		this.moNo = moNo;
	}

	public long getMoNo() {
		return moNo;
	}
	
	

}
