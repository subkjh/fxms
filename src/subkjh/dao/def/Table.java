package subkjh.dao.def;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import subkjh.dao.database.DataBase;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.exp.ColumnNotFoundException;
import subkjh.dao.exp.DBObjectNameInvalidExeption;
import subkjh.dao.util.DaoUtil;

/**
 * 테이블 정보를 나타냅니다.
 * 
 * @author subkjh
 * @since 2007-01-01
 */
public class Table {

	private int tableNo;

	private String name;

	private String comment;

	private String className;

	private String packageName;

	private final List<Column> columns;

	private final List<Index> indexes;

	private String tableSpaceData;

	private String tableSpaceIndex;

	private String alias;

	public Table() {
		this.indexes = new ArrayList<>();
		this.columns = new ArrayList<>();
	}

	boolean containsFk(String name) {
		for (Index idx : indexes) {
			if (idx.isFk() && idx.getFkTable().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * 컬럼을 추가합니다.
	 * 
	 * @param column
	 */
	public void addColumn(Column column) {
		columns.add(column);
	}

	/**
	 * 인덱스를 추가합니다.
	 * 
	 * @param index
	 */
	public void addIndex(Index index) {
		index.setNo(indexes.size());
		indexes.add(index);
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

		for (Column col : columns) {
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

	public String getAllColumnInfo(boolean isWeb) {

		StringBuffer sb = new StringBuffer();
		for (Column col : columns) {
			sb.append(col.getFieldName());
			sb.append("\t");
			sb.append(col.getComments());
			sb.append("\t");
			sb.append(col.isNullable() ? "N" : "Y");
			if (isWeb) {
				sb.append("<br>");
			}
			sb.append("\n");

		}

		return sb.toString();
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
		for (Column col : columns) {
			if (col.getName().equals(name) || col.getFieldName().equals(name)) {
				return col;
			}
		}

		return null;
	}

	/**
	 * NOT NULL인 컬럼이고 기본값이 정의되어 있지 않는 컬럼을 조회한다.
	 * 
	 * @return
	 */
	public List<Column> getColumnNotNullList() {

		List<Column> ret = new ArrayList<Column>();

		for (Column col : columns) {

			if (col.isNullable() == false) {
				if (col.hasDefaultValue() == false) {
					ret.add(col);
				}
			}
		}

		return ret;
	}

	/**
	 * NULL 가능한 컬럼을 조회한다. 기본값이 설정되어 있는 컬럼을 조회한다.
	 * 
	 * @return
	 */
	public List<Column> getColumnNullableList() {

		List<Column> ret = new ArrayList<Column>();

		for (Column col : columns) {

			// NULL 가능하거나 기본값이 있으면
			if (col.isNullable() || col.hasDefaultValue()) {
				ret.add(col);
			}
		}

		return ret;
	}

	/**
	 * @return the columns
	 */
	public List<Column> getColumns() {
		return columns;
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

		for (Column column : getColumns()) {
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

		for (Index idx : indexes) {
			if (idx.getColumnNames() != null) {
				ret.addAll(idx.getColumnNames());
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return 인덱스 목록
	 */
	public List<Index> getIndexList() {
		return indexes;
	}

	public String[] getInfoArrIdx() {
		String ret[] = new String[indexes.size()];
		int i = 0;
		for (Index e : indexes) {
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

		for (Column c : columns) {
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

		for (Index idx : indexes) {
			if (idx.isPk()) {
				for (String colName : idx.getColumnNames()) {
					col = getColumn(colName);
					colList.add(col);
				}
			}
		}

		return colList;
	}

	public Index getPkIndex() {
		for (Index o : indexes) {
			if (o.getIndexType() == INDEX_TYPE.PK) {
				return o;
			}
		}
		return null;
	}

	/**
	 * 테이블 생성용 쿼리를 만듭니다.
	 * 
	 * @param database  데이터베이스
	 * @param ts4Data   테이블스페이스(데이터)
	 * @param ts4Index  테이블스페이스(인덱스)
	 * @param isComment 코멘트 작성 여부
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
		for (Index idx : indexes) {
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
	 * @param columns the columns to set
	 */
	public void setColumns(List<Column> colList) {
		this.columns.clear();
		this.columns.addAll(colList);
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setIndexes(List<Index> idxList) {

		this.indexes.clear();

		Index idx;
		for (int i = 0; i < idxList.size(); i++) {
			idx = idxList.get(i);
			idx.setNo(i);
			this.indexes.add(idx);
		}

	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setName(String tableName) {
		this.name = tableName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 입력된 필드만 SELECT문의 조회 컬럼에 포함할 수 있도록 한다.
	 * 
	 * @param fieldList
	 * @return
	 */
	public void setSelectable(List<Field> fieldList) {
		List<Column> toRemoveList = new ArrayList<Column>();

		A: for (Column a : columns) {
			if (a.getField() != null) {
				for (Field f : fieldList) {
					if (a.getField().getName().equals(f.getName())) {
						a.setSelectable(true);
						continue A;
					}
				}
				toRemoveList.add(a);
			}
			a.setSelectable(false);
		}
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
		return columns == null ? 0 : columns.size();
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

		for (Column col : columns) {
			if (DaoUtil.isValidName(col.getName()) == false)
				throw new DBObjectNameInvalidExeption(getName(), col.getName());
		}

		for (Index idx : indexes) {

			if (DaoUtil.isValidName(idx.getIndexName()) == false)
				throw new DBObjectNameInvalidExeption(getName(), idx.getIndexName());

			for (String columnName : idx.getColumnNames()) {

				isOk = false;

				for (Column col : columns) {

					if (col.getName().equalsIgnoreCase(columnName)) {

						// PK 인덱스인 경우
						if (idx.isPk()) {
							col.setPk(true);
						}

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

}
