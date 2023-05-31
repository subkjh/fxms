package subkjh.dao.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import subkjh.dao.exp.ParsingException;
import subkjh.dao.util.DaoUtil;

public class SqlElement {

	/** XML 화일의 오리지널 SQL 문장 */
	private String sql;
	private List<SqlVar> varList;
	/** 파싱된 SQL */
	private String sqlParsed;

	public SqlElement(String sql) throws ParsingException {

		if (sql == null || sql.trim().length() == 0) {
			this.sql = sql;
			return;
		}

		SqlParser parser = new SqlParser(sql);
		parser.parse();
		this.sql = sql;
		varList = parser.getSqlVars();
		sqlParsed = parser.getStrParsed();
	}

	/**
	 * XML 화일에 정의된 SQL 문을 제공합니다.
	 * 
	 * @return XML 화일에 정의된 SQL 문
	 */
	public String getSql() {
		return sql;
	}

	public String getSql(Object obj) {

		if (sqlParsed == null)
			return "";

		String retSql = sqlParsed;

		if (obj != null && varList != null) {
			for (SqlVar var : varList) {
				if (var.isReplace()) {
					Object val = getValue(obj, var);
					retSql = retSql.replaceFirst(SqlVar.REPLACE_TAG, val == null ? "" : val.toString());
				}
			}
		}

		return retSql.trim() + " ";
	}

	public String getSqlParsed() {
		return sqlParsed;
	}

	public List<SqlVar> getVarList() {
		return varList;
	}

	/**
	 * 
	 * @return
	 */
	public String getVars() {
		if (varList == null || varList.size() == 0)
			return "";

		String ret = "";
		for (SqlVar var : varList) {
			ret += ", " + (var.isReplace() ? "#" : "$");
			ret += var.name;
			ret += var.isMethod() ? "()" : "";
		}
		return ret.substring(2);
	}

	/**
	 * 
	 * 
	 * @return 필요한 인수 갯수
	 */
	public int getVarSize() {
		return varList == null ? 0 : varList.size();
	}

	@Override
	public String toString() {
		return sqlParsed + "|" + varList;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	protected List<Object> getPara4Obj(Object obj) {

		if (varList != null) {
			List<Object> list = new ArrayList<Object>();
			for (SqlVar var : varList) {
				if (var.isReplace() == false) {
					list.add(getValue(obj, var));
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 객체에서 변수에 해당되는 값을 찾아 제공합니다.
	 * 
	 * @param obj 객체
	 * @param var 변수
	 * @return 객체에서 변수에 해당되는 값
	 */
	@SuppressWarnings("rawtypes")
	public static Object getValue(Object obj, SqlVar var) {

		Object ret = null;

		if (obj == null)
			return null;

		// primitive 값이면 그냥 그 객체를 넘긴다.
		if (obj != null && DaoUtil.isPrimitive(obj.getClass())) {
			ret = obj;
		} else if (obj instanceof Map) {
			ret = ((Map) obj).get(var.name);
		} else if (var.isField()) {
			try {
				Field f = obj.getClass().getField(var.name);
				f.setAccessible(true);
				ret = f.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (var.isMethod()) {

			Method method = null;
			try {
				method = obj.getClass().getMethod(var.name);
				try {
					ret = method.invoke(obj, (Object[]) null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

		} else {
			ret = obj;
		}

		if (ret instanceof Boolean) {
			ret = ((Boolean) ret).booleanValue() ? "Y" : "N";
		}
		return ret;
	}

}
