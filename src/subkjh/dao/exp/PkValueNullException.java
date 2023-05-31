package subkjh.dao.exp;

/**
 * UPDATE, DELETE WHERE 조건절에서 PK 값이 NULL인 경우 발생됨
 * 
 * @author subkjh
 *
 */
public class PkValueNullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2016812230557229688L;

	private String table;
	private String column;

	public PkValueNullException(String table, String column) {
		super("PK Column Value Is Null in " + table + "." + column);

		this.table = table;
		this.column = column;
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

}
