package fxms.bas.impl.ext.influxdb;

import fxms.bas.impl.api.AppApiDfo;

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
