package subkjh.dao.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.DaoExecutor;
import subkjh.dao.QidDao;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.Sequence;
import subkjh.dao.def.Table;
import subkjh.dao.def.View;
import subkjh.dao.model.QidPool;
import subkjh.dao.model.SqlConst;
import subkjh.dao.queries.QueriesQid;

/**
 * 데이터베이스 정보
 * 
 * @author subkjh
 * @since 2007-01-01
 * 
 */
public abstract class DataBase implements Serializable {

	class TranBean {

		private Connection connection;

		/** 인덱스 */
		private int index;

		private long mstime;

		/** 사용중인 스레드명 */
		private String threadName;

		/** 사용중 여부 */
		private boolean use;

		public TranBean(int index, boolean use) {
			this.index = index;
			this.use = use;
		}

		/**
		 * 
		 * @param c
		 * @return Connection이 같은지 여부
		 */
		public boolean equals(Connection c) {
			if (c == null || connection == null)
				return false;
			return c.equals(connection);
		}

		public Connection getConnection() {
			return connection;
		}

		public int getIndex() {
			return index;
		}

		public String getThreadName() {
			return threadName;
		}

		public boolean isUse() {
			return use;
		}

		public void setConnection(Connection connection) {
			mstime = System.currentTimeMillis();
			this.connection = connection;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setThreadName(String threadName) {
			this.threadName = threadName;
		}

		public void setUse(boolean use) {
			this.use = use;
		}

		@Override
		public String toString() {
			return index + "|" + (use ? "Y" : "N") //
					+ (threadName == null ? "" : "|" + threadName) //
					+ (mstime > 0 ? "|" + Logger.getDate(mstime) : "");

		}
	}

	protected static final String CONST_NVL = "NVL";
	protected static final String CONST_TRUNC = "TRUNC";
	protected static final String COUNT_CONNECTIO_NMAX = "countConnectionMax";
	protected static final String DRIVER = "driver";
	protected static final String IS_AUTO_COMMIT = "isAutoCommit";
	protected static final String IS_READONLY = "isReadOnly";
	protected static final String PASSWORD = "password";
	protected static final String PERMIT_CONNECTION_POOL_OVER = "permitConnectionPoolOver";
	protected static final String RECONNEC_TTIMEOUT = "reconnectTimeout";
	protected static final String RECONNECT_TRY_COUNT = "reconnectTryCount";
	protected static final String SECONDS_WAIT_POOL = "secondsWaitPool";
	protected static final String SQL_SELECT_KEEP_ALIVE = "sqlSelectKeepAlive";
	protected static final String TIMEOUT_LOGIN = "timeoutLogin";
	protected static final String URL = "url";
	protected static final String USER = "user";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2425262432658747569L;

	protected final String PATTERN = "(:|/)+";

	protected transient QueriesQid QID = new QueriesQid();
	/** 자동 커밋 여부. 기본값을 true이다. */
	private boolean autoCommit = false;
	private Map<String, SqlConst> constMap;
	/** 최대 연결 수. 0이거나 작으면 무제한 */
	private int countConnectionMax = 5;
	/** DB명 */
	private String dbName;
	/** 드라이버 */
	private String driver;
	private String ipAddress;
	private Map<String, Object> map;
	/** 사용명 */
	private String name;
	/** 암호 */
	private String password;
	/** 연결 수가 pool을 넘을 경우도 새롭게 생성할 지 여부 */
	private boolean permitConnectionPoolOver = true;
	private int port;
	/** 읽기 전용 여부 */
	private boolean readOnly = false;
	/** 재연결 시도 회수 */
	private int reconnectRetry = 3;
	/** 재연결이 필요할 때 대기 시간(초) */
	private int reconnectWaitTimeSec = 5;
	/** POLL에서 사용가능한 자원이 생길때까지 대기 시간(초) */
	private int secondsWaitPool = 10;
	/** 컨넥션을 제공할 때 ONLINE 여부 확인할 쿼리 */
	private String sqlSelectKeepAlive = null;
	/** 타임아웃 로그인 */
	private int timeoutLogin = 10;
	/** Connection 목록 */
	private TranBean tranArray[];
	/** URL */
	private String url;
	/** USER */
	private String user;

	public DataBase() {
		tranArray = new TranBean[0];
	}

	/**
	 * 상수를 추가합니다.
	 * 
	 * @param id
	 * @param val
	 */
	public void addConst(String id, String val) {

		if (constMap == null) {
			constMap = new HashMap<String, SqlConst>();
		}

		SqlConst sc = new SqlConst();
		sc.id = id;
		sc.text = val;
		sc.database = getKind();
		constMap.put(sc.id, sc);
	}

