package subkjh.dao.def;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import subkjh.dao.util.DaoUtil;

/**
 * 테이블의 필드 구성을 나타냅니다.
 * 
 * 
 * @author subkjh
 */
public class Column {

	/**
	 * 컬럼 조작 조건
	 * 
	 * @author subkjh
	 *
	 */

	public enum COLUMN_OP {

		insert {
			@Override
			public boolean isInsertable() {
				return true;
			}

			@Override
			public boolean isUpdatable() {
				return false;
			}

		}

		,
		update {
			@Override
			public boolean isInsertable() {
				return false;
			}

			@Override
			public boolean isUpdatable() {
				return true;
			}

		}

		,
		all {
			@Override
			public boolean isInsertable() {
				return true;
			}

			@Override
			public boolean isUpdatable() {
				return true;
			}

		},
		nothing {
			@Override
			public boolean isInsertable() {
				return false;
			}

			@Override
			public boolean isUpdatable() {
				return false;
			}

		};

		private COLUMN_OP() {

		}

		/**
		 * insert문 사용 가능 여부
		 * 
		 * @return
		 */
		public abstract boolean isInsertable();

		/**
		 * update문 사용 가능 여부
		 * 
		 * @return
		 */
		public abstract boolean isUpdatable();

	}

	/**
	 * 컬럼 종류
	 * 
	 * @author subkjh
	 *
	 */
	public enum COLUMN_TYPE {
		CHAR, NUMBER, VARCHAR2, DATE, AUTO;
	}

	/**
	 * 컬럼의 해당되는 자바의 필드 타입을 객체로 할 것인지 여부
	 */
	public static boolean JAVA_FIELD_STYLE_OBJECT = false;

	public static int getDataLength(Field field) {
		Class<?> paraType = field.getType();

		if (paraType == int.class || paraType.equals(Integer.class)) {
			return 9;
		} else if (paraType == long.class || paraType.equals(Long.class)) {
			return 19;
		} else if (paraType == boolean.class || paraType.equals(Boolean.class)) {
			return 1;
		} else if (paraType == short.class || paraType.equals(Short.class)) {
			return 5;
		} else if (paraType == double.class || paraType.equals(Double.class)) {
			return 20;
		} else if (paraType == float.class || paraType.equals(Float.class)) {
			return 20;
		} else if (paraType == byte.class || paraType.equals(Byte.class)) {
			return 3;
		} else if (paraType.equals(Character.class)) {
			return 1;
		} else if (paraType == String.class) {
			return 0;
		} else {
			return 0;
		}

	}

