package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.mo.ContainerMo;

public class ViewMoContainerButton extends ViewMoMenuButton {

	public ViewMoContainerButton() {
		super(ContainerMo.MO_CLASS, "Bio Containers");
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoContainerList;
	}

}