package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.mo.PbrMo;

public class ViewMoPbrButton extends ViewMoMenuButton {

	public ViewMoPbrButton() {

		super(PbrMo.MO_CLASS, "PBRs");
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoPbrList;
	}

}