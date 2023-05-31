package subkjh.dao.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.dao.exp.ParsingException;

public class QueryResult {

	private StringBuffer sqlSb;

	private List<Object> paraList;

	/** 결과 객체 */
	private Class<?> classOfResult;

	/** 테이블 객체 필드 */
	private List<Field> fieldList;
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
		return fieldList;
	}

	private Field getField(String name, Class<?> classOf) {
		try {
			Field field = classOf.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			if (classOf.getSuperclass() != null) {
				return getField(name, classOf.getSuperclass());
			} else {
				return null;
			}
		}
	}

	public Field getResultField(int index) {

		if (resultFieldList == null) {
			resultFieldList = new ArrayList<Field>();
		}

		if (resultFieldList.size() > index) {
			return resultFieldList.get(index);
		}

		try {
			Field field = getField(fieldList.get(index).getName(), classOfResult);
			if (field != null) {
				field.setAccessible(true);
			} else {
				// System.out.println("Not found field " +
				// fieldList.get(index).getName());
			}
			resultFieldList.add(field);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
			resultFieldList.add(null);
			return null;
		}
	}

	public int getFieldSize() {
		return fieldList.size();
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
		this.fieldList = resultFieldList;
	}

	public void setSql(String sql) {
		this.sqlSb = new StringBuffer(sql);
	}

}
