package com.fxms.ui.dx.item;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewNetworkButton extends FxMenuButton {

	public ViewNetworkButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.NetworkListSelect;
	}

}
