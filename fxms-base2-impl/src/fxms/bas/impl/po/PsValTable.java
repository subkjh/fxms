package fxms.bas.impl.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.po.item.PsItem;

/**
 * 기록할 성능을 나타냄
 * 
 * @author subkjh
 * 
 */
public class PsValTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6093637241683001160L;

	/** DB 테이블 컬럼명들 */
	private List<String> columnList;

	/** DB 테이블 명 */
	private String realPsTable;

	private String psTable;

	private List<PsItem> itemList;

	/** 컬럼속성 */
	private List<ValueSaver.TYPE> columnTypeList;

	/** key=MO_NO + INSTANCE, 값 */
	private Map<String, Object[]> valueMap;

	/**
	 * 
	 * @param tableName
	 *            테이블 명<br>
	 *            성능 내용을 기록할 테이블
	 */
	public PsValTable(String realPsTable, String psTable) {
		this.realPsTable = realPsTable;
		this.psTable = psTable;

		columnList = new ArrayList<String>();
		columnTypeList = new ArrayList<ValueSaver.TYPE>();
		itemList = new ArrayList<PsItem>();
		valueMap = new HashMap<String, Object[]>();
	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param item
	 *            성능항목. 없으면 null
	 * @param column
	 *            컬럼명
	 * @param type
	 *            컬럼의 자료형
	 */
	public void addColumn(PsItem item, String column, ValueSaver.TYPE type) {
		itemList.add(item);
		columnList.add(column);
		columnTypeList.add(type);
	}

	public int getColumnSize()

	{
		return columnList.size();
	}

	public String getColumnString() {
		String sql;

		sql = columnList.get(0);
		for (int i = 1, size = columnList.size(); i < size; i++)
			sql += ", " + columnList.get(i);

		return sql;
	}

	public List<String> getDbColumns() {
		return columnList;
	}

	public String getRealPsTable() {
		return realPsTable;
	}

	/**
	 * 성능항목에 대한 인덱스를 제공합니다.<br>
	 * . 0부터 시작합니다.
	 * 
	 * @param perfNo
	 * @return 인덱스
	 */
	public int getIndexForPerfNo(String psCode) {

		if (itemList == null)
			return -1;

		for (int i = 0, size = itemList.size(); i < size; i++) {
			if (itemList.get(i) != null && itemList.get(i).getPsCode().equals(psCode))
				return i;
		}

		return -1;
	}

	public PsItem getItem(String psCode) {
		for (PsItem item : itemList) {
			if (item != null && item.getPsCode().equals(psCode))
				return item;
		}
		return null;
	}

	public String getPsTable() {
		return psTable;
	}

	public String getQuerySelectAvg(String tableName, String whereStr, String... columns) {

		String sql = "select \n";
		if (columns != null && columns.length > 0) {
			sql += " " + columns[0];
			for (int index = 1; index < columns.length; index++) {
				sql += ", " + columns[index];
			}
		}

		if (columns != null && columns.length > 0)
			sql += ", ";

		sql += " avg(" + columnList.get(0) + ") " + columnList.get(0);
		for (int i = 0, size = columnList.size(); i < size; i++)
			sql += ", avg(" + columnList.get(i) + ") " + columnList.get(i);

		sql += "\n from " + tableName;

		sql += "\n " + whereStr;

		if (columns != null && columns.length > 0) {
			sql += "\n group by " + columns[0];
			for (int index = 1; index < columns.length; index++) {
				sql += "\t, " + columns[index] + "\n";
			}
		}

		return sql;
	}

	/**
	 * SELECT
	 * 
	 * @param tableName
	 * @param columns
	 * @return SELECT문 쿼리
	 */
	public String getSelectQuery(String tableName, String... columns) {
		String sql = "SELECT \n";

		sql += "\t" + columnList.get(0) + "\n";
		for (int i = 1, size = columnList.size(); i < size; i++)
			sql += "\t, " + columnList.get(i) + "\n";

		for (String col : columns)
			sql += "\t, " + col + "\n";

		sql += "FROM\t" + tableName;

		return sql;
	}

	public String getSqlInsert() {

		StringBuffer sb = new StringBuffer();

		sb.append("insert into " + realPsTable + " ( ");
		sb.append(columnList.get(0));
		for (int i = 1; i < columnList.size(); i++) {
			sb.append(", ");
			sb.append(columnList.get(i));
		}
		sb.append(" ) values ( ");
		sb.append("?");
		for (int i = 1; i < columnList.size(); i++) {
			sb.append(", ?");
		}
		sb.append(" )");

		return sb.toString();
	}

	public Object[] getValue(long moNo, String instance) {
		if (instance == null)
			return valueMap.get(moNo + "");
		else
			return valueMap.get(moNo + "\t" + instance);
	}

	public List<Object[]> getValueArrayList() {
		List<Object[]> valueList = new ArrayList<Object[]>();
		for (Object[] value : getValueMap().values()) {
			valueList.add(value);

		}

		return valueList;
	}

	public Map<String, Object[]> getValueMap() {
		return valueMap;
	}

	public void putValue(long moNo, String instance, Object[] valueArray) {
		if (instance == null)
			valueMap.put(moNo + "", valueArray);
		else
			valueMap.put(moNo + "\t" + instance, valueArray);
	}

	public void setColumnList(List<String> dbColumns) {
		this.columnList = dbColumns;
	}

	public void setColumnTypeList(List<ValueSaver.TYPE> types) {
		this.columnTypeList = types;
	}
}
