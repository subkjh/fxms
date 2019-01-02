package com.fxms.nms.mo;

import com.fxms.nms.dbo.FN_MO_CARD;

import fxms.bas.mo.property.MoNeedManager;

public class NeCardMo extends FN_MO_CARD implements MoNeedManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542356707091446383L;
	/**
	 * MO분류. INTERFACE
	 */
	public static final String MO_CLASS = "CARD";

	@Override
	public long getManagerMoNo() {
		return getUpperMoNo();
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

}
