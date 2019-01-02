package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewPerfIfButton extends FxMenuButton {

	public ViewPerfIfButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.PsMoIfView;
	}

}