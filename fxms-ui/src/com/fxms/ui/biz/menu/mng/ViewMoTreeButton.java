package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.biz.pane.MoTreePane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViewMoTreeButton extends FxMenuButton {

	public ViewMoTreeButton() {

		getCenterButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FxWindow.showStage(ViewMoTreeButton.this, new MoTreePane(), "MO Tree");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.MoTreeShow;
	}

}
