package subkjh.dao.exp;

public class ZeroColumnException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866580043017368243L;

	public ZeroColumnException(String name) {
		super("TABLE=" + name);
	}
}
