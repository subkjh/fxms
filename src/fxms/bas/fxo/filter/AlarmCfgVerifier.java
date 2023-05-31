package fxms.bas.fxo.filter;

import fxms.bas.mo.Moable;

public interface AlarmCfgVerifier {

	public boolean isValid(Moable mo, String verifierValue);

}
