package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;

import subkjh.dao.exp.ParsingException;

public class TestSqlElement extends SqlElement {

	// public static void main(String[] args) throws Exception {
	// TestSqlElement bean;
	// Object value = "우리나라";
	//
	// bean = new TestSqlElement("getName()", "not null", " and NAME = $getName() or
	// NAME = #getName()");
	// System.out.println(bean.getSqlSub(value));
	// // System.out.println(bean.getVar());
	//
	// bean = new TestSqlElement("getName()", "not null", " and NAME = #getName() or
	// NAME = #getName()");
	// System.out.println(bean.getSqlSub(value));
	//
	// bean = new TestSqlElement("getName()", "not null", " and NAME = #getName()");
	// value = "우리나라";
	// System.out.println(bean.getSqlSub(value));
	// // System.out.println(bean.getVar());
	// //
	// value = null;
	// System.out.println(bean.getSqlSub(value));
	//
	// bean = new TestSqlElement("name", "not null", " and NAME = $name");
	// value = "우리나라";
	// System.out.println(bean.getSqlSub(value));
	//
	// value = null;
	// System.out.println(bean.getSqlSub(value));
	//
	// bean = new TestSqlElement("name", "not null", " and NAME = '#name'");
	// value = "우리나라";
	// System.out.println(bean.getSqlSub(value));
	//
	// value = null;
	// System.out.println(bean.getSqlSub(value));
	//
	// bean = new TestSqlElement("name", "6", " and NAME = '#name'");
	// value = "우리나라";
	// System.out.println(bean.getSqlSub(value));
	//
	// value = null;
	// System.out.println(bean.getSqlSub(value));
	//
	// }

	private List<SqlElement> children;
	private String equals;
	private SqlVar sqlVar;

	/**
	 * 
	 * @param var
	 * @param equals
	 * @param sql
	 * @throws ParsingException
	 */
	public TestSqlElement(String var, String equals, String sql) throws ParsingException {

		super(sql);

		this.equals = (equals != null ? equals : "not null");

		byte varType;
		if (var.indexOf("()") > 0) {
			varType = SqlVar.VAR_TYPE_METHOD;
			var = var.replaceFirst("\\(\\)", "");
		} else {
			varType = SqlVar.VAR_TYPE_FIELD;
		}

		sqlVar = new SqlVar(var, varType);

	}

	public List<SqlElement> getChildren() {

		if (children == null) {
			children = new ArrayList<SqlElement>();
		}

		return children;
	}

	@Override
	public String getSql(Object obj) {

		if (children == null) {
			return super.getSql(obj);
		}

		StringBuffer sb = new StringBuffer();

		TestSqlElement te;

		for (SqlElement e : children) {
			if (e instanceof TestSqlElement) {
				te = (TestSqlElement) e;
				if (te.isAddable(obj)) {
					sb.append(e.getSql(obj));
					sb.append("\n");
				}
			} else {
				sb.append(e.getSql(obj).trim());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String getVar() {
		return sqlVar.getVarNameOrg();
	}

	/**
	 * 
	 * 
	 * @param value
	 * @return 입력된 값으로 내가 추가되는지 여부
	 */
	public boolean isAddable(Object obj) {

		if (equals == null)
			return false;

		Object value = getValue(obj, sqlVar);

		// not null이고 자료가 있어야함 true입니다.
		if (equals.equalsIgnoreCase("not null") && value != null && value.toString().length() > 0) {
			return true;
		}

		if (equals.equalsIgnoreCase("null") && value == null) {
			return true;
		}

		if (value == null)
			return false;

		return equals.equals(value);
	}

	@Override
	public String toString() {
		return sqlVar + "|" + equals + "," + super.toString();
	}

	@Override
	protected List<Object> getPara4Obj(Object obj) {
		List<Object> paraList = new ArrayList<Object>();
		List<Object> entry;

		entry = super.getPara4Obj(obj);
		if (entry != null) {
			paraList.addAll(entry);
		}

		TestSqlElement te;

		for (SqlElement e : children) {
			if (e instanceof TestSqlElement) {
				te = (TestSqlElement) e;
				if (te.isAddable(obj)) {
					entry = te.getPara4Obj(obj);
					if (entry != null) {
						paraList.addAll(entry);
					}
				}
			}
		}

		return paraList;
	}
}
