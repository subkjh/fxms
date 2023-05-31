package fxms.bas.impl.api;

import fxms.bas.impl.dpo.ps.PsStatMakeInfluxDfo;

/**
 * AppApi를 구현한 API
 * 
 * @author subkjh
 *
 */
public class AppApiInfluxDb extends AppApiDfo {

	@Override
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception {
		int size = new PsStatMakeInfluxDfo().generateStatistics(psTbl, psKindName, psDtm);
		return size;
	}
}
