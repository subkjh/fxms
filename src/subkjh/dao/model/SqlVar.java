package subkjh.dao.model;

public class SqlVar {

	public static final byte VAR_TYPE_FIELD = 0x02;
	public static final byte VAR_TYPE_METHOD = 0x01;
	public static final byte VAR_TYPE_NOT_VAR = 0x00;
	public static final byte VAR_TYPE_REPLACE = 0x10;
	public static final String REPLACE_TAG = "XXREPLACEXX";

	String name;
	byte type;

	public SqlVar() {
		this.name = "";
		this.type = 0;
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public SqlVar(String name, byte type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public String getVarName() {
		return name;
	}

	/**
	 * 
	 * @return 메소드이면 method(), 필드이면 field
	 */
	public String getVarNameOrg() {
		return name + (isMethod() ? "()" : "");
	}

	public boolean isField() {
		return (type & VAR_TYPE_FIELD) == VAR_TYPE_FIELD;
	}

	public boolean isMethod() {
		return (type & VAR_TYPE_METHOD) == VAR_TYPE_METHOD;
	}

	public boolean isReplace() {
		return (type & VAR_TYPE_REPLACE) == VAR_TYPE_REPLACE;
	}

	public String toString() {
		return name + "|" + (isReplace() ? "replace " : "") + (isField() ? "field" : "method");

	}
}
