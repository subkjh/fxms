package fxms.bas.impl.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.impl.api.ValueSaver;
import fxms.bas.vo.PsItem;

/**
 * 기록할 성능을 나타냄
 * 
 * @author subkjh
 * 
 */
public class PsValTable {

	class COL {
		final int index;
		final String column;
		final ValueSaver.TYPE type;
		final PsItem psItem;

		COL(int index, PsItem psItem, String column, ValueSaver.TYPE type) {
			this.index = index;
			this.psItem = psItem;
			this.column = column;
			this.type = type;
		}
	}

	private final List<COL> columnList; // DB 테이블 컬럼명들
	private final String realPsTable; // DB 테이블 명
	private final String psTable;
	private final Map<Long, Object[]> valueMap; // key=MO_NO + INSTANCE, 값

	/**
	 * 
	 * @param tableName 테이블 명<br>
	 *                  성능 내용을 기록할 테이블
	 */
	public PsValTable(String realPsTable, String psTable) {
		this.realPsTable = realPsTable;
		this.psTable = psTable;
		this.columnList = new ArrayList<COL>();
		this.valueMap = new HashMap<>();
	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param item   성능항목. 없으면 null
	 * @param column 컬럼명
	 * @param type   컬럼의 자료형
	 * 
	 * @return 성능항목의 인덱스
	 */
	public int addColumn(PsItem item, String column, ValueSaver.TYPE type) {

		int index = this.columnList.size();

		this.columnList.add(new COL(index, item, column, type));

		return index;
	}

	public int getColSize() {
		return this.columnList.size();
	}

	/**
	 * 성능항목에 대한 인덱스를 제공합니다.<br>
	 * . 0부터 시작합니다.
	 * 
	 * @param perfNo
	 * @return 인덱스
	 */
	public int getIndexPsId(String psId) {
		COL col = getColumn(psId);
		return col != null ? col.index : -1;
	}

	public PsItem getItem(String psId) {
		COL col = getColumn(psId);
		return col != null ? col.psItem : null;
	}

	public String getPsTable() {
		return psTable;
	}

	public String getRealPsTable() {
		return realPsTable;
	}

	public String getSqlInsert() {

		StringBuffer sb = new StringBuffer();

		sb.append("insert into ").append(realPsTable).append(" ( ");
		sb.append(columnList.get(0).column);
		for (int i = 1; i < columnList.size(); i++) {
			sb.append(", ");
			sb.append(columnList.get(i).column);
		}
		sb.append(" ) values ( ? ");
		for (int i = 1; i < columnList.size(); i++) {
			sb.append(", ?");
		}
		sb.append(" )");

		return sb.toString();
	}

	public Object[] getValue(long moNo) {
		return valueMap.get(moNo);
	}

	public List<Object[]> getValueArrayList() {
		List<Object[]> valueList = new ArrayList<Object[]>();
		for (Object[] value : getValueMap().values()) {
			valueList.add(value);

		}

		return valueList;
	}

	public Map<Long, Object[]> getValueMap() {
		return valueMap;
	}

	public void putValue(long moNo, Object[] valueArray) {
		valueMap.put(moNo, valueArray);
	}

	private COL getColumn(String psId) {
		for (COL col : this.columnList) {
			if (col.psItem != null && col.psItem.getPsId().equals(psId)) {
				return col;
			}
		}
		return null;
	}

}
