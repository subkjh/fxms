package subkjh.bas.co.user.exception;

import fxms.bas.exp.NotFoundException;
import subkjh.bas.co.lang.Lang;

public class UgrpNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961368396727258844L;

	public UgrpNotFoundException(Object id) {
		super("ugrp", id, Lang.get("User group not found.", id));
	}

}