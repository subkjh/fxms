package subkjh.dao.exp;

public class DBObjectNameInvalidExeption extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623371120608358103L;

	public DBObjectNameInvalidExeption(String name) {
		super(name);
	}

	public DBObjectNameInvalidExeption(String tab, String col) {
		super("COLUMN(" + col + "@" + tab + ") NOT FOUND");
	}
}
