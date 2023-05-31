package fxms.bas.impl.dpo;

import fxms.bas.api.VarApi;
import fxms.bas.event.FxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.signal.ReloadSignal;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

public class BroadcastDfo implements FxDfo<FxEvent, Boolean> {

	@Override
	public Boolean call(FxFact fact, FxEvent event) throws Exception {
		return broadcast(event);
	}

	public boolean broadcast(FxEvent event) {

		// 다시 읽기 이면 저장소에 기록한다.
		if (event instanceof ReloadSignal) {
			try {
				VarApi.getApi().setTimeUpdated(((ReloadSignal) event).getReloadType(), DateUtil.getDtm());
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		// 통보한다.
		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.sendEvent(event, true, true);
		}

		return true;
	}
}