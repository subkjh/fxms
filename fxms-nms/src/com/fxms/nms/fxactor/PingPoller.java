package com.fxms.nms.fxactor;

import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fxms.nms.fxactor.ping.PingIpActor;
import com.fxms.nms.mo.pmo.IpPmo;
import com.fxms.nms.mo.pmo.IpsPmo;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.poller.PollCallback;
import fxms.bas.poller.Poller;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class PingPoller extends Poller<IpPmo> {

	public PingPoller() {
		super(PingPoller.class.getSimpleName(), true);
	}

	@Override
	protected void polling(long pollMsdate, PollingMo pollingMo, PollCallback callback)
			throws TimeoutException, Exception {
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void pollingGroup(long pollMsdate, List<PollingMo> moList, PollCallback callback)
			throws PollingTimeoutException, Exception {

		PingIpActor actor = FxActorParser.getParser().getActor(PingIpActor.class);
		if (actor == null) {
			Logger.logger.fail("'{}' FxActor NOT FOUND", PingIpActor.class.getName());
			return;
		}

		IpsPmo ips = new IpsPmo();
		for (PollingMo mo : moList) {
			ips.getIpList().add((IpPmo) mo.getMo());
		}

		List<PsVo> list = actor.getValues(pollMsdate, ips);
		if (list != null) {
			callback.onValue(actor.getClass().getName(), pollMsdate, list, null);
		}
	}

}
