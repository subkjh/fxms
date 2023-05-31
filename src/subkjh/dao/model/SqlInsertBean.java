package subkjh.dao.model;

/**
 * INSERT용 SQL<br>
 * 
 * @author subkjh
 * 
 */
public class SqlInsertBean extends SqlBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2920228866103357583L;

	public SqlInsertBean(String qid, String database) {
		super(qid, database);

	}

	public SqlInsertBean(String id) {
		super(id, null);
	}

	@Override
	protected String getXmlTag() {
		return "insert";
	}
}
