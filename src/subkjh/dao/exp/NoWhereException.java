package subkjh.dao.exp;

/**
 * DELETE, UPDATE문에 WHERE 조건이 없으면 발생한다.
 * 
 * @author subkjh
 *
 */
public class NoWhereException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5526589716854260940L;

	public NoWhereException(String tableName, String msg) {
		super("No Where in " + tableName + " : " + msg);
	}

}
