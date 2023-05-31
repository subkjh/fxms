package subkjh.dao.database;

import java.util.Map;



/**
 * HSQL 데이터베이스
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class HSql extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 37049670097342755L;

	public HSql() {
		setUrl("org.hsqldb.jdbcDriver");
	}

	@Override
	public String makeUrl(Map<String, Object> para) {

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}
		}

		return "jdbc:sqlserver://" + getIpAddress() + ":" + getPort() + ";databaseName=" + getDbName();
	}

	@Override
	public Exception makeException(Exception e, String msg) {
		return e;
	}

}
