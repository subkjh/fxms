package fxms.bas.exp;

public class PsItemNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4664879152832772664L;

	public PsItemNotFoundException(String psId) {
		super("psId", psId);
	}
}
