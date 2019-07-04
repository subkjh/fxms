package subkjh.bas.fxdao.beans;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.define.SQL_TYPE;

public class QueryColumn {

	private String sql;

	private List<Object> paraList;

	private SQL_TYPE type;

	public QueryColumn(SQL_TYPE type) {
		this.type = type;
		paraList = new ArrayList<Object>();
	}

	public void addColumn(Column column) {
		paraList.add(column);
	}

	public void addValue(Object value) {
		paraList.add(value);
	}

	public List<Object> getParaList() {
		return paraList;
	}

	public String getSql() {
		return sql;
	}

	public SQL_TYPE getType() {
		return type;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
