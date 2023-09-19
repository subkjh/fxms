package subkjh.dao.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransientException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import subkjh.bas.co.log.Logger;
import subkjh.dao.QidDao;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.IndexDto;
import subkjh.dao.def.Sequence;
import subkjh.dao.def.Table;
import subkjh.dao.exp.DBObjectDupException;
import subkjh.dao.exp.TableNotFoundException;

/**
 * MYSQL 데이터베이스<br>
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class MySql extends DataBase {

	public enum DATA_TYPE {
		BIT(0, "1 ~ 64bit를 표현합니다. b'0000' 과 같이 표현", Byte.class) //
		, TINYINT(1, "-128 ~ 127", Byte.class) //
		, SMALLINT(2, "-32,768 ~ 32,767", Short.class) //
		, MEDIUMINT(3, "-8,388,608 ~ 8,388,607", Integer.class) //
		, INT(4, "-21억 ~ +21억", Integer.class) //
		, INTEGER(4, "-21억 ~ +21억", Integer.class) //
		, BIGINT(8, "약 -900경 ~ +900경", Long.class)//
		, FLOAT(4, "-3.40E+38 ~ -1.17E-38", Float.class) //
		, DOUBLE(8, "1.22E-308 ~ 1.79E+308", Double.class) //
		, REAL(8, "1.22E-308 ~ 1.79E+308", Double.class) //
		, DECIMAL("5~17", "-10^38+1 ~ +10^38-1", Number.class) //
		, NUMERIC("5~17", "-10^38+1 ~ +10^38-1", Number.class)

		, CHAR("1 ~ 255", "고정길이 문자형. n을 1부터 255까지 지정.", String.class) //
		, VARCHAR("1 ~ 65535", "가변길이 문자형. n을 사용하면 1부터 65535까지 지정", String.class) //
		, BINARY("1 ~ 255", "고정길이 이진 데이터 값", String.class) //
		, VARBINARY("1 ~ 255", "가변길이 이진 데이터 값", String.class)

		, TINYTEXT("1 ~ 255", "255 크기의 TEXT 데이터 값", String.class) //
		, TEXT("1 ~ 65535", "N 크기의 TEXT 데이터 값", String.class) //
		, MEDIUMTEXT("1 ~ 16777215", "16777215 크기의 TEXT 데이터 값", String.class) //
		, LONGTEXT("1 ~ 4294967295", "최대 4GB 크기의 TEXT 데이터 값", String.class)

		// BLOB : Binary Large Object

		, TINYBLOB("1 ~ 255", "255 크기의 BLOB 데이터 값", Object.class)//
		, BLOB("1 ~ 65535", "N 크기의 BLOB 데이터 값", Object.class) //
		, MEDIUMBLOB("1 ~ 16777215", "16777215 크기의 BLOB 데이터 값", Object.class)//
		, LONGBLOB("1 ~ 4294967295", "최대 4GB 크기의 BLOB 데이터 값", Object.class)

		, ENUM("1 또는 2", "최대 65535개의 열거형 데이터 값", Object.class),
		SET("1, 2, 3, 4, 8", "최대 64개의 중복되지 않는 데이터 값", Object.class)

		;

		DATA_TYPE(Object len, String descr, Class<?> classOfJava) {

		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822714624889076781L;

	public static final int PORT = 3306;

	private static String engine = "InnoDB";

	public static void setEngine(String engine) {
		MySql.engine = engine;
	}

	/**
	 * 시퀀스 제공 여부
	 */
	private boolean supportSequence = true;

	public MySql() {

//		setDriver("org.gjt.mm.mysql.Driver");
		setDriver("com.mysql.cj.jdbc.Driver");
		setPort(PORT);

		addConst(CONST_NVL, "ifnull");
		addConst(CONST_TRUNC, "truncate");
	}

	@Override
	public String getDataTypeFull(Column column) {

		String datatype = column.getDataType().toLowerCase();
		String md = column.getDataLength() + "," + column.getDataScale();

		if (datatype.startsWith("varchar")) {
			return "varchar(" + column.getDataLength() + ") binary ";
		} else if (datatype.equalsIgnoreCase("char")) {
			return column.getDataType() + "(" + column.getDataLength() + ")";
		} else if (datatype.equals("number")) {
			if (column.getDataScale() == 0) {
//				return column.getDataLength() > 9 ? "bigint" : "int";
				return column.getDataLength() > 9 ? "bigint(" + column.getDataLength() + ")"
						: "int(" + column.getDataLength() + ")";
			} else {
//				return column.getDataLength() > 9 ? "double" : "float";
				return column.getDataLength() > 9 ? "double(" + md + ")" : "float(" + md + ")";
			}
		} else {
			return column.getDataType();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Index> getIndexList(QidDao dao, String tableName) throws Exception {

		if (tableName == null)
			return new ArrayList<Index>();

		Map<String, Index> idxMap = new HashMap<String, Index>();
		List<Map<String, Object>> list = null;
		List<IndexDto> resList;
		Index index;
		String idxName, colName;
		INDEX_TYPE indexType;

		try {
			list = dao.selectSql2Map("show indexes from " + tableName, null);

			for (Map<String, Object> e : list) {

				idxName = e.get("KEY_NAME").toString();

				if (idxName.equals("PRIMARY")) {
					indexType = INDEX_TYPE.PK;
				} else {
					if ("1".equals(e.get("NON_UNIQUE"))) {
						indexType = INDEX_TYPE.KEY;
					} else {
						indexType = INDEX_TYPE.UK;
					}
				}
				colName = e.get("COLUMN_NAME").toString();

				index = idxMap.get(idxName);
				if (index == null) {
					index = new Index(idxName, indexType);
					idxMap.put(index.getIndexName(), index);
				}

				index.addColumn(colName);
			}

			resList = dao.selectQid2Res(QID.QID_SELECT_TABLE_INDEX, tableName);

			for (IndexDto e : resList) {

				idxName = e.getIndexName();

				index = idxMap.get(idxName);

				if (index == null) {

					if (e.getIndexType().indexOf("FOREIGN") >= 0) {
						indexType = INDEX_TYPE.FK;
					} else if (e.getIndexType().indexOf("PRIMARY") >= 0) {
						indexType = INDEX_TYPE.PK;
					} else if (e.getIndexType().indexOf("UNIQUE") >= 0) {
						indexType = INDEX_TYPE.UK;
					} else {
						indexType = INDEX_TYPE.KEY;
					}

					index = new Index(e.getIndexName(), indexType);
					index.setFkTable(e.getFkTableName());
					index.setFkColumn(e.getFkColumnName());
					idxMap.put(idxName, index);
				}

				index.addColumn(e.getColumnName());

			}

			return new ArrayList<Index>(idxMap.values());
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public List<String> getSqlComment(Table table) throws Exception {
		return new ArrayList<String>();
	}

	@Override
	public String getSqlCreate(Column column) {

		String ret = super.getSqlCreate(column);

		if (column.hasComment()) {
			ret += " COMMENT '" + column.getComments() + "'";
		}

		return ret;
	}

	@Override
	public String getSqlCreate(Index idx, Table tab) {
		return getSqlCreate0(idx, tab);
	}

	@Override
	public String getSqlCreate(Sequence seq) {
		if (supportSequence) {
			StringBuffer sb = new StringBuffer();

//		CREATE [OR REPLACE] [TEMPORARY] SEQUENCE [IF NOT EXISTS] sequence_name
//		[ INCREMENT [ BY | = ] increment ]
//		[ MINVALUE [=] minvalue | NO MINVALUE | NOMINVALUE ]
//		[ MAXVALUE [=] maxvalue | NO MAXVALUE | NOMAXVALUE ]
//		[ START [ WITH | = ] start ] 
//		[ CACHE [=] cache | NOCACHE ] [ CYCLE | NOCYCLE] 
//		[table_options]

			sb.append("CREATE SEQUENCE ").append(seq.getSequenceName()).append("\n");
			sb.append("INCREMENT BY ").append(seq.getIncBy()).append("\n");
			sb.append("START WITH ").append(seq.getValueNext()).append("\n");
			sb.append("MINVALUE ").append(seq.getValueMin()).append("\n");
			sb.append("MAXVALUE ").append(seq.getValueMax()).append("\n");
			if (seq.isCycle()) {
				sb.append("CYCLE \n");
			} else {
				sb.append("NOCYCLE \n");
			}
			sb.append("CACHE 20");

			return sb.toString();
		} else {

			return "insert into FX_TBL_SEQ( " //
					+ "  SEQ_NAME " //
					+ ", INC_BY" //
					+ ", VALUE_MIN" //
					+ ", VALUE_MAX" //
					+ ", VALUE_NEXT" //
					+ ", IS_CYCLE" //
					+ " ) values ( " //
					+ "  '" + seq.getSequenceName() + "'" //
					+ ", " + seq.getIncBy() //
					+ ", " + seq.getValueMin() //
					+ ", " + seq.getValueMax() //
					+ ", " + seq.getValueNext() //
					+ ", '" + (seq.isCycle() ? "Y" : "N") + "'" //
					+ ")";
		}
	}

	/**
	 * 
	 * @param table 테이블정보
	 * @return 쿼리문
	 * @throws TrNotFoundExp
	 * @throws Exception
	 */
	public String getSqlCreate(Table table) throws Exception {
		String ret = getSqlCreate0(table);
		if (table.hasComment()) {
			if (engine != null && engine.length() > 0) {
				return ret + " ENGINE=" + engine + " COMMENT '" + table.getComment() + "'";
			} else {
				return ret + " COMMENT '" + table.getComment() + "'";
			}
		}
		return ret;
	}

	@Override
	public String getSqlDrop(Sequence seq) {
		return "delete from SEQ_TAB where SEQ_NAME = '" + seq.getSequenceName() + "'";
	}

	@Override
	public String getSqlSequenceNextVal(String sequence) {
		if (supportSequence) {
			return "select nextval(" + sequence + ")";
		} else {
			return "select nextval('" + sequence + "')";
		}
	}

	@Override
	public String getVerDb() throws Exception {
		QidDao tran = createQidDao();

		try {
			tran.start();
			List<Object[]> objList = tran.selectSql2Obj("show variables like 'version'", null);
			return objList != null && objList.size() == 1 ? objList.get(0)[1] + "" : null;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public void initPreparedStatement(PreparedStatement ps) throws SQLException {

		// 이렇게 설정하면 자료를 미리 가지고 있지 않음 ( 2011-07-13 )
		ps.setFetchSize(Integer.MIN_VALUE);
	}

	@Override
	public boolean isUseUserPass() {
		return true;
	}

	@Override
	public Exception makeException(Exception e, String sql) {

		StringBuffer msg = new StringBuffer();
		if (sql != null)
			msg.append("SQL :").append(sql);
		msg.append("Exception=").append(e.getMessage());
		Logger.logger.error(e);
		Logger.logger.fail((e != null ? e.getClass().getName() + " " : "") + msg);

		String errmsg = e.getMessage() == null ? "" : e.getMessage().toLowerCase();

		if (errmsg.contains("duplicate")) {
			return new DBObjectDupException(e.getMessage());
		} else if (errmsg.indexOf("already exists") >= 0) {
			return new DBObjectDupException(e.getMessage());
		} else if (errmsg.indexOf("doesn't exist") >= 0) {
			return new TableNotFoundException(e.getMessage());
		} else if (e instanceof CommunicationsException) {
			return new IOException(e.getMessage());
		} else if (e.getClass().getName().indexOf("Connection") >= 0) {
			return new IOException(e.getMessage());
		} else if (e instanceof SQLTransientException) {
			return new IOException(e.getMessage());
		} else if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;

			if (sqle.getErrorCode() == 1062) {
				return new DBObjectDupException(e.getMessage());
			}
			// 필드가 이미 존재할 때 발생
			else if (sqle.getErrorCode() == 1060) {
				return new DBObjectDupException(e.getMessage());
			} else if (sqle.getErrorCode() == 1050 || sqle.getErrorCode() == 1051 || sqle.getErrorCode() == 1146) {
				return new TableNotFoundException(e.getMessage());
			} else if (sqle.getErrorCode() == 0) {
				return new IOException(e.getMessage());
			}

		}

		return e;
	}

	@Override
	public String makeUrl(Map<String, Object> para) {
		return "jdbc:mysql://" + makeUri(para);
	}

	@Override
	public void setUrl(String url) {

		if (url != null) {
			String ss[] = url.split(PATTERN);
			String s1[] = ss[ss.length - 1].split("\\?");
			setDbName(s1[0]);
		}

		super.setUrl(url);
	}

	protected String makeUri(Map<String, Object> para) {

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}
			Object user = para.get("user");
			Object password = para.get("password");
			if (user != null)
				setUser(user + "");
			if (password != null)
				setPassword(password + "");
		}

		// String useUnicode = para.get("useUnicode");

		String paraStr = "";
		paraStr = makePara(paraStr, "user", getUser());
		paraStr = makePara(paraStr, "password", getPassword());

		if (para != null) {
			for (String key : para.keySet()) {
				if (key.equals("user") || key.equals("password") || key.equals("ipAddress") || key.equals("port")
						|| key.equals("databaseKind") || key.equals("databaseName") || key.equals("dbName"))
					continue;
				paraStr = makePara(paraStr, key, para.get(key));
			}
		}

		return getIpAddress() + ":" + getPort() + "/" + getDbName() + paraStr;
	}

	private String makePara(String prevPara, String name, Object value) {
		if (value == null)
			return prevPara;

		// try {
		// if (prevPara.length() == 0) {
		// return "?" + URLEncoder.encode(name, "utf-8") + "=" +
		// URLEncoder.encode(value.toString(), "utf-8");
		// }
		// else {
		// return prevPara + "&" + URLEncoder.encode(name, "utf-8") + "="
		// + URLEncoder.encode(value.toString(), "utf-8");
		// }
		// }
		// catch (Exception e) {
		if (prevPara.length() == 0) {
			return "?" + name + "=" + value;
		} else {
			return prevPara + "&" + name + "=" + value;
		}
		// }
	}
}
