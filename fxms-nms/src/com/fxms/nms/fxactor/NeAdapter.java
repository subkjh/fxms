package com.fxms.nms.fxactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fxms.nms.mo.NeMo;

import fxms.bas.api.FxApi;
import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class NeAdapter extends Adapter<NeMo> {

	@Override
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception {

		Mo mo = children.getParent();

		List<NeConfigActor> adapterList = getConfigAdapter(mo);

		for (NeConfigActor adapter : adapterList) {
			Logger.logger.trace("fx-actor={}, mo={}", adapter.getName(), mo.toString());
			adapter.getConfigChildren(children, moClasses);
		}

		if (mo instanceof NeMo) {
			((NeMo) mo).getNeStatus().setConfSyncDate(FxApi.getDate());
		}

	}

	@Override
	public List<PsVo> getValue(NeMo mo, String psCodes[]) throws Exception {

		List<NeValueActor> actorList = getNeValueActorList(mo);
		List<PsVo> ret = new ArrayList<PsVo>();
		List<PsVo> e;
		for (NeValueActor actor : actorList) {
			e = actor.getValues(0, mo, psCodes);
			if (e != null && e.size() > 0) {
				ret.addAll(e);
			}
		}
		return ret;
	}

	@Override
	public void setValue(NeMo mo, String method, Map<String, Object> para) throws Exception {
		Logger.logger.info("MO({}), PARA({}) NOT IMPLEMENTS", mo, para);
	}

	private List<NeConfigActor> getConfigAdapter(Mo mo) {

		List<NeConfigActor> adapterList = FxActorParser.getParser().getActorList(NeConfigActor.class);
		List<NeConfigActor> retList = new ArrayList<NeConfigActor>();
		for (NeConfigActor adapter : adapterList) {
			if (adapter.match(mo)) {
				retList.add(adapter);
			}
		}
		return retList;
	}

	private List<NeValueActor> getNeValueActorList(Mo mo) {

		List<NeValueActor> actorList = FxActorParser.getParser().getActorList(NeValueActor.class);
		List<NeValueActor> retList = new ArrayList<NeValueActor>();
		for (NeValueActor actor : actorList) {
			if (actor.match(mo)) {
				retList.add(actor);
			}
		}

		return retList;
	}

}
