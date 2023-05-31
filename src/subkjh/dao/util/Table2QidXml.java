package subkjh.dao.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Table;
import subkjh.dao.exp.ParsingException;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.RetMappVo;
import subkjh.dao.model.SqlBean;
import subkjh.dao.model.SqlDeleteBean;
import subkjh.dao.model.SqlElement;
import subkjh.dao.model.SqlInsertBean;
import subkjh.dao.model.SqlSelectBean;
import subkjh.dao.model.SqlUpdateBean;
import subkjh.dao.model.TestSqlElement;

/**
 * 클래스를 이용하여 query.xml에 포함되는 insert, update, delete, select, result 문을 출력합니다.
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
class Table2QidXml {

	/**
	 * 접속할 데이터 베이스를 설정합니다.<br>
	 * 
	 * <br>
	 * <br>
	 * 사용법<br>
	 * <br>
	 * MakeQueryXml makeQueryXml = new MakeQueryXml(database);<br>
	 * makeQueryXml.add(c, tableName);<br>
	 * makeQueryXml.make();<br>
	 * 
	 * @param database 접속할 데이터베이스
	 */
	public Table2QidXml() {
	}

	public String make(Class<?>... classes) throws Exception {
		Table tables[] = new Table[classes.length];
		for (int i = 0; i < classes.length; i++) {
			tables[i] = convert(classes[i]);
		}
		return make(tables);
	}

	public String make(Table... tables) throws Exception {

		StringBuffer sb = new StringBuffer();
		List<SqlBean> sqlBeans = new ArrayList<SqlBean>();

		for (int i = 0; i < tables.length; i++) {
			List<SqlBean> tmp = convert(tables[i]);
			if (tmp != null)
				sqlBeans.addAll(tmp);
		}

		sb.append("<?xml version=\"1.0\" encoding=\"" + DaoUtil.CHARSET + "\" ?>" + "\n");
		sb.append("<queries>" + "\n");

		sb.append("\n");
		sb.append("<!--  -->\n");
		sb.append("<!-- Result Map -->\n");
		sb.append("<!--  -->\n");
		sb.append("\n");

		sb.append(getResultString(sqlBeans) + "\n");

		sb.append("\n");
		sb.append("<!--  -->\n");
		sb.append("<!-- SELECT QIDs -->\n");
		sb.append("<!--  -->\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlSelectBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--  -->\n");
		sb.append("<!-- INSERT QIDs -->\n");
		sb.append("<!--  -->\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlInsertBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--  -->\n");
		sb.append("<!-- UPDATE QIDs -->\n");
		sb.append("<!--  -->\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlUpdateBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--  -->\n");
		sb.append("<!-- DELETE QIDs -->\n");
		sb.append("<!--  -->\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlDeleteBean.class) + "\n");

		// sb.append( "\n");
		// sb.append( "<!--" + "\n");
		// sb.append(
		// "
		// ***************************************************************************
		// C R E A T E ***"
		// + "\n");
		// sb.append( "-->" + "\n");
		// sb.append( "\n");
		//
		// sb.append( getQueryString(sqlBeans, SqlInitBean.class) + "\n");

		sb.append("</queries>" + "\n");

		return sb.toString();
	}

	private Table convert(Class<?> c) {
		Table table = new Table();

		table.setName(DaoUtil.getField(c.getSimpleName()));

		Object obj = null;
		try {
			obj = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// Row row = null;
		// if (obj instanceof Row) {
		// row = (Row) obj;
		// List<Index> indexes = new ArrayList<Index>();
		// for (Index index : row.getKeys()) {
		// indexes.add(index);
		// }
		// table.setIdxList(indexes);
		//
		// if (row.getTableName() != null && row.getTableName().length() > 0) {
		// table.setTableName(row.getTableName());
		// }
		// }

		List<Column> columns = new ArrayList<Column>();
		String field;
		Method methods[] = c.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
				field = DaoUtil.getField(method.getName());

				if (method.getParameterTypes()[0] == Boolean.TYPE) {
					field = "IS_" + field;
				}

				if (field.equals("CLASS") == false) {
					Column column = new Column();
					column.setName(field);
					columns.add(column);
				}
			}
		}

		Collections.sort(columns, new Comparator<Column>() {
			@Override
			public int compare(Column o1, Column o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		table.setClassName(c.getName());
		table.setColumns(columns);

		return table;

	}

	private List<SqlBean> convert(Table table) throws Exception {
		List<SqlBean> sqlBeans = new ArrayList<SqlBean>();

		DaoResult resultBean = new DaoResult("RESULT_" + table.getName(), table.getClassName());
		for (Column column : table.getColumns()) {
			resultBean.add(new RetMappVo(column.getSetter() + "()", column.getName()));
		}

		SqlSelectBean selectBean = new SqlSelectBean("SELECT_" + table.getName(), resultBean, null);
		selectBean.getChildren().add(new SqlElement(getSelectSql(table)));
		selectBean.setDescr(table.getComment());
		setWhere(selectBean, table.getPkIndex());
		sqlBeans.add(selectBean);

		SqlInsertBean insertBean = new SqlInsertBean("INSERT_" + table.getName(), getInsertSql(table));
		insertBean.getChildren().add(new SqlElement(getInsertSql(table)));
		insertBean.setParaJavaClass(table.getClassName());
		sqlBeans.add(insertBean);

		SqlUpdateBean updateBean = new SqlUpdateBean("UPDATE_" + table.getName());
		updateBean.getChildren().add(new SqlElement(getUpdateSql(table)));
		updateBean.setParaJavaClass(table.getClassName());
		setWhere(updateBean, table.getPkIndex());
		sqlBeans.add(updateBean);

		SqlDeleteBean deleteBean = new SqlDeleteBean("DELETE_" + table.getName());
		deleteBean.getChildren().add(new SqlElement(getDeleteSql(table)));
		deleteBean.setParaJavaClass(table.getClassName());
		setWhere(deleteBean, table.getPkIndex());
		sqlBeans.add(deleteBean);

		// String schema = DaoUtil.getTableSchema(database, table.getTableName());
		// sqlBeans.add(new SqlInitBean("A00_DROP_" + table.getTableName(), "DROP TABLE
		// " + table.getTableName(), null));
		// sqlBeans.add(new SqlInitBean("A01_CREATE_" + table.getTableName(), schema,
		// null));

		return sqlBeans;

	}

	private String getDeleteSql(Table table) {
		String ret = "";

		ret += "delete\nfrom\t" + table.getName();

		return ret;
	}

	private String getInsertSql(Table table) {

		StringBuffer ret = new StringBuffer();

		List<Column> colList = table.getColumns();
		String method;

		ret.append("insert into \n");
		ret.append("\t").append(to32(table.getName())).append(" /*").append(table.getComment()).append(" */\n ( \n");

		for (int i = 0, size = colList.size(); i < size; i++) {
			ret.append("\t").append(i == 0 ? "  " : ", ");
			ret.append(to32(colList.get(i).getName()));
			ret.append(" /*").append(colList.get(i).getComments()).append(" */\n");
		}
		ret.append(") values (\n");

		for (int i = 0, size = colList.size(); i < size; i++) {
			method = "$" + DaoUtil.getGetter(colList.get(i).getName()) + "()";
			ret.append("\t").append(i == 0 ? "  " : ", ").append(to32(method));
			ret.append(" /*").append(colList.get(i).getComments()).append(" */\n");
		}
		ret.append(")");

		return ret.toString();

	}

	private String getQueryString(List<SqlBean> sqlBeans, Class<?> c) {
		String ret = "";
		for (SqlBean sqlBean : sqlBeans) {
			if (sqlBean.getClass() == c) {
				ret += SqlBean.addPrevString(sqlBean.getXml(), "\t");
				ret += "\n";
			}
		}
		return ret;
	}

	private String getResultString(List<SqlBean> sqlBeans) {
		String ret = "";
		for (SqlBean sqlBean : sqlBeans) {
			if (sqlBean instanceof SqlSelectBean) {
				ret += SqlBean.addPrevString(((SqlSelectBean) sqlBean).getResultMap().getXml(), "\t");
				ret += "\n";
			}
		}
		return ret;
	}

	private String getSelectSql(Table table) {
		StringBuffer sb = new StringBuffer();

		for (Column col : table.getColumns()) {
			if (sb.length() == 0) {
				sb.append("select   ");
			} else {
				sb.append("        ,");
			}
			sb.append(" a.").append(to32(col.getName())).append(" /*").append(col.getComments()).append(" */\n");
		}
		sb.append("from    ").append(table.getName()).append(" a  /*").append(table.getComment()).append(" */");

		return sb.toString();

	}

	private String to32(String s) {
		if (s.length() >= 32)
			return s;
		StringBuffer sb = new StringBuffer();
		sb.append(s);
		for (int i = s.length(); i <= 32; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private String getUpdateSql(Table table) {

		StringBuffer sb = new StringBuffer();
		List<Column> colList = table.getColumns();
		Column col;
		String method;

		sb.append("update ").append(table.getName()).append(" set\n");

		for (int i = 0, size = colList.size(); i < size; i++) {

			col = colList.get(i);
			method = "$" + DaoUtil.getGetter(col.getName()) + "()";

			sb.append("\t").append(i > 0 ? ", " : "  ");

			sb.append(to32(col.getName())).append(" = ").append(to32(method));

			sb.append(" /*").append(col.getComments()).append(" */\n");
		}

		return sb.toString();
	}

	private String getVarName(String name) {
//		return Column.getJavaFieldName(name);
		return DaoUtil.getGetter(name) + "()";
	}

	private void setWhere(SqlBean bean, Index index) throws ParsingException {

		if (index == null || index.getColumnNames().size() == 0)
			return;

		bean.getChildren().add(new SqlElement("where 1 = 1"));

		String var;

		for (String col : index.getColumnNames()) {
			var = getVarName(col);
			bean.getChildren().add(new TestSqlElement(var, "not null", "and " + col + " = " + "$" + var));
		}
	}

}
