package com.fxms.bio.poller;

import java.util.Map;

import com.fxms.bio.mo.GwMo;

import fxms.bas.poller.PollerMgr;

public class GwPollerMgr extends PollerMgr<GwMo> {

	public GwPollerMgr() {
		super(GwMo.class, GwPoller.class);
	}

	@Override
	protected Map<String, Object> getParameters() {
		Map<String, Object> para = super.getParameters();
		para.put("gwType", "소노넷");

		return para;
	}
}