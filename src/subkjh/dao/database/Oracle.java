package subkjh.dao.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ntp.TimeStamp;

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

	public enum DATA_TYPE {

		CHAR("n", " 고정길이 문자 / 최대 2000byte / 디폴트 값은 1byte", String.class) //
		, VARCHAR2("n", " 가변길이 문자 / 최대 4000BYTE / 디폴트 값은 1byte ", String.class) //

		, NCHAR("n", " 고정길이 유니코드 문자(다국어 입력가능) / 최대 2000byte / 디폴트 값은 1byte ", String.class) //
		, NVARCHAR("n", "가변길이 유니코드 문자(다국어 입력가능) / 최대 2000byte / 디폴트 값은 1byte  ", String.class) //
		, LONG("n", "최대 2GB 크기의 가변길이 문자형 ", String.class) //

		// Character Large Object
		, CLOB("n", "대용량 텍스트 데이터 타입(최대 4Gbyte", String.class) //
		, NCLOB("n", "대용량 텍스트 유니코드 데이터 타입(최대 4Gbyte)", String.class) //

		, NUMBER("n", "가변숫자 / P (1 ~ 38, 디폴트 : 38) / S (-84 ~ 127, 디폴트 값 : 0)  / 최대 22byte", Number.class) //
		, FLOAT("n", "NUMBER의 하위타입 / P (1~128 .디폴트 : 128) / 이진수 기준 / 최대 22byte ", Float.class) //
		, BINARY_FLOAT("n", "32비트 부동소수점 수 / 최대 4byte", Float.class) //
		, BINARY_DOUBLE("n", "64비트 부동소수점 수 / 최대 8byte ", Double.class) //

		, DATE("n", "BC 4712년 1월 1일부터 9999년 12월 31일, 연, 월, 일, 시, 분, 초 까지 입력 가능 ", Date.class) //
		, TIMESTAMP("n", "연도, 월, 일, 시, 분, 초 + 밀리초까지 입력가능 ", TimeStamp.class) //

		, BLOB("n", "이진형 대용량 객체", Object.class) //
		, BFILE("n", "대용량 이진 파일에 대한 위치,이름 저장 ", Object.class) //

		;

		DATA_TYPE(Object len, String descr, Class<?> classOfJava) {

		}

	}

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
