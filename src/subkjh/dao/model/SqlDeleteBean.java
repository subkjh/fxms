package subkjh.dao.model;

/**
 * 삭제용 SQL
 * 
 * @author subkjh
 * 
 */
public class SqlDeleteBean extends SqlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6001130483262766789L;

	public SqlDeleteBean(String qid, String database) {
		super(qid, database);
	}

	public SqlDeleteBean(String qid) {
		super(qid, null);
	}

	@Override
	protected String getXmlTag() {
		return "delete";
	}

}
