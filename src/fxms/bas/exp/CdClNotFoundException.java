package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

public class CdClNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7894320931020977313L;

	public CdClNotFoundException(String cdClass) {
		super("cdCl", cdClass, Lang.get("This is an unregistered code classification.", cdClass));
	}
}
