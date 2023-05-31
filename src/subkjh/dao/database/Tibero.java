package subkjh.dao.database;

import java.util.Map;

/**
 * 티베로
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Tibero extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6655234057880224711L;

	public Tibero ( ) {
	}

	@Override
	public String makeUrl ( Map<String, Object> para ) {

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}
		}

		return "jdbc:tibero:thin:@" + getIpAddress() + ":" + getPort() + ":" + getDbName();
	}

	@Override
	public Exception makeException(Exception e, String msg) {
		return e;
	}

}
