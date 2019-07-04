package fxms.bas.co.exp;

public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7359714969819219527L;

	public NotFoundException(String type, String name) {
		super(type + "(" + name + ")");
	}
}
