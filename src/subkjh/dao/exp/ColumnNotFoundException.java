package subkjh.dao.exp;

public class ColumnNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1420454103204073171L;

	public ColumnNotFoundException(String name) {
		super("COLUMN(" + name + ") NOT FOUND");
	}

	public ColumnNotFoundException(String tab, String col) {
		super("COLUMN(" + col + "@" + tab + ") NOT FOUND");
	}
}