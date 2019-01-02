package com.fxms.ui.bas.renderer;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class YnRenderer extends HBox implements FxRenderer {

	public YnRenderer() {
		super(8);
	}

	public YnRenderer(Object value) {
		super(8);
		setValue(value, null);
	}

	@Override
	public void setValue(Object value, String type) {
		Boolean isY = null;
		if (value instanceof Boolean) {
			isY = ((Boolean) value).booleanValue();
		} else if (value != null) {
			isY = String.valueOf(value).equalsIgnoreCase("y");
		}

		CheckBox rbYes = new CheckBox();

		if (isY != null) {
			if (isY) {
				rbYes.setSelected(true);
			}
		}

		this.getChildren().add(rbYes);
	}

}