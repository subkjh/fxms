package fxms.bas.po.exp;

public class PsItemNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4664879152832772664L;

	public PsItemNotFoundException(String psCode) {
		super("PS-CODE(" + psCode + ")");
	}
}
