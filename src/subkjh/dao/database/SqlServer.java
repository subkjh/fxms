package subkjh.dao.database;

import java.util.Map;

/**
 * Microsoft SQL Service
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class SqlServer extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1436846712484978770L;

	public SqlServer() {
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
