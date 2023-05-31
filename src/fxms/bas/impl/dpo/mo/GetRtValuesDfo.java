package fxms.bas.impl.dpo.mo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.AdapterApi;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsVoRaw;
import subkjh.bas.co.log.Logger;

public class GetRtValuesDfo implements FxDfo<Mo, List<PsVoRaw>> {

	@Override
	public List<PsVoRaw> call(FxFact fact, Mo mo) throws Exception {
		return getRtValues(mo);
	}

	public List<PsVoRaw> getRtValues(Mo mo) throws Exception {

		List<PsVoRaw> retList = new ArrayList<PsVoRaw>();

		try {

			List<PsVoRaw> voList;
			List<FxGetValueAdapter> list = AdapterApi.getApi().getAdapters(FxGetValueAdapter.class, mo);
			for (FxGetValueAdapter adapter : list) {
				Logger.logger.debug("adapter {}", adapter.getClass().getName());
				voList = adapter.getValue(mo);
				if (voList != null && voList.size() > 0) {
					retList.addAll(voList);
				}
			}
			return retList;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}
}
