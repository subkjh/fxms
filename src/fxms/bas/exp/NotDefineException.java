package fxms.bas.exp;

public class NotDefineException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6199054035746565299L;

	public NotDefineException(String name) {
		super("parameter", name);
	}

}
