package com.fxms.ui.biz.action;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public abstract class FxButton extends Button {

	private UiOpCodeVo opCode;

	public FxButton(OP_NAME opName) {
		opCode = CodeMap.getMap().getOpCode(opName);

		if (opCode == null) {
			setText(opName.name());
			this.setDisable(true);
			return;
		}

		setText(opCode.getOpTypeText());

		setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				onAction();
			}
		});

	}

	public final UiOpCodeVo getOpCode() {
		return opCode;
	}

	protected abstract void onAction();
}
