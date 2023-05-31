package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 실제 실행에 사용되는 SQL 내용
 * 
 * @author subkjh
 * 
 */
public class BatchVo {

	private final String query; // 쿼리
	private final List<Object[]> values; // 배치처리 값 목록

	BatchVo(String query) {
		this.query = query;
		this.values = new ArrayList<>();
	}

	/**
	 * 열 값을 추가합니다.
	 * 
	 * @param value
	 */
	public void addValue(Object value[]) {
		values.add(value);
	}

	public String getSql() {
		return query;
	}

	/**
	 * 값 목록을 제공합니다.
	 * 
	 * @return 값 목록
	 */
	public List<Object[]> getValues() {
		return values;
	}

}
