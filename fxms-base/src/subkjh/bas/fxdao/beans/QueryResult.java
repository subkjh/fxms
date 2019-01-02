package subkjh.bas.fxdao.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.bas.dao.exception.ParsingException;
import subkjh.bas.dao.model.SqlElement;
import subkjh.bas.dao.model.SqlParser;
import subkjh.bas.dao.model.SqlVar;

public class QueryResult {

	private StringBuffer sqlSb;

	private List<Object> paraList;

	private Class<?> classOfResult;

	private List<Field> resultFieldList;

	public QueryResult() {

	}

	public StringBuffer getSqlStringBuffer() {
		if (sqlSb == null) {
			sqlSb = new StringBuffer();
		}
		return sqlSb;
	}

	public QueryResult(String sql, List<Object> paraList) {
		this.sqlSb = new StringBuffer(sql);
		this.paraList = paraList;
	}

	public QueryResult(String sql, Object obj) throws ParsingException {

		SqlParser parser = new SqlParser(sql);
		parser.parse();

		String retSql = parser.getStrParsed();

		if (obj != null && parser.getSqlVars() != null) {
			for (SqlVar var : parser.getSqlVars()) {
				if (var.isReplace()) {
					Object val = SqlElement.getValue(obj, var);
					retSql = retSql.replaceFirst(SqlVar.REPLACE_TAG, val == null ? "" : val.toString());
				}
			}
		}

		this.sqlSb = new StringBuffer(retSql);

		if (parser.getSqlVars() != null) {
			List<Object> list = new ArrayList<Object>();
			for (SqlVar var : parser.getSqlVars()) {
				if (var.isReplace() == false) {
					list.add(SqlElement.getValue(obj, var));
				}
			}

			this.paraList = list;
		}
	}

	public QueryResult(String sql, Object[] value) {
		this.sqlSb = new StringBuffer(sql);
		this.paraList = value == null ? null : Arrays.asList(value);
	}

	public Class<?> getClassOfResult() {
		return classOfResult;
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();
		sb.append("*************************************************************************\n");
		sb.append("SQL(");
		sb.append(sqlSb);
		sb.append(")");
		sb.append("\nPARA(");
		sb.append(paraList);
		sb.append(")");
		return sb.toString();
	}

	public String getExecutableSql() {
		StringBuffer sb = new StringBuffer();

		int i = 0;
		char chs[] = sqlSb.toString().toCharArray();
		for (char ch : chs) {
			if (ch == '?') {

				if (paraList.get(i) instanceof Number) {
					sb.append(paraList.get(i));
				} else {
					sb.append("'");
					sb.append(paraList.get(i));
					sb.append("'");
				}
				i++;
				continue;
			}

			sb.append(ch);
		}

		return sb.toString();
	}

	/**
	 * @return the value
	 */
	public Object[] getParaArray() {
		return getParaList().toArray(new Object[getParaList().size()]);
	}

	public List<Field> getResultFieldList() {
		return resultFieldList;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sqlSb.toString();
	}

	public void setClassOfResult(Class<?> classOfResult) {
		this.classOfResult = classOfResult;
	}

	public List<Object> getParaList() {
		if (paraList == null) {
			paraList = new ArrayList<Object>();
		}
		return paraList;
	}

	public void setParaList(List<Object> paraList) {
		this.paraList = paraList;
	}

	public void setResultFieldList(List<Field> resultFieldList) {
		this.resultFieldList = resultFieldList;
	}

	public void setSql(String sql) {
		this.sqlSb = new StringBuffer(sql);
	}

}