	/**
	 * 정상적으로 설정되었는지 확인합니다.
	 * 
	 * @throws Exception 정상적이지 않은 경우에 예외가 발생합니다.
	 */
	public void check() throws Exception {

		if (url == null || url.length() == 0)
			throw new Exception("url is empty");
		if (name == null || name.length() == 0)
			throw new Exception("database name is empty");

		if (countConnectionMax > 0) {
			tranArray = new TranBean[countConnectionMax];
			for (int i = 0; i < tranArray.length; i++) {
				tranArray[i] = new TranBean(i, false);
			}
		}

		// String msg = name + "|" + url + "|" + "PCPO=" +
		// permitConnectionPoolOver //
		// + ",RO=" + readOnly //
		// + ",AC=" + autoCommit //
		// + ",CCM=" + countConnectionMax //
		// + ",RR=" + reconnectRetry //
		// + ",RWTS=" + reconnectWaitTimeSec //
		// + ",SWP=" + secondsWaitPool //
		// + "," + constMap;

//		Logger.logger.info(Logger.makeString("DATABASE " + name, "CHECKED"));
	}

	/**
	 * POOL에 존재하는 Connection이면 사용여부만 해제하고 존재하지 않으면 실제 Connection.close()을 호출합니다.
	 * 
	 * @param connection
	 * @param closeRealConnection POOL인 경우 실제 CONNECTION을 닫을지 여부
	 * @throws Exception
	 */
	public void close(Connection connection, boolean closeRealConnection) throws Exception {

		if (connection == null)
			return;

		boolean isConnectionInPool = false;

		for (TranBean tran : tranArray) {

			if (tran.equals(connection)) {

				if (isAutoCommit() == false && connection.isClosed() == false) {
					try {
						if (connection != null)
							connection.commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				tran.setUse(false);

				isConnectionInPool = true;
				break;
			}
		}

		if (isConnectionInPool && closeRealConnection) {
			closeConnection(connection);
		}

		if (isConnectionInPool == false) {
			Logger.logger.trace("connection.close()");
			if (connection != null) {
				Logger.logger.debug("Disconnected [" + connection + "]");
				connection.close();
			}
		}
	}

	public void closePool() {

		long ptime;

		for (TranBean tran : tranArray) {
			ptime = System.currentTimeMillis();
			while (tran.isUse()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ptime + 5000 < System.currentTimeMillis()) {
					Logger.logger.debug("Using : " + tran.getThreadName());
					ptime = System.currentTimeMillis();
				}
			}
			try {
				if (tran.getConnection() != null)
					tran.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			tran.setConnection(null);
			tran.setUse(false);
		}

	}

	public ClassDao createClassDao(String... sqlFiles) throws Exception {
		ClassDao tran = new ClassDao(this, sqlFiles);
		return tran;
	}

	public DaoExecutor createDaoExecutor(String... sqlFiles) throws Exception {
		DaoExecutor tran = new DaoExecutor(this, sqlFiles);
		return tran;
	}

	public QidDao createQidDao() {
		QidDao tran = new QidDao();
		tran.setDatabase(this);
		return tran;
	}

	public QidDao createQidDao(InputStream inputStream) throws Exception {
		QidPool sqlPool = new QidPool(getKind(), inputStream);
		QidDao tran = new QidDao(sqlPool);
		tran.setDatabase(this);
		return tran;
	}

	public QidDao createQidDao(String... qidFileArr) throws Exception {
		QidDao tran = new QidDao(this, qidFileArr);
		return tran;
	}

	/**
	 * 테이블에 인덱스가 존재하는지 확인 합니다.<br>
	 * 데이터베이스의 종류에 따라 테이블명은 null일 수 있습니다. 이 경우는 인덱스명이 데이터베이스에서 유일해야 하는 경우 입니다. <br>
	 * 
	 * @param tableName 테이블명
	 * @param indexName 인덱스명
	 * @return 존재여부
	 * @throws Exception
	 */
	public boolean existIndex(QidDao t, String tableName, String indexName) throws Exception {
		List<Index> idxList = getIndexList(t, tableName);
		for (Index idx : idxList) {
			if (idx.getIndexName().equals(indexName))
				return true;
		}
		return false;
	}

	public boolean existTable(DaoExecutor tran, String tableName) throws Exception {
		try {
			Map<String, Object> para = makePara(tableName);
			List<String> list = tran.select(String.class, QID.SELECT_TABLE_NAME__BY_NAME, para);
			return list != null && list.size() == 1;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 테이블의 존재 여부를 확인합니다.
	 * 
	 * @param tableName 테이블명
	 * @return 존재여부
	 * @throws Exception
	 */
	public boolean existTable(QidDao t, String tableName) throws Exception {
		QidDao tran = (t != null ? t : createQidDao());

		try {
			if (t == null)
				tran.start();

			Map<String, Object> para = makePara(tableName);

			List<?> list = (List<?>) tran.selectQid2Res(QID.SELECT_TABLE_NAME__BY_NAME, para);
			return list != null && list.size() == 1;

		} catch (Exception e) {
			throw e;
		} finally {
			if (t == null)
				tran.stop();
		}
	}

	public boolean existTable(String tableName) throws Exception {
		DaoExecutor tran = createDaoExecutor();
		try {
			tran.start();
			return existTable(tableName);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 각 데이터베이스에서 사용할 컬럼의 데이터 Type를 가져옵니다.
	 * 
	 * @param col 컬럼
	 * @return 쿼리용 데이터 타입
	 */
	public String getColumnDataType(Column col) {
		if (col.getDataScale() <= 0) {
			return col.getDataType() + "(" + col.getDataLength() + ")";
		} else {
			return col.getDataType() + "(" + col.getDataLength() + "," + col.getDataScale() + ")";
		}
	}

	/**
	 * 테이블을 구성하는 컬럼 목록을 제공합니다.
	 * 
	 * @param t         사용할 트랜잭션. null이면 새롭게 연결한 후 조회하고 다시 닫음
	 * @param tableName 테이블명
	 * @return 컬럼목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getColumnList(QidDao t, String tableName) throws Exception {

		Map<String, Object> para = makePara(tableName);

		QidDao tran = (t != null ? t : createQidDao());

		try {
			if (t == null)
				tran.start();
			return (List<String>) tran.selectQid2Res(QID.SELECT_COLUMN_NAME__BY_TABLE, para);
		} catch (Exception e) {
			throw e;
		} finally {
			if (t == null)
				tran.stop();
		}
	}

	/**
	 * POOL에 있는 Connection을 제공합니다.<br>
	 * POOL 크기가 0이면 새롭게 생성해서 제공합니다.
	 * 
	 * @return 콘넥션
	 * @throws ConPollOverException , Exception
	 */
	public synchronized Connection getConnection() throws IOException, Exception {

		Connection c = null;

		if (countConnectionMax <= 0)
			return createConnection();

		long ptime = System.currentTimeMillis();

		while (true) {

			for (int i = 0; i < tranArray.length; i++) {

				if (tranArray[i].isUse() == false) {

					c = tranArray[i].getConnection();
					try {
						if (isOkConection(c) == false) {
							tranArray[i].setConnection(null);
							c = createConnection();
							tranArray[i].setConnection(c);
						}
					} catch (Exception e) {
						Logger.logger.error(e);
						throw e;
					}

					tranArray[i].setUse(true);
					tranArray[i].setThreadName(Thread.currentThread().getName());

					return c;

				}
			}

			if (permitConnectionPoolOver) {
				return createConnection();
			}

			if (secondsWaitPool <= 0 || System.currentTimeMillis() > ptime + secondsWaitPool * 1000L) {
				break;
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		String msg = "";
		for (int i = 0; i < tranArray.length; i++) {
			msg += " " + tranArray[i] + "";
		}

		throw new IOException("Connection fail. ERRMSG:" + msg.trim());
	}

	public Map<String, SqlConst> getConstMap() {
		return constMap;
	}

	/**
	 * 
	 * @return 풀 내 컨넥션 개수
	 */
	public int getCountConnectionMax() {
		return countConnectionMax;
	}

	/**
	 * 컬럼의 자료형, 길이가 포함된 내역을 제공합니다.<br>
	 * 예) varchar2(10), number(10), number(10, 2)<br>
	 * 
	 * @param column
	 * @return
	 */
	public String getDataTypeFull(Column column) {
		return column.getDataType2Define();
	}

	public String getDbName() {
		return dbName;
	}

	public String getDebug() {

		StringBuffer sb = new StringBuffer();
		sb.append("DB(");
		sb.append("NAME(" + name + ")");
		sb.append("URL(" + url + ")");
		sb.append(")");

		return sb.toString();
	}

	/**
	 * 
	 * @return 드라이버
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * 테이블의 인덱스 목록을 조회합니다.
	 * 
	 * @param tableName 테이블 명
	 * @return 인덱스 목록
	 * @throws Exception
	 */
	public List<Index> getIndexList(QidDao t, String tableName) throws Exception {

		QidDao tran = (t != null ? t : createQidDao());

		try {
			if (t == null)
				tran.start();

			Map<String, Object> para = makePara(tableName);

			Map<String, Index> map = new HashMap<String, Index>();

			List<Object[]> list = null;
			try {
				list = tran.selectQid2Obj(QID.QID_SELECT_TABLE_INDEX, para);
			} catch (Exception e) {
				throw e;
			}

			for (Object[] row : list) {
				Index index = map.get(row[0]);
				if (index == null) {
					index = new Index(row[0].toString(), INDEX_TYPE.getType(row[1].toString()));
					index.setColumns(row[2].toString());
					map.put(index.getIndexName(), index);
				} else {
					index.addColumn(row[2].toString());
				}
			}

			return new ArrayList<Index>(map.values());
		} catch (Exception e) {
			throw e;
		} finally {
			if (t == null)
				tran.stop();
		}
	}

	/**
	 * 
	 * @return IP주소
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * 데이터베이스 컬럼에 매칭되는 자바 객체 타입을 제공합니다.
	 * 
	 * @param column
	 * @return 자바객체 타입
	 */
	public Class<?> getJavaType(Column column) {
		return column.makeFieldType();
	}

	/**
	 * 연결된 데이터베이스의 종류를 제공합니다.<br>
	 * 소문자로 제공합니다.
	 * 
	 * @return 데이터베이스 종류
	 */
	public String getKind() {
		return getClass().getSimpleName().toLowerCase();
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return 접속 암호
	 */
	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public Object getProperty(String key) {
		return map != null ? map.get(key) : null;
	}

	/**
	 * @return 연결이 끊긴 경우 재 연결 시도 횟수
	 */
	public int getReconnectRetry() {
		return reconnectRetry;
	}

	/**
	 * @return 재 연결 시도 후 다시 시도 할 때까지 대기 시간
	 */
	public int getReconnectWaitTimeSec() {
		return reconnectWaitTimeSec;
	}

	/**
	 * 
	 * @return 풀내 컨넥션 대기 시간(초)
	 */
	public int getSecondsWaitPool() {
		return secondsWaitPool;
	}

	/**
	 * 데이터베이스의 시퀀스 목록을 조회합니다.
	 * 
	 * @return 시궠스 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Sequence> getSequenceList(QidDao t) throws Exception {
		QidDao tran = (t != null ? t : createQidDao());
		try {
			if (t == null)
				tran.start();
			Map<String, Object> para = makePara(null);

			return (List<Sequence>) tran.selectQid2Res(QID.SELECT_SEQUENCE__ALL, para);
		} catch (Exception e) {
			throw e;
		} finally {
			if (t == null)
				tran.stop();
		}

	}

	/**
	 * 테이블에 컬럼을 추가하는 쿼리 제공
	 * 
	 * @param tableName 테이블명
	 * @return 쿼리
	 */
	public String getSqlAdd(Column column, String tableName) {
		return "alter table " + tableName + " add column " + getSqlCreate(column);
	}

	/**
	 * 커멘트 쿼리를 생성합니다.
	 * 
	 * @param tableName 테이블명
	 * @return 커맨트 쿼리
	 */
	public String getSqlComment(Column column, String tableName) {
		return "comment on column " + makeColSize(tableName + "." + column.getName(), 48) + " is '"
				+ column.getComments() + "';" + "\n";
	}

	/**
	 * 
	 * @return 코멘트 목록
	 * @throws Exception
	 */
	public List<String> getSqlComment(Table table) throws Exception {

		List<String> list = new ArrayList<String>();

		list.add("comment on table  " + makeColSize(table.getName(), 48) + " is '" + table.getComment() + "'");

		for (Column col : table.getColumns()) {
			list.add("comment on column " + makeColSize(table.getName() + "." + col.getName(), 48) + " is '"
					+ col.getComments() + "'");
		}

		return list;
	}

	/**
	 * 컬럼 정의 쿼리
	 * 
	 * @param column
	 * @return
	 */
	public String getSqlCreate(Column column) {

		String defaultValue = checkDefaultValue(column.getDataDefault());

		StringBuffer sb = new StringBuffer();
		sb.append(makeColSize(column.getName(), 30));
		sb.append(" ").append(getDataTypeFull(column));

		if (isEmpty(defaultValue) == false) {
			sb.append(" default ");
			if (column.getFieldType() == String.class || column.getFieldType() == boolean.class) {
				sb.append("'").append(defaultValue).append("'");
			} else {
				sb.append(defaultValue);
			}
		}
		if (column.isNullable() == false) {
			sb.append(" not null ");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param idx 생성할 인데스
	 * @param tab 관련 테이블
	 * @return 인덱스 생성 쿼리
	 */
	public String getSqlCreate(Index idx, Table tab) {

		String ret = getSqlCreate0(idx, tab);

		if (tab.isDefTsIdx()) {
			return ret + " tablespace " + tab.getTableSpaceIndex();
		} else {
			return ret;
		}
	}

	/**
	 * 시퀀스를 생성하는 쿼리
	 * 
	 * @param seq 시퀀스
	 * @return 시퀀스를 생성하는 쿼리
	 */
	public String getSqlCreate(Sequence seq) {
		return "create sequence " + makeColSize(seq.getSequenceName(), 32) + " increment by " + seq.getIncBy()
				+ " maxvalue " + seq.getValueMax() + " minvalue " + seq.getValueMin() + (seq.isCycle() ? " cycle" : "")
				+ " cache 20";
	}

	/**
	 * 테이블 생성용 쿼리를 만듭니다.
	 * 
	 * @param table 테이블
	 * @return 테이블 생성 쿼리
	 * @throws Exception
	 */
	public String getSqlCreate(Table table) throws Exception {

		String ret = getSqlCreate0(table);

		if (table.isDefTsData()) {
			return ret + " tablespace " + table.getTableSpaceData();
		}

		return ret;
	}

	/**
	 * 뷰를 생성하는 쿼리
	 * 
	 * @param v
	 * @return
	 */
	public String getSqlCreate(View v) {
		return "create or replace view " + makeColSize(v.getName(), 32) + " as \n" + v.getQuery();
	}

	public List<String> getSqlCreateIndex(Table table) {

		List<String> list = new ArrayList<String>();

		for (Index idx : table.getIndexList()) {

			if (idx.isPk()) {
			} else if (idx.isFk()) {
			} else {
				list.add(getSqlCreate(idx, table));
			}
		}

		return list;
	}

	/**
	 * 테이블에서 컬럼을 제거하는 쿼리
	 * 
	 * @param column
	 * @param tableName
	 * @return
	 */
	public String getSqlDelete(Column column, String tableName) {
		return "alter table " + tableName + " drop column " + column.getName();
	}

	/**
	 * 테이블의 인덱스 또는 제약조건을 삭제합니다.
	 * 
	 * @param idx
	 * @param tableName
	 * @return
	 */
	public String getSqlDrop(Index idx, String tableName) {
		if (idx.isFk()) {
			return "alter table " + tableName + " drop constraint " + idx.getIndexName();
		} else {
			return "drop index " + idx.getIndexName();
		}
	}

	/**
	 * 시퀀스를 삭제하는 쿼리
	 * 
	 * @param seq
	 * @return 시퀀스를 삭제하는 쿼리
	 */
	public String getSqlDrop(Sequence seq) {
		return "drop sequence " + seq.getSequenceName();
	}

	/**
	 * 
	 * @param table 테이블
	 * @return 테이블 삭제 쿼리
	 */
	public String getSqlDrop(Table table) {
		return "drop table " + table.getName();
	}

	public String getSqlSelectKeepAlive() {
		return sqlSelectKeepAlive;
	}

	public String getSqlSequenceNextVal(String sequence) {
		return " select " + sequence + ".nextval from dual";
	}

	/**
	 * 테이블에 컬럼을 수정하는 쿼리
	 * 
	 * @param column
	 * @param tableName
	 * @return
	 */
	public String getSqlUpdate(Column column, String tableName) {
		return "alter table " + tableName + " modify column " + getSqlCreate(column);
	}

	/**
	 * 테이블의 컬럼, 인덱스 정보를 조회합니다.
	 * 
	 * @param tran      트랜잭션
	 * @param tableName 테이블명
	 * @return 테이블 정보
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Table getTable(QidDao t, String tableName) throws Exception {

		QidDao tran = (t != null ? t : createQidDao());

		Map<String, Object> para = makePara(tableName);

		Table table = new Table();
		table.setName(tableName);

		try {

			if (t == null)
				tran.start();

			try {
				String comment = tran.selectQidStr(QID.QID_SELECT_TABLE_COMMENT, para);
				table.setComment(comment);
			} catch (Exception e) {
				table.setComment("");
			}

			List<Column> columns = null;

			columns = tran.selectQid2Res(QID.QID_SELECT_TABLE_INFO, para);
			for (Column c : columns) {
				c.setFieldType(getJavaType(c));
				c.setDataTypeDefined(getColumnDataType(c));
			}
			table.setColumns(columns);

			table.setIndexes(getIndexList(tran, tableName));

			return table;

		} catch (Exception e1) {
			throw e1;
		} finally {
			if (t == null)
				tran.stop();
		}

	}

	/**
	 * 
	 * @param t
	 * @param tabNameArr
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTableList(QidDao t, String... tabNameArr) throws Exception {

		QidDao tran = (t != null ? t : createQidDao());

		try {
			if (t == null)
				tran.start();
			List<Table> tabList = new ArrayList<Table>();
			for (String name : tabNameArr) {
				try {
					tabList.add(getTable(tran, name));
				} catch (Exception e) {
					tabList.add(null);
				}
			}
			return tabList;
		} catch (Exception e1) {
			throw e1;
		} finally {
			if (t == null)
				tran.stop();
		}
	}

	/**
	 * 전체 테이블 명을 조회합니다.
	 * 
	 * @return 테이블명 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTabNameList(QidDao t, String likeName) throws Exception {

		QidDao tran = (t != null ? t : createQidDao());
		try {
			if (t == null)
				tran.start();
			Map<String, Object> para = makePara(likeName);

			if (likeName == null || likeName.trim().length() == 0) {
				return (List<String>) tran.selectQid2Res(QID.SELECT_TABLE_NAME__ALL, para);
			} else {
				return (List<String>) tran.selectQid2Res(QID.SELECT_TABLE_NAME__LIKE, para);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (t == null)
				tran.stop();
		}
	}

	public int getTimeoutLogin() {
		return timeoutLogin;
	}

	/**
	 * 
	 * @return 접속 URL
	 */
	public String getUrl() {
		if (url == null) {
			url = makeUrl(null);
		}
		return url;
	}

	/**
	 * 
	 * @return 접속 운용자 ID
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 데이터베이스의 현재 버전을 조회합니다.
	 * 
	 * @return 버전
	 * @throws Exception
	 */
	public String getVerDb() throws Exception {
		QidDao tran = createQidDao();

		try {
			tran.start();
			Object version = tran.selectQidObj(QID.SELECT_VERSION, null);
			if (version == null)
				return "";
			return version.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 뷰 목록을 조회합니다.
	 * 
	 * @return 뷰 목록
	 * @throws Exception
	 */
	public List<View> getViewList() throws Exception {

		QidDao tran = createQidDao();
		try {
			tran.start();
			return getViewList(tran);

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 입력된 트랜잭션을 이용하여 뷰를 조회합니다.
	 * 
	 * @param tran 트랜잭션
	 * @return 뷰 목록
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<View> getViewList(QidDao tran) throws Exception {

		Map<String, Object> para = makePara(null);

		return (List<View>) tran.selectQid2Res(QID.SELECT_VIEW__ALL, para);
	}

	/**
	 * 시퀀스를 가지고 있는 데이터베이스인지 여부
	 * 
	 * @return 시퀀스를 가지고 있는 데이터베이스인지 여부
	 */
	public boolean hasSequence() {
		return true;
	}

	/**
	 * PreparedStatement에 대한 기초 작업을 수행합니다.
	 * 
	 * @param ps
	 */
	public void initPreparedStatement(PreparedStatement ps) throws SQLException {

	}

	/**
	 * 
	 * @return 자동 커밋 여부
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * 
	 * @return 풀 오버인 경우 새로운 컨넥션 생성 여부
	 */
	public boolean isPermitConnectionPoolOver() {
		return permitConnectionPoolOver;
	}

	/**
	 * @return 읽기 전용 여부
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * 
	 * @return 계정 필요 여부
	 */
	public boolean isUseUserPass() {
		return true;
	}

	public abstract Exception makeException(Exception e, String msg);

	/**
	 * 입력된 인수를 이용하여 URL으을 만듭니다.
	 * 
	 * @param para 인수
	 * @return URL
	 */
	public abstract String makeUrl(Map<String, Object> para);

	/**
	 * 데이터베이스 종류에 따라 연결할 때 사용한 URL을 만듭니다.
	 * 
	 * @param ipAddress IP주소
	 * @param port      포트
	 * @param para      기타 속성값
	 * @return URL 문자열
	 */
	public String makeUrl(String ipAddress, int port, Map<String, Object> para) {
		setIpAddress(ipAddress);
		setPort(port);
		return makeUrl(para);
	}

	public String makeXml() {

		// <!ELEMENT database (driver,url,user?,password?,isReadOnly?
		// ,countConnectionMax,isAutoCommit?,permitConnectionPoolOver?,reconnectTimeout?,sqlSelectKeepAlive?,const*)>

		StringBuffer sb = new StringBuffer();
		sb.append("<database name=\"" + name + "\">\n");
		if (getDriver() != null)
			sb.append("<driver>" + getDriver() + "</driver>\n");
		if (getDbName() != null)
			sb.append("<dbname>" + getDbName() + "</dbname>\n");
		if (getUrl() != null)
			sb.append("<url>" + getUrl() + "</url>\n");
		if (getUser() != null)
			sb.append("<user>" + getUser() + "</user>\n");
		if (getPassword() != null)
			sb.append("<password>" + getPassword() + "</password>\n");
		sb.append("<isReadOnly>" + isReadOnly() + "</isReadOnly>\n");
		sb.append("<countConnectionMax>" + countConnectionMax + "</countConnectionMax>\n");
		sb.append("<isAutoCommit>" + isAutoCommit() + "</isAutoCommit>\n");
		sb.append("<permitConnectionPoolOver>" + permitConnectionPoolOver + "</permitConnectionPoolOver>\n");
		sb.append("<reconnectTimeout>" + reconnectWaitTimeSec + "</reconnectTimeout>\n");
		sb.append("<isAutoReconnect>" + isPermitConnectionPoolOver() + "</isAutoReconnect>\n");
		sb.append("<reconnectTryCount>" + reconnectRetry + "</reconnectTryCount>\n");

		if (constMap != null) {
			for (String key : constMap.keySet()) {
				sb.append("<const id=\"" + key + "\">" + constMap.get(key).text + "</const>\n");
			}
		}

		sb.append("</database>\n");

		return sb.toString();
	}

	/**
	 * 자동 커밋 여부를 설정합니다.
	 * 
	 * @param autoCommit
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * 
	 * @param countConnectionMax 풀 컨넥션 개수
	 */
	public void setCountConnectionMax(int countConnectionMax) {
		this.countConnectionMax = countConnectionMax;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @param driver 사용할 드라이버
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @param name 명칭
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 암호 설정
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @param permitConnectionPoolOver 컨넥션 풀 초과인 경우 새로운 컨넥션 생성 여부
	 */
	public void setPermitConnectionPoolOver(boolean permitConnectionPoolOver) {
		this.permitConnectionPoolOver = permitConnectionPoolOver;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setProperty(String key, Object value) {

		if (map == null) {
			map = new HashMap<String, Object>();
		}

		map.put(key, value);
	}

	/**
	 * @param readOnly 읽기 전용 여부
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @param reconnectRetry 재 연결 시도 회수
	 */
	public void setReconnectRetry(int reconnectRetry) {
		this.reconnectRetry = reconnectRetry;
	}

	/**
	 * @param reconnectWaitTimeSec 재 연결 중간의 휴식 시간
	 */
	public void setReconnectWaitTimeSec(int reconnectWaitTimeSec) {
		this.reconnectWaitTimeSec = reconnectWaitTimeSec;
	}

	/**
	 * POOL에 가용한 Connection이 없을 경우 대기 시간(초) 설정
	 * 
	 * @param secondsWaitPool
	 */
	public void setSecondsWaitPool(int secondsWaitPool) {
		this.secondsWaitPool = secondsWaitPool;
	}

	public void setSqlSelectKeepAlive(String sqlSelectKeepAlive) {
		this.sqlSelectKeepAlive = sqlSelectKeepAlive;
	}

	public void setTimeoutLogin(int timeoutLogin) {
		if (timeoutLogin <= 0)
			return;
		this.timeoutLogin = timeoutLogin;
	}

	/**
	 * URL 설정
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		if (url != null)
			this.url = url;
	}

	/**
	 * 운용자 설정
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + url + "," + user + ")";
	}

	/**
	 * 따움표를 제거한다.
	 * 
	 * @param defaultValue
	 * @return
	 */
	protected String checkDefaultValue(String defaultValue) {
		if (defaultValue != null && defaultValue.trim().length() > 0) {
			defaultValue = defaultValue.replaceAll("‘", "'");
			defaultValue = defaultValue.replaceAll("’", "'");
		}
		return defaultValue;
	}

	/**
	 * 인덱스 생성 쿼리를 만듭니다.
	 * 
	 * @param idx        인덱스
	 * @param table      테이블
	 * @param tablespace 사용할 테이블 스페이스
	 * @return
	 */
	protected String getSqlCreate0(Index idx, Table tab) {

		List<String> columnList = idx.getColumnNames();

		String columns = columnList.get(0);
		for (int index = 1; index < columnList.size(); index++) {
			columns += ", " + columnList.get(index);
		}

		String ret = "";
		if (idx.isFk()) {
			ret = "alter table " + tab.getName() + " add constraint " + makeColSize(idx.getIndexName(), 32)
					+ " foreign key ( " + columnList.get(0) + " ) references " + idx.getFkTable() + " ( "
					+ idx.getFkColumn() + " )";
		} else if (idx.isPk()) {
			ret = "alter table " + tab.getName() + " add constraint " + idx.getIndexName() + " primary key ( " + columns
					+ ")";
		} else {
			ret = "create " + (idx.getIndexType() == INDEX_TYPE.UK ? "unique " : "") + "index "
					+ makeColSize(idx.getIndexName(), 32) + " on " + tab.getName() + " ( " + columns + " )";
		}

		return ret;
	}

	/**
	 * 
	 * @param table 테이블정보
	 * @return 쿼리문
	 * @throws Exception
	 */
	protected String getSqlCreate0(Table table) throws Exception {

		if (table.sizeCol() == 0) {
			Lang.throwException("The table has no columns.", table.getName());
		}

		List<Column> colList = table.getColumns();

		StringBuffer sb = new StringBuffer();

		sb.append("create table \t" + table.getName() + " ( ");
		sb.append("\n\t  " + getSqlCreate(colList.get(0)));

		for (int index = 1; index < colList.size(); index++) {
			sb.append("\n\t, " + getSqlCreate(colList.get(index)));
		}

		for (Index idx : table.getIndexList()) {

			if (idx.isPk()) {
				sb.append("\n\t, " + idx.getSqlPk(table.getTableSpaceIndex()));
			} else if (idx.isFk()) {
				try {
					sb.append("\n\t, " + idx.getSqlFk());
				} catch (Exception e) {
					System.out.println("FK : " + idx.getColumnAll(",") + " >>> ");
					e.printStackTrace();
				}
			}

		}

		sb.append("\n )");

		return sb.toString();

	}

	protected boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 
	 * @param name
	 * @param size
	 * @return
	 */
	protected String makeColSize(String name, int size) {
		if (size <= 0)
			return name.trim();

		String s = name;
		for (int i = s.length(); i < size; i++)
			s += " ";

		return s;
	}

	protected Map<String, Object> makePara(String tableName) {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("user", getUser());

		if (tableName != null)
			para.put("tableName", tableName);
		if (getDbName() != null)
			para.put("dbName", dbName);

		return para;
	}

	protected List<String> splitUrl(String url) {

		if (url == null)
			return null;

		List<String> ret = new ArrayList<String>();

		// jdbc:Altibase://167.1.21.31:20300/mydb
		// jdbc:oracle:thin:@70.70.200.230:1521:DAIMS
		// jdbc:oracle:thin:@167.1.21.37:1521:orcl
		// jdbc:mysql://167.1.21.31/nprism?useUnicode=true&amp;user=nprism&amp;password=nprism03&amp;characterEncoding=utf8
		char chArr[] = url.toCharArray();
		String s = "";
		for (char ch : chArr) {
			if (ch != '/' && ch != ':' && ch != '@') {
				if (s == null)
					s = ch + "";
				else
					s += ch;
			} else if (s != null) {
				ret.add(s);
				s = null;
			}
		}

		if (s != null)
			ret.add(s);

		return ret;
	}

	/**
	 * 
	 * @param connection
	 * @return 제거 여부
	 */
	private boolean closeConnection(Connection connection) {

		for (TranBean tran : tranArray) {
			if (tran.equals(connection)) {
				try {
					tran.setUse(false);
					Logger.logger.debug("Disconnected [" + connection + "]");
					connection.close();
				} catch (SQLException e) {
				}
				tran.setConnection(null);
				return true;
			}
		}

		return true;
	}

	/**
	 * 새로운 콘넥션을 생성합니다.
	 * 
	 * @return 콘넥션
	 * @throws Exception
	 */
	private Connection createConnection() throws Exception {

		long ptime = System.currentTimeMillis();

		try {

			Logger.logger.trace("1/5) new create");

			Class.forName(getDriver()).newInstance();

			Connection c;

			DriverManager.setLoginTimeout(timeoutLogin);

			if (isUseUserPass()) {
				Logger.logger.trace("2/5-1) {} {}/{}", getUrl(), getUser(), getPassword());
				c = DriverManager.getConnection(getUrl(), getUser(), getPassword());
			} else {
				Logger.logger.trace("2/5-2) {}", getUrl());
				c = DriverManager.getConnection(getUrl());
			}

			Logger.logger.trace("3/5) auto commit = {}", isAutoCommit());

			try {
				c.setAutoCommit(isAutoCommit());
			} catch (Exception e) {
				Logger.logger.fail("Auto Commit : " + e.getMessage());
			}

			Logger.logger.trace("4/5) read only = {}", isReadOnly());

			try {
				c.setReadOnly(isReadOnly());
			} catch (Exception e) {
				Logger.logger.fail("Read Only : " + e.getMessage());
			}

			Logger.logger.trace("5/5) connect");

			Logger.logger.debug("Connected [" + c + "] =" + (System.currentTimeMillis() - ptime) + "(ms)");

			return c;

		} catch (Exception ex) {
			Logger.logger.error(ex);
			throw makeException(ex, "DATABASE(" + name + ")");
		}
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	private boolean isOkConection(Connection c) {

		if (c == null)
			return false;

		if (sqlSelectKeepAlive == null || sqlSelectKeepAlive.trim().length() == 0) {
			try {
				return c.isClosed() == false;
			} catch (SQLException e) {
				return false;
			}
		}

		Statement statement = null;
		try {
			statement = c.createStatement();
			if (Logger.debug)
				System.out.println("### Check Connection : " + sqlSelectKeepAlive);
			boolean bret = statement.execute(sqlSelectKeepAlive);
			try {
				statement.close();
			} catch (SQLException e) {
			}
			return bret;
		} catch (Exception e) {
			Logger.logger.error(e);
			try {
				c.close();
			} catch (Exception e2) {
			}
			return false;
		}
	}
}
