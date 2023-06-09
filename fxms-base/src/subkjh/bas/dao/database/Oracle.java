package subkjh.bas.dao.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.exception.ColumnNotFoundException;
import subkjh.bas.dao.exception.DBObjectDupException;
import subkjh.bas.dao.exception.TableNotFoundException;

/**
 * 오라클 데이터베이스
 * 
 * @author subkjh
 * @since 2007-01-01
 * 
 */
@SuppressWarnings("unused")
public class Oracle extends DataBase {

	public static final int PORT = 1521;

	/**
	 * no data found
	 */
	private final int SQL_NOTFOUND = 1403;

	/**
	 * unique constraint
	 */
	private final int SQL_DUPLICATE = 1;

	/**
	 * not logged on
	 */
	private final int SQL_NOTCONNECT = 1012;

	/**
	 * table or view does not exist.
	 */
	private final int SQL_TABLENOTFOUND = 942;

	/**
	 * name is already used by an existing object. table already exist.
	 */
	private final int SQL_TABLEALREADYEXIST = 955;

	/**
	 * resource busy and acquire with NOWAIT specified
	 */
	private final int SQL_RESOURCE_BUSY = 54;

	/**
	 * integrity constraint (%s.%s) violated - parent key not found
	 */
	private final int SQL_PARENT_NOT_FOUND = 2291;

	/**
	 * integrity constraint (%s.%s) violated - child record found
	 */
	private final int SQL_CHILD_FOUND = 2292;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6955990073628408762L;

	public Oracle() {
		setDriver("oracle.jdbc.driver.OracleDriver");
		setPort(PORT);

		addConst(CONST_NVL, "nvl");
		addConst(CONST_TRUNC, "trunc");

	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
		List<String> ret = splitUrl(url);
		if (ret != null && ret.size() > 3) {
			setIpAddress(ret.get(3));
		}

		if (ret != null && ret.size() > 4) {
			try {
				int port = Integer.parseInt(ret.get(4));
				setPort(port);
			} catch (Exception e) {
			}
		}

		if (ret != null && ret.size() > 5) {
			setDbName(ret.get(5));
		}
	}

	@Override
	public Exception makeException(Exception e, String msg2) {

		String msg = msg2 == null ? "" : msg2;
		msg += " ex=[" + e.getMessage() + "]";

		if (e instanceof IOException) {
			return e;
		} else if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;

			if (sqle.getErrorCode() == 17002) {
				return new IOException(e.getMessage());
			} else if (sqle.getErrorCode() == 1062) {
				return new DBObjectDupException(e.getMessage());
			}
			//
			else if (sqle.getErrorCode() == 1060) {
				return new DBObjectDupException(e.getMessage());
			}

			// ROW 중복
			else if (sqle.getErrorCode() == SQL_DUPLICATE) {
				return new DBObjectDupException(e.getMessage());
			}
			// 이미 사용된 객체명입니다
			else if (sqle.getErrorCode() == SQL_TABLEALREADYEXIST) {
				return new DBObjectDupException(e.getMessage());
			} else if (sqle.getErrorCode() == SQL_TABLENOTFOUND) {
				return new TableNotFoundException(e.getMessage());
			} else if (sqle.getErrorCode() == SQL_CHILD_FOUND) {
				return new ColumnNotFoundException(e.getMessage());
			}
		}

		return e;
	}

	@Override
	public String makeUrl(Map<String, Object> para) {

		Object sid = null;
		Object serviceName = null;

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}

			sid = para.get("sid");
			serviceName = para.get("serviceName");
		}

		if (sid != null && sid.toString().trim().length() > 0) {
			setDbName(sid + "");
			return "jdbc:oracle:thin:@" + getIpAddress() + ":" + getPort() + ":" + getDbName();
		} else if (serviceName != null && serviceName.toString().trim().length() > 0) {
			setDbName(serviceName + "");
			return "jdbc:oracle:thin:@" + getIpAddress() + ":" + getPort() + "/" + getDbName();
		} else {
			return "jdbc:oracle:thin:@" + getIpAddress() + ":" + getPort() + ":" + getDbName();
		}

	}

	@Override
	public String getSqlAdd(Column column, String tableName) {
		return "alter table " + tableName + " add " + getSqlCreate(column);
	}

	@Override
	public String getSqlUpdate(Column column, String tableName) {
		return "alter table " + tableName + " modify " + getSqlCreate(column);
	}

}
