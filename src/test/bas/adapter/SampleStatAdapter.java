package test.bas.adapter;

import fxms.bas.fxo.adapter.PsStatAfterAdapter;
import subkjh.bas.co.log.Logger;

/**
 * 샘플용 PsStatAdapter
 * 
 * @author subkjh
 *
 */
public class SampleStatAdapter extends PsStatAfterAdapter {

	@Override
	public void afterStat(String psTable, String psDataCd, long psDate) throws Exception {
		Logger.logger.info("SAMPLE SampleStatAdapter, psTable={}, psDataCd={}, psDate={}", psTable, psDataCd, psDate);
	}

}
