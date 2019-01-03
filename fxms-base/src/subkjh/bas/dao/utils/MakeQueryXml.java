package subkjh.bas.dao.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.SoDo;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.exception.ParsingException;
import subkjh.bas.dao.model.ResultBean;
import subkjh.bas.dao.model.ResultMappingBean;
import subkjh.bas.dao.model.SqlBean;
import subkjh.bas.dao.model.SqlDeleteBean;
import subkjh.bas.dao.model.SqlElement;
import subkjh.bas.dao.model.SqlInsertBean;
import subkjh.bas.dao.model.SqlSelectBean;
import subkjh.bas.dao.model.SqlUpdateBean;
import subkjh.bas.dao.model.TestSqlElement;

/**
 * 클래스를 이용하여 query.xml에 포함되는 insert, update, delete, select, result 문을 출력합니다.
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
class MakeQueryXml extends SoDo {

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
	 * @param database
	 *            접속할 데이터베이스
	 */
	public MakeQueryXml() {
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

		sb.append("<?xml version=\"1.0\" encoding=\"" + CHARSET + "\" ?>" + "\n");
		sb.append("<queries>" + "\n");

		sb.append("\n");
		sb.append("<!--" + "\n");
		sb.append(LINE + "\n");
		sb.append("# R E S U L T\n");
		sb.append(LINE + "\n");
		sb.append("-->" + "\n");
		sb.append("\n");

		sb.append(getResultString(sqlBeans) + "\n");

		sb.append("\n");
		sb.append("<!--" + "\n");
		sb.append(LINE + "\n");
		sb.append("# S E L E C T\n");
		sb.append(LINE + "\n");
		sb.append("-->" + "\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlSelectBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--" + "\n");
		sb.append(LINE + "\n");
		sb.append("# I N S E R T\n");
		sb.append(LINE + "\n");
		sb.append("-->" + "\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlInsertBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--" + "\n");
		sb.append(LINE + "\n");
		sb.append("# U P D A T E\n");
		sb.append(LINE + "\n");
		sb.append("-->" + "\n");
		sb.append("\n");

		sb.append(getQueryString(sqlBeans, SqlUpdateBean.class) + "\n");

		sb.append("\n");
		sb.append("<!--" + "\n");
		sb.append(LINE + "\n");
		sb.append("# D E L E T E\n");
		sb.append(LINE + "\n");
		sb.append("-->" + "\n");
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

		table.setName(Column.getField(c.getSimpleName()));

		List<Column> columns = new ArrayList<Column>();
		String field;
		Method methods[] = c.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
				field = Column.getField(method.getName());

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
		table.setColList(columns);

		return table;

	}

	private List<SqlBean> convert(Table table) throws Exception {
		List<SqlBean> sqlBeans = new ArrayList<SqlBean>();

		ResultBean resultBean = new ResultBean("RESULT_" + table.getName(), table.getClassName());
		for (Column column : table.getColumnList()) {
			resultBean.add(new ResultMappingBean(column.getSetter() + "()", column.getName()));
		}

		SqlSelectBean selectBean = new SqlSelectBean("SELECT_" + table.getName(), resultBean, null);
		selectBean.getChildren().add(new SqlElement(getSelectSql(table)));
		selectBean.setDescr(table.getComment());
		sqlBeans.add(selectBean);

		if (table.getIndexList() != null) {
			for (Index index : table.getIndexList()) {
				setWhere(selectBean, index);
			}
		}

		SqlInsertBean insertBean = new SqlInsertBean("INSERT_" + table.getName(), getInsertSql(table));
		insertBean.getChildren().add(new SqlElement(getInsertSql(table)));
		insertBean.setParaJavaClass(table.getClassName());
		sqlBeans.add(insertBean);

		SqlUpdateBean updateBean = new SqlUpdateBean("UPDATE_" + table.getName());
		updateBean.getChildren().add(new SqlElement(getUpdateSql(table)));
		updateBean.setParaJavaClass(table.getClassName());
		sqlBeans.add(updateBean);

		if (table.getIndexList() != null) {

			for (Index index : table.getIndexList()) {
				setWhere(updateBean, index);
				sqlBeans.add(updateBean);
			}
		}

		SqlDeleteBean deleteBean = new SqlDeleteBean("DELETE_" + table.getName());
		deleteBean.getChildren().add(new SqlElement(getDeleteSql(table)));
		deleteBean.setParaJavaClass(table.getClassName());
		sqlBeans.add(deleteBean);

		if (table.getIndexList() != null) {
			for (Index index : table.getIndexList()) {
				setWhere(deleteBean, index);
			}
		}

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
		String ret = "";

		List<Column> colList = table.getColumnList();

		ret += "insert into " + table.getName() + " (\n";
		String fields = "\t" + colList.get(0).getName() + "\n";

		for (int i = 1, size = colList.size(); i < size; i++) {
			fields += "\t, " + colList.get(i).getName() + "\n";
		}
		ret += fields;

		ret += ") values (\n";

		fields = "\t" + "$" + Column.getGetter(colList.get(0).getName()) + "()\n";
		for (int i = 1, size = colList.size(); i < size; i++) {
			fields += "\t, " + "$" + Column.getGetter(colList.get(i).getName()) + "()\n";
		}
		ret += fields;

		ret += ")";

		return ret;
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
		List<Column> columns = table.getColumnList();
		String ret = "select";

		String fields = "\t" + columns.get(0).getName() + "\n";

		for (int i = 1, size = columns.size(); i < size; i++) {
			fields += "\t, " + columns.get(i).getName() + "\n";
		}

		ret += fields;

		ret += "from\t" + table.getName();

		return ret;

	}

	private String getUpdateSql(Table table) {
		String ret = "";

		List<Column> colList = table.getColumnList();

		ret += "update " + table.getName() + " set\n";

		String fields = "\t" + colList.get(0).getName() + " = " + "$" + Column.getGetter(colList.get(0).getName())
				+ "()\n";

		for (int i = 1, size = colList.size(); i < size; i++) {
			fields += "\t, " + colList.get(i).getName() + " = " + "$" + Column.getGetter(colList.get(i).getName())
					+ "()\n";
		}

		ret += fields;

		return ret;
	}

	private void setWhere(SqlBean bean, Index index) throws ParsingException {

		if (index.getColumnNameList().size() == 0)
			return;

		bean.getChildren().add(new SqlElement("where 1 = 1"));

		String var;

		for (int i = 0; i < index.getColumnNameList().size(); i++) {

			var = Column.getGetter(index.getColumnNameList().get(i)) + "()";

			bean.getChildren().add(
					new TestSqlElement(var, "not null", "and " + index.getColumnNameList().get(i) + " = " + "$" + var));
		}
	}

}
