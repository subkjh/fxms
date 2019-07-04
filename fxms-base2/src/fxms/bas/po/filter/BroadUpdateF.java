package fxms.bas.po.filter;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

public class BroadUpdateF extends UpdateFilter {

	@Override
	public void updated(long moNo) throws Exception {

		if (FxServiceImpl.fxService == null) {
			return;
		}

		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			Logger.logger.fail("mo-no(" + moNo + ") NOT FOUND");
			return;
		}

		FxServiceImpl.fxService.send(mo);

	}

}
