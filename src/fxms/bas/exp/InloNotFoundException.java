package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

/**
 * 설치위치를 찾지 못함.
 * 
 * @author subkjh
 *
 */
public class InloNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5287052751258583113L;

	public InloNotFoundException(Object inlo) {
		super("inlo", inlo, Lang.get("Location information not found.", inlo));

	}

}
