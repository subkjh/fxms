package subkjh.bas.dao.control;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.exception.QidNotFoundException;
import subkjh.bas.dao.model.SqlBean;
import subkjh.bas.dao.model.SqlSelectBean;
import subkjh.bas.dao.model._BatchSQL;
import subkjh.bas.fxdao.beans.QueryResult;
import subkjh.bas.log.Logger;

public class DaoExecutor extends AbstractDaoExecutor {

	class Item {
		PreparedStatement ps;
		String sql;

		Item(String sql, PreparedStatement ps) {
			this.sql = sql;
			this.ps = ps;
		}
	}

	private QidPool sqlPool;

	public DaoExecutor() {
	}

	public DaoExecutor(DataBase database, String... sqlFiles) throws Exception {
		this.sqlPool = new QidPool(database.getKind(), database.getConstMap());
		this.sqlPool.addFile(sqlFiles);
		setDatabase(database);
	}

	@SuppressWarnings("rawtypes")
	public int execute(String qid, Object obj) throws QidNotFoundException, Exception {

		long ptime = System.currentTimeMillis();

		SqlBean sql = getSqlBean(qid);

		if (sql == null)
			throw new QidNotFoundException(qid);

		int ret;

		if (obj instanceof Collection) {
			Collection dataList = (Collection) obj;
			_BatchSQL esql = null;
			esql = sql.getBatchSql(dataList);
			ret = executeSql(esql.getSql(), esql.getValues());

		} else {
			QueryResult esql = null;
			esql = sql.getOneSql(obj);
			ret = executeSql(esql.getSql(), esql.getParaArray());
		}

		Logger.logger.debug("QID({}),RET({}),MS({})", qid, ret, (System.currentTimeMillis() - ptime));

		return ret;
	}

	public <T> List<T> select(Class<T> classOfT, String qid, Object para) throws Exception {
		return select(classOfT, qid, para, Integer.MAX_VALUE);
	}

	public SqlBean getSqlBean(String qid) {

		SqlBean sqlBean = null;

		if (sqlPool != null) {
			sqlBean = sqlPool.getSqlBean(qid, getDatabase());
		}

		if (sqlBean == null) {
			return DBManager.getMgr().getSqlBean(qid, getDatabase());
		}

		return sqlBean;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> select(Class<T> classOfT, String qid, Object para, int size) throws Exception {
		try {
			SqlSelectBean sql = (SqlSelectBean) getSqlBean(qid);
			if (sql != null) {
				QueryResult esql = sql.getOneSql(para);
				return selectSql(esql.getSql(), esql.getParaArray(), EntryType.Result, sql.getResultMap(), size);
			} else {
				QueryResult bean = new QueryResult(qid, para);
				return selectSql(bean.getSql(), bean.getParaArray(), EntryType.Map, classOfT, size);
			}
		} catch (Exception e) {
			throw getDatabase().makeException(e, "QID(" + qid + ")");
		}
	}
	
	public int getInt(List<Object[]> data, int defInt) {
		
		if ( data != null && data.size() == 1 ) {
			Object ret = data.get(0)[0];
			if ( ret instanceof Number ) {
				return ((Number)ret).intValue();
			}
		}
		
		return defInt;
	}

}
