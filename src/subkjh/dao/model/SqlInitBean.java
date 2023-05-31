package subkjh.dao.model;

/**
 * 초기 시작하는 SqlBean으로 인수는 존재하지 않는다.<br>
 * 
 * @author subkjh
 * 
 */
public class SqlInitBean extends SqlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6522226620888120635L;

	public SqlInitBean(String id, String database) {
		super(id, database);
	}

	@Override
	protected String getXmlTag() {
		return "init";
	}
}
