package com.fxms.bio.poller;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fxms.bio.mo.GwMo;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.poller.PollCallback;
import fxms.bas.poller.Poller;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class GwPoller extends Poller<GwMo> {

	public GwPoller() {
		super(GwPoller.class.getSimpleName(), false);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void polling(long pollMsdate, PollingMo pollingMo, PollCallback callback)
			throws TimeoutException, Exception {

		GwMo gw = (GwMo) pollingMo.getMo();

		Adapter<GwMo> adapter = FxActorParser.getParser().getActor(Adapter.class, "gwType", gw.getGwType());
		if (adapter == null) {
			Logger.logger.fail("PO({}) ADAPTER({}) NOT FOUND", gw, gw.getGwType());
			return;
		}

		List<PsVo> list = adapter.getValue(gw, null);
		if (list != null) {
			callback.onValue(adapter.getClass().getName(), pollMsdate, list, null);
		}

	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void pollingGroup(long arg0, List<PollingMo> arg1, PollCallback callback)
			throws PollingTimeoutException, Exception {
	}

}