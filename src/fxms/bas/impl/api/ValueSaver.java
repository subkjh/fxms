package fxms.bas.impl.api;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.dao.DaoExecutor;
import subkjh.dao.database.DataBase;

/**
 * 성능을 기록한다.<br>
 * 
 * 사용방법<br>
 * try { <br>
 * DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
 * <br>
 * catch (Exception e) {<br>
 * e.printStackTrace(); <br>
 * <br>
 * <br>
 * <br>
 * DataBase database = DaoFactory.getInstance().getDataBase("MKBSDB");<br>
 * 
 * ValueSaver c = new ValueSaver(database); <br>
 * <br>
 * c.ready("TEST", new String[] { "A1", "A2", "A3" }, new TYPE[] { TYPE.Long,
 * TYPE.String, TYPE.Double }); <br>
 * c.push(1, "aa", 2); <br>
 * c.push(1, "aa", 2); <br>
 * c.push(1, "aa", 2); <br>
 * c.push(1, "aa", 2);<br>
 * c.push(1, "aa", 2);<br>
 * <br>
 * <br>
 * c.action(); <br>
 * <br>
 * c.finish();<br>
 * <br>
 * 
 * 
 * @author 김종훈
 * 
 */
public class ValueSaver extends DaoExecutor {

	/**
	 * 자료형
	 * 
	 * @author subkjh
	 * 
	 */
	public enum TYPE {
		Double, Float, Integer, Long, String;
	}

	public static final int COUNT_MAX_BATCH = 1000;

	private PreparedStatement pstmt;
	/** 테이블명 */
	private String tableName;
	/** 컬럼의 자료형 */
	private TYPE types[];

	/**
	 * 
	 * @param database
	 *            사용할 데이터베이스
	 */
	public ValueSaver(DataBase database) {
		this.setDatabase(database);
	}

	/**
	 * 기록할 데이터를 추가합니다.
	 * 
	 * @param values
	 *            기록할 데이터
	 * @throws Exception
	 */
	public void addBatch(Object... valueArray) throws Exception {

		if (types.length != valueArray.length)
			throw new Exception(Lang.get("자료형과 값의 개수가 상이합니다.") + " : " + types.length + "," + valueArray.length);

		int size = valueArray.length;

		for (int i = 0; i < size; i++) {

			if (valueArray[i] == null) {
				if (types[i] == TYPE.Long)
					pstmt.setNull(i + 1, java.sql.Types.BIGINT);
				else if (types[i] == TYPE.String)
					pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
				else if (types[i] == TYPE.Double)
					pstmt.setNull(i + 1, java.sql.Types.DOUBLE);
				else if (types[i] == TYPE.Float)
					pstmt.setNull(i + 1, java.sql.Types.FLOAT);
				else if (types[i] == TYPE.Integer)
					pstmt.setNull(i + 1, java.sql.Types.INTEGER);
			} else {
				if (types[i] == TYPE.Long)
					pstmt.setLong(i + 1, ((Number) valueArray[i]).longValue());
				else if (types[i] == TYPE.String)
					pstmt.setString(i + 1, (String) valueArray[i]);
				else if (types[i] == TYPE.Double)
					pstmt.setDouble(i + 1, ((Number) valueArray[i]).doubleValue());
				else if (types[i] == TYPE.Float)
					pstmt.setFloat(i + 1, ((Number) valueArray[i]).floatValue());
				else if (types[i] == TYPE.Integer)
					pstmt.setInt(i + 1, ((Number) valueArray[i]).intValue());
			}
		}

		pstmt.addBatch();

	}

	/**
	 * 일반적인 데이터베이스인 경우 배치 실행
	 * 
	 * @return
	 */
	public int executeBatch() throws Exception {

		int cnt[];
		long ptime;
		int total = 0;

		ptime = System.currentTimeMillis();

		try {
			cnt = pstmt.executeBatch();
			for (int i = 0; i < cnt.length; i++) {
				if (cnt[i] > 0) {
					total += cnt[i];
				} else if (cnt[i] == Statement.SUCCESS_NO_INFO) {
					total++;
				}
			}

			Logger.logger.info(tableName + "|" + total + "|" + (System.currentTimeMillis() - ptime) + "ms");

			commit();

			return total;
		} catch (Exception e) {
			Logger.logger.error(e);

			try {
				pstmt.clearBatch();
			} catch (SQLException e1) {
			}

			rollback();

			throw e;
		}

	}

