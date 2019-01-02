package fxms.bas.exception;

public class ModelNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2050036805622348627L;

	public ModelNotFoundException(String model)
	{
		super("MODEL", model);
	}

}
