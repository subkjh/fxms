package fxms.bas.impl.ao;

import fxms.bas.impl.dbo.FX_AL_CODE;

public class AlarmCodeDbo extends FX_AL_CODE {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368025774151192465L;

	public AlarmCodeDbo() {
	}

	public boolean hasPs() {
		return getPsCode() != null && getPsCode().length() > 0;
	}
}