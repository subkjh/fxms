package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.CodeMap;

import javafx.scene.control.TextField;

public class PsCodeRenderer extends TextField implements FxRenderer {

	public PsCodeRenderer() {
		getStyleClass().add("text-renderer");
	}

	@Override
	public void setValue(Object value, String type) {

		if (value != null) {
			try {
				setText(CodeMap.getMap().getPsItemName(value.toString()));
				return;
			} catch (Exception e) {
			}
		}
		setText("");

	}
}
