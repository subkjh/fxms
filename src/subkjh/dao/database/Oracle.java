package subkjh.dao.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import subkjh.dao.def.Column;
import subkjh.dao.exp.ColumnNotFoundException;
import subkjh.dao.exp.DBObjectDupException;
import subkjh.dao.exp.TableNotFoundException;

/**
 * 오라클 데이터베이스
 * 
 * @author subkjh
 * @since 2007-01-01
 *
 * 
 *        <pre>
 * 
 * 
 *        </pre>
 */
public class Oracle extends DataBase {

	public static final int PORT = 1521;

	/**
	 * no data found
	 */
	final int SQL_NOTFOUND = 1403;

	/**
	 * unique constraint
	 */
	final int SQL_DUPLICATE = 1;

	/**
	 * not logged on
	 */
	final int SQL_NOTCONNECT = 1012;

	/**
	 * table or view does not exist.
	 */
	final int SQL_TABLENOTFOUND = 942;

	/**
	 * name is already used by an existing object. table already exist.
	 */
	final int SQL_TABLEALREADYEXIST = 955;

	/**
	 * resource busy and acquire with NOWAIT specified
	 */
	final int SQL_RESOURCE_BUSY = 54;

	/**
	 * integrity constraint (%s.%s) violated - parent key not found
	 */
	final int SQL_PARENT_NOT_FOUND = 2291;

	/**
	 * integrity constraint (%s.%s) violated - child record found
	 */
	final int SQL_CHILD_FOUND = 2292;

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

			String message = e.getMessage() + msg;

			if (sqle.getErrorCode() == 17002) {
				return new IOException(message);
			} else if (sqle.getErrorCode() == 1062) {
				return new DBObjectDupException(message);
			}
			//
			else if (sqle.getErrorCode() == 1060) {
				return new DBObjectDupException(message);
			}

			// ROW 중복
			else if (sqle.getErrorCode() == SQL_DUPLICATE) {
				return new DBObjectDupException(message);
			}
			// 이미 사용된 객체명입니다
			else if (sqle.getErrorCode() == SQL_TABLEALREADYEXIST) {
				return new DBObjectDupException(message);
			} else if (sqle.getErrorCode() == SQL_TABLENOTFOUND) {
				return new TableNotFoundException(message);
			} else if (sqle.getErrorCode() == SQL_CHILD_FOUND) {
				return new ColumnNotFoundException(message);
			}
		}

		return new SQLException(msg);
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
