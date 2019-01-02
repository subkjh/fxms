package subkjh.bas.dao.data;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.exception.ColumnNotFoundException;
import subkjh.bas.dao.exception.DBObjectNameInvalidExeption;

/**
 * 테이블 정보를 나타냅니다.
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Table extends SoDo {

	private int tableNo;

	private String name;

	private String comment;

	private String className;

	private String packageName;

	private List<Column> columnList = new ArrayList<Column>();

	private List<Index> indexList = new ArrayList<Index>();

	private String tableSpaceData;

	private String tableSpaceIndex;

	private String alias;

	public Table() {

	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param column
	 */
	public void add(Column column) {
		columnList.add(column);
	}

	/**
	 * 인덱스를 추가합니다.
	 * 
	 * @param index
	 */
	public void add(Index index) {

		index.setNo(indexList.size());
		indexList.add(index);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean containsColumn(String columnName) {

		for (Column col : columnList) {
			if (col.isMatch(columnName)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Table) {
			Table tab = (Table) o;
			return tab.getName().equals(name);
		}

		return super.equals(o);
	}

	public String getAlias() {
		return alias;
	}

	/**
	 * @return the javaClassName
	 */
	public String getClassName() {
		return className;
	}

	public String getClassSimpleName() {
		String ss[] = className.split("\\.");
		return ss[ss.length - 1];
	}

	public Column getColumn(String name) {
		for (Column col : columnList) {
			if (col.getName().equals(name) || col.getFieldName().equals(name)) {
				return col;
			}
		}

		return null;
	}

	/**
	 * @return the columns
	 */
	public List<Column> getColumnList() {
		return columnList;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		if (comment == null || comment.length() == 0)
			return comment;
		return comment.replaceAll("\\'", "''");
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		sb.append("TABLE(");
		sb.append("NAME(" + getName() + ")");
		sb.append("CLASS(" + getClassName() + ")");
		sb.append(")\n");

		for (Column column : getColumnList()) {
			sb.append("\t" + column.getDebug() + "\n");
		}
		sb.append("\n");
		for (Index idx : getIndexList()) {
			sb.append("\t" + idx.getDebug() + "\n");
		}

		return sb.toString();
	}

	public List<String> getIndexColumnList() {
		List<String> ret = new ArrayList<String>();

		for (Index idx : indexList) {
			if (idx.getColumnNameList() != null) {
				ret.addAll(idx.getColumnNameList());
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return 인덱스 목록
	 */
	public List<Index> getIndexList() {
		return indexList;
	}

	public String[] getInfoArrIdx() {
		String ret[] = new String[indexList.size()];
		int i = 0;
		for (Index e : indexList) {
			ret[i] = e.getInfo();
			i++;
		}
		return ret;
	}

	/**
	 * 
	 * 
	 * @return 테이블의 한 열을 구성하는 데이터의 최대 크기
	 */
	public int getMaxLength() {
		int len = 0;

		for (Column c : columnList) {
			len += c.getDataLength();
		}

		len += 10; // 예비

		return len;
	}

	/**
	 * @return the tableName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the javaPackageName
	 */
	public String getPackageName() {
		return packageName == null ? "subkjh" : packageName;
	}

	/**
	 *
	 * @return PK에 포함된 컬럼 목록
	 */
	public List<Column> getPkColumns() {

		Column col;
		List<Column> colList = new ArrayList<Column>();

		for (Index idx : indexList) {
			if (idx.isPk()) {
				for (String colName : idx.getColumnNameList()) {
					col = getColumn(colName);
					colList.add(col);
				}
			}
		}

		return colList;
	}

	/**
	 * 테이블 생성용 쿼리를 만듭니다.
	 * 
	 * @param database
	 *            데이터베이스
	 * @param ts4Data
	 *            테이블스페이스(데이터)
	 * @param ts4Index
	 *            테이블스페이스(인덱스)
	 * @param isComment
	 *            코멘트 작성 여부
	 * @return 쿼리문
	 * @throws Exception
	 */
	public String getSqlAll(DataBase database) throws Exception {

		StringBuffer sb = new StringBuffer();

		sb.append(database.getSqlCreate(this));
		sb.append(" ;\n\n");

		for (String sql : database.getSqlCreateIndex(this)) {
			sb.append(sql + " ;\n");
		}

		sb.append("\n");

		List<String> commentList = database.getSqlComment(this);
		if (commentList != null) {
			for (String sql : commentList) {
				sb.append(sql + " ;\n");
			}
		}

		return sb.toString();
	}

	public int getTableNo() {
		return tableNo;
	}

	public String getTableSpaceData() {
		return tableSpaceData;
	}

	public String getTableSpaceIndex() {
		return tableSpaceIndex;
	}

	/**
	 * 
	 * @return 코멘트 존재 여부
	 */
	public boolean hasComment() {
		return comment != null && comment.trim().length() > 0;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	/**
	 * 인덱스 중에 연결키가 존재하는지 확인합니다.
	 * 
	 * @return
	 */
	public boolean isContainsFk() {
		for (Index idx : indexList) {
			if (idx.isFk())
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @return 데이터용 테이블스페이스 정의 여부
	 */
	public boolean isDefTsData() {
		return tableSpaceData != null && tableSpaceData.trim().length() > 0;
	}

	/**
	 * 
	 * @return 인덱스용 테이블스페이스 정의 여부
	 */
	public boolean isDefTsIdx() {
		return tableSpaceIndex != null && tableSpaceIndex.trim().length() > 0;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColList(List<Column> colList) {
		this.columnList = colList;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setIdxList(List<Index> idxList) {

		if (idxList == null || idxList.size() == 0) {
			this.indexList = idxList;
			return;
		}

		Index idx;
		for (int i = 0; i < idxList.size(); i++) {
			idx = idxList.get(i);
			idx.setNo(i);
			this.indexList.add(idx);
		}

	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setName(String tableName) {
		this.name = tableName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	public void setTableSpaceData(String tableSpaceData) {
		this.tableSpaceData = tableSpaceData;
	}

	public void setTableSpaceIndex(String tableSpaceIndex) {
		this.tableSpaceIndex = tableSpaceIndex;
	}

	public int sizeCol() {
		return columnList == null ? 0 : columnList.size();
	}

	@Override
	public String toString() {
		return "TABLE(" + name + ")";
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		boolean isOk;

		for (Column col : columnList) {
			if (isValidName(col.getName()) == false)
				throw new DBObjectNameInvalidExeption(getName(), col.getName());
		}

		for (Index idx : indexList) {

			if (isValidName(idx.getName()) == false)
				throw new DBObjectNameInvalidExeption(getName(), idx.getName());

			for (String columnName : idx.getColumnNameList()) {

				isOk = false;

				for (Column col : columnList) {
					if (col.getName().equals(columnName)) {
						isOk = true;
						break;
					}
				}

				if (isOk == false) {
					throw new ColumnNotFoundException(getName(), columnName);
				}
			}
		}
	}

	boolean containsFk(String name) {
		for (Index idx : indexList) {
			if (idx.isFk() && idx.getFkTable().equals(name))
				return true;
		}
		return false;
	}

	public void fillColInfo(StringBuffer man, StringBuffer opt) {

		if (man.length() == 0) {
			man.append("<h3>mandatory : </h3>\n");
		}

		if (opt.length() == 0) {
			opt.append("<h3>optional : </h3>\n");
		}

		StringBuffer sb;

		for (Column col : columnList) {
			if (col.isNullable()) {
				sb = opt;
			} else {
				sb = man;
			}
			
			sb.append("\t");
			sb.append(col.getFieldName());
			sb.append(" : ");
			sb.append(col.getComments());
			sb.append("<br>\n");

		}

	}

}
