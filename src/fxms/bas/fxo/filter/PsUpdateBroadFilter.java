package fxms.bas.fxo.filter;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

/**
 * 관리대상의 수집값이 변경되었다고 다른 서비스에 공유한다.
 * 
 * @author subkjh
 *
 */
public class PsUpdateBroadFilter extends PsUpdateFilter {

	@Override
	public void updated(long moNo, String psCode, Object value) throws Exception {

		if (FxServiceImpl.fxService == null) {
			return;
		}

		Mo mo = null;
		try {
			mo = MoApi.getApi().getMo(moNo);
		} catch (Exception ex) {
			Logger.logger.fail("mo-no(" + moNo + ") NOT FOUND");
			return;
		}

		FxServiceImpl.fxService.sendEvent(mo, true, true);

	}

}
