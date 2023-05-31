package subkjh.dao.exp;

public class TableNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3515807569268598512L;

	public TableNotFoundException(String name) {
		super("TABLE(" + name + ") NOT FOUND");
	}
}
