package subkjh.bas.dao.model;

/**
 * UPDATE용 SQL
 * 
 * @author subkjh
 * 
 */
public class SqlUpdateBean extends SqlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8113926899933940831L;

	/**
	 * 
	 * @param qid
	 *            QID
	 * @param sql
	 *            사용 쿼리
	 * @param database
	 *            데이터베이스 종류
	 */
	public SqlUpdateBean(String qid, String database) {
		super(qid, database);
	}

	/**
	 * 
	 * @param qid
	 *            QID
	 * @param sql
	 *            사용 쿼리
	 */
	public SqlUpdateBean(String qid) {
		super(qid, null);
	}

	@Override
	protected String getXmlTag() {
		return "update";
	}
}
