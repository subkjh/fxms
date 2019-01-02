package subkjh.bas.dao.control;

import java.util.HashMap;
import java.util.Map;

import subkjh.bas.dao.database.DataBase;
import subkjh.bas.log.Logger;

/**
 * subkjh Object : Database Access Object
 * 
 * @author subkjh
 * 
 */
public class SoDao {

	private static int fetchSizeDefault = 0;

	public static void setFetchSizeDefault(int fetchSize) {
		fetchSizeDefault = fetchSize;
	}

	/** 접근할 데이터 베이스 */
	protected DataBase database;
	/** 쿼리 풀 */
	protected QidPool sqlPool;
	/** preparedStatement에서 사용할 패치 크기 */
	private int fetchSize;

	public SoDao() {
		fetchSize = fetchSizeDefault;
	}

	/**
	 * 
	 * @param database
	 *            접근할 데이터베이스
	 * @param logger
	 *            로거
	 * @param sqlPool
	 *            사용할 풀
	 */
	public SoDao(DataBase database, QidPool sqlPool) {
		this.database = database;
		this.sqlPool = sqlPool;
	}

	/**
	 * Service를 사용하는 프로젝트에서 데이터베이스 접근을 위해 사용하는 DAO입니다.
	 * 
	 * @param database
	 *            접근할 데이터베이스
	 * @param logger
	 *            로거
	 * @param xmlFileArr
	 *            쿼리 XML 화일
	 */
	public SoDao(DataBase database, String... xmlFileArr) {
		this.database = database;

		try {
			sqlPool = new QidPool(database.getKind(), database.getConstMap());
			sqlPool.addFile(xmlFileArr);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 기본 데이터베이스를 이용하여 트랜잭션을 생성합니다.
	 * 
	 * @return 기본 데이터베이스 접근 트랜잭션
	 */
	public DbTrans createDbTrans() {
		DbTrans tran = database.createDbTrans();
		tran.setSqlPool(sqlPool);
		tran.setFetchSize(fetchSize);
		return tran;
	}

	/**
	 * 입력된 데이터베이스를 이용하여 트랜잭션을 생성합니다.
	 * 
	 * @param database
	 *            접근할 데이터베이스
	 * @return 입력 데이터베이스 접근 트랜잭션
	 */
	public DbTrans createDbTrans(DataBase database) {
		DbTrans tran = database.createDbTrans();
		tran.setSqlPool(sqlPool);
		tran.setFetchSize(fetchSize);
		return tran;
	}

	/**
	 * @return the database
	 */
	public DataBase getDatabase() {
		return database;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * @return the sqlPool
	 */
	public QidPool getSqlPool() {
		return sqlPool;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	protected Map<String, Object> makePara(String firstName, Object firstValue, Object... nextParas) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put(firstName, firstValue);
		if (nextParas != null && nextParas.length % 2 == 1) return null;
		for (int i = 0; i < nextParas.length; i += 2) {
			para.put(nextParas[i].toString(), nextParas[i + 1]);
		}
		return para;
	}

}
