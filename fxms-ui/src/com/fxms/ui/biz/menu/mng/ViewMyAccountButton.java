package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.bas.pane.EditorPane;

import javafx.scene.Parent;

public class ViewMyAccountButton extends FxMenuButton {

	public ViewMyAccountButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.UserPwdChange;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo opcode) {
		EditorPane pane = new EditorPane(opcode.getOpTypeText(), "Cancel", true);
		pane.init(opcode);
		return pane;
	}
}