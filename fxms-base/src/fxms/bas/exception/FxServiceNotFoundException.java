package fxms.bas.exception;

public class FxServiceNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717058034162784174L;

	public FxServiceNotFoundException(String service)
	{
		super("SERVICE", service);
	}

}