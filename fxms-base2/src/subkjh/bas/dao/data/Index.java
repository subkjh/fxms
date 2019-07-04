package subkjh.bas.dao.data;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.define.INDEX_TYPE;

/**
 * 테이블의 인덱스를 나타냅니다.
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Index extends SoDo {

	public static void main(String[] args) throws Exception {
		Index index = new Index();
		index.setName("aaa");
		index.setType("FK");
		index.setColumns("loc_no_me : loc_no @ bbb");

		System.out.println(index.getSqlFk());

	}

	private String name;

	/** PK, UK, FK, KEY */
	private String type;

	/** 인덱스를 구성하는 컬럼명 */
	private List<String> columnNameList = new ArrayList<String>();

	private String fkTable;

	private String fkColumn;

	/** 테이블 기준 인덱스 번호 */
	private int no;

	public Index() {

	}

	/**
	 * 
	 * @param s
	 *            name:type:columns
	 */
	public Index(String s) {

		String typeSs[] = s.split(":");
		String ss[] = typeSs[2].split(",");

		this.name = typeSs[0];
		this.type = typeSs[1];

		for (String c : ss) {
			addColumn(c);
		}

	}

	/**
	 * 
	 * @param indexName
	 *            인덱스명
	 * @param type
	 *            종류
	 * @param colArr
	 *            컬럼 목록
	 */
	public Index(String indexName, String type, String... colArr) {
		this.name = indexName;
		this.type = type;
		for (String c : colArr) {
			addColumn(c);
		}
	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param s
	 */
	public void addColumn(String str) {

		if (columnNameList == null) {
			columnNameList = new ArrayList<String>();
		}

		String column = str.replaceAll("\\[|\\]", "");

		if (column.indexOf('@') > 0) {
			String ss[] = column.split("\\@");
			fkTable = ss[1].trim();
			if (ss[0].indexOf(':') > 0) {
				String s[] = ss[0].split(":");
				fkColumn = s[1].trim();
				columnNameList.add(s[0].trim());
			} else {
				fkColumn = ss[0].trim();
				columnNameList.add(ss[0].trim());
			}
		} else {
			column = column.replaceAll("\\+", ",");
			String ss[] = column.split(",");
			for (String s : ss) {
				if (s.trim().length() > 0)
					columnNameList.add(s.trim());
			}
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Index) {
			Index idx = (Index) obj;
			if (idx.name.equals(name) //
					&& idx.type.equalsIgnoreCase(type)) {
				if (idx.columnNameList.size() == columnNameList.size()) {
					for (int i = 0; i < columnNameList.size(); i++) {
						if (idx.columnNameList.get(i).equals(columnNameList.get(i)) == false)
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
	 * 
	 * @return 컬럼명 배열
	 */
	public String[] getColArr() {
		return columnNameList.toArray(new String[columnNameList.size()]);
	}

	/**
	 * 컬럼 목록을 하나의 문자열로 리턴합니다.
	 * 
	 * @param separator
	 *            컬럼 구분자
	 * @return 컬럼 목록 문자열
	 */
	public String getColumnAll(String separator) {

		String columns = columnNameList.get(0);
		for (int index = 1; index < columnNameList.size(); index++) {
			columns += separator + columnNameList.get(index);
		}

		return columns;
	}

	/**
	 * 
	 * @return 인덱스를 구성하는 컬럼 목록
	 */
	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		sb.append("INDEX(");
		sb.append("NAME(" + getName() + ")");
		sb.append("TYPE(" + getType() + ")");
		sb.append("COLUMNS(" + columnNameList + ")");
		if (fkColumn != null && fkColumn.isEmpty() == false) {
			sb.append("FK(" + fkColumn + "@" + fkTable + ")");
		}
		sb.append(")");

		return sb.toString();
	}

	public String getFkColumn() {
		return fkColumn;
	}

	/**
	 * 
	 * @return
	 */
	public String getFkTable() {
		return fkTable;
	}

	public INDEX_TYPE getIndexType() {
		return INDEX_TYPE.getType(type);
	}

	/**
	 * 
	 * @return 인덱스명:인덱스종류:컬럼명[,컬럼명] 형식
	 */
	public String getInfo() {
		String s = name + ":" + getIndexType() + ":";
		int index = 0;
		for (String e : columnNameList) {
			if (index > 0)
				s += ",";
			s += e;
			index++;
		}

		return s;
	}

	/**
	 * @return the indexName
	 */
	public String getName() {
		return name;
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
		return "constraint " + makeColSize(name, 32) + " foreign key ( " + columnNameList.get(0) + " ) references "
				+ fkTable + " ( " + fkColumn + " )";
	}

	public String getSqlPk(String tablespace) {
		String columns = columnNameList.get(0);
		for (int index = 1; index < columnNameList.size(); index++) {
			columns += ", " + columnNameList.get(index);
		}

		String ret = "constraint " + makeColSize(name, 32) + " primary key ( " + columns + " )";

		if (tablespace != null && tablespace.trim().length() > 0) {
			return ret + " using index tablespace " + tablespace;
		} else {
			return ret;
		}
	}

	/**
	 * 
	 * @return 인덱스 종류
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @return foreign key 여부
	 */
	public boolean isFk() {
		return INDEX_TYPE.FK.name().equalsIgnoreCase(type);
	}

	/**
	 * 
	 * @return primary key 여부
	 */
	public boolean isPk() {
		return INDEX_TYPE.PK.name().equalsIgnoreCase(type);
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	/**
	 * [컬럼명:]:컬럼명@테이블명
	 * 
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(String columns) throws Exception {

		columnNameList.clear();

		addColumn(columns);

	}

	public void setFkColumn(String fkColumn) {
		this.fkColumn = fkColumn;
	}

	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	/**
	 * @param indexName
	 *            the indexName to set
	 */
	public void setName(String indexName) {
		this.name = indexName;
	}

	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * 인덱스 구분<br>
	 * PK, FK, KEY
	 * 
	 * @param type
	 *            인덱스 구분
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return name + "|" + type + "|" + columnNameList //
				+ (fkTable == null ? "" : "FK=" + fkTable) //
				+ (fkColumn == null ? "" : "." + fkColumn) //
		;
	}

}
