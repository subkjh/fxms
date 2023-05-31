package subkjh.dao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Query.SQL_TYPE;
import subkjh.dao.def.Table;
import subkjh.dao.exp.NoWhereException;
import subkjh.dao.exp.PkValueNullException;
import subkjh.dao.model.QueryColumn;
import subkjh.dao.model.QueryResult;

/**
 * QUERY를 생성하는 클래스
 * 
 * @author subkjh
 *
 */
public class QueryMaker {

	class Conditions {
		String colName;
		String condition = null;

		public String toString() {
			return colName + "|" + condition;
		}
	}

	public enum VAR_TYPE {
		Hash, Dollar
	}

	private final StringAdapter stringAdapter;
	private final VAR_TYPE varType;

	public QueryMaker() {
		this.stringAdapter = null;
		this.varType = VAR_TYPE.Dollar;
	}

	/**
	 * 
	 * @param stringAdapter
	 */
	public QueryMaker(StringAdapter stringAdapter, VAR_TYPE varType) {
		this.stringAdapter = stringAdapter;
		this.varType = varType;
	}

	/**
	 * 삭제용 QueryResult를 가져온다.
	 * 
	 * @param table  테이블 정보
	 * @param delObj 삭제할 데이터
	 * @return
	 */
	public QueryResult getDeleteQueryResult(Table table, Object delObj) throws Exception {

		QueryColumn col = getDeleteQueryColumn(table, delObj);

		return makeQueryResult(col, delObj);
	}

	/**
	 * 입력된 조건으로만 테이블 데이터 삭제 쿼리를 만든다.
	 * 
	 * @param table 테이블
	 * @param para  입력된 조건
	 * @return
	 * @throws Exception
	 */
	public QueryResult getDeleteQueryResultPara(Table table, Map<String, Object> para) throws Exception {

		Column column;
		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.DELETE);
		StringBuffer sb = new StringBuffer();
		StringBuffer whereSb = new StringBuffer();

		sb.append("delete ");
		sb.append("\n from " + table.getName());

		for (String key : para.keySet()) {
			column = table.getColumn(key);
			if (column != null) {
				if (whereSb.length() == 0)
					whereSb.append("\n where ");
				else
					whereSb.append("\n and ");

				whereSb.append(column.getName() + " = ? ");
				queryColumn.addValue(para.get(key));
			}
		}

		queryColumn.setSql(sb.toString() + whereSb.toString());

