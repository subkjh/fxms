package subkjh.bas.co.user.exception;

import fxms.bas.exp.NotFoundException;
import subkjh.bas.co.lang.Lang;

public class UserNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5897691470032913437L;

	public UserNotFoundException(String msg) {
		super("user", msg, Lang.get("Member information not found."));
	}

}