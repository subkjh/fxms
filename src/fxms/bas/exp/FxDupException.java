package fxms.bas.exp;

public class FxDupException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 806420423331014967L;

	private String type;

	private String name;

	public FxDupException(String type, String name) {
		super("duplicated : " + type + "," + name);
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