		return new QueryResult(queryColumn.getSql(), queryColumn.getParaList());
	}

	/**
	 * 
	 * @param table
	 * @return
	 */
	public String getDeleteSql(Table table) {
		return "delete from " + table.getName();
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public Object[] getInsertPara(Table table, Object value[]) {
		if (value.length == table.sizeCol())
			return value;

		Object ret[] = new Object[table.sizeCol()];
		int no;
		for (int i = 0; i < ret.length; i++) {
			no = table.getColumns().get(i).getColumnNo();

			if (value.length > no) {
				ret[i] = value[no];
			} else {
				ret[i] = null;
			}
		}

		return ret;
	}

	/**
	 * INSERT 쿼리문 생성
	 * 
	 * @param table
	 * @return
	 */
	public QueryColumn getInsertQueryColumn(Table table) {

		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.INSERT);
		StringBuffer dataSb = new StringBuffer();
		StringBuffer colSb = new StringBuffer();

		for (Column column : table.getColumns()) {
			dataSb.append(dataSb.length() > 0 ? ", " : " ");
			colSb.append(colSb.length() > 0 ? ", " : " ");

			colSb.append(column.getName());
			dataSb.append("?");

			queryColumn.addColumn(column);
		}

		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append(table.getName());
		sql.append(" ( ").append(colSb.toString()).append(" ) values ( ");
		sql.append(dataSb.toString()).append(" )");

		queryColumn.setSql(sql.toString());

		return queryColumn;

	}

	public QueryResult getInsertQueryResult(Table table, Object obj) {
		return makeQueryResult(getInsertQueryColumn(table), obj);
	}

	public String getInsertSampleSql(Table table) {
		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		List<Column> columnList = table.getColumns();
		Column col;

		sb.append("insert into " + minLen(table.getName()) + " /* " + table.getComment() + " */").append("\n");
		sb.append("(\n");
		col = columnList.get(0);

		sb.append("\t  ").append(minLen(col.getName()));
		sb.append(" /* ").append(col.getComments()).append(" */ ").append("\n");
		data.append("\t  ").append(minLen("#{" + col.getFieldName() + "}"));
		data.append(" /* ").append(col.getComments()).append(" */ ").append("\n");

		for (int index = 1; index < columnList.size(); index++) {
			col = columnList.get(index);
			sb.append("\t, ");
			data.append("\t, ");
			sb.append(minLen(col.getName())).append(" /* ").append(col.getComments()).append(" */ ").append("\n");
			data.append(getColumnVar(col)).append(" /* ").append(col.getComments()).append(" */ ").append("\n");

		}
		sb.append(") values ( \n");
		sb.append(data.toString());
		sb.append(")");

		return sb.toString();
	}

	private String getColumnVar(Column col) {
		if (this.varType == VAR_TYPE.Dollar) {
			return minLen("$" + col.getFieldName());
		} else if (this.varType == VAR_TYPE.Hash) {
			return minLen("#{" + col.getFieldName() + "}");
		} else {
			return minLen(col.getFieldName());
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public String getInsertSql(Table table, Object value[]) {
		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		List<Column> columnList = table.getColumns();

		sb.append("insert into " + table.getName() + " ( ");

		sb.append(columnList.get(0).getName());
		data.append(getValue(value[columnList.get(0).getColumnNo()]));

		for (int index = 1; index < columnList.size(); index++) {

			sb.append(", " + columnList.get(index).getName());
			if (index < value.length) {
				data.append(", " + getValue(value[columnList.get(index).getColumnNo()]));
			} else {
				data.append(", " + getValue(null));
			}

		}
		sb.append(") values ( ");
		sb.append(data.toString());
		sb.append(")");

		return sb.toString();
	}

	/**
	 * MERGE 쿼리문 생성
	 * 
	 * @param table
	 * @return
	 */
	public QueryColumn getMergeQueryColumn(Table table) {

		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.INSERT);
		StringBuffer dataSb = new StringBuffer();
		StringBuffer colSb = new StringBuffer();

		for (Column column : table.getColumns()) {
			dataSb.append(dataSb.length() > 0 ? ", " : " ");
			colSb.append(colSb.length() > 0 ? ", " : " ");

			colSb.append(column.getName());
			dataSb.append("?");

			queryColumn.addColumn(column);
		}

		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append(table.getName());
		sql.append(" ( ").append(colSb.toString()).append(" ) values ( ");
		sql.append(dataSb.toString()).append(" )");

		// MERGE
		sql.append(" on duplicate key \n");
		sql.append(" update ");

		int updateIdx = 0;
		for (Column column : table.getColumns()) {
			if (column.isPk() == false) {
				if (column.getOperator().isUpdatable() == false) {
					continue;
				}
				if (updateIdx > 0) {
					sql.append(" ,");
				}
				sql.append(column.getName()).append(" = ? ");
				queryColumn.addColumn(column);
				updateIdx++;
			}
		}

		queryColumn.setSql(sql.toString());

		return queryColumn;

	}

	/**
	 * MERGE에 사용된 내용 가져오기
	 * 
	 * @param table
	 * @param obj
	 * @return
	 */
	public QueryResult getMergeQueryResult(Table table, Object obj) {
		return makeQueryResult(getMergeQueryColumn(table), obj);
	}

	/**
	 * 카운트용 쿼리 생성
	 * 
	 * @param tableList
	 * @param wherePara
	 * @return
	 */
	public QueryResult getSelectCountQueryResult(List<Table> tableList, Map<String, Object> wherePara) {

		Map<String, Table> tabMap = new HashMap<String, Table>();

		QueryResult info = new QueryResult();
		List<Object> inParaList = new ArrayList<Object>();
		List<Field> resultFieldList = new ArrayList<Field>();
		StringBuffer sb = new StringBuffer();
		StringBuffer tmp;
		Table fkTable;
		Column fkColumn;
		Conditions conditions;

		int aliasIndex = 0;
		for (Table tab : tableList) {
			aliasIndex++;
			tab.setAlias("T" + aliasIndex);
			tabMap.put(tab.getName(), tab);
		}

		// select

		sb.append(" select count(1) as CNT ");

		// from

		tmp = new StringBuffer();
		for (Table table : tabMap.values()) {
			if (tmp.length() > 0) {
				tmp.append("\n, ");
			} else {
				tmp.append("\nfrom ");
			}
			tmp.append(table.getName() + " " + table.getAlias());
		}

		sb.append(tmp);

		// where key
		tmp = new StringBuffer();

		if (tabMap.size() > 1) {
			for (Table table : tabMap.values()) {
				for (Index index : table.getIndexList()) {
					if (index.isFk()) {
						fkTable = tabMap.get(index.getFkTable());
						if (fkTable != null) {
							fkColumn = fkTable.getColumn(index.getFkColumn());
							if (tmp.length() == 0) {
								tmp.append("\n where ");
							} else {
								tmp.append("\n and ");
							}
							tmp.append(table.getAlias() + "." + index.getColumnAll("") + " = " + fkTable.getAlias()
									+ "." + fkColumn.getName());
						}
					}
				}
			}
		}

		// where input parameters
		if (wherePara != null) {
			Object paraValue;
			PARA: for (String paraName : wherePara.keySet()) {

				conditions = parseContidion(paraName);

				for (Table table : tabMap.values()) {
					for (Column column : table.getColumns()) {
						if (column.isMatch(conditions.colName)) {

							if (tmp.length() == 0) {
								tmp.append("\n where ");
							} else {
								tmp.append("\n and ");
							}

							paraValue = wherePara.get(paraName);

							if (paraValue instanceof List) {
								tmp.append(table.getAlias() + "." + column.getName() + " in ( "
										+ getWhereIn((List<?>) paraValue) + " ) ");
							} else if ("in".equalsIgnoreCase(conditions.condition)) {
								tmp.append(table.getAlias() + "." + column.getName() + " in (" + paraValue + ")");
							} else {
								tmp.append(table.getAlias() + "." + column.getName() + " "
										+ (conditions.condition == null ? "=" : conditions.condition) + " ?");

								inParaList.add(wherePara.get(paraName));
							}
							continue PARA;
						}
					}
				}
			}
		}

		sb.append(tmp.toString());

		info.setSql(sb.toString());
		info.setParaList(inParaList);
		info.setResultFieldList(resultFieldList);

		return info;
	}

	/**
	 * SELECT 문을 생성한다.
	 * 
	 * @param tableList
	 * @param wherePara
	 * @param order
	 * @return
	 */
	public QueryResult getSelectQueryResult(List<Table> tableList, Map<String, Object> wherePara, String order[]) {

		Map<String, Table> tabMap = new HashMap<String, Table>();

		QueryResult info = new QueryResult();
		List<Object> inParaList = new ArrayList<Object>();
		List<Field> resultFieldList = new ArrayList<Field>();
		StringBuffer sb = new StringBuffer();
		StringBuffer tmp;
		Table fkTable;
		Column fkColumn;
		Conditions conditions;

		// SELECT 컬럼에 중복된 명칭을 제거하기 위해 사용한다.
		List<String> selColList = new ArrayList<String>();

		int aliasIndex = 0;
		for (Table tab : tableList) {
			aliasIndex++;
			tab.setAlias("T" + aliasIndex);
			tabMap.put(tab.getName(), tab);
		}

		sb.append(" select ");

		// select columns

		tmp = new StringBuffer();
		for (Table table : tabMap.values()) {
			for (Column column : table.getColumns()) {

				// SELECT 컬럼에 포함되지 않으면 제외
				// WHERE 절에서 사용할 수 있음.
				if (column.isSelectable() == false) {
					continue;
				}

				// select문 컬럼 목록에 동일한 내용 있으면 무시
				if (selColList.contains(column.getName())) {
					continue;
				}

				if (tmp.length() > 0)
					tmp.append(", ");

				tmp.append(table.getAlias() + "." + column.getName());
				resultFieldList.add(column.getField());

				// select문 컬럼 목록 넣기
				selColList.add(column.getName());
			}
		}

		sb.append(tmp);

		// from

		tmp = new StringBuffer();
		for (Table table : tabMap.values()) {
			if (tmp.length() > 0) {
				tmp.append("\n, ");
			} else {
				tmp.append("\nfrom ");
			}
			tmp.append(table.getName() + " " + table.getAlias());
		}

		sb.append(tmp);

		// where key
		tmp = new StringBuffer();

		if (tabMap.size() > 1) {
			for (Table table : tabMap.values()) {
				for (Index index : table.getIndexList()) {
					if (index.isFk()) {
						fkTable = tabMap.get(index.getFkTable());
						if (fkTable != null) {
							fkColumn = fkTable.getColumn(index.getFkColumn());
							if (tmp.length() == 0) {
								tmp.append("\n where ");
							} else {
								tmp.append("\n and ");
							}
							tmp.append(table.getAlias() + "." + index.getColumnAll("") + " = " + fkTable.getAlias()
									+ "." + fkColumn.getName());
						}
					}
				}
			}
		}

//		System.out.println(FxmsUtil.toJson(wherePara));

		// where input parameters
		if (wherePara != null) {
			Object paraValue;
			PARA: for (String paraName : wherePara.keySet()) {

				conditions = parseContidion(paraName);

				// System.out.println(FxmsUtil.toJson(conditions));
				// System.out.println(FxmsUtil.toJson(tabMap.keySet()));

				for (Table table : tabMap.values()) {
					for (Column column : table.getColumns()) {

						// System.out.println(table.getName() + ":" + column.getName() + ":"+
						// Column.getDaoName(conditions.colName) + ":" +
						// column.isMatch(conditions.colName));

						if (column.isMatch(conditions.colName)) {

							if (tmp.length() == 0) {
								tmp.append("\n where ");
							} else {
								tmp.append("\n and ");
							}

							paraValue = wherePara.get(paraName);

							if (paraValue instanceof List) {
								tmp.append(table.getAlias() + "." + column.getName() + " in ( "
										+ getWhereIn((List<?>) paraValue) + " ) ");
							} else if ("in".equalsIgnoreCase(conditions.condition)) {
								tmp.append(table.getAlias() + "." + column.getName() + " in (" + paraValue + ")");
							} else {
								tmp.append(table.getAlias() + "." + column.getName() + " "
										+ (conditions.condition == null ? "=" : conditions.condition) + " ?");

								inParaList.add(wherePara.get(paraName));
							}
							continue PARA;
						}
					}
				}
			}
		}

		sb.append(tmp.toString());

		// order by
		sb.append(makeOrderBy(tableList, order));

		info.setSql(sb.toString());
		info.setParaList(inParaList);
		info.setResultFieldList(resultFieldList);

		return info;
	}

	public QueryResult getSelectQueryResult(Table table, Map<String, Object> para, String orderby[]) {

		StringBuffer whereSb = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		String orderString;
		for (Column column : table.getColumns()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(column.getName());
		}

		sb.append("\n from ");
		sb.append(table.getName());

		List<Object> preparePara = fillSqlWhere(whereSb, table, para);
		orderString = makeOrderBy(table, orderby);

		return new QueryResult("select " + sb.toString() + whereSb.toString() + orderString, preparePara);

	}

	/**
	 * 테이블의 PK를 이용하여 UPDATE를 만는다.
	 * 
	 * @param table 테이블정보
	 * @param Mao   업데이트 데이터
	 * @return
	 * @throws NoWhereException
	 */
	public QueryResult getUpdateQueryResult(Table table, Map<String, Object> updateMap) throws Exception {

		QueryColumn queryColumn = getUpdateQueryColumn(table, updateMap);

		return makeQueryResult(queryColumn, updateMap);
	}

	public QueryResult getUpdateQueryResult(Table table, Object updateObj) throws Exception {

		QueryColumn queryColumn = getUpdateQueryColumn(table, updateObj);

		return makeQueryResult(queryColumn, updateObj);
	}

	public String getUpdateSampleSql(Table table) {
		StringBuffer sb = new StringBuffer();
		List<Column> columnList = table.getColumns();
		Column col;

		sb.append("update " + minLen(table.getName()) + " /* " + table.getComment() + " */").append("\n");
		sb.append("set \n");

		col = columnList.get(0);

		sb.append("\t  ");
		sb.append(minLen(col.getName())).append(" = ").append(getColumnVar(col)).append(" /* ")
				.append(col.getComments()).append(" */ ").append("\n");

		for (int index = 1; index < columnList.size(); index++) {
			col = columnList.get(index);
			sb.append("\t, ");
			sb.append(minLen(col.getName())).append(" = ").append(getColumnVar(col)).append(" /* ")
					.append(col.getComments()).append(" */ ").append("\n");
		}

		return sb.toString();
	}

	/**
	 * Update 쿼리를 제공합니다.
	 * 
	 * @param value
	 * @return Update 쿼리
	 */
	public String getUpdateSql(Table table, Object value[], String... colNms) {
		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		for (Column column : table.getColumns()) {

			if (column.isPk()) {
				data.append(data.length() == 0 ? " where " : " and ");
				data.append(column.getName() + " = " + getValue(value[column.getColumnNo()]));
			} else {

				if (column.getOperator().isUpdatable() == false) {
					continue;
				}

				if (value.length > column.getColumnNo()) {

					if (colNms.length > 0) {
						for (String colNm : colNms) {
							if (colNm.equals(column.getName())) {
								sb.append(sb.length() == 0 ? " " : ", ");
								sb.append(column.getName() + " = " + getValue(value[column.getColumnNo()]));
								break;
							}
						}
					} else {
						sb.append(sb.length() == 0 ? " " : ", ");
						sb.append(column.getName() + " = " + getValue(value[column.getColumnNo()]));
					}
				}
			}
		}

		return "update " + table.getName() + " set " + sb.toString() + data.toString();
	}

	/**
	 * 쿼리와 이에 대응하는 파라메터 생성
	 * 
	 * @param queryColumn
	 * @param obj
	 * @return
	 */
	public QueryResult makeQueryResult(QueryColumn queryColumn, Object obj) {

		List<Object> paraList = makeQueryPara(queryColumn, obj);

		return new QueryResult(queryColumn.getSql(), paraList);
	}

	public String getSelectSampleSql(Table table) {

		StringBuffer sb = new StringBuffer();
		StringBuffer cols = new StringBuffer();

		for (Column column : table.getColumns()) {
			if (cols.length() > 0) {
				cols.append("        , ");
			}
			cols.append("a.").append(minLen(column.getName())).append("as  ").append(minLen(column.getName()))
					.append("/* ").append(column.getComments()).append(" */\n");
		}
		sb.append("select    ").append(cols).append("from \t").append(table.getName()).append(" a ").append(" /* ")
				.append(table.getComment()).append(" */\n");
		return sb.toString();
	}

	private Object appendWhere(StringBuffer whereSb, String colName, Conditions c, Object paraValue) {

		if (paraValue instanceof List) {
			whereSb.append(colName + " in ( " + getWhereIn((List<?>) paraValue) + " ) ");
			return null;
		}

		if (c == null || c.condition == null) {
			whereSb.append(colName + " = ?");
			return paraValue;
		}

		if ("in".equalsIgnoreCase(c.condition)) {
			whereSb.append(colName + " in (" + paraValue + ")");
		} else if ("exists".equalsIgnoreCase(c.condition)) {
			whereSb.append(colName + " exists (" + paraValue + ")");
		} else {
			whereSb.append(colName + " " + c.condition + " ?");
			return paraValue;
		}

		return null;
	}

	private void fillOrderBy(StringBuffer tmp, Table table, String orderby[]) {

		// order by
		if (orderby != null && orderby.length > 0) {
			Conditions conditions;

			for (String s : orderby) {

				conditions = parseContidion(s);

				if (table.containsColumn(conditions.colName) == false) {
					continue;
				}

				if (tmp.length() > 0) {
					tmp.append(", ");
				} else {
					tmp.append("\n order by ");
				}

				if (table.getAlias() != null && table.getAlias().length() > 0) {
					tmp.append(table.getAlias() + "." + conditions.colName);
				} else {
					tmp.append(conditions.colName);
				}

				if (conditions.condition != null) {
					tmp.append(" ");
					tmp.append(conditions.condition);
				}
			}
		}
	}

	/**
	 * 
	 * @param whereSb
	 * @param table   테이블
	 * @param para    인자
	 * @return 인자값 목록
	 */
	private List<Object> fillSqlWhere(StringBuffer whereSb, Table table, Map<String, Object> para) {

		Conditions conditions;
		List<Object> paraList = new ArrayList<Object>();
		String tableName = table.getAlias() == null || table.getAlias().length() == 0 ? "" : table.getAlias() + ".";
		Object paraValue;

		for (String paraName : para.keySet()) {

			conditions = parseContidion(paraName);

			for (Column column : table.getColumns()) {

				if (column.isMatch(conditions.colName)) {

					if (whereSb.length() == 0) {
						whereSb.append("\n where ");
					} else {
						whereSb.append("\n and ");
					}

					paraValue = appendWhere(whereSb, tableName + column.getName(), conditions, para.get(paraName));

					if (paraValue != null) {
						paraList.add(paraValue);
					}

					break;
				}
			}
		}

		return paraList;
	}

	/**
	 * 입력된 객체에서 컬럼에 해당되는 값을 가져온다.
	 * 
	 * @param obj    객체
	 * @param column 컬럼
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Object getColumnValue(Object obj, Column column) {

		if (obj instanceof Map) {
			Map map = (Map) obj;
			return map.get(column.getFieldName());
		} else if (obj != null) {
			return ObjectUtil.get(obj, column.getFieldName());
		} else {
			return null;
		}

	}

	/**
	 * 삭제 쿼리를 만는다.
	 * 
	 * @param table     테이블 정보
	 * @param incPk     테이블의 PK 정보를 포함 여부
	 * @param wherePara 조건
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private QueryColumn getDeleteQueryColumn(Table table, Object delObj) throws PkValueNullException, NoWhereException {

		Object colVal;
		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.DELETE);
		StringBuffer sb = new StringBuffer();
		StringBuffer whereSb = new StringBuffer();

		sb.append("delete ");
		sb.append("\n from " + table.getName());

		// PK를 포함할 경우
		for (Column column : table.getColumns()) {
			if (column.isPk()) {

				colVal = getColumnValue(delObj, column);
				if (colVal == null) {
					throw new PkValueNullException(table.getName(), column.getName());
				}

				if (whereSb.length() == 0)
					whereSb.append("\n where ");
				else
					whereSb.append("\n and ");

				// PK 조건 추가
				whereSb.append(column.getName() + " = ? ");
				// PK 컬럼 값 추가
				queryColumn.addValue(colVal);
			}
		}

		// where 조건절이 없으면 오류를 발생한다.
		if (whereSb.length() == 0) {

			// 조건이 없으나 MAP인 경우 다른 조건이 있는지 확인한다.
			if (delObj instanceof Map) {
				List<Object> valList = fillSqlWhere(whereSb, table, ((Map) delObj));
				for (Object val : valList) {
					queryColumn.addValue(val);
				}
			}

			if (whereSb.length() == 0) {
				throw new NoWhereException(table.getName(), "");
			}
		}

		queryColumn.setSql(sb.toString() + whereSb.toString());

		return queryColumn;
	}

	private QueryColumn getUpdateQueryColumn(Table table, Object updateObj)
			throws PkValueNullException, NoWhereException {

		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		StringBuffer whereSb = new StringBuffer();

		Object colVal;
		boolean firstCol = true;
		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.UPDATE);

		sb.append("update " + table.getName() + " set ");

		// set

		for (Column column : table.getColumns()) {

			// PK인 경우 SET하지 않는다.
			if (column.isPk()) {
				continue;
			}

			// UPDATE 컬럼이 아니면 SET하지 않는다.
			if (column.getOperator().isUpdatable() == false) {
				continue;
			}

			// NULL인 경우 SET하지 않는다.
			colVal = getColumnValue(updateObj, column);
			if (colVal == null) {
				continue;
			}

			if (firstCol) {
				sb.append("\n ");
				firstCol = false;
			} else
				sb.append("\n , ");

			// 인자 설정
			sb.append(column.getName() + " = ? ");

			// 인자 설정 값 넣긱
			queryColumn.addValue(colVal);

		}

		// where

		for (Column column : table.getColumns()) {

			if (column.isPk() == false) {
				continue;
			}

			colVal = getColumnValue(updateObj, column);
			if (colVal == null) {
				throw new PkValueNullException(table.getName(), column.getName());
			}

			if (whereSb.length() == 0) {
				whereSb.append("\n where ");
			} else {
				whereSb.append("\n and ");
			}

			whereSb.append(column.getName() + " = ?");
			queryColumn.addValue(colVal);
		}

		if (whereSb.length() == 0) {
			throw new NoWhereException(table.getName(), "");
		}

		queryColumn.setSql(sb.toString() + data.toString() + whereSb.toString());

		return queryColumn;
	}

	private String getValue(Object value) {
		if (value == null || value.toString().length() == 0)
			return "null";
		else if (value instanceof Double)
			return ((Double) value).doubleValue() + "";
		else if (value instanceof Float)
			return ((Float) value).floatValue() + "";
		else if (value instanceof Number)
			return ((Number) value).longValue() + "";

		String val = value.toString().trim();
		if (stringAdapter != null) {
			val = stringAdapter.convert(val);
		}

		val = val.replaceAll("'", "\\\'");

		return "'" + val + "'";
	}

	private String getWhereIn(List<?> list) {
		StringBuffer sb = new StringBuffer();
		for (Object o : list) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			if (o instanceof Number) {
				sb.append(o.toString());
			} else {
				sb.append("'" + o.toString() + "'");
			}
		}

		return sb.toString();
	}

	private String makeOrderBy(List<Table> tableList, String orderby[]) {

		StringBuffer tmp = new StringBuffer();

		for (Table table : tableList) {
			fillOrderBy(tmp, table, orderby);
		}

		return tmp.toString();
	}

	private String makeOrderBy(Table table, String orderby[]) {

		StringBuffer tmp = new StringBuffer();

		fillOrderBy(tmp, table, orderby);

		return tmp.toString();
	}

	@SuppressWarnings("rawtypes")
	private List<Object> makeQueryPara(QueryColumn sqlCol, Object o) {

		List<Object> paraList = new ArrayList<Object>();
		Object val;
		Column column;

		if (o instanceof Map) {
			Map map = (Map) o;

			for (Object value : sqlCol.getParaList()) {

				if (value instanceof Column) {
					// 인자가 컬럼이면 컬럼에 대응하는 값 찾아 넣기
					column = (Column) value;
					val = map.get(column.getFieldName());
					if (val == null && column.getDataDefault() != null) {
						paraList.add(column.getDataDefault());
					} else {
						paraList.add(val);
					}
				} else {
					// 인자가 값이면 그대로 넣기
					paraList.add(value);
				}

			}

		} else if (o != null) {

			for (Object value : sqlCol.getParaList()) {

				if (value instanceof Column) {
					// 인자가 컬럼에 대응하는 값 찾아 넣기
					column = (Column) value;
					val = ObjectUtil.get(o, column.getFieldName());
					if (val == null && column.getDataDefault() != null) {
						paraList.add(column.getDataDefault());
					} else {
						paraList.add(val);
					}
				} else {
					// 인자가 값이면 그래도 넣기
					paraList.add(value);
				}
			}
		} else {
			for (Object value : sqlCol.getParaList()) {
				if ((value instanceof Column) == false) {
					paraList.add(value);
				}
			}
		}

		return paraList;
	}

	private String minLen(String s) {
		return minLen(s, 24);
	}

	private String minLen(String s, int len) {
		int ret = len - s.toCharArray().length;
		StringBuffer sb = new StringBuffer(s);
		for (int i = ret; i >= 0; i--) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private Conditions parseContidion(String paraName) {

		char chs[] = paraName.toCharArray();

		StringBuffer col = new StringBuffer();
		StringBuffer con = new StringBuffer();
		boolean isIf = false;

		for (char ch : chs) {
			if (ch == '=' || ch == '<' || ch == '>' || ch == ' ') {
				isIf = true;
			}
			if (isIf) {
				con.append(ch);
			} else {
				col.append(ch);
			}
		}

		Conditions ret = new Conditions();
		ret.colName = col.toString().trim();
		ret.condition = con.length() > 0 ? con.toString().trim() : null;
		return ret;
	}

}
