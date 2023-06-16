package fxms.bas.impl.adapter;

import java.util.List;

import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.adapter.PsStatAfterAdapter;
import subkjh.bas.co.log.Logger;

/**
 * 샘플용 PsStatAdapter
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AppService", descr = "단순한 로그를 남긴다.")
public class SampleStatAdapter extends PsStatAfterAdapter {

	@Override
	public void afterStat(String psTable, List<String> psIds, String psDataCd, long psDate) throws Exception {
		Logger.logger.info("SAMPLE SampleStatAdapter, psTable={}, psIds={}, psDataCd={}, psDate={}", psTable, psIds,
				psDataCd, psDate);
	}

}
