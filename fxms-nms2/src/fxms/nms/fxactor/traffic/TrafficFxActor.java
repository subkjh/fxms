package fxms.nms.fxactor.traffic;

import fxms.bas.fxo.FxActorImpl;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.fxactor.NeValueActor;

public abstract class TrafficFxActor extends FxActorImpl implements NeValueActor {

	protected SnmpUtil getSnmpUtil() {
		return SnmpUtil.getSnmpUtil("TrafficFxActor");
	}

}
