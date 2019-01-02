package com.fxms.nms.fxactor;

import java.util.List;

import com.fxms.nms.fxactor.snmpping.SnmpPingFxActor;
import com.fxms.nms.mo.NeMo;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.poller.PollCallback;
import fxms.bas.poller.Poller;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class SnmpPoller extends Poller<NeMo> {

	public SnmpPoller() {
		super(SnmpPoller.class.getSimpleName(), false);
	}

	@Override
	protected void polling(long pollMsdate, PollingMo pollingMo, PollCallback callback)
			throws PollingTimeoutException, Exception {

		NeMo node = (NeMo) pollingMo.getMo();

		List<SnmpPingFxActor> fList = FxActorParser.getParser().getActorList(SnmpPingFxActor.class);
		if (fList == null || fList.size() == 0)
			return;

		for (SnmpPingFxActor f : fList) {

			if (f.match(node) == false) {
				continue;
			}

			try {
				List<PsVo> v = f.getValues(pollMsdate, node);
				if (v != null) {
					callback.onValue(f.getClass().getName(), pollMsdate, v, null);
				}
			} catch (PollingTimeoutException e) {
				throw e;
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}

	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void pollingGroup(long pollMsdate, List<PollingMo> moList, PollCallback callback)
			throws PollingTimeoutException, Exception {
	}

}
