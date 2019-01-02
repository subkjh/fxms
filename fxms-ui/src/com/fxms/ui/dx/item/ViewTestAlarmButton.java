package com.fxms.ui.dx.item;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewTestAlarmButton extends FxMenuButton {

	public ViewTestAlarmButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.TestAlarmFire;
	}

}
