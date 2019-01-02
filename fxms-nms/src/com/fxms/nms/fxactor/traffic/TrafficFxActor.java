package com.fxms.nms.fxactor.traffic;

import com.fxms.nms.fxactor.NeValueActor;
import com.fxms.nms.snmp.SnmpUtil;

import fxms.bas.fxo.FxActorImpl;

public abstract class TrafficFxActor extends FxActorImpl implements NeValueActor {

	protected SnmpUtil getSnmpUtil() {
		return SnmpUtil.getSnmpUtil("TrafficFxActor");
	}

}
