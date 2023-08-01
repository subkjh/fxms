package subkjh.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.DaoListener;
import subkjh.dao.exp.DBObjectDupException;
import subkjh.dao.exp.QidNotFoundException;
import subkjh.dao.model.BatchVo;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.QidPool;
import subkjh.dao.model.QueryResult;
import subkjh.dao.model.SqlBean;
import subkjh.dao.model.SqlCreateBean;
import subkjh.dao.model.SqlSelectBean;

/**
 * 데이터 베이스와 미리 연결하여 풀 형식으로 사용되는 객체<br>
 * <br>
 * <br>
 * 사용법<br>
 * SqlTransaction tran = createSqlTransaction(); <br>
 * try {<br>
 * tran.start();<br>
 * 데이터 처리 <br>
 * catch (Exception e) {<br>
 * 오류 처리 throw e;<br>
 * <br>
 * finally {<br>
 * tran.stop();<br>
 * <br>
 * 
 * @author subkjh
 * 
 */
public class QidDao extends CommDao {

	/**
	 * 
	 * @author subkjh
	 * 
	 */
	class Item {
		PreparedStatement ps;
		String sql;

		Item(String sql, PreparedStatement ps) {
			this.sql = sql;
			this.ps = ps;
		}
	}

	private boolean isConnected;
	private final List<Item> pstat;
	private final QidPool sqlPool;

	/**
	 * 
	 */
	public QidDao() {
		this.pstat = new ArrayList<>();
		this.sqlPool = null;
	}

	/**
	 * 
	 * @param sqlXmlFiles
	 * @throws Exception
	 */
	public QidDao(DataBase database, String... sqlXmlFiles) throws Exception {
		this.pstat = new ArrayList<>();
		this.sqlPool = new QidPool(database.getKind(), database.getConstMap());
		this.sqlPool.addFile(sqlXmlFiles);
		setDatabase(database);
	}

	/**
	 * 
	 * @param sqlPool
	 */
	public QidDao(QidPool sqlPool) {
		this.pstat = new ArrayList<Item>();
		this.sqlPool = sqlPool;
	}

	/**
	 * 객체를 생성하는 쿼리 수행<br>
	 * SqlBean이 SqlCreateBean이면 쿼리를 수행하기 전에 쿼리에서 객체 정보를 찾아 존재여부를 확인합니다.
	 * 
	 * @param qid
	 * @param obj
	 * @return 처리결과
	 */
	public int create(String qid, Object obj) throws Exception {
		SqlBean sqlBean = getSqlBean(qid);
		SqlCreateBean sqlCreateBean;

		if (sqlBean instanceof SqlCreateBean) {
			String table, index;

			sqlCreateBean = (SqlCreateBean) sqlBean;
			try {
				Map<String, String> map = sqlCreateBean.getNameAndType(obj);
				table = map.get("table");
				index = map.get("index");

				if (index != null) {
					if (database.existIndex(this, table, index)) {
						throw new DBObjectDupException(index + "@" + table);
					}
				} else if (table != null) {
					if (database.existTable(this, table)) {
						throw new DBObjectDupException(table);
					}
				} else {

				}
			} catch (Exception e) {
			}
		}

		return execute(qid, obj);
	}

	/**
	 * 해당 내용을 실행한다.
	 * 
	 * @param sqlId
	 * @param obj
	 * @return 처리 결과<br>
	 *         Ret.intValue()가 처리 건수를 나타냅니다.
	 */
	@SuppressWarnings("rawtypes")
	public int execute(String qid, Object obj) throws Exception {

		long ptime = System.currentTimeMillis();

		SqlBean sql = getSqlBean(qid);

		if (sql == null)
			throw new QidNotFoundException(qid);

		int ret;

		if (obj instanceof Collection) {
			Collection dataList = (Collection) obj;
			BatchVo esql = null;
			esql = sql.getBatchSql(dataList);
			ret = executeQidSql(qid, esql.getSql(), esql.getValues());

		} else {
			QueryResult esql = null;
			esql = sql.getOneSql(obj);
			ret = executeQidSql(qid, esql.getSql(), esql.getParaArray());
		}

		Logger.logger.debugc(getClass(), "QID={}, ret={}, ms={}", qid, ret, (System.currentTimeMillis() - ptime));

		return ret;
	}

	/**
	 * QID의 내용을 조회합니다.
	 * 
	 * @param qid 찾은 QID
	 * @return 찾은 QID의 내용
	 */
	public SqlBean getSqlBean(String qid) {

		SqlBean sqlBean = null;

		if (sqlPool != null) {
			sqlBean = sqlPool.getSqlBean(qid, database);
		}

		if (sqlBean == null) {
			return DBManager.getMgr().getSqlBean(qid, database);
		}

		return sqlBean;
	}

