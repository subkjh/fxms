package fxms.bas.alarm;

import fxms.bas.mo.Mo;

public interface AlarmCfgVerifier {

	public boolean isValid(Mo mo, String verifierValue);

}
