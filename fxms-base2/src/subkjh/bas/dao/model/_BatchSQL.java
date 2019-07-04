package subkjh.bas.dao.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 실제 실행에 사용되는 SQL 내용
 * 
 * @author subkjh
 * 
 */
public class _BatchSQL {

	private String sql;

	/**
	 * 값
	 */
	private List<Object[]> values;

	_BatchSQL() {

	}

	_BatchSQL(String sql, Object[]... value) {
		this.sql = sql;
		
	}

	/**
	 * 열 값을 추가합니다.
	 * 
	 * @param value
	 */
	public void addValue(Object value[]) {
		if (values == null) values = new ArrayList<Object[]>();
		values.add(value);
	}

	public String getSql() {
		return sql;
	}

	public Object[] getValue(int index) {
		return values.get(index);
	}

	/**
	 * 값 목록을 제공합니다.
	 * 
	 * @return 값 목록
	 */
	public List<Object[]> getValues() {
		return values;
	}

	public int getValueSize() {
		return values == null ? 0 : values.size();
	}

	/**
	 * raplace가 존재할 때 SQL문이 여러개가 된다.
	 * 
	 * @param sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

}
