package subkjh.dao.def;

import java.util.ArrayList;
import java.util.List;

import subkjh.dao.util.DaoUtil;

/**
 * 테이블의 인덱스를 나타냅니다.
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Index {

	public enum INDEX_TYPE {

		PK, UK, FK, KEY;

		public static INDEX_TYPE getType(String name) {
			for (INDEX_TYPE key : INDEX_TYPE.values()) {
				if (key.name().equalsIgnoreCase(name))
					return key;
			}
			return null;
		}

	}

	private final String indexName;

	/** PK, UK, FK, KEY */
	private final INDEX_TYPE indexType;

	/** 인덱스를 구성하는 컬럼명 */
	private final List<String> columnNames;

	private String fkTable;

	private String fkColumn;

	/** 테이블 기준 인덱스 번호 */
	private int no;

	/**
	 * 
	 * @param indexName
	 * @param indexType
	 */
	public Index(String indexName, INDEX_TYPE indexType) {
		this.indexName = indexName;
		this.indexType = indexType;
		this.columnNames = new ArrayList<String>();
	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param s
	 */
	public void addColumn(String str) {

		String column = str.replaceAll("\\[|\\]", "");

		if (column.indexOf('@') > 0) {
			String ss[] = column.split("\\@");
			fkTable = ss[1].trim();
			if (ss[0].indexOf(':') > 0) {
				String s[] = ss[0].split(":");
				fkColumn = s[1].trim();
				columnNames.add(s[0].trim().toUpperCase());
			} else {
				fkColumn = ss[0].trim();
				columnNames.add(ss[0].trim().toUpperCase());
			}
		} else {
			column = column.replaceAll("\\+", ",");
			String ss[] = column.split(",");
			for (String s : ss) {
				if (s.trim().length() > 0)
					columnNames.add(s.trim().toUpperCase());
			}
		}
	}

	public void clearColumns() {
		this.columnNames.clear();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Index) {
			Index idx = (Index) obj;
			if (idx.indexName.equals(indexName) && idx.indexType == indexType) {
				if (idx.columnNames.size() == columnNames.size()) {
					for (int i = 0; i < columnNames.size(); i++) {
						if (idx.columnNames.get(i).equals(columnNames.get(i)) == false)
							return false;
					}
					return true;
				}
			}
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * 컬럼 목록을 하나의 문자열로 리턴합니다.
	 * 
	 * @param separator 컬럼 구분자
	 * @return 컬럼 목록 문자열
	 */
	public String getColumnAll(String separator) {

		String columns = columnNames.get(0);
		for (int index = 1; index < columnNames.size(); index++) {
			columns += separator + columnNames.get(index);
		}

		return columns;
	}

	/**
	 * 
	 * @return 인덱스를 구성하는 컬럼 목록
	 */
	public List<String> getColumnNames() {
		return columnNames;
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		sb.append("INDEX(");
		sb.append("NAME(" + getIndexName() + ")");
		sb.append("TYPE(" + getIndexType() + ")");
		sb.append("COLUMNS(" + columnNames + ")");
		if (fkColumn != null && fkColumn.isEmpty() == false) {
			sb.append("FK(" + fkColumn + "@" + fkTable + ")");
		}
		sb.append(")");

		return sb.toString();
	}

	/**
	 *
	 * @return 연결테이블의 연결컬럼
	 */
	public String getFkColumn() {
		return fkColumn;
	}

	/**
	 * 
	 * @return 연결테이블명
	 */
	public String getFkTable() {
		return fkTable;
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * 
	 * @return 인덱스 종류
	 */
	public INDEX_TYPE getIndexType() {
		return indexType;
	}

	/**
	 * 
	 * @return 인덱스명:인덱스종류:컬럼명[,컬럼명] 형식
	 */
	public String getInfo() {
		String s = indexName + ":" + this.indexType + ":";
		int index = 0;
		for (String e : columnNames) {
			if (index > 0)
				s += ",";
			s += e;
			index++;
		}

		return s;
	}

	public int getNo() {
		return no;
	}

	/**
	 * 
	 * @return foreign key 문장
	 * @throws Exception
	 */
	public String getSqlFk() throws Exception {
		return "constraint " + DaoUtil.makeColSize(indexName, 32) + " foreign key ( " + columnNames.get(0)
				+ " ) references " + fkTable + " ( " + fkColumn + " )";
	}

	public String getSqlPk(String tablespace) {
		String columns = columnNames.get(0);
		for (int index = 1; index < columnNames.size(); index++) {
			columns += ", " + columnNames.get(index);
		}

		String ret = "constraint " + DaoUtil.makeColSize(indexName, 32) + " primary key ( " + columns + " )";

		if (tablespace != null && tablespace.trim().length() > 0) {
			return ret + " using index tablespace " + tablespace;
		} else {
			return ret;
		}
	}

	/**
	 * 
	 * @return foreign key 여부
	 */
	public boolean isFk() {
		return INDEX_TYPE.FK == this.indexType;
	}

	/**
	 * 
	 * @return primary key 여부
	 */
	public boolean isPk() {
		return INDEX_TYPE.PK == this.indexType;
	}

	/**
	 * [컬럼명:]:컬럼명@테이블명
	 * 
	 * @param columns the columns to set
	 */
	public void setColumns(String columns) throws Exception {

		columnNames.clear();

		addColumn(columns);

	}

	public void setFkColumn(String fkColumn) {
		this.fkColumn = fkColumn;
	}

	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return indexName + "|" + indexType + "|" + columnNames //
				+ (fkTable == null ? "" : "FK=" + fkTable) //
				+ (fkColumn == null ? "" : "." + fkColumn) //
		;
	}

}
