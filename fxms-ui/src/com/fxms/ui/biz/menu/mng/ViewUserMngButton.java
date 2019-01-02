package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewUserMngButton extends FxMenuButton {

	public ViewUserMngButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.UserList;
	}

}