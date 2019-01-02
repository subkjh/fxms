package subkjh.bas.dao.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransientException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.CommunicationsException;

import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Sequence;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.dao.exception.DBObjectDupException;
import subkjh.bas.dao.exception.TableNotFoundException;
import subkjh.bas.log.Logger;

/**
 * MYSQL 데이터베이스
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class MySql extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822714624889076781L;

	public static final int PORT = 3306;

	public MySql() {

		setDriver("org.gjt.mm.mysql.Driver");
		setPort(PORT);

		addConst(CONST_NVL, "ifnull");
		addConst(CONST_TRUNC, "truncate");
	}

	@Override
	public String getSqlSequenceNextVal(String sequence) {
		return "select nextval('" + sequence + "')";
	}

	@Override
	public String getDataTypeFull(Column column) {

		String datatype = column.getDataType().toLowerCase();

		if (datatype.startsWith("varchar")) {
			return "varchar(" + column.getDataLength() + ") binary ";
		} else if (datatype.equalsIgnoreCase("char")) {
			return column.getDataType() + "(" + column.getDataLength() + ")";
		} else if (datatype.equals("number")) {
			if (column.getDataScale() == 0) {
				return column.getDataLength() > 9 ? "bigint" : "int";
			} else {
				return column.getDataLength() > 9 ? "double" : "float";
			}
		} else {
			return column.getDataType();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Index> getIndexList(DbTrans dao, String tableName) throws Exception {

		if (tableName == null)
			return new ArrayList<Index>();

		Map<String, Index> idxMap = new HashMap<String, Index>();
		List<Map<String, Object>> list = null;
		List<Index> idxList;
		Index index;

		try {
			list = dao.selectSql2Map("show indexes from " + tableName, null);

			String idxName;
			String idxType;
			String colName;

			for (Map<String, Object> e : list) {

				idxName = e.get("KEY_NAME").toString();

				if (idxName.equals("PRIMARY")) {
					idxType = INDEX_TYPE.PK.name();
				} else {
					if ("1".equals(e.get("NON_UNIQUE"))) {
						idxType = INDEX_TYPE.KEY.name();
					} else {
						idxType = INDEX_TYPE.UK.name();
					}
				}
				colName = e.get("COLUMN_NAME").toString();

				index = idxMap.get(idxName);
				if (index == null) {
					index = new Index(idxName, idxType, colName);
					idxMap.put(index.getName(), index);
				} else {
					index.addColumn(colName);
				}
			}

			idxList = dao.selectQid2Res(QID.QID_SELECT_TABLE_INDEX, tableName);

			for (Index e : idxList) {

				idxName = e.getName();

				index = idxMap.get(idxName);

				if (index == null) {
					idxMap.put(idxName, e);
					index = e;
				} else {

					idxType = index.getType();

					if (idxType.indexOf("FOREIGN") >= 0) {
						index.setType(INDEX_TYPE.FK.name());
					} else if (idxType.indexOf("PRIMARY") >= 0) {
						index.setType(INDEX_TYPE.PK.name());
					} else if (idxType.indexOf("UNIQUE") >= 0) {
						index.setType(INDEX_TYPE.UK.name());
					}

					index.setFkTable(e.getFkTable());
					index.setFkColumn(e.getFkColumn());

				}

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
		return "insert into SEQ_TAB ( " //
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

	/**
	 * 
	 * @param table
	 *            테이블정보
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

	private static String engine = "InnoDB";

	public static void setEngine(String engine) {
		MySql.engine = engine;
	}

	@Override
	public String getSqlDrop(Sequence seq) {
		return "delete from SEQ_TAB where SEQ_NAME = '" + seq.getSequenceName() + "'";
	}

	@Override
	public void initPreparedStatement(PreparedStatement ps) throws SQLException {

		// 이렇게 설정하면 자료를 미리 가지고 있지 않음 ( 2011-07-13 )
		ps.setFetchSize(Integer.MIN_VALUE);
	}

	@Override
	public String makeUrl(Map<String, Object> para) {
		return "jdbc:mysql://" + makeUri(para);
	}

	@Override
	public String getVerDb() throws Exception {
		DbTrans tran = createDbTrans();

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

	@Override
	public Exception makeException(Exception e, String sql) {

		String msg = "";

		if (sql != null)
			msg += "SQL [" + sql + "]";
		msg += " EXCEPTION=[" + e.getMessage() + "]";

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
	public void setUrl(String url) {

		if (url != null) {
			String ss[] = url.split(PATTERN);
			String s1[] = ss[ss.length - 1].split("\\?");
			setDbName(s1[0]);
		}

		super.setUrl(url);
	}

	@Override
	public boolean isUseUserPass() {
		return false;
	}
}
