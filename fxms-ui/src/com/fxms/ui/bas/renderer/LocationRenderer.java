package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.CodeMap;

import javafx.scene.control.TextField;

public class LocationRenderer extends TextField implements FxRenderer {

	public LocationRenderer() {
		getStyleClass().add("text-renderer");
	}

	@Override
	public void setValue(Object value, String type) {

		int inloNo = FxRenderer.getInt(value, -1);

		if (inloNo < 0) {
			setText("");
		} else {
			setText(CodeMap.getMap().getLocationName(inloNo));
		}

	}

}
