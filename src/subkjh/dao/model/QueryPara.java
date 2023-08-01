package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;

import subkjh.dao.def.Query.SQL_TYPE;

/**
 * 
 * @author subkjh
 *
 */
public class QueryPara extends QueryInfo {

	private final List<Object> paraList;

	public QueryPara(SQL_TYPE type) {
		super(type);
		paraList = new ArrayList<>();
	}

	public QueryPara(SQL_TYPE type, String sql, List<Object> para) {
		super(type);
		setSql(sql);
		this.paraList = para;
	}

	public void addPara(Object value) {
		paraList.add(value);
	}

	public Object[] getParaList() {
		return paraList.toArray(new Object[paraList.size()]);
	}

}
