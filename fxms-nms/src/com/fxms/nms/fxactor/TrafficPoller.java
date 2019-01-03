package com.fxms.nms.fxactor;

import java.util.List;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.fxactor.traffic.TrafficFxActor;
import com.fxms.nms.mo.NeMo;

import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.poller.PollCallback;
import fxms.bas.poller.Poller;
import fxms.bas.poller.beans.PollingItem;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.exception.PollingNoTargetException;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class TrafficPoller extends Poller<NeMo> {

	public TrafficPoller() {
		super(TrafficPoller.class.getSimpleName(), false);
	}

	@Override
	protected void polling(long pollMsdate, PollingMo pollingMo, PollCallback callback) throws PollingTimeoutException, Exception {

		Logger.logger.trace("Started");

		NeMo node = (NeMo) pollingMo.getMo();

		List<TrafficFxActor> fList = FxActorParser.getParser().getActorList(TrafficFxActor.class);
		List<PsVo> val;

		for (TrafficFxActor f : fList) {

			if (f.match(node) == false) {
				continue;
			}

			try {
				val = f.getValues(pollMsdate, node);
				if (val != null) {
					callback.onValue(f.getClass().getName(), pollMsdate, val, null);
				}

				pollingMo.setMoState(PollingItem.MoState.ok);
			
			} catch (PollingNoTargetException e) {
				
				pollingMo.setMoState(PollingItem.MoState.notarget);
				return ;
				
			} catch (PollingTimeoutException e) {

				pollingMo.setMoState(PollingItem.MoState.timeout);

				Logger.logger.fail("Stopped with SnmpTimeout" + "[" + node.getIpAddress() + "]");

				node.getNeStatus().setStatusSnmp(NeMo.STATUS_SNMP_TIMEOUT);

				EventApi.getApi().check(node, FxApi.getDate(0) + "", NmsCodes.AlarmCode.NeSnmpTimeout,
						"HSTIME(" + FxApi.getDate(0) + ") SnmpTimeout", null);

				return ;

			} catch (Exception e) {

				pollingMo.setMoState(PollingItem.MoState.error);

				Logger.logger.error(e);
				Logger.logger.fail("Stopped with " + e.getMessage() + "[" + node.getIpAddress() + "]");

				node.getNeStatus().setStatusSnmp(NeMo.STATUS_SNMP_ERROR);

				return ;
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