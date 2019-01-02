package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.mo.NeMo;

public class ViewMoNeButton extends ViewMoMenuButton {

	public ViewMoNeButton() {
		super(NeMo.MO_CLASS, "Network Devices");
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoNeList;
	}

}