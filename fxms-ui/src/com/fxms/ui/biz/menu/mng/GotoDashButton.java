package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.biz.main.DxMainRun;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GotoDashButton extends FxMenuButton {

	public GotoDashButton() {
		super();

		getCenterButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				new DxMainRun().run(null);
			}
		});
	}

	@Override
	protected OP_NAME getContentOpName() {
		return null;
	}

}