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

import subkjh.bas.dao.DaoCode;
import subkjh.bas.dao.data.SoDo;
import subkjh.bas.dao.database.Altibase;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.model.ResultBean;
import subkjh.bas.dao.model.ResultMappingBean;
import subkjh.bas.fxdao.beans.QueryResult;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class AbstractDaoExecutor {

	public enum EntryType {

		Result

		, ClassOfT

		, Map

		, QueryInfo

		, ObjectArray;
	}

	enum ExecuteType {
		Null

		, Collection

		, Array;

	}

	static final String RET_LOG = DaoCode.RET_LOG;

	static final String ERR_LOG = DaoCode.ERR_LOG;
	public static Map<String, Object> makePara(Object... parameters) {
		if (parameters == null || parameters.length < 2) {
			return null;
		}
		Map<String, Object> para = new HashMap<String, Object>();
		for (int i = 0; i < parameters.length; i += 2) {
			para.put(String.valueOf(parameters[i]), parameters[i + 1]);
		}

		return para;
	}

	/** 연결된 콘넥션 */
	private Connection connection = null;

	private DataBase database;

	private int fetchSize = 0;

	private boolean isConnected;

	public void commit() throws Exception {

		if (connection != null && connection.getAutoCommit() == false) {
			connection.commit();
		}
	}

	protected void connect() throws IOException, Exception {
		if (database == null)
			throw new Exception("database not set");
		connection = database.getConnection();
	}

	protected void disconnect(boolean closeRealConnection) throws Exception {
		database.close(connection, closeRealConnection);
		connection = null;
	}

	private int execute(String sql, Collection<Object[]> dataList) throws IOException, Exception {

		PreparedStatement pstmt = null;

		try {

			pstmt = getPrepareStatement(sql);

			// one by one
			if (dataList.size() == 1 || database instanceof Altibase) {
				int count = 0;
				for (Object v[] : dataList) {
					setPreSt(pstmt, v);
					count += pstmt.executeUpdate();
				}
				return count;
			}
			// batch
			else {
				for (Object v[] : dataList) {
					setPreSt(pstmt, v);
					pstmt.addBatch();
				}

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

		} catch (Exception e) {
			throw database.makeException(e, sql);
		} finally {
			if (pstmt != null)
				free(pstmt);
		}

	}

	private int execute(String sql, Object para[]) throws IOException, Exception {

		PreparedStatement pstmt = null;
		try {
			pstmt = getPrepareStatement(sql);
			setPreSt(pstmt, para);
			return pstmt.executeUpdate();

		} catch (Exception e) {
			throw database.makeException(e, sql);
		} finally {
			if (pstmt != null)
				free(pstmt);
		}

	}

	public int executeSql(String sql) throws Exception {
		return executeSql(sql, ExecuteType.Null, null);
	}

	protected int executeSql(String sql, Collection<Object[]> dataList) throws Exception {
		return executeSql(sql, ExecuteType.Collection, dataList);

	}

	@SuppressWarnings("unchecked")
	private int executeSql(String sql, ExecuteType type, Object obj) throws Exception {

		int count = -1;
		int tryIndex = 0;
		long ptime = System.currentTimeMillis();

		while (true) {

			try {
				if (type == ExecuteType.Array) {
					count = execute(sql, (Object[]) obj);
				} else if (type == ExecuteType.Collection) {
					count = execute(sql, (Collection<Object[]>) obj);
				} else {
					count = execute(sql, (Object[]) null);
				}

				break;
			} catch (IOException e) {
				tryIndex++;
				if (tryIndex >= database.getReconnectRetry()) {
					throw e;
				}

				try {
					database.close(connection, true);
				} catch (Exception e1) {
				}

				try {
					Thread.sleep(database.getReconnectWaitTimeSec() * 1000);
				} catch (InterruptedException ex) {
				}

				try {
					connection = database.getConnection();
				} catch (Exception ex) {
					continue;
				}

			} catch (Exception e) {
				Logger.logger.fail(RET_LOG, sql,
						(type == ExecuteType.Array ? Arrays.toString((Object[]) obj)
								: type == ExecuteType.Null ? "null" : "size=" + ((Collection<Object[]>) obj).size()),
						e.getMessage());

				throw e;
			}

		}

		Logger.logger.debug(RET_LOG, sql,
				(type == ExecuteType.Array ? Arrays.toString((Object[]) obj)
						: type == ExecuteType.Null ? "null" : "size=" + ((Collection<Object[]>) obj).size()),
				count, System.currentTimeMillis() - ptime);

		return count;
	}

	protected int executeSql(String sql, Object data[]) throws Exception {
		return executeSql(sql, ExecuteType.Array, data);
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
		if (connection != null && c.equals(connection))
			return;

		try {
			database.close(c, false);
		} catch (Exception ex) {
			throw database.makeException(ex, null);
		}
	}

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

	private String[] getColumnNameArray(ResultSet r) throws Exception {
		// 결과 필드 개수
		int colCnt = r.getMetaData().getColumnCount();
		if (colCnt == 0)
			return new String[0];

		List<String> colList = new ArrayList<String>();
		for (int i = 1; i <= colCnt; i++) {
			colList.add(r.getMetaData().getColumnLabel(i).toUpperCase());
		}
		String[] colArr = colList.toArray(new String[colList.size()]);

		return colArr;
	}

	public Connection getConnection() throws IOException, Exception {
		if (connection == null) {
			return database.getConnection();
		} else
			return connection;
	}

	/**
	 * 
	 * 
	 * @return 데이터베이스
	 */
	public DataBase getDatabase() {
		return database;
	}

	public int getFetchSize() {
		return fetchSize;
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
	private Map<String, Object> getMap(ResultSet r, ResultBean map, String colArr[]) throws Exception {

		Map<String, Object> ret = new HashMap<String, Object>();
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
	 * 
	 * @param sequence
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	public <T> T getNextVal(String sequence, Class<T> classOfT) throws Exception {
		List<T> list = selectSql(database.getSqlSequenceNextVal(sequence), null, classOfT);
		if (list != null && list.size() >= 1) {
			if (list.get(0) == null) {
				throw new Exception("Sequence(" + sequence + ") CHECK");
			}
			return list.get(0);
		}
		throw new Exception("No Datas");
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
	protected PreparedStatement getPrepareStatement(String sql) throws IOException, Exception {

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

	private Object[] getResult2Array(ResultSet r, int colCnt) throws Exception {
		Object ret[] = new Object[colCnt];
		for (int i = 1; i <= colCnt; i++)
			ret[i - 1] = r.getObject(i);
		return ret;
	}

	private boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}

		return o.toString().isEmpty();
	}

	private Object makeResult(ResultSet r, QueryResult info) throws Exception {

		Object target = info.getClassOfResult().newInstance();
		Field field;

		for (int i = 0; i < info.getResultFieldList().size(); i++) {
			field = info.getResultFieldList().get(i);
			try {
				setField(target, field, r.getObject(i + 1));
			} catch (Exception e) {

				// Number에 공백을 넣으려고 할 경우 무시한다.
				if (ObjectUtil.isNumber(field.getType()) && isEmpty(r.getObject(i + 1))) {
					continue;
				}

				throw new Exception(e.getClass().getName() + "(" + field.getName() + " : " + e.getMessage() + ")");
			}

		}

		return target;
	}

	public void rollback() {
		
		if (connection != null) {

			try {
				if (connection.getAutoCommit() == false) {
					Logger.logger.trace("rollback");
					connection.rollback();
				}
			} catch (SQLException e) {
				Logger.logger.error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> select(QueryResult info) throws Exception {
		return selectSql(info.getSql(), info.getParaArray(), EntryType.QueryInfo, info, Integer.MAX_VALUE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List select(String sql, Object para[], EntryType entryType, Object inPara, int size) throws Exception {

		long stime = System.currentTimeMillis();
		List list = new ArrayList();
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		Object entry;

		try {
			pstmt = getPrepareStatement(sql);

			setPreSt(pstmt, para);

			resultSet = pstmt.executeQuery();

			String colArr[] = getColumnNameArray(resultSet);

			if (resultSet != null) {

				if (entryType == EntryType.Map) {
					while (resultSet.next()) {
						entry = getMap(resultSet, null, colArr);
						list.add(entry);
						if (list.size() >= size)
							break;
					}
				} else if (entryType == EntryType.ObjectArray) {
					while (resultSet.next()) {
						Object[] o = getResult2Array(resultSet, colArr.length);
						list.add(o);
						if (Logger.debug) {
							if (list.size() % 100 == 1) {
								System.out.println("selectSql.count=" + list.size());
							}
						}
						if (list.size() >= size)
							break;
					}
				} else if (entryType == EntryType.Result) {

					ResultBean map = (ResultBean) inPara;
					Class<?> classOfT = map.getJavaClass();

					while (resultSet.next()) {

						// primitive가 아니면 객체를 생성해서 그 객체에 값을 넣는다.
						if (SoDo.isPrimitive(classOfT) == false) {

							try {
								entry = classOfT.newInstance();
							} catch (Exception e) {
								throw e;
							}

							if (entry instanceof Map) {
								entry = getMap(resultSet, map, colArr);
							} else {
								entry = getBean(resultSet, entry, map);
							}

						}
						// primitive이면 첫번째 내용을 그 값에 넣는다.
						else {

							if (colArr.length == 1) {
								if (classOfT == Long.class && resultSet.getObject(1) != null) {
									entry = resultSet.getLong(1);
								} else if (classOfT == Integer.class && resultSet.getObject(1) != null) {
									entry = resultSet.getInt(1);
								} else {
									entry = resultSet.getObject(1);
								}
							} else {
								entry = getResult2Array(resultSet, colArr.length);
							}
						}

						list.add(entry);
						if (list.size() >= size)
							break;
					}
				} else if (entryType == EntryType.QueryInfo) {
					while (resultSet.next()) {
						entry = makeResult(resultSet, (QueryResult) inPara);
						list.add(entry);
						if (list.size() >= size)
							break;
					}
				} else if (entryType == EntryType.ClassOfT) {

					Class<?> classOfT = (Class<?>) inPara;

					while (resultSet.next()) {

						// primitive가 아니면 객체를 생성해서 그 객체에 값을 넣는다.
						if (SoDo.isPrimitive(classOfT) == false) {

							try {
								entry = classOfT.newInstance();
							} catch (Exception e) {
								throw e;
							}

							if (entry instanceof Map) {
								entry = getMap(resultSet, null, colArr);
								list.add(entry);
							} else {
								// TODO
							}

						}
						// primitive이면 첫번째 내용을 그 값에 넣는다.
						else {

							if (colArr.length == 1) {
								if (classOfT == Long.class && resultSet.getObject(1) != null) {
									entry = resultSet.getLong(1);
								} else if (classOfT == Integer.class && resultSet.getObject(1) != null) {
									entry = resultSet.getInt(1);
								} else {
									entry = resultSet.getObject(1);
								}
							} else {
								entry = getResult2Array(resultSet, colArr.length);
							}
						}

						list.add(entry);
						if (list.size() >= size)
							break;
					}
				}
			}

			Logger.logger.debug(RET_LOG, sql, Arrays.toString(para), list.size(), (System.currentTimeMillis() - stime));

			return list;
		} catch (Exception e) {
			if (Logger.debug)
				System.err.println((pstmt != null ? pstmt.toString() : "null") + " - " + e);
			throw database.makeException(e, sql);
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
				}
			}
			free(pstmt);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> selectSql(String sql, Object para[]) throws Exception {
		return selectSql(sql, para, EntryType.ObjectArray, null, Integer.MAX_VALUE);
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> selectSql(String sql, Object para[], Class<T> classOfT) throws Exception {
		return selectSql(sql, para, EntryType.ClassOfT, classOfT, Integer.MAX_VALUE);
	}

	@SuppressWarnings("rawtypes")
	protected List selectSql(String sql, Object para[], EntryType entryType, Object inPara, int size) throws Exception {

		int tryIndex = 0;
		List list;

		while (true) {

			try {

				list = select(sql, para, entryType, inPara, size);
				break;

			} catch (IOException e) {
				tryIndex++;
				if (tryIndex >= database.getReconnectRetry()) {
					throw e;
				}

				try {
					database.close(connection, true);
				} catch (Exception e1) {
				}

				try {
					Thread.sleep(database.getReconnectWaitTimeSec() * 1000);
				} catch (InterruptedException ex) {
				}

				try {
					connection = database.getConnection();
				} catch (Exception ex) {
					continue;
				}

			} catch (Exception e) {
				throw e;
			}
		}

		return list;
	}

	public void setDatabase(DataBase database) {
		this.database = database;
	}

	protected void setField(Object target, Field f, Object value) throws Exception {
		ObjectUtil.setField(target, f, value);
	}

	protected void setMethod(Object target, Method method, Object value) throws Exception {
		ObjectUtil.setMethod(target, method, value);
	}

	protected void setPreSt(PreparedStatement pstmt, int index, Object val) throws Exception {
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

	protected void setPreSt(PreparedStatement pstmt, Object para[]) throws Exception {

		if (para == null)
			return;

		for (int i = 0; i < para.length; i++) {
			setPreSt(pstmt, i + 1, para[i]);
		}
	}

	public synchronized void start() throws IOException, Exception {
		try {
			connect();
			isConnected = true;
		} catch (Exception e) {
			isConnected = false;
			throw e;
		}
	}

	public void stop() {
		if (isConnected == false)
			return;

		isConnected = false;

		try {
			disconnect(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void trace(Object para[]) {

		if (para == null || para.length == 0)
			return;

		if (Logger.logger.isTrace()) {
			StringBuffer sb = new StringBuffer();
			if (para != null) {
				for (Object o : para) {
					sb.append(o + "|");
				}
				Logger.logger.trace(sb.toString());
			}
		}
	}
}
