package subkjh.bas.fxdao.control;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.mo.FxServiceMo;
import subkjh.bas.dao.control.DaoExecutor;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.exception.QidNotFoundException;
import subkjh.bas.fxdao.beans.QueryColumn;
import subkjh.bas.fxdao.beans.QueryResult;
import subkjh.bas.fxdao.define.FxOrder;
import subkjh.bas.fxdao.exception.NotFxTableException;
import subkjh.bas.fxdao.vo.FxTableVo;
import subkjh.bas.log.Logger;

public class FxDaoExecutor extends DaoExecutor {

	class Item {
		PreparedStatement ps;
		String sql;

		Item(String sql, PreparedStatement ps) {
			this.sql = sql;
			this.ps = ps;
		}
	}

	public static void main(String[] args) {
		FxDaoExecutor dao = new FxDaoExecutor();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alarmCfgNo", 100);
		map.put("moNo", 200);
		map.put("msIpaddr", "10.0.0.1");
		map.put("chgDate", "20180312");
		map.put("chgUserNo", "111");

		try {
			dao.updateOfClass(FxServiceMo.class, map);
		} catch (QidNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, Object> makaParameters(Object... objects) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int index = 0;
		while (index + 1 < objects.length) {
			parameters.put(objects[index] + "", objects[index + 1]);
			index += 2;
		}
		return parameters;
	}

	public FxDaoExecutor() {
	}

	public FxDaoExecutor(DataBase database, String... sqlFiles) throws Exception {
		super(database, sqlFiles);
	}

	public void createTable(Class<?> classOf) throws Exception {

		List<Table> tableList = getTableAll(classOf);

		for (Table table : tableList) {

			createTable(table);

		}

	}

	public boolean createTable(Table table) throws Exception {

		List<String> sqlList;

		if (getDatabase().existTable(this, table.getName())) {
			Logger.logger.debug("TABLE({}) EXIST", table.getName());
			return false;
		}

		executeSql(getDatabase().getSqlCreate(table));

		sqlList = getDatabase().getSqlCreateIndex(table);
		for (String sql : sqlList) {
			executeSql(sql);
		}
		sqlList = getDatabase().getSqlComment(table);
		for (String sql : sqlList) {
			executeSql(sql);
		}

		return true;
	}

	public int delete(FxTableVo vo, Map<String, Object> wherePara) throws Exception {

		QueryMaker maker = new QueryMaker();
		Table table = new FxTableMaker().getTable(vo);
		QueryResult query;
		int size;

		query = maker.getDeleteQueryResult(table, null, wherePara);
		size = executeSql(query.getSql(), query.getParaArray());

		return size;
	}

	public int deleteOfClass(Class<?> classOf, Map<String, Object> wherePara) throws Exception {

		QueryMaker maker = new QueryMaker();
		List<Table> tableList = getTableAll(classOf);
		QueryResult query;
		int ret = 0;
		int size;

		for (int i = tableList.size() - 1; i >= 0; i--) {
			query = maker.getDeleteQueryResult(tableList.get(i), null, wherePara);
			size = executeSql(query.getSql(), query.getParaArray());
			ret += size;
		}

		return ret;
	}

	public int deleteOfObject(Object obj) throws Exception {

		QueryMaker maker = new QueryMaker();
		List<Table> tableList = getTableAll(obj.getClass());
		QueryResult query;
		int ret = 0;
		int size;

		for (int i = tableList.size() - 1; i >= 0; i--) {
			query = maker.getDeleteQueryResult(tableList.get(i), obj, null);
			size = executeSql(query.getSql(), query.getParaArray());
			ret += size;
		}

		return ret;
	}

	public boolean dropTable(Table table) throws NotFxTableException, Exception {

		if (getDatabase().existTable(this, table.getName())) {
			return executeSql(getDatabase().getSqlDrop(table)) >= 0;
		} else {
			throw new NotFxTableException(table.getName());
		}
	}

	public int insert(FxTableVo vo, Map<String, Object> data) throws Exception {

		if (data == null) {
			return 0;
		}

		int size;

		Table table = new FxTableMaker().getTable(vo);

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		query = maker.getInsertQueryResult(table, data);
		size = executeSql(query.getSql(), query.getParaArray());

		return size;

	}

	public int insert(Object o) throws Exception {
		return insertOfClass(o.getClass(), o);
	}

	@SuppressWarnings("rawtypes")
	public int insertOfClass(Class<?> classOfT, Object o) throws Exception {

		if (o == null) {
			return 0;
		}

		if (o instanceof Collection) {

			return insertListOfClass(classOfT, (Collection) o);

		} else {

			int ret = 0;
			int size;

			List<Table> tableList = getTableAll(classOfT);

			QueryResult query;
			QueryMaker maker = new QueryMaker();

			for (Table table : tableList) {

				initSeqence(table, o);

				query = maker.getInsertQueryResult(table, o);
				size = executeSql(query.getSql(), query.getParaArray());
				ret += size;
			}

			return ret;

		}
	}

	/**
	 * 객체가 시퀀스를 가지고 있으면 가져와 설정한다.
	 * 
	 * @param table
	 * @param o
	 * @throws Exception
	 */
	private void initSeqence(Table table, Object o) throws Exception {

		return;

		// sequence 자동 설정
		// for (Column c : table.getColumnList()) {
		// if (c.getSequence() != null && c.getSequence().length() > 0) {
		// Object nowSequence = ObjectUtil.get(o, c.getFieldName());
		// if (nowSequence == null || ((Number) nowSequence).longValue() <= 0) {
		// long sequence = getNextVal(c.getSequence(), Long.class);
		// ObjectUtil.setField(o, c.getFieldName(), sequence);
		// }
		// }
		// }
	}

	public <T> List<T> select(Class<T> classOfT, Map<String, Object> parameters) throws Exception {
		return select(classOfT, parameters, Integer.MAX_VALUE);
	}

	public <T> T selectOne(Class<T> classOfT, Map<String, Object> parameters) throws Exception {
		List<T> list = select(classOfT, parameters, 1);
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}

		return null;
	}

