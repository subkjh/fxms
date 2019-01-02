package com.fxms.ui.dx.item;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.css.image.ImagePointer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class ReloadDataButton extends FxMenuButton {

	public ReloadDataButton() {

		super();

		getCenterButton().setGraphic(new ImageView(ImagePointer.getImage("s32x32/refresh-codes.png")));
		getCenterButton().setTooltip(new Tooltip("reload codes"));

		getCenterButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				new Thread() {
					public void run() {
						try {
							UiCode.reload(DxItemInformation.getInformation());
						} catch (Exception e1) {
							DxItemInformation.getInformation().showMsg(0, e1.getMessage());
						}
					}
				}.start();

			}
		});
	}

	@Override
	protected OP_NAME getContentOpName() {
		return null;
	}

}