package com.fxms.ui.biz.menu.mng;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.bas.pane.EditorPane;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Parent;

public class UpdateMyInfoButton extends FxMenuButton {

	public UpdateMyInfoButton() {
		super();
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.UserMyChange;
	}

	@Override
	protected Parent makeScreen(UiOpCodeVo opcode) {

		EditorPane pane = new EditorPane(opcode.getOpTypeText(), "Cancel", true);
		pane.setCallback(new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				DxAsyncSelector.getSelector().getUserMap().putAll(data);
			}
		});

		pane.init(opcode);
		pane.initData(DxAsyncSelector.getSelector().getUserMap());

		return pane;
	}
}