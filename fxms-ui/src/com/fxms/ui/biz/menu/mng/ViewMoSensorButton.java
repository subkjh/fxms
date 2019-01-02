package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.mo.UiDeviceMo;

public class ViewMoSensorButton extends ViewMoMenuButton {

	public ViewMoSensorButton() {
		super(UiDeviceMo.MO_CLASS, "Sensors");
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoSensorList;
	}

}