package subkjh.dao.model;

import subkjh.dao.def.Query.SQL_TYPE;

/**
 * 
 * @author subkjh
 *
 */
public class QueryInfo {

	private String sql;
	private final SQL_TYPE type;

	public QueryInfo(SQL_TYPE type) {
		this.type = type;
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
