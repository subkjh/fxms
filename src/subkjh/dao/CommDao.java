package subkjh.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.DaoListener;
import subkjh.dao.model.ColList;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.DaoResult.ResultKeyCase;

/**
 * SQL 문을 처리하는 클래스
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
public class CommDao extends Dao {

	public enum INSERT_TYPE {
		batch, oneByOne, oneByOneIgnoreExp;
	}

	public enum RTYPE {
		Map, Array, Object;
	}

	/**
	 * 처리 과정을 stdout으로 출력할 것인지를 나타냅니다.<br>
	 * 기본 값을 false이면 이 클래스가 생성될 때 이 값이 기본값으로 사용됩니다.
	 */
	public static boolean isDefaultTrace = false;
	public int batchsize = 5000;
	/** 접속 데이터 베이스 */
	protected DataBase database;
	protected final String LINE = "--------------------------------------------------------------------";
	/** 패치 크기 */
	private int fetchSize = 0;
	/** 연결된 콘넥션 */
	private Connection m_connection = null;
	private INSERT_TYPE insertType = INSERT_TYPE.batch;

	/**
	 * 
	 */
	protected CommDao() {
	}

	/**
	 * commit 처리
	 */
	public void commit() {
		try {
			if (m_connection != null && m_connection.getAutoCommit() == false) {
				m_connection.commit();
				if (Logger.debug)
					System.out.println("commit");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * SQL문을 실행합니다.<br>
	 * Connection에 이상이 있으면 닫고 다시 시도합니다.
	 * 
	 * @param sql 실행할 SQL문
	 * @param obj
	 * @return
	 */
	public int executeSql(String sql, Object obj) throws Exception {
		return executeQidSql(null, sql, obj);
	}

	/**
	 * 
	 * 
	 * @return 데이터베이스
	 */
	public DataBase getDatabase() {
		return database;
	}

	/**
	 * 
	 * @return 설정된 패치크기
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	public INSERT_TYPE getInsertType() {
		return insertType;
	}

	/**
	 * sql 문의 PreparedStatement를 넘긴다.
	 * 
	 * 만약 Connection이 되어 있다면 새롭게 Connect 하지 않는다.
	 * 
	 * @param sql
	 * @return sql 문의 PreparedStatement
	 * @throws Exception
	 */
	public PreparedStatement getPrepareStatement(String sql) throws IOException, Exception {

		PreparedStatement preStmt = null;

		Connection c = getConnection();
		try {
			preStmt = c.prepareStatement(sql);
		} catch (SQLException e) {
			free(c);
			throw database.makeException(e, sql);
		}

		if (fetchSize != 0) {
			try {
				preStmt.setFetchSize(fetchSize);
			} catch (Exception e) {
				throw database.makeException(e, sql);
			}
		}

		return preStmt;

	}

	/**
	 * rollback 처리
	 */
	public void rollback() {
		if (m_connection != null) {
			try {
				if (m_connection.getAutoCommit() == false) {
					m_connection.rollback();
					if (Logger.debug)
						System.out.println("rollback");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param sql
	 * @param para
	 * @param daoListener
	 * @throws Exception
	 */
	public int selectSql(String sql, Object para[]) throws Exception {

		ResultSet r = null;
		PreparedStatement pstmt = null;
		int rowNo = 0;
		Exception ex = null;

		try {

			pstmt = getPrepareStatement(sql);
			setPreSt(pstmt, para);

			long tm = System.currentTimeMillis();

			r = pstmt.executeQuery();

			if (r != null) {

				ColList columns = makeColumns(r, ResultKeyCase.java);

				if (daoListener != null) {
					daoListener.onStart(columns.toColumnArray());
				}

				Object entry[];
				while (r.next()) {
					entry = new Object[columns.size()];
					for (int i = 1; i <= entry.length; i++) {
						entry[i - 1] = r.getObject(i);
					}
					if (daoListener != null) {
						daoListener.onSelected(rowNo, entry);
					}
					rowNo++;
				}
			}

			if (Logger.debug)
				System.out.println(pstmt + " - ok " + (System.currentTimeMillis() - tm) + "(ms)");

		} catch (Exception e) {
			if (Logger.debug)
				System.err.println((pstmt != null ? pstmt.toString() : "null") + " - " + e);
			ex = database.makeException(e, sql);
			throw ex;
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
				}
			}
			free(pstmt);
			if (daoListener != null) {
				daoListener.onFinish(ex);
			}
		}

		return rowNo;

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSql2Map(String sql, Object para[]) throws Exception {
		return (List<Map<String, Object>>) selectSql(sql, sql, para, RTYPE.Map, null);
	}

	/**
	 * 문자열 배열로 SQL 결과를 가져온다.
	 * 
	 * @param sql
	 * @param para
	 * @return null이면 오류가 발생했다는 의미이다. 결과가 없다면 size=0인 List를 넘긴다.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSql2Map(String qid, String sql, Object para[]) throws Exception {
		return (List<Map<String, Object>>) selectSql(qid, sql, para, RTYPE.Map, null);
	}

	/**
	 * 문자열 배열로 SQL 결과를 가져온다.
	 * 
	 * @param sql
	 * @param para
	 * @return null이면 오류가 발생했다는 의미이다. 결과가 없다면 size=0인 List를 넘긴다.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> selectSql2Obj(String sql, Object para[]) throws Exception {
		return (List<Object[]>) selectSql(sql, sql, para, RTYPE.Array, null);
	}

	/**
	 * 문자열 배열로 SQL 결과를 가져온다.
	 * 
	 * @param sql
	 * @param para
	 * @return null이면 오류가 발생했다는 의미이다. 결과가 없다면 size=0인 List를 넘긴다.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> selectSql2Obj(String qid, String sql, Object para[]) throws Exception {
		return (List<Object[]>) selectSql(qid, sql, para, RTYPE.Array, null);
	}

	/**
	 * 
	 * @param daoListener
	 */
	public void setDaoListener(DaoListener daoListener) {
		this.daoListener = daoListener;
	}

	/**
	 * 데이터 베이스를 설정한다.
	 * 
	 * @param database 사용할 데이터베이스
	 */
	public void setDatabase(DataBase database) {
		this.database = database;
	}

	/**
	 * 
	 * @param fetchSize 사용할 패치 크기
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * 
	 * @param pstmt 대상
	 * @param index 인덱스
	 * @param val   값
	 * @throws Exception
	 */
	public void setPreSt(PreparedStatement pstmt, int index, Object val) throws Exception {
		try {

			if (val instanceof String) {
				// 오라클의 경우 공백이면 null로 기록됨
				// 다른 데이터베이스는 그렇치 않은 관계로 공백이면 null로 강제 설정합니다.
				// 2015.09.18 by subkjh
				if (val.toString().length() == 0) {
					pstmt.setString(index, null);
				} else {
					pstmt.setString(index, val.toString());
				}
			} else if (val instanceof Boolean) {
				pstmt.setString(index, (((Boolean) val).booleanValue() ? "Y" : "N"));
			} else if (val instanceof Integer) {
				pstmt.setInt(index, ((Integer) val).intValue());
			} else if (val instanceof Short) {
				pstmt.setShort(index, ((Short) val).shortValue());
			} else if (val instanceof Byte) {
				pstmt.setByte(index, ((Byte) val).byteValue());
			} else if (val instanceof Long) {
				pstmt.setLong(index, ((Long) val).longValue());
			} else if (val instanceof Double) {
				pstmt.setDouble(index, ((Double) val).doubleValue());
			} else if (val instanceof Float) {
				pstmt.setFloat(index, ((Float) val).floatValue());
			} else if (val instanceof BigDecimal) {
				pstmt.setBigDecimal(index, (BigDecimal) val);
			} else if (val == null) {
				try {
					pstmt.setObject(index, null);
				} catch (Exception e) {
					try {
						pstmt.setString(index, null);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} else {
				pstmt.setObject(index, val);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * PrepareedStatement에 값을 설정한다.
	 * 
	 * @param pstmt 선언된 문장
	 * @param para  설정한 인수 목록
	 */
	public void setPreSt(PreparedStatement pstmt, Object para[]) throws Exception {

		if (para == null)
			return;

		for (int i = 0; i < para.length; i++) {
			setPreSt(pstmt, i + 1, para[i]);
		}
	}

	/**
	 * 데이터 베이스와 연결한다.
	 * 
	 * @throws Exception
	 */
	protected void connect() throws IOException, Exception {
		if (database == null)
			throw new Exception("database not set");
		m_connection = database.getConnection();
	}

	/**
	 * 
	 * @param closeRealConnection POOL인 경우 실제 CONNECTION을 닫을지 여부
	 * @throws Exception
	 */
	protected void disconnect(boolean closeRealConnection) throws Exception {
		database.close(m_connection, closeRealConnection);
		m_connection = null;
	}

	protected int executeQidSql(String qid, String sql, Object obj) throws Exception {

		int count;

		try {
			count = execute(qid, sql, obj);
			return count;
		} catch (IOException e) {
		} catch (Exception e) {
			throw e;
		}

		for (int i = 0; i < database.getReconnectRetry(); i++) {

			try {
				database.close(m_connection, true);
			} catch (Exception e1) {
			}

			try {
				Thread.sleep(database.getReconnectWaitTimeSec() * 1000);
			} catch (InterruptedException e) {
			}

			try {
				m_connection = database.getConnection();
			} catch (Exception e) {
				continue;
			}

			try {
				count = execute(qid, sql, obj);
				return count;
			} catch (IOException e) {
			} catch (Exception e) {
				throw e;
			}
		}

		return 0;
	}

	/**
	 * PreparedStatemetn는 닫는다.
	 * 
	 * @param preStmt
	 */
	protected void free(PreparedStatement preStmt) throws Exception {
		if (preStmt != null) {
			try {
				Connection c = preStmt.getConnection();
				preStmt.close();
				free(c);
			} catch (Exception e) {
				throw database.makeException(e, null);
			}
		}
	}

	/**
	 * ResultSet을 닫는다.
	 * 
	 * @param rs
	 */
	protected void free(ResultSet rs) {
		if (rs == null) {
			return;
		}

		try {
			Statement stmt = rs.getStatement();
			rs.close();
			free(stmt);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	/**
	 * Statement를 닫는다.
	 * 
	 * @param s
	 */
	protected void free(Statement s) throws Exception {
		try {
			if (s == null) {
				return;
			}
			Connection c = s.getConnection();
			s.close();
			free(c);
		} catch (Exception e) {
			throw database.makeException(e, null);
		}
	}

	/**
	 * Connection을 얻는다.<br>
	 * 
	 * 미리 연결되어 있다면 그것을 넘기고 그렇치 않다면 새롭게 연결하여 넘긴다.
	 * 
	 * @return 연결된 Connection
	 * @throws Exception
	 */
	protected Connection getConnection() throws IOException, Exception {
		if (m_connection == null) {
			return database.getConnection();
		} else
			return m_connection;
	}

	protected String getSql(String sql2, Object para[]) {
		String sql = sql2;
		if (para != null) {
			for (Object o : para) {
				if (o != null) {
					if (o instanceof Number)
						sql = sql.replaceFirst("\\?", o.toString());
					else
						sql = sql.replaceFirst("\\?", "'" + o.toString() + "'");
				} else {
					sql = sql.replaceFirst("\\?", "'(null)'");
				}
			}
		}
		return sql;
	}

	protected String makeString(String tableName, int cnt, int total) {

		if (total == 0) {
			return tableName + " = 0/0 : 100.000%";
		}

		String msg = tableName + " = " + cnt + "/" + total + " : " + String.format("%.3f", ((100f * cnt) / total))
				+ "%";
		return msg;
	}

	protected List<?> selectSql(String qid, String sql, Object para[], RTYPE retType, DaoResult result)
			throws Exception {

		try {
			if (retType == RTYPE.Array) {
				return select2Array(sql, para);
			} else if (retType == RTYPE.Map) {
				return select2Map(sql, para);
			} else if (retType == RTYPE.Object) {
				return select2Object(sql, para, result);
			} else {
				throw new Exception("RTYPE not implement");
			}
		} catch (IOException e) {
		} catch (Exception e) {
			throw e;
		}

		Exception exception = null;

		for (int i = 0; i < database.getReconnectRetry(); i++) {

			try {
				database.close(m_connection, true);
			} catch (Exception e1) {
			}

			try {
				Thread.sleep(database.getReconnectWaitTimeSec() * 1000);
			} catch (InterruptedException e) {
			}

			try {
				m_connection = database.getConnection();
			} catch (Exception e) {
				exception = database.makeException(e, sql);
				continue;
			}

			try {
				if (retType == RTYPE.Array) {
					return select2Array(sql, para);
				} else if (retType == RTYPE.Map) {
					return select2Map(sql, para);
				} else if (retType == RTYPE.Object) {
					return select2Object(sql, para, result);
				} else {
					throw new Exception("RTYPE not implement");
				}
			} catch (IOException e) {
			} catch (Exception e) {
				throw e;
			}
		}

		if (exception == null)
			return null;

		throw exception;
	}

	/**
	 * 로그를 남깁니다.
	 * 
	 * @param para 로그 남길 인수
	 */
	protected void trace(Object para[]) {

		if (para == null || para.length == 0)
			return;

		if (Logger.logger.isTrace()) {
			if (para != null) {
				Logger.logger.trace(Arrays.toString(para));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private int execute(String qid, String sql, Object obj) throws IOException, Exception {

		Collection<Object[]> dataList = null;
		Object para[] = null;
		String paraMsg;
		long ptime = System.currentTimeMillis();
		long gtime = ptime;
		int ret = 0;
		Exception ex = null;

		if (obj instanceof Collection) {
			dataList = (Collection<Object[]>) obj;
			paraMsg = dataList.size() + " datas";
		} else if (obj != null) {
			para = (Object[]) obj;
			paraMsg = Arrays.toString(para);
		} else {
			paraMsg = "null";
		}

		PreparedStatement pstmt = null;

		try {

			pstmt = getPrepareStatement(sql);

			if (daoListener != null) {
				daoListener.onStart(new String[0]);
			}

			Logger.logger.trace("QID({}) SQL({}) PARA({})", qid, sql, paraMsg);

			if (dataList == null) {

				setPreSt(pstmt, para);
				ret = pstmt.executeUpdate();

			} else if (insertType == INSERT_TYPE.oneByOne //
					|| insertType == INSERT_TYPE.oneByOneIgnoreExp //
					|| (dataList.size() == 1)) {

				for (Object v[] : dataList) {

					setPreSt(pstmt, v);

					try {
						ret += pstmt.executeUpdate();
					} catch (Exception e) {
						if (insertType == INSERT_TYPE.oneByOneIgnoreExp) {
							if (daoListener != null) {
								daoListener.onExecuted(v, e);
							} else {
								Logger.logger.fail(Arrays.toString(v) + "CLASS(" + e.getClass().getName() + ") MSG("
										+ e.getMessage() + ")");
							}
						} else {
							throw e;
						}
					}

					if (System.currentTimeMillis() > gtime + 5000L) {
						gtime += 5000L;
						Logger.logger.trace(qid + " row count = " + ret);
					}
				}
			}
			//
			else {

				ret = 0;

				for (Object v[] : dataList) {

					setPreSt(pstmt, v);

					pstmt.addBatch();

					ret++;

					if (System.currentTimeMillis() > gtime + 5000L) {
						gtime += 5000L;
						Logger.logger.trace(qid + " row count = " + ret);
					}

				}

				ret = executeBatch(pstmt);

			}

			Logger.logger.debug("SQL({})\n PARA({})RET({})TIME({})", sql, paraMsg, ret,
					(System.currentTimeMillis() - ptime));
			return ret;

		} catch (Exception e) {
			ex = database.makeException(e, "SQL(" + sql + ") PARA(" + paraMsg + ")");
			throw ex;
		} finally {
			if (daoListener != null) {
				daoListener.onFinish(ex);
			}
			if (pstmt != null)
				free(pstmt);
		}

	}

	private int executeBatch(PreparedStatement pstmt) throws SQLException {
		int cnt[] = pstmt.executeBatch();

		int total = 0;
		for (int i = 0; i < cnt.length; i++) {
			if (cnt[i] > 0)
				total += cnt[i];
			else if (cnt[i] == Statement.SUCCESS_NO_INFO) {
				total++;
			}
		}
		return total;

	}

	/**
	 * 연결을 해지한다.
	 * 
	 * @param c
	 */
	private void free(Connection c) throws Exception {

		if (c == null)
			return;

		// 멤버변수가 같다면 disconnect()에 의해서만 닫힌다.
		if (m_connection != null && c.equals(m_connection))
			return;

		try {
			database.close(c, false);
		} catch (Exception ex) {
			throw database.makeException(ex, null);
		}
	}

	private List<Object[]> select2Array(String sql, Object para[]) throws Exception {

		long ptime = System.currentTimeMillis();
		List<Object[]> list = new ArrayList<Object[]>();
		Object entry[];
		ResultSet r = null;
		PreparedStatement pstmt = null;
		int rowNo = 0;

		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			if (r != null) {

				ColList columns = makeColumns(r, ResultKeyCase.upper);

				if (daoListener != null) {
					Logger.logger.debug(sql);
					daoListener.onStart(columns.toColumnArray());
				}

				while (r.next()) {

					entry = makeResultArray(r, columns);

					if (daoListener != null) {
						daoListener.onSelected(++rowNo, entry);
					} else {
						list.add(entry);
					}

					if (System.currentTimeMillis() > ptime + 5000) {
						ptime += 5000;
						if (Logger.logger != null && Logger.logger.isTrace()) {
							Logger.logger.trace("row count = " + rowNo);
						}
					}
				}
			}

			if (daoListener != null) {
				daoListener.onFinish(null);
			}

			return list;
		} catch (Exception e) {

			Logger.logger.error(e);
			Exception ex = database.makeException(e, sql);
			if (ex != e) {
				Logger.logger.fail(ex.getMessage());
			}

			if (daoListener != null) {
				daoListener.onFinish(ex);
			}
			throw ex;
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
				}
			}
			free(pstmt);
		}
	}

	private List<Map<String, Object>> select2Map(String sql, Object para[]) throws Exception {

		long stime = System.currentTimeMillis();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResultSet r = null;
		PreparedStatement pstmt = null;
		Map<String, Object> entry;
		int rowNo = 0;

		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			// 결과 필드 개수
			ColList columns = makeColumns(r, ResultKeyCase.upper);
			if (columns.isEmpty())
				return list;

			if (daoListener != null) {
				daoListener.onStart(columns.toColumnArray());
			}

			if (r != null) {
				while (r.next()) {
					entry = makeResultMap(r, columns);
					
					if (daoListener != null) {
						daoListener.onSelected(++rowNo, entry);
					} else {
						list.add(entry);
					}					
				}
			}

			Logger.logger.debug(sql + "=" + list.size() + ", stime=" + (System.currentTimeMillis() - stime) + "(ms)");

			return list;
		} catch (Exception e) {
			if (Logger.debug)
				System.err.println((pstmt != null ? pstmt.toString() : "null") + " - " + e);
			throw database.makeException(e, sql);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
				}
			}
			free(pstmt);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private List<?> select2Object(String sql, Object para[], DaoResult map) throws Exception {

		ResultSet r = null;
		PreparedStatement pstmt = null;

		try {

			pstmt = getPrepareStatement(sql);
			database.initPreparedStatement(pstmt);
			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			List list = makeResultList(r, map);

			return list;

		} catch (Exception e) {
			Logger.logger.fail("SQL[{}]PARA[{}]", sql, Arrays.toString(para));
			throw database.makeException(e, sql);
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (SQLException e) {
				}
			}
			free(pstmt);
		}
	}
}