	/**
	 * 
	 * @param qid
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public int selectQid(String qid, Object para, DaoListener daoListener) throws Exception {

		try {

			SqlSelectBean sql = (SqlSelectBean) getSqlBean(qid);
			if (sql != null) {
				QueryResult esql = sql.getOneSql(para);
				return selectSql(esql.getSql(), esql.getParaArray(), daoListener);
			} else {
				throw new Exception("QID(" + qid + ") NOT FOUND");
			}
		} catch (Exception e) {
			throw database.makeException(e, "QID(" + qid + ")");
		}
	}

	/**
	 * QID의 결과를 제공합니다.
	 * 
	 * @param qid
	 * @param para
	 * @return 객체 배열로 구성된 리스트
	 * @throws OdbcException
	 */
	public List<Object[]> selectQid2Obj(String qid, Object para) throws Exception {
		long ptime = System.currentTimeMillis();

		try {
			SqlSelectBean sql = (SqlSelectBean) getSqlBean(qid);
			if (sql != null) {
				QueryResult esql = sql.getOneSql(para);

				Logger.logger.trace("{}", esql);

				List<Object[]> ret = selectSql2Obj(qid, esql.getSql(), esql.getParaArray());

				if (Logger.logger != null)
					Logger.logger.debugc(getClass(), "qid={}, size={}, ms={}", qid, ret.size(),
							(System.currentTimeMillis() - ptime));

				return ret;
			} else {
				throw new Exception("QID(" + qid + ") NOT FOUND");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw database.makeException(e, "[SQLID=" + qid + "]");
		}
	}

	/**
	 * 자료가 없다면 size()가 0이고 오류가 발생했다면 Exception을 보낸다.
	 * 
	 * @param sqlId
	 * @param para
	 * @return 조회 결과
	 */
	@SuppressWarnings("rawtypes")
	public List selectQid2Res(String qid, Object para) throws Exception {
		SqlSelectBean bean = (SqlSelectBean) getSqlBean(qid);
		if (bean == null) {
			throw new Exception("QID(" + qid + ") NOT FOUND");
		}
		return select(bean, para);
	}

	/**
	 * 첫번째 자료를 int 형으로 변경하여 넘긴다.
	 * 
	 * @param sqlId
	 * @param obj
	 * @param defaultInt
	 * @return 첫 열의 첫 행의 정수형 값
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int selectQidInt(String qid, Object obj, int defaultInt) throws Exception {
		try {
			List list = selectQid2Res(qid, obj);
			if (list.size() == 0)
				return defaultInt;
			Object entry = list.get(0);

			if (entry instanceof Number)
				return ((Number) entry).intValue();
			else
				return defaultInt;
		} catch (Exception e) {
			throw database.makeException(e, qid);
		}
	}

	/**
	 * 첫번재 자료를 Long 형으로 변경하여 넘긴다.
	 * 
	 * @param id
	 * @param obj
	 * @param defaultLong
	 * @return 첫 열의 첫 행의 long값
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public long selectQidLong(String id, Object obj, long defaultLong) throws Exception {
		try {
			List list = selectQid2Res(id, obj);
			if (list.size() == 0)
				return defaultLong;
			Object entry = list.get(0);

			if (entry instanceof Number)
				return ((Number) entry).longValue();
			else
				return defaultLong;
		} catch (Exception e) {
			throw database.makeException(e, id);
		}
	}

	/**
	 * 첫번째 자료를 넘긴다.
	 * 
	 * @param id
	 * @param obj
	 * @return 첫 열에 대한 객체
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Object selectQidObj(String qid, Object obj) throws Exception {
		List list = selectQid2Res(qid, obj);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	/**
	 * 첫번재 자료를 toString()하여 넘긴다.
	 * 
	 * @param id
	 * @param obj
	 * @return 첫 열의 객체의 문자열
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String selectQidStr(String qid, Object obj) throws Exception {
		List list = selectQid2Res(qid, obj);
		if (list.size() == 0)
			return null;
		Object entry = list.get(0);
		return entry == null ? null : entry.toString();
	}

	/**
	 * 트랜잭션 시작
	 * 
	 * @throws Exception
	 */
	public synchronized void start() throws IOException, Exception {
		try {
			connect();
			isConnected = true;
		} catch (Exception e) {
			isConnected = false;
			throw e;
		}
	}

	/**
	 * 트랜잭션을 종료합니다.<br>
	 * POOL을 사용하는 경우 CONNECTION을 반납합니다.
	 */
	public synchronized void stop() {
		stop(false);
	}

	/**
	 * 트랜잭션을 종료합니다.<br>
	 * 
	 * @param closeRealConnection POOL을 사용하는 경우 CONNECTION을 반납만 할지 닫을지를 결정합니다.
	 */
	public synchronized void stop(boolean closeRealConnection) {
		if (isConnected == false)
			return;

		isConnected = false;

		for (Item item : pstat) {
			try {
				free(item.ps);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		pstat.clear();

		try {
			disconnect(closeRealConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param qid
	 * @param obj
	 * @return 처리결과
	 * @throws Exception
	 */
	public int update(String qid, Object obj) throws Exception {
		return execute(qid, obj);
	}

	/**
	 * sql에 해당되는 PreparedStatement를 넘긴다.
	 * 
	 * @param sql
	 * @return sql에 해당되는 PreparedStatement
	 */
	PreparedStatement getPs(String sql) throws Exception {
		for (Item item : pstat) {
			if (item.sql.equals(sql))
				return item.ps;
		}

		PreparedStatement ps = this.getPrepareStatement(sql);
		if (ps != null)
			pstat.add(new Item(sql, ps));
		return ps;
	}

	/**
	 * 
	 * @param sqlBean   쿼리정보
	 * @param para      파라메터
	 * @param javaField 결과를 자바 필드 형식으로 보일지 여부
	 * @return 조회 목록
	 * @throws OdbcException
	 */
	@SuppressWarnings("rawtypes")
	private List select(SqlSelectBean sqlBean, Object para) throws Exception {

		if (sqlBean == null) {
			throw new QidNotFoundException("null");
		}

		long ptime = System.currentTimeMillis();

		try {
			QueryResult esql = sqlBean.getOneSql(para);
			DaoResult rmap = sqlBean.getResultMap();

			List ret = selectSql(sqlBean.getQid(), esql.getSql(), esql.getParaArray(), RTYPE.Object, rmap);

			Logger.logger.debugc(getClass(), "QID({})PARA({})RET({})TIME({})\nSQL({})", sqlBean.getQid(),
					Arrays.toString(esql.getParaArray()), ret.size(), (System.currentTimeMillis() - ptime),
					esql.getSql());

			return ret;

		} catch (Exception e) {
			throw e;
		}

	}

}
