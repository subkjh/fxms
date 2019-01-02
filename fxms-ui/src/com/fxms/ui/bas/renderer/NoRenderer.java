package com.fxms.ui.bas.renderer;

import javafx.scene.control.Label;

public class NoRenderer extends Label {

	public NoRenderer(Object msdate) {
		setText(String.valueOf(((Number) msdate).longValue()));
	}

}
