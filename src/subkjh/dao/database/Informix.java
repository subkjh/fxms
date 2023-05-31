package subkjh.dao.database;

import java.util.Map;

import subkjh.dao.def.Column;
import subkjh.dao.exp.DBObjectDupException;
import subkjh.dao.exp.TableNotFoundException;

/**
 * 인포믹스
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Informix extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9157133910842457278L;

	public Informix() {
		setDriver("com.informix.jdbc.IfxDriver");
	}

	@Override
	public Exception makeException(Exception e, String msg) {

		String emsg = e.getMessage();
		if (emsg != null) {
			if (emsg.indexOf("duplicate") >= 0)
				return new DBObjectDupException(e.getMessage());
			if (emsg.indexOf("already exists in database") >= 0)
				return new DBObjectDupException(e.getMessage());
			if (emsg.indexOf("is not in the database") >= 0)
				return new TableNotFoundException(e.getMessage());
		}

		return null;
	}

	@Override
	public Class<?> getJavaType(Column column) {

		/**
		 * 0 = CHAR 1 = SMALLINT 2 = INTEGE R 3 = FLOAT 4 = SMALLFLOAT 5 =
		 * DECIMAL 6 = SERIAL * 7 = DATE 8 = MONEY 9 = NULL 10 = DATETIME 11 =
		 * BYTE 12 = TEXT 13 = VARCHAR 14 = INTERVAL 15 = NCHAR 16 = NVARCHAR 17
		 * = INT8 18 = SERIAL8 * 19 = SET 20 = MULTISET 21 = LIST 22 = 명명되지 않은
		 * ROW 40 = 가변 길이 은폐(opaque) 데이터 형식 4118 = HAMED ROW
		 */
		int type = Integer.parseInt(column.getDataType());

		if (column.getName().toUpperCase().startsWith("IS_"))
			return boolean.class;

		switch (type) {
		case 0:
		case 7:
		case 12:
		case 13:
		case 15:
		case 16:
		case 269:
			return String.class;
		case 1:
		case 2:
		case 11:
		case 14:
		case 258:
			return int.class;
		case 5:
		case 17:
			return long.class;
		case 3:
		case 4:
			return double.class;
		default:
			return Object.class;

		}

	}

	@Override
	public String makeUrl(Map<String, Object> para) {

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}
		}

		Object serverName = para.get("serverName");

		return "jdbc:informix-sqli://" + getIpAddress() + ":" + getPort() + "/" + getDbName() + ":INFORMIXSERVER="
				+ serverName;
	}

}
