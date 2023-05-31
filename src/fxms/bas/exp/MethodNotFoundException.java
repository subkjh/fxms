package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

/**
 * 메소드를 찾지 못한 예외
 * 
 * @author subkjh
 *
 */
public class MethodNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262943324081983612L;

	public MethodNotFoundException(String method) {
		super("method", method, Lang.get("The requested method was not found."));
	}

}
