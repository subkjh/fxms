package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;

import subkjh.dao.def.Column;
import subkjh.dao.def.Query.SQL_TYPE;

/**
 * 
 * @author subkjh
 *
 */
public class QueryColumn {

	private String sql;

	private List<Object> paraList;

	private SQL_TYPE type;

	public QueryColumn(SQL_TYPE type) {
		this.type = type;
		paraList = new ArrayList<>();
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