	public int update(FxTableVo vo, Map<String, Object> map) throws QidNotFoundException, Exception {

		Table table = new FxTableMaker().getTable(vo);

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		query = maker.getUpdateQueryResult(table, map);

		return executeSql(query.getSql(), query.getParaArray());

	}

	/**
	 * 
	 * @param o
	 *            업데이트 대상
	 * @param para
	 *            추가할 WHERE 조건
	 * @return 처리 건수
	 * @throws QidNotFoundException
	 * @throws Exception
	 */
	public int update(Object o, Map<String, Object> para) throws QidNotFoundException, Exception {
		int ret = 0;
		int size;

		List<Table> tableList = getTableAll(o.getClass());

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		for (Table table : tableList) {
			query = maker.getUpdateQueryResult(table, o, para);
			size = executeSql(query.getSql(), query.getParaArray());
			ret += size;
		}

		return ret;
	}

	public int updateOfClass(Class<?> classOfT, Map<String, Object> map) throws QidNotFoundException, Exception {

		List<Table> tableList = getTableAll(classOfT);
		List<QueryResult> eList = new ArrayList<QueryResult>();

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		for (Table table : tableList) {
			query = maker.getUpdateQueryResult(table, map);
			if (query != null) {
				eList.add(query);
			}
		}

		int size;
		int ret = 0;
		for (QueryResult e : eList) {
			size = executeSql(e.getSql(), e.getParaArray());
			ret += size;
			//
			// System.out.println(e.getDebug());
		}

		return ret;
	}

	private List<Table> getTableAll(Class<?> classOfMain) throws Exception {
		return new FxTableMaker().makeTableList(classOfMain);
	}

	@SuppressWarnings("rawtypes")
	private int insertListOfClass(Class<?> classOfT, Collection list) throws Exception {

		if (list == null || list.size() == 0) {
			return 0;
		}

		int ret = 0;
		int size;

		List<Table> tableList = getTableAll(classOfT);
		List<Object[]> dataList = new ArrayList<Object[]>();
		QueryResult queryInfo;
		QueryColumn queryColumn;
		QueryMaker maker = new QueryMaker();

		for (Table table : tableList) {
			queryColumn = maker.getInsertQueryColumn(table);
			for (Object obj : list) {

				initSeqence(table, obj);

				queryInfo = maker.makeQueryResult(queryColumn, obj);
				dataList.add(queryInfo.getParaArray());
			}

			size = executeSql(queryColumn.getSql(), dataList);
			ret += size;
		}

		return ret;

	}

	private <T> List<T> select(Class<T> classOfResult, Map<String, Object> paraMap, int size) throws Exception {
		try {

			List<Table> tableList = getTableAll(classOfResult);

			// TODO
			String order[] = null;
			FxOrder fxOrder = classOfResult.getAnnotation(FxOrder.class);
			if (fxOrder != null) {
				order = fxOrder.columns();
			}

			QueryResult info = new QueryMaker().getSelectQueryResult(tableList, paraMap, order);

			info.setClassOfResult(classOfResult);

			return select(info);

		} catch (Exception e) {
			throw getDatabase().makeException(e, null);
		}
	}
}
