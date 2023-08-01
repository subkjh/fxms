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
public class QueryColumn extends QueryInfo {

	private List<Column> columns;

	public QueryColumn(SQL_TYPE type) {
		super(type);
		this.columns = new ArrayList<>();
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	public List<Column> getColumns() {
		return columns;
	}

}