	public static COLUMN_TYPE getType(Field field) {
		Class<?> paraType = field.getType();

		if (paraType == int.class || paraType.equals(Integer.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType == long.class || paraType.equals(Long.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType == boolean.class || paraType.equals(Boolean.class)) {
			return COLUMN_TYPE.CHAR;
		} else if (paraType == short.class || paraType.equals(Short.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType == double.class || paraType.equals(Double.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType == float.class || paraType.equals(Float.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType == byte.class || paraType.equals(Byte.class)) {
			return COLUMN_TYPE.NUMBER;
		} else if (paraType.equals(Character.class)) {
			return COLUMN_TYPE.CHAR;
		} else if (paraType == String.class) {
			return COLUMN_TYPE.VARCHAR2;
		} else if (paraType == Timestamp.class) {
			return COLUMN_TYPE.DATE;
		} else if (paraType == Number.class) {
			return COLUMN_TYPE.NUMBER;
		} else {
			return null;
		}

	}

	public static void main(String[] args) {

		Column c = new Column();
		c.setDataTypeDefined("number(9,0)");
		System.out.println(c.dataType + ", " + c.dataLength + "," + c.dataScale);
		c.setDataTypeDefined("NUMBER(9)");
		System.out.println(c.dataType + ", " + c.dataLength + "," + c.dataScale);

		c.setDataTypeDefined("Varchar2(9)");
		System.out.println(c.dataType + ", " + c.dataLength + "," + c.dataScale);

		String a = "x = 'y'";
		System.out.println(a);
		System.out.println(a.replaceAll("\\'", "''"));
	}

	private int columnNo;

	private String name;

	private String comments;

	private String dataDefault;

	private int dataLength;

	private int dataScale;

	private String dataType;

	private String example;

	private boolean nullable = false;

	private boolean pk;

	private Class<?> fieldType;

	/** java field name */
	private String fieldName = null;

	private COLUMN_OP operator = COLUMN_OP.all;

	private String sequence;

	private Field field;

	/** SELECT문에서 사용할 컬럼인지 여부 */
	private boolean selectable = true;

	public Column() {

	}

	/**
	 * 
	 * @param s name:datatype:length:scale:default
	 */
	public Column(String s) {

		String ss[] = s.split(":");
		name = ss[0];
		dataType = ss[1];

		if (ss.length > 2)
			dataLength = Integer.parseInt(ss[2]);
		if (ss.length > 3)
			dataScale = Integer.parseInt(ss[3]);
		if (ss.length > 4)
			dataDefault = ss[4];
	}

	public Column(String name, String dataType, int length, int scale, boolean nullable, String comments) {
		setName(name);
		setDataType(dataType);
		setDataLength(length);
		setDataScale(scale);
		setNullable(nullable);
		setComments(comments);
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Column) {
			Column col = (Column) obj;
			if (col.getName().equalsIgnoreCase(getName())) {
				if (col.dataType.equalsIgnoreCase(dataType) && col.dataLength == dataLength
						&& col.dataScale == dataScale) {
					return true;
				}
			}

			return false;
		}

		return super.equals(obj);
	}

	public int getColumnNo() {
		return columnNo;
	}

	/**
	 * 
	 * @return
	 */
	public COLUMN_TYPE getColumnType() {
		return getType(this.field);
	}

	/**
	 * @return the comments
	 */
	public String getComments() {

		if (comments != null && comments.length() > 0) {
			return comments.replaceAll("\\'", "''");
		}

		return comments;
	}

	/**
	 * @return the dataDefault
	 */
	public String getDataDefault() {
		return dataDefault;
	}

	public boolean getDataDefaultBoolean() {

		if (hasDefaultValue() == false) {
			return false;
		}

		String s = dataDefault.toLowerCase();

		return s.contains("y") || s.contains("true") || s.contains("yes");
	}

	public String getDataDefaultUse() {
		String ret = dataDefault == null ? "" : dataDefault.replaceAll("'|‘|’", "\"");
		if (ret.length() > 0) {
			if (getFieldType() == Long.class || getFieldType() == long.class) {
				ret += "L";
			} else if (getFieldType() == Double.class || getFieldType() == double.class) {
				ret += "D";
			}
		}
		return ret;
	}

	/**
	 * @return the dataLength
	 */
	public int getDataLength() {
		return dataLength;
	}

	/**
	 * @return the dataScale
	 */
	public int getDataScale() {
		return dataScale;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	public String getDataType2Define() {
		
		String type = getDataType().toLowerCase();
		
		if (type.indexOf("text") >= 0 || type.indexOf("bigint") >= 0 || type.indexOf("int") >= 0
				|| type.indexOf("double") >= 0 || type.indexOf("date") >= 0) {
			return type;
		}
		if (type.indexOf("number") >= 0) {
			return type + "(" + getDataLength() + (getDataScale() > 0 ? "," + getDataScale() : "") + ")";
		}

		return type + "(" + getDataLength() + ")";

	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		sb.append("COLUMN(");
		sb.append("" + getName());
		sb.append(", " + getDataType2Define());
		sb.append(", " + (isPk() ? "Y" : "N"));
		sb.append(", " + getFieldType() + " " + getFieldName());
		sb.append(", " + getOperator());
		if (getSequence() != null && getSequence().length() > 0) {
			sb.append(", " + getSequence());
		}

		sb.append(")");

		return sb.toString();
	}

	public String getExample() {
		return example;
	}

	public Field getField() {
		return field;
	}

	/**
	 * 자바에서 사용할 필드명을 제공합니다.
	 * 
	 * @return 자바 필드 명
	 */
	public String getFieldName() {
		if (field != null) {
			return field.getName();
		}
		if (fieldName == null) {
			fieldName = DaoUtil.getJavaFieldName(name);
		}

		return fieldName;
	}

	/**
	 * 자바에서 사용하는 자료형명을 제공합니다.
	 * 
	 * @return 자바 자료형 명
	 */
	public Class<?> getFieldType() {

		if (field != null) {
			return field.getType();
		}

		if (fieldType == null) {
			fieldType = makeFieldType();
		}
		return fieldType;
	}

	public String getGetter() {
		return DaoUtil.getGetter(name);
	}

	/**
	 * @return the columnName
	 */
	public String getName() {
		return name;
	}

	public COLUMN_OP getOperator() {
		return operator;
	}

	public String getSequence() {
		return sequence;
	}

	public String getSetter() {
		return DaoUtil.getSetter(name);
	}

	/**
	 * 
	 * @return 코멘트 존재 여부
	 */
	public boolean hasComment() {
		return comments != null && comments.trim().length() > 0;
	}

	/**
	 * 
	 * @return 기본값을 가지고 있는지 여부
	 */
	public boolean hasDefaultValue() {
		return (dataDefault != null && dataDefault.length() > 0);
	}

	/**
	 * 입력된 이름이 컬럼명과 일치하는 여부를 확인한다.
	 * 
	 * @param name 비교할 이름
	 * @return 일치여부
	 */
	public boolean isMatch(String name) {
		return this.name.equals(DaoUtil.getColumnName(name));
	}

	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @return the pk
	 */
	public boolean isPk() {
		return pk;
	}

	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * 데이터베이스 컬럼에 매칭되는 자바 객체 타입을 제공합니다.
	 * 
	 * @param column
	 * @return 자바객체 타입
	 */
	public Class<?> makeFieldType() {
		String type = getDataType().toLowerCase();

		if (JAVA_FIELD_STYLE_OBJECT) {
			if (type.indexOf("char") >= 0)
				return String.class;
			else if (type.indexOf("text") >= 0)
				return String.class;
			else if (type.indexOf("bigint") >= 0)
				return Long.class;
			else if (type.indexOf("int") >= 0)
				return Integer.class;
			else if (type.indexOf("double") >= 0)
				return Double.class;
			else if (type.indexOf("number") >= 0) {
				if (getDataScale() > 0) {
					return getDataLength() < 10 ? Float.class : Double.class;
				} else if (getDataLength() < 10)
					return Integer.class;
				else
					return Long.class;
			} else if (type.indexOf("date") >= 0) {
				return Timestamp.class;
			} else
				return Object.class;
		} else {
			if (getName().startsWith("IS_"))
				return boolean.class;
			else if (getName().endsWith("_YN"))
				return boolean.class;
			else if (type.indexOf("char") >= 0)
				return String.class;
			else if (type.indexOf("text") >= 0)
				return String.class;
			else if (type.indexOf("bigint") >= 0)
				return long.class;
			else if (type.indexOf("int") >= 0)
				return int.class;
			else if (type.indexOf("double") >= 0)
				return double.class;
			else if (type.indexOf("number") >= 0) {
				if (getDataScale() > 0)
					return getDataLength() < 10 ? float.class : double.class;
				else if (getDataLength() < 10)
					return int.class;
				else
					return long.class;
			} else if (type.indexOf("date") >= 0) {
				return Timestamp.class;
			} else
				return Object.class;
		}
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param dataDefault the dataDefault to set
	 */
	public void setDataDefault(String dataDefault) {
		this.dataDefault = (dataDefault == null ? null : dataDefault.trim());
	}

	/**
	 * @param dataLength the dataLength to set
	 */
	public void setDataLength(int dataLength) {
		if (dataLength < 0)
			return;

		this.dataLength = dataLength;
	}

	/**
	 * @param dataScale the dataScale to set
	 */
	public void setDataScale(int dataScale) {
		if (dataScale < 0) {
			this.dataScale = 0;
		} else {
			this.dataScale = dataScale;
		}
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = (dataType == null ? null : dataType.toLowerCase());
	}

	/**
	 * 
	 * @param dataTypeFull
	 */
	public void setDataTypeDefined(String dataTypeFull) {

		if (dataTypeFull != null) {
			try {
				int pos = dataTypeFull.indexOf('(');
				if (pos > 0) {
					dataType = dataTypeFull.substring(0, pos).trim().toLowerCase();
					String str = dataTypeFull.substring(pos).replaceAll("\\(|\\)", "");
					str = str.replaceAll("byte", "");
					String ss[] = str.split(",");
					dataLength = Integer.parseInt(ss[0].trim());
					if (ss.length > 1) {
						dataScale = Integer.parseInt(ss[1].trim());
					}
				} else { // date
					dataType = dataTypeFull;
					// dataLength = 4;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setExample(String example) {
		if (example != null) {
			int pos = example.indexOf(')');
			if (pos < 0)
				pos = example.indexOf(' ');
			if (pos > 0) {
				this.example = example.substring(pos + 1).trim();
				return;
			}
		}

		this.example = example;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void setFieldName(String field) {
		this.fieldName = field;
	}

	public void setFieldType(Class<?> javaType) {
		this.fieldType = javaType;
	}

	/**
	 * 컬럼명을 설정합니다.<br>
	 * 컬럼명에 (PK)가 존재하면 이를 분석하여 isPK() = true가 되도록 설정합니다.
	 * 
	 * @param columnName the columnName to set
	 */
	public void setName(String columnName) {
		if (columnName == null) {
			this.name = null;
			return;
		}

		int pos = columnName.indexOf('(');
		if (pos > 0) {
			this.name = columnName.substring(0, pos).trim();
			if (columnName.substring(pos).toUpperCase().indexOf("PK") > 0) {
				pk = true;
			}
		} else {
			this.name = columnName.trim();
		}
	}

	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setOperator(COLUMN_OP operator) {
		this.operator = operator;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return getName() + "|" + getDataType2Define() + "," + getDataDefault();
	}

}
