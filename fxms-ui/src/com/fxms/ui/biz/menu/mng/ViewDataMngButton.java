package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewDataMngButton extends FxMenuButton {

	public ViewDataMngButton() {
		super();
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.EtcTableUi;
	}

}
