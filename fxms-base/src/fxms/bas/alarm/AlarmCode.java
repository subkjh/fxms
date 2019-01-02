package fxms.bas.alarm;

import fxms.bas.dbo.ao.FX_AL_CODE;

public class AlarmCode extends FX_AL_CODE {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368025774151192465L;

	public AlarmCode() {
	}

	public boolean hasPs()
	{
		return getPsCode() != null && getPsCode().length() > 0;
	}
}