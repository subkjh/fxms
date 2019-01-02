package com.fxms.ui.bas.renderer;

import javafx.scene.control.TextField;

public class TextRenderer extends TextField implements FxRenderer {

	public TextRenderer() {
		this.setEditable(false);
		this.getStyleClass().add("text-renderer");
		
	}

	@Override
	public void setValue(Object value, String type) {

		if (value == null) {
			setText("");
		} else {
			setText(value.toString());
		}
	}

}
