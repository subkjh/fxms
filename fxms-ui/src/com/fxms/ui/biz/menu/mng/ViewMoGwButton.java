package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;

public class ViewMoGwButton extends ViewMoMenuButton {

	public ViewMoGwButton() {
		super("GW", "Gateways");
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoGwList;
	}

}