	/**
	 * 작업을 종료합니다.<br>
	 * 작업 후 반드시 이 메소드를 호출해야 합니다.
	 */
	public void finish() {

		try {
			if (pstmt != null)
				free(pstmt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.disconnect(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 입력된 데이터를 추가합니다.
	 * 
	 * @param valueArray
	 * @return
	 * @throws Exception
	 */
	public int insert(Object... valueArray) throws Exception {

		if (types.length != valueArray.length)
			throw new Exception(Lang.get("자료형과 값의 개수가 상이합니다.") + " : " + types.length + "," + valueArray.length);

		int size = valueArray.length;
		int ret;

		for (int i = 0; i < size; i++) {

			if (valueArray[i] == null) {
				if (types[i] == TYPE.Long)
					pstmt.setNull(i + 1, java.sql.Types.NUMERIC);
				if (types[i] == TYPE.String)
					pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
				if (types[i] == TYPE.Double)
					pstmt.setNull(i + 1, java.sql.Types.NUMERIC);
				if (types[i] == TYPE.Float)
					pstmt.setNull(i + 1, java.sql.Types.NUMERIC);
				if (types[i] == TYPE.Integer)
					pstmt.setNull(i + 1, java.sql.Types.NUMERIC);
			} else {
				if (types[i] == TYPE.Long)
					pstmt.setLong(i + 1, ((Number) valueArray[i]).longValue());
				if (types[i] == TYPE.String)
					pstmt.setString(i + 1, (String) valueArray[i]);
				if (types[i] == TYPE.Double)
					pstmt.setDouble(i + 1, ((Number) valueArray[i]).doubleValue());
				if (types[i] == TYPE.Float)
					pstmt.setFloat(i + 1, ((Number) valueArray[i]).floatValue());
				if (types[i] == TYPE.Integer)
					pstmt.setInt(i + 1, ((Number) valueArray[i]).intValue());
			}
		}

		try {
			ret = pstmt.executeUpdate();
			commit();
			return ret;
		} catch (SQLException e) {
			throw getDatabase().makeException(e, "");
		}

	}

	/**
	 * 기록할 데이터에 대해서 테이블, 컬럼, 각 컬럼의 자료형을 설정합니다.
	 * 
	 * @param tableName
	 *            테이블명
	 * @param columns
	 *            컬럼
	 * @param types
	 *            컬럼의 자료형
	 * @throws Exception
	 */
	public void prepare(String tableName, String columns[], TYPE types[]) throws Exception {
		prepare(tableName, columns, types, false);
	}

	/**
	 * 기록할 데이터에 대해서 테이블, 컬럼, 각 컬럼의 자료형을 설정합니다.
	 * 
	 * @param tableName
	 * @param columns
	 * @param types
	 * @param truncate
	 *            테이블을 truncate할지 여부
	 * @throws Exception
	 */
	public void prepare(String tableName, String columns[], TYPE types[], boolean truncate) throws Exception {

		this.tableName = tableName;

		connect();

		if (truncate) {
			String sql = "truncate table " + tableName;
			int ret = executeSql(sql);
			Logger.logger.debug(sql + "=" + ret);
		}

		StringBuffer sb = new StringBuffer();

		sb.append("insert into " + tableName + " ( ");
		sb.append(columns[0]);
		for (int i = 1; i < columns.length; i++) {
			sb.append(", " + columns[i]);
		}
		sb.append(" ) values ( ");
		sb.append("?");
		for (int i = 1; i < columns.length; i++) {
			sb.append(", ?");
		}
		sb.append(" )");

		pstmt = getPrepareStatement(sb.toString());

		Logger.logger.debug(sb.toString());

		this.types = types;

	}

	String makeString(Object... valueArray) {
		StringBuffer sb = new StringBuffer();

		if (valueArray.length > 0)
			sb.append(valueArray[0]);
		for (int i = 1; i < valueArray.length; i++) {
			sb.append(",");
			sb.append(valueArray[i]);
		}
		sb.append(".");

		return sb.toString();
	}
}
