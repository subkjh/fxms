package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

public class AlcdNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244528453079292942L;

	public AlcdNotFoundException(int alcdNo) {
		super("alcd", alcdNo, Lang.get("This is an unregistered alarm code.", alcdNo));
	}
}
