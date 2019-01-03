package subkjh.bas.dao.control;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.dao.data.SoDo;
import subkjh.bas.dao.data.TabData;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.model.ResultBean;
import subkjh.bas.dao.model.ResultMappingBean;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

/**
 * SQL 문을 처리하는 클래스
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
public class CommDao {

	public enum INSERT_TYPE {
		batch, oneByOne, oneByOneIgnoreExp;
	}

	enum RTYPE {
		map, object, resultmap;
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

	private DaoListener daoListener;

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
	 * @param closeRealConnection
	 *            POOL인 경우 실제 CONNECTION을 닫을지 여부
	 * @throws Exception
	 */
	protected void disconnect(boolean closeRealConnection) throws Exception {
		database.close(m_connection, closeRealConnection);
		m_connection = null;
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
	 * SQL문을 실행합니다.<br>
	 * Connection에 이상이 있으면 닫고 다시 시도합니다.
	 * 
	 * @param sql
	 *            실행할 SQL문
	 * @param obj
	 * @return
	 */
	public int executeSql(String sql, Object obj) throws Exception {
		return executeQidSql(null, sql, obj);
	}

	public int executeQidSql(String qid, String sql, Object obj) throws Exception {

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
	 * ResultSet를 map을 이용하여 target에 채운다.
	 * 
	 * @param r
	 * @param target
	 * @param map
	 * @return
	 * @throws Exception
	 */
	protected Object getBean(ResultSet r, Object target, ResultBean map) throws Exception {

		Field f;
		boolean isOk;
		List<ResultMappingBean> fields = map.getMappingFields();

		// 필드 속성이 없다면 배열로 넘긴다.
		if (fields == null || fields.size() == 0) {

			int colCnt = r.getMetaData().getColumnCount();

			Object entry[] = new Object[colCnt];
			for (int i = 1; i <= colCnt; i++) {
				entry[i - 1] = r.getObject(i);
			}
			return entry;

		} else {

			for (ResultMappingBean af : fields) {

				isOk = false;

				if (af.isJavaMethod()) {

					try {
						Method methods[] = target.getClass().getMethods();
						for (Method m : methods) {
							if (m.getName().equals(af.getJavaField()) && m.getParameterTypes().length == 1) {
								setMethod(target, m, r.getObject(af.getColumn()));
								isOk = true;
								break;
							}
						}
					} catch (Exception e) {
						throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : " + e.getMessage());
					}
				} else {
					f = target.getClass().getField(af.getJavaField());
					try {
						setField(target, f, r.getObject(af.getColumn()));
						isOk = true;
					} catch (Exception e) {
						throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : " + e.getMessage());
					}
				}

				if (isOk == false) {
					throw new Exception(af.getJavaField() + "-" + af.getColumn() + " : There is no method or field");
				}
			}
		}

		return target;
	}

	/**
	 * 컬럼목록을 제공합니다
	 * 
	 * @param r
	 * @param javaField
	 *            컬럼을 자바필드명으로 할지 여부 ( MO_NAME이냐 moName이냐 )
	 * @return 컬럼목록
	 * @throws Exception
	 */
	private String[] getColArr(ResultSet r) throws Exception {
		// 결과 필드 개수
		int colCnt = r.getMetaData().getColumnCount();
		if (colCnt == 0)
			return new String[0];

		List<String> colList = new ArrayList<String>();
		for (int i = 1; i <= colCnt; i++) {
			colList.add(r.getMetaData().getColumnLabel(i).toUpperCase());
		}
		String[] colArr = colList.toArray(new String[colList.size()]);

		if (Logger.debug)
			System.out.println("DEBUG : " + Arrays.toString(colArr));

		return colArr;
	}

	/**
	 * Connection을 얻는다.<br>
	 * 
	 * 미리 연결되어 있다면 그것을 넘기고 그렇치 않다면 새롭게 연결하여 넘긴다.
	 * 
	 * @return 연결된 Connection
	 * @throws Exception
	 */
	public Connection getConnection() throws IOException, Exception {
		if (m_connection == null) {
			return database.getConnection();
		} else
			return m_connection;
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
	 * ResultSet를 이용하여 값을 얻는 넘긴다.
	 * 
	 * @param r
	 * @param c
	 *            List를 구성하는 요소 c가 primitive일 때 결과의 컬럼이 1개이면 List의 요소가 되고, 컬럼이 1
	 *            이상이면 Map<String, Object>이 List의 요소가 되며, String은 컬럼명을 소문자로 한다.
	 * @param field
	 *            필드 매칭 값
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getDataToList(String qid, ResultSet r, ResultBean resultBean) throws Exception {

		List ret = new ArrayList();
		int size = 0;
		int rowNo = 0;
		long ptime = System.currentTimeMillis();

		try {
			Object entry = null;

			// 결과 필드 개수
			String colArr[] = getColArr(r);
			if (colArr.length == 0)
				return ret;

			if (daoListener != null) {
				daoListener.onStart(colArr);
			}

			while (r.next()) {

				size++;

				// primitive가 아니면 객체를 생성해서 그 객체에 값을 넣는다.
				if (SoDo.isPrimitive(resultBean.getJavaClass()) == false) {

					try {
						entry = resultBean.getJavaClass().newInstance();
					} catch (Exception e) {
						throw e;
					}

					if (entry instanceof Map) {
						entry = getMap(r, (Class<? extends Map>) resultBean.getJavaClass(), resultBean, colArr);
					} else {
						entry = getBean(r, entry, resultBean);
					}

				}
				// primitive이면 첫번째 내용을 그 값에 넣는다.
				else {

					if (colArr.length == 1) {
						if (resultBean.getJavaClass() == Long.class && r.getObject(1) != null) {
							entry = r.getLong(1);
						} else if (resultBean.getJavaClass() == Integer.class && r.getObject(1) != null) {
							entry = r.getInt(1);
						} else {
							entry = r.getObject(1);
						}
					} else {
						entry = getObjecArray(r, colArr.length);
					}
				}

				if (daoListener != null) {
					daoListener.onSelected(rowNo, entry);
				} else {
					ret.add(entry);
				}

				rowNo++;
				if (System.currentTimeMillis() > ptime + 5000) {
					ptime += 5000;
					if (Logger.logger != null && Logger.logger.isTrace()) {
						Logger.logger.trace("QID[" + qid + "] row count = " + rowNo);
					}
				}
			}

			if (daoListener != null) {
				daoListener.onFinish(null);
			}

			if (Logger.logger != null && Logger.logger.isTrace()) {
				Logger.logger.trace("COL-LENGTH(" + colArr.length + ")RESULT-CLASS("
						+ (resultBean == null ? "NULL" : resultBean.getJavaClass().getSimpleName()) + ")SIZE(" + size + ")");
			}

		} catch (Exception e) {

			if (daoListener != null) {
				daoListener.onFinish(e);
			}

			throw e;
		}

		return ret;
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
	 * Result내용으로 Map을 채운다.<br>
	 * 
	 * Map의 key는 SELECT문의 컴럼명이며 모두 소문자 처리한다.
	 * 
	 * @param colCnt
	 * @param r
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> getMap(ResultSet r, Class<? extends Map> classOfData, ResultBean map, String colArr[]) throws Exception {

		Map<String, Object> ret = classOfData.newInstance();
		
		List<ResultMappingBean> fields = map == null ? null : map.getMappingFields();

		if (fields == null || fields.size() == 0) {
			int colCnt = r.getMetaData().getColumnCount();
			for (int i = 1; i <= colCnt; i++) {

				if (Logger.debug)
					System.out.println("DEBUG : " + i + ") " + colArr[i - 1] + "=" + r.getObject(i));

				if (r.getObject(i) instanceof BigDecimal) {
					ret.put(colArr[i - 1], ((BigDecimal) r.getObject(i)).doubleValue());
				} else if (r.getObject(i) instanceof BigInteger) {
					ret.put(colArr[i - 1], ((BigInteger) r.getObject(i)).longValue());
				} else {
					ret.put(colArr[i - 1], r.getObject(i));
				}
			}
		} else {

			Object value;
			for (ResultMappingBean af : fields) {
				value = r.getObject(af.getColumn());

				if (value instanceof BigDecimal) {
					ret.put(af.getJavaField(), ((BigDecimal) value).longValue());
				} else if (value instanceof BigInteger) {
					ret.put(af.getJavaField(), ((BigInteger) value).longValue());
				} else {
					ret.put(af.getJavaField(), value);
				}

			}
		}

		return ret;
	}

	/**
	 * Object 배열로 넘긴다.
	 * 
	 * @param r
	 * @param colCnt
	 * @return
	 * @throws Exception
	 */
	private Object[] getObjecArray(ResultSet r, int colCnt) throws Exception {
		Object ret[] = new Object[colCnt];
		for (int i = 1; i <= colCnt; i++)
			ret[i - 1] = r.getObject(i);
		return ret;
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

	private List<Map<String, Object>> select2Map(String sql, Object para[]) throws Exception {

		long stime = System.currentTimeMillis();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResultSet r = null;
		PreparedStatement pstmt = null;
		Map<String, Object> entry;

		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			// 결과 필드 개수
			String colArr[] = getColArr(r);
			if (colArr.length == 0)
				return list;

			if (daoListener != null) {
				daoListener.onStart(colArr);
			}

			if (r != null) {
				while (r.next()) {
					entry = getMap(r, HashMap.class, null, colArr);
					list.add(entry);
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

	private List<Object[]> select2Obj(String qid, String sql, Object para[]) throws Exception {

		List<Object[]> list = new ArrayList<Object[]>();
		long ptime = System.currentTimeMillis();

		ResultSet r = null;
		PreparedStatement pstmt = null;
		int rowNo = 0;
		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			// 결과 필드 개수
			String colArr[] = getColArr(r);
			if (colArr.length == 0)
				return list;

			if (daoListener != null) {
				Logger.logger.debug(sql);
				daoListener.onStart(colArr);
			}

			if (r != null) {
				int colCnt = r.getMetaData().getColumnCount();
				Object entry[];
				while (r.next()) {
					entry = new Object[colCnt];
					for (int i = 1; i <= colCnt; i++) {
						entry[i - 1] = r.getObject(i);
					}

					rowNo++;

					if (daoListener != null) {
						daoListener.onSelected(rowNo, entry);
					} else {
						list.add(entry);
					}

					if (System.currentTimeMillis() > ptime + 5000) {
						ptime += 5000;
						if (Logger.logger != null && Logger.logger.isTrace()) {
							Logger.logger.trace("QID[" + qid + "] row count = " + rowNo);
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

	@SuppressWarnings({ "rawtypes" })
	private List<?> select2Res(String qid, String sql, Object para[], ResultBean map) throws Exception {

		ResultSet r = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = getPrepareStatement(sql);

			database.initPreparedStatement(pstmt);

			setPreSt(pstmt, para);
			r = pstmt.executeQuery();

			List list = getDataToList(qid, r, map);

			return list;

		} catch (Exception e) {
			Logger.logger.fail("QID[{}]SQL[{}]PARA[{}]", qid, sql, Arrays.toString(para));
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

				int colCnt = r.getMetaData().getColumnCount();
				List<String> columnNames = new ArrayList<String>();
				for (int i = 1; i <= colCnt; i++) {
					// columnNames.add(r.getMetaData().getColumnName(i));
					columnNames.add(r.getMetaData().getColumnLabel(i));
				}

				if (daoListener != null) {
					daoListener.onStart(columnNames.toArray(new String[columnNames.size()]));
				}

				Object entry[];
				while (r.next()) {
					entry = new Object[colCnt];
					for (int i = 1; i <= colCnt; i++) {
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

	protected List<?> selectSql(String qid, String sql, Object para[], RTYPE retType, ResultBean result)
			throws Exception {

		if (Logger.logger != null && Logger.logger.isTrace()) {
			Logger.logger.trace("{}, {}", retType, sql);
		}

		try {
			if (retType == RTYPE.object) {
				return select2Obj(qid, sql, para);
			} else if (retType == RTYPE.map) {
				return select2Map(sql, para);
			} else if (retType == RTYPE.resultmap) {
				return select2Res(qid, sql, para, result);
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
				if (retType == RTYPE.object) {
					return select2Obj(qid, sql, para);
				} else if (retType == RTYPE.map) {
					return select2Map(sql, para);
				} else if (retType == RTYPE.resultmap) {
					return select2Res(qid, sql, para, result);
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

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSql2Map(String sql, Object para[]) throws Exception {
		return (List<Map<String, Object>>) selectSql(sql, sql, para, RTYPE.map, null);
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
		return (List<Map<String, Object>>) selectSql(qid, sql, para, RTYPE.map, null);
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
		return (List<Object[]>) selectSql(sql, sql, para, RTYPE.object, null);
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
		return (List<Object[]>) selectSql(qid, sql, para, RTYPE.object, null);
	}

	public TabData selectSql2TabData(String sql, Object para[]) throws Exception {

		TabData tab = new TabData();

		long stime = System.currentTimeMillis();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResultSet r = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			r = pstmt.executeQuery();

			tab.setColArr(getColArr(r));

			if (r != null) {
				while (r.next()) {
					tab.addDataArr(getObjecArray(r, tab.getColArr().length));
				}
			}

			Logger.logger.debug(sql + "=" + list.size() + ", stime=" + (System.currentTimeMillis() - stime) + "(ms)");

			return tab;
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

	public void setDaoListener(DaoListener daoListener) {
		this.daoListener = daoListener;
	}

	/**
	 * 데이터 베이스를 설정한다.
	 * 
	 * @param database
	 *            사용할 데이터베이스
	 */
	public void setDatabase(DataBase database) {
		this.database = database;
	}

	/**
	 * 
	 * @param fetchSize
	 *            사용할 패치 크기
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	protected void setField(Object target, Field f, Object value) throws Exception {
		ObjectUtil.setField(target, f, value);
	}

	public void setInsertType(INSERT_TYPE insertType) {
		this.insertType = insertType;
	}

	protected void setMethod(Object target, Method method, Object value) throws Exception {
		try {
			ObjectUtil.setMethod(target, method, value);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	/**
	 * 
	 * @param pstmt
	 *            대상
	 * @param index
	 *            인덱스
	 * @param val
	 *            값
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
	 * @param pstmt
	 *            선언된 문장
	 * @param para
	 *            설정한 인수 목록
	 */
	public void setPreSt(PreparedStatement pstmt, Object para[]) throws Exception {

		if (para == null)
			return;

		for (int i = 0; i < para.length; i++) {
			setPreSt(pstmt, i + 1, para[i]);
		}
	}

	/**
	 * 로그를 남깁니다.
	 * 
	 * @param para
	 *            로그 남길 인수
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
}
