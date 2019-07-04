package subkjh.bas.fxdao.control;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.define.SQL_TYPE;
import subkjh.bas.fxdao.beans.QueryColumn;
import subkjh.bas.fxdao.beans.QueryResult;

public class QueryMaker {

	class Conditions {
		String colName;
		String condition = null;
	}

	public QueryColumn getDeleteQueryColumn(Table table, Map<String, Object> wherePara, boolean onlyWherePara) {

		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.DELETE);
		StringBuffer sb = new StringBuffer();
		StringBuffer whereSb = new StringBuffer();

		sb.append("delete ");
		sb.append("\n from " + table.getName());

		// 입력된 wherePara에 대해서만 조건을 원할 경우
		if (onlyWherePara == false) {
			for (Column column : table.getColumnList()) {
				if (column.isPk()) {
					if (whereSb.length() == 0)
						whereSb.append("\n where ");
					else
						whereSb.append("\n and ");

					whereSb.append(column.getName() + " = ? ");
					queryColumn.addColumn(column);
				}
			}
		}

		if (wherePara != null) {
			setWhere(table, queryColumn, whereSb, wherePara);
		}

		queryColumn.setSql(sb.toString() + whereSb.toString());
		return queryColumn;
	}

	public QueryResult getDeleteQueryResult(Table table, Object obj, Map<String, Object> wherePara) {
		return makeQueryResult(getDeleteQueryColumn(table, wherePara, obj == null), obj);
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
			no = table.getColumnList().get(i).getColumnNo();

			if (value.length > no) {
				ret[i] = value[no];
			} else {
				ret[i] = null;
			}
		}

		return ret;
	}

	public QueryColumn getInsertQueryColumn(Table table) {

		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.INSERT);
		StringBuffer dataSb = new StringBuffer();
		StringBuffer colSb = new StringBuffer();

		for (Column column : table.getColumnList()) {
			dataSb.append(dataSb.length() > 0 ? ", " : " ");
			colSb.append(colSb.length() > 0 ? ", " : " ");

			colSb.append(column.getName());
			dataSb.append("?");
			queryColumn.addColumn(column);
		}

		queryColumn.setSql(
				"insert into " + table.getName() + "(" + colSb.toString() + ") values ( " + dataSb.toString() + ")");

		return queryColumn;

	}

	public QueryResult getInsertQueryResult(Table table, Object obj) {
		return makeQueryResult(getInsertQueryColumn(table), obj);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public String getInsertSql(Table table, Object value[]) {
		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		List<Column> columnList = table.getColumnList();

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

	public QueryResult getSelectQueryResult(List<Table> tableList, Map<String, Object> para, String order[]) {

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

		sb.append(" select ");

		// select columns

		tmp = new StringBuffer();
		for (Table table : tabMap.values()) {
			for (Column column : table.getColumnList()) {
				if (tmp.length() > 0)
					tmp.append(", ");
				tmp.append(table.getAlias() + "." + column.getName());
				resultFieldList.add(column.getField());
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

		// where input parameters
		if (para != null) {
			Object paraValue;
			PARA: for (String paraName : para.keySet()) {

				conditions = parseContidion(paraName);

				for (Table table : tabMap.values()) {
					for (Column column : table.getColumnList()) {
						if (column.isMatch(conditions.colName)) {

							if (tmp.length() == 0) {
								tmp.append("\n where ");
							} else {
								tmp.append("\n and ");
							}

							paraValue = para.get(paraName);

							if (paraValue instanceof List) {
								tmp.append(table.getAlias() + "." + column.getName() + " in ( "
										+ getWhereIn((List<?>) paraValue) + " ) ");
							} else if ("in".equalsIgnoreCase(conditions.condition)) {
								tmp.append(table.getAlias() + "." + column.getName() + " in (" + paraValue + ")");
							} else {
								tmp.append(table.getAlias() + "." + column.getName() + " "
										+ (conditions.condition == null ? "=" : conditions.condition) + " ?");

								inParaList.add(para.get(paraName));
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
		for (Column column : table.getColumnList()) {
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

	public String getDeleteSql(Table table) {
		return "delete from " + table.getName();
	}

	public QueryResult getUpdateQueryResult(Table table, Object obj, Map<String, Object> wherePara) {
		return makeQueryResult(getUpdateQueryColumn(table, wherePara), obj);
	}

	public QueryResult getUpdateQueryResult(Table table, Map<String, Object> map) throws Exception {

		boolean firstCol = true;
		Object value;
		QueryResult ret = new QueryResult();

		ret.getSqlStringBuffer().append("update " + table.getName() + " set ");

		// set

		for (Column column : table.getColumnList()) {

			if (column.isPk() == false) {

				if (column.getOperator().isUpdatable() == false) {
					continue;
				}

				value = map.get(column.getFieldName());
				if (value != null) {
					if (firstCol) {
						ret.getSqlStringBuffer().append("\n ");
						firstCol = false;
					} else
						ret.getSqlStringBuffer().append("\n , ");

					value = appendWhere(ret.getSqlStringBuffer(), column.getName(), null, value);
					if (value != null) {
						ret.getParaList().add(value);
					}
				}
			}
		}

		if (firstCol) {
			Logger.logger.fail("Not found column to update in table '{}'", table.getName());
			return null;
		}

		// where
		firstCol = true;
		for (Column column : table.getColumnList()) {
			if (column.isPk()) {
				value = map.get(column.getFieldName());
				if (value != null) {
					if (firstCol) {
						ret.getSqlStringBuffer().append("\n where ");
						firstCol = false;
					} else {
						ret.getSqlStringBuffer().append("\n and ");
					}

					value = appendWhere(ret.getSqlStringBuffer(), column.getName(), null, value);
					if (value != null) {
						ret.getParaList().add(value);
					}

				} else {
					Logger.logger.fail("Not found pk-value '{}' in table '{}'", column.getFieldName(), table.getName());
					return null;
				}
			}
		}

		return ret;
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
		for (Column column : table.getColumnList()) {

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

	public QueryResult makeQueryResult(QueryColumn queryColumn, Object obj) {

		List<Object> paraList = makeQueryPara(queryColumn, obj);

		return new QueryResult(queryColumn.getSql(), paraList);
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

	private List<Object> fillSqlWhere(StringBuffer whereSb, Table table, Map<String, Object> para) {

		Conditions conditions;
		List<Object> paraList = new ArrayList<Object>();
		String tableName = table.getAlias() == null || table.getAlias().length() == 0 ? "" : table.getAlias() + ".";
		Object paraValue;

		for (String paraName : para.keySet()) {

			conditions = parseContidion(paraName);

			for (Column column : table.getColumnList()) {

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

					// paraValue = para.get(paraName);
					//
					// if (paraValue instanceof List) {
					// whereSb.append(
					// tableName + column.getName() + " in ( " + getWhereIn((List<?>) paraValue) + "
					// ) ");
					// } else if ("in".equalsIgnoreCase(conditions.condition)) {
					// whereSb.append(tableName + column.getName() + " in (" + paraValue + ")");
					// } else {
					// whereSb.append(tableName + column.getName() + " "
					// + (conditions.condition == null ? "=" : conditions.condition) + " ?");
					// }
					break;
				}
			}
		}

		return paraList;
	}

	private Object appendWhere(StringBuffer whereSb, String colName, Conditions c, Object paraValue) {

		if (paraValue instanceof List) {
			whereSb.append(colName + " in ( " + getWhereIn((List<?>) paraValue) + " ) ");
			return null;
		}

		if (c == null) {
			whereSb.append(colName + " = ?");
			return paraValue;
		}

		
		if ("in".equalsIgnoreCase(c.condition)) {
			whereSb.append(colName + " in (" + paraValue + ")");
		} else {
			whereSb.append(colName + " " + (c.condition == null ? "=" : c.condition) + " ?");
			return paraValue;
		}

		return null;
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

	private QueryColumn getUpdateQueryColumn(Table table, Map<String, Object> wherePara) {
		StringBuffer sb = new StringBuffer();
		StringBuffer data = new StringBuffer();
		StringBuffer strWhere = new StringBuffer();

		boolean firstCol = true;
		QueryColumn queryColumn = new QueryColumn(SQL_TYPE.UPDATE);

		sb.append("update " + table.getName() + " set ");

		// set

		for (Column column : table.getColumnList()) {

			if (column.isPk() == false) {

				if (column.getOperator().isUpdatable() == false) {
					continue;
				}

				if (firstCol) {
					sb.append("\n ");
					firstCol = false;
				} else
					sb.append("\n , ");

				sb.append(column.getName() + " = ?");
				queryColumn.addColumn(column);

			}
		}

		// where

		for (Column column : table.getColumnList()) {
			if (column.isPk()) {
				if (strWhere.length() == 0) {
					strWhere.append("\n where ");
				} else {
					strWhere.append("\n and ");
				}
				strWhere.append(column.getName() + " = ?");
				queryColumn.addColumn(column);
			}
		}

		if (wherePara != null) {
			setWhere(table, queryColumn, strWhere, wherePara);
		}

		queryColumn.setSql(sb.toString() + data.toString() + strWhere.toString());

		return queryColumn;
	}

	private String getValue(Object value) {
		if (value == null)
			return "null";
		else if (value instanceof Double)
			return ((Double) value).doubleValue() + "";
		else if (value instanceof Float)
			return ((Float) value).floatValue() + "";
		else if (value instanceof Number)
			return ((Number) value).longValue() + "";

		String val = value.toString().trim();
		val = val.replaceAll("'", "\\\'");
		return "'" + val + "'";
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

		if (o instanceof Map) {
			Map map = (Map) o;
			for (Object value : sqlCol.getParaList()) {
				if (value instanceof Column) {
					paraList.add(map.get(((Column) value).getFieldName()));
				} else {
					paraList.add(value);
				}
			}
		} else if (o != null) {
			for (Object value : sqlCol.getParaList()) {
				if (value instanceof Column) {
					paraList.add(ObjectUtil.get(o, ((Column) value).getFieldName()));
				} else {
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

	private void setWhere(Table table, QueryColumn queryColumn, StringBuffer whereSb, Map<String, Object> wherePara) {
		List<Object> paraList = fillSqlWhere(whereSb, table, wherePara);

		for (Object obj : paraList) {
			queryColumn.addValue(obj);
		}
	}

}
