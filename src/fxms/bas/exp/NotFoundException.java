package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7359714969819219527L;

	private final String type;
	private final Object value;

	public NotFoundException(String type, Object value) {
		this(type, value, null);
	}

	public NotFoundException(String type, Object value, String message) {
		super(message == null ? Lang.get("No data found.") + " " + type + "(" +  value + ")" : message);
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

}
