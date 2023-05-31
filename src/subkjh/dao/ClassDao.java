package subkjh.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.database.DataBase;
import subkjh.dao.def.FxOrder;
import subkjh.dao.def.Table;
import subkjh.dao.exp.NotFxTableException;
import subkjh.dao.exp.QidNotFoundException;
import subkjh.dao.model.QueryColumn;
import subkjh.dao.model.QueryResult;
import subkjh.dao.util.FxTableMaker;
import subkjh.dao.util.QueryMaker;

public class ClassDao extends DaoExecutor {

	class Item {
		PreparedStatement ps;
		String sql;

		Item(String sql, PreparedStatement ps) {
			this.sql = sql;
			this.ps = ps;
		}
	}

//	public static void main(String[] args) {
//		FxDaoExecutor dao = new FxDaoExecutor();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("alarmCfgNo", 100);
//		map.put("moNo", 200);
//		map.put("msIpaddr", "10.0.0.1");
//		map.put("chgDate", "20180312");
//		map.put("chgUserNo", "111");
//
//		try {
//			dao.updateOfClass(ServiceMo.class, map);
//		} catch (QidNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public ClassDao() {
	}

	public ClassDao(DataBase database, String... sqlFiles) throws Exception {
		super(database, sqlFiles);
	}

	public void createTable(Class<?> classOf) throws Exception {

		List<Table> tableList = getTableAll(classOf);

		for (Table table : tableList) {

			createTable(table);

		}

	}

	/**
	 * 테이블을 생상한다.
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 테이블 정보를 이용하여 데이터를 삭제한다.
	 * 
	 * @param table     테이블 정보
	 * @param wherePara 조건절
	 * @return
	 * @throws Exception
	 */
	public int delete(Table table, Map<String, Object> wherePara) throws Exception {

		QueryMaker maker = new QueryMaker();
		// Table table = new FxTableMaker().getTable(vo);
		QueryResult query;
		int size;

		query = maker.getDeleteQueryResult(table, wherePara);

		size = executeSql(query.getSql(), query.getParaArray());

		return size;
	}

	/**
	 * 
	 * @param classOfTable
	 * @return
	 * @throws Exception
	 */
	public int deleteAllOfClass(Class<?> classOfTable) throws Exception {

		int ret = 0;
		List<Table> tableList = getTableAll(classOfTable);
		QueryMaker maker = new QueryMaker();

		// 반대부터 삭제한다.
		for (int i = tableList.size() - 1; i >= 0; i--) {
			ret = executeSql(maker.getDeleteSql(tableList.get(i)), new Object[0]);
		}

		return ret;
	}

	/**
	 * 입력된 조건으로 테이블의 데이터를 삭제한다.
	 * 
	 * @param classOfTable 테이블
	 * @param para         입력조건
	 * @return
	 * @throws Exception
	 */
	public int deleteOfClass(Class<?> classOfTable, Map<String, Object> para) throws Exception {

		QueryMaker maker = new QueryMaker();
		List<Table> tableList = getTableAll(classOfTable);
		QueryResult query;
		int ret = 0;
		int size;

		// 반대부터 삭제한다.
		for (int i = tableList.size() - 1; i >= 0; i--) {

			query = maker.getDeleteQueryResultPara(tableList.get(i), para);

			size = executeSql(query.getSql(), query.getParaArray());

			ret += size;
		}

		return ret;
	}

	/**
	 * 테이블 클래스를 이용하여 데이터를 삭제한다.<br>
	 * delObj를 이용하여 PK 조건에 사용한다.<br>
	 * 
	 * @param classOfTable 테이블 클래스, 테이블 정보 및 조건(PK)가 존재한다.
	 * @param delObj       PK 정보가 담겨있는 객체
	 * @return
	 * @throws Exception
	 */
	public int deleteOfObject(Class<?> classOfTable, Object delObj) throws Exception {

		QueryMaker maker = new QueryMaker();
		List<Table> tableList = getTableAll(classOfTable);
		QueryResult query;
		int ret = 0;
		int size;

		// 반대부터 삭제한다.
		for (int i = tableList.size() - 1; i >= 0; i--) {

			query = maker.getDeleteQueryResult(tableList.get(i), delObj);

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

	public int insert(Table table, Map<String, Object> data) throws Exception {

		if (data == null) {
			return 0;
		}

		int size;

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		query = maker.getInsertQueryResult(table, data);
		size = executeSql(query.getSql(), query.getParaArray());

		return size;

	}

	/**
	 * 테이블클래스를 이용하여 데이터를 기록한다.
	 * 
	 * @param classOfT 테이블클래스
	 * @param data     등록할 데이터
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int insertOfClass(Class<?> classOfT, Object data) throws Exception {

		if (data == null) {
			return 0;
		}

		if (data instanceof Collection) {

			return insertListOfClass(classOfT, (Collection) data);

		} else {

			int ret = 0;
			int size;

			List<Table> tableList = getTableAll(classOfT);

			QueryResult query;
			QueryMaker maker = new QueryMaker();

			for (Table table : tableList) {

				initSeqence(table, data);

				query = maker.getInsertQueryResult(table, data);
				size = executeSql(query.getSql(), query.getParaArray());
				ret += size;
			}

			return ret;

		}
	}

	@SuppressWarnings("rawtypes")
	public int mergeOfClass(Class<?> classOfT, Object o) throws Exception {

		if (o == null) {
			return 0;
		}

		if (o instanceof Collection) {

			return mergeListOfClass(classOfT, (Collection) o);

		} else {

			int ret = 0;
			int size;

			List<Table> tableList = getTableAll(classOfT);

			QueryResult query;
			QueryMaker maker = new QueryMaker();

			for (Table table : tableList) {
				query = maker.getMergeQueryResult(table, o);
				size = executeSql(query.getSql(), query.getParaArray());
				ret += size;
			}

			return ret;

		}
	}

	/**
	 * SELECT 실행한 후 결과를 리턴한다.
	 * 
	 * @param <T>
	 * @param classOfTable  테이블클래스
	 * @param whereObj      조건
	 * @param classOfResult 데이터클래스
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> select(Class<?> classOfDbo, Object whereObj, Class<T> classOfResult) throws Exception {
		try {
			return select(classOfDbo, whereObj, Integer.MAX_VALUE, classOfResult);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * SELECT 실행한 후 결과를 리턴한다.
	 * 
	 * @param <T>
	 * @param classOfTable 해당 테이블 정보 및 결과를 담을 클래스
	 * @param whereObj     조건
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> select(Class<T> classOfDbo, Object whereObj) throws Exception {
		return select(classOfDbo, whereObj, Integer.MAX_VALUE, classOfDbo);
	}

	/**
	 * 
	 * @param classOfTable
	 * @param whereObj
	 * @return
	 * @throws Exception
	 */
	public int selectCount(Class<?> classOfTable, Object whereObj) throws Exception {
		try {

			List<Table> tableList = getTableAll(classOfTable);
			QueryResult info = new QueryMaker().getSelectCountQueryResult(tableList, makeWherePara(whereObj));
			info.setClassOfResult(Integer.class);
			List<?> list = select(info);

			if (list != null && list.size() == 1) {
				return (Integer) (list.get(0));
			}
			return -1;
		} catch (Exception e) {
			throw getDatabase().makeException(e, null);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param classOfTable
	 * @param whereObj
	 * @param classOfResult
	 * @return
	 * @throws Exception
	 */
	public <T> T selectOne(Class<?> classOfTable, Object whereObj, Class<T> classOfResult) throws Exception {
		List<T> list = select(classOfTable, whereObj, 1, classOfResult);
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 
	 * @param <T>
	 * @param classOfTable
	 * @param whereObj
	 * @return
	 * @throws Exception
	 */
	public <T> T selectOne(Class<T> classOfTable, Object whereObj) throws Exception {
		List<T> list = select(classOfTable, whereObj, 1, classOfTable);
		if (list != null && list.size() >= 1) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * 테이블클래스 PK에 해당되는 데이터가 updateObj에 존재하면 그 조건으로 데이터를 수정한다.<br>
	 * PK가 해당 내용이 없다면 오류를 발생한다.
	 * 
	 * @param classOfT  테이블클래스
	 * @param updateObj 수정할데이터
	 * @return
	 * @throws Exception
	 */
	public int updateOfClass(Class<?> classOfT, Object updateObj) throws Exception {

		List<Table> tableList = getTableAll(classOfT);
		List<QueryResult> eList = new ArrayList<QueryResult>();

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		for (Table table : tableList) {
			query = maker.getUpdateQueryResult(table, updateObj);
			if (query != null) {
				eList.add(query);
			}
		}

		int size;
		int ret = 0;
		for (QueryResult e : eList) {
			size = executeSql(e.getSql(), e.getParaArray());
			ret += size;
		}

		return ret;
	}

	/**
	 * 
	 * @param table
	 * @param map
	 * @return
	 * @throws QidNotFoundException
	 * @throws Exception
	 */
	protected int update(Table table, Object updateObj) throws QidNotFoundException, Exception {

		QueryResult query;
		QueryMaker maker = new QueryMaker();

		query = maker.getUpdateQueryResult(table, updateObj);

		return executeSql(query.getSql(), query.getParaArray());

	}

	private List<Table> getTableAll(Class<?> classOfMain) throws Exception {
		if (classOfMain == null) {
			throw new Exception("Class is null");
		}
		return new FxTableMaker().getTables(classOfMain);
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
				queryInfo = maker.makeQueryResult(queryColumn, obj);
				dataList.add(queryInfo.getParaArray());
			}

			size = executeSql(queryColumn.getSql(), dataList);
			ret += size;
		}

		return ret;

	}

	/**
	 * 객체를 맵 형식으로 변경한다.
	 * 
	 * @param whereObj
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> makeWherePara(Object whereObj) {
		if (whereObj == null) {
			return null;
		} else if (Map.class.isAssignableFrom(whereObj.getClass())) {
			return (Map) whereObj;
		} else {
			return ObjectUtil.toMap(whereObj);
		}
	}

	@SuppressWarnings("rawtypes")
	private int mergeListOfClass(Class<?> classOfT, Collection list) throws Exception {

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
			queryColumn = maker.getMergeQueryColumn(table);
			for (Object obj : list) {
				queryInfo = maker.makeQueryResult(queryColumn, obj);
				dataList.add(queryInfo.getParaArray());
			}

			size = executeSql(queryColumn.getSql(), dataList);
			ret += size;
		}

		return ret;

	}

	/**
	 * SELECT 문을 실행한다.
	 * 
	 * @param <T>
	 * @param classOfTable  테이블 정보
	 * @param whereObj      조건
	 * @param size          데이터 개수
	 * @param classOfResult 결과를 담을 클래스
	 * @return
	 * @throws Exception
	 */
	private <T> List<T> select(Class<?> classOfTable, Object whereObj, int size, Class<T> classOfResult)
			throws Exception {

		Map<String, Object> whereMap = makeWherePara(whereObj);

		try {

			List<Table> tableList = getTableAll(classOfTable);

			if (classOfTable != classOfResult) {

				List<Field> fieldList = new ArrayList<Field>();

				setFields(fieldList, classOfResult);

				// 결과 클래스에 없는 컬럼을 조회 쿼리에서 제외하기 위해 컬럼을 제거한다.
				for (Table t : tableList) {
					t.setSelectable(fieldList);
				}
			}

			// TODO
			String order[] = null;
			FxOrder fxOrder = classOfResult.getAnnotation(FxOrder.class);
			if (fxOrder != null) {
				order = fxOrder.columns();
			}

			QueryResult info = new QueryMaker().getSelectQueryResult(tableList, whereMap, order);

			info.setClassOfResult(classOfResult);

			return select(info);

		} catch (Exception e) {
			throw getDatabase().makeException(e, null);
		}
	}

	/**
	 * 클래스가 가지고 있는 필드를 모두 가져온다.
	 * 
	 * @param fieldList
	 * @param classOfResult
	 */
	private void setFields(List<Field> fieldList, Class<?> classOfResult) {

		for (Field f : classOfResult.getDeclaredFields()) {
			if ((f.getModifiers() & Modifier.FINAL) != Modifier.FINAL //
					&& (f.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
				fieldList.add(f);
			}
		}

		if (classOfResult.getSuperclass() != null) {
			setFields(fieldList, classOfResult.getSuperclass());
		}
	}

}
