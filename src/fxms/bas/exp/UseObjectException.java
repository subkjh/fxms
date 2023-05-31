package fxms.bas.exp;

/**
 * 객체를 사용중임을 통보한다.
 * 
 * @author subkjh
 *
 */
public class UseObjectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2451156299725013782L;

	private long count;

	public UseObjectException(String target, long count) {
		super(target + ":" + count);
	}

	public long getCount() {
		return count;
	}

}
