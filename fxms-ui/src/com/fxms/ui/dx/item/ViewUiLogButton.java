package com.fxms.ui.dx.item;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.bas.pane.list.ListPaneBase;

import FX.MS.UiData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ViewUiLogButton extends FxMenuButton {

	public ViewUiLogButton() {

		super();

		getCenterButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				ListPaneBase listPane = new ListPaneBase();
				listPane.init(getOpCode());
				listPane.setPrefSize(1.6181 * 600, 600);
				FxStage stage = new FxStage(listPane, getOpCode().getOpTitle());
				stage.showDialog(ViewUiLogButton.this);
				listPane.showData(UiData.getUiLog());
			}
		});

	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.DxMenuViewUiLog;
	}

}