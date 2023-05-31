package fxms.bas.exp;

import subkjh.bas.co.lang.Lang;

public class AlCfgNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1189450639227043440L;

	public AlCfgNotFoundException(int alCfgNo) {
		super("alCfg", alCfgNo, Lang.get("This is an unregistered alarm condition.", alCfgNo));
	}
}
