package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

public class ModelNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2050036805622348627L;

	public ModelNotFoundException(String model) {
		super("model", model, Lang.get("Model information not found."));

	}

}
