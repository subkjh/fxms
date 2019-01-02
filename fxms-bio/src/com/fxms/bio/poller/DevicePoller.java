package com.fxms.bio.poller;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fxms.bio.mo.DeviceMo;
import com.fxms.bio.mo.GwMo;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.poller.PollCallback;
import fxms.bas.poller.Poller;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class DevicePoller extends Poller<DeviceMo> {

	public DevicePoller() {
		super(DevicePoller.class.getSimpleName(), false);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void polling(long pollMsdate, PollingMo pollingMo, PollCallback callback) throws TimeoutException, Exception {

		DeviceMo node = (DeviceMo) pollingMo.getMo();

		GwMo gw = (GwMo) MoApi.getApi().getMo(node.getGwMoNo());
		if (gw == null) {
			Logger.logger.fail("GW={} not found", node.getGwMoNo());
			return;
		}

		Adapter<DeviceMo> adapter = FxActorParser.getParser().getActor(Adapter.class, "gwType", gw.getGwType());
		if (adapter == null) {
			Logger.logger.fail("PO({}) ADAPTER({}) NOT FOUND", node, gw.getGwType());
			return;
		}

		List<PsVo> list =  adapter.getValue(node, null);
		if (list != null) {
			callback.onValue(adapter.getClass().getName(), pollMsdate, list, null);
		}
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void pollingGroup(long arg0, List<PollingMo> arg1, PollCallback callback) throws PollingTimeoutException, Exception {
	}

}
