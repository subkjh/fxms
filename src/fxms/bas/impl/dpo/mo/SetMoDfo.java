package fxms.bas.impl.dpo.mo;

import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.MoApi;
import fxms.bas.fxo.adapter.FxSetValueAdapter;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

public class SetMoDfo implements FxDfo<Map<String, Object>, Boolean> {

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {

		long moNo = fact.getMoNo();
		String method = fact.getString("method");
		return set(moNo, method, data);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean set(long moNo, String method, Map<String, Object> datas) throws Exception {

		Mo mo = MoApi.getApi().getMo(moNo);

		Thread.currentThread().setName("setValue-" + mo.getMoNo());

		Logger.logger.info(Logger.makeString(mo + ", " + method + "\nparameters=" + datas));

		List<FxSetValueAdapter> list = AdapterApi.getApi().getAdapters(FxSetValueAdapter.class, mo);
		for (FxSetValueAdapter adapter : list) {
			try {
				adapter.setValue(mo, method, datas);
				Logger.logger.info("{} SET OK : method={}, parameters={}", mo, method, datas);
				return true;
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}

		throw new Exception("Not implement Adapter");

	}
}
