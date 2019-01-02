package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.biz.pane.LocationTreeTableViewPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViewLocationTreeButton extends FxMenuButton {

	public ViewLocationTreeButton() {

		getCenterButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FxStage.showStage(ViewLocationTreeButton.this, new LocationTreeTableViewPane(),
							getCenterButton().getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.LocationTreeView;
	}

}
