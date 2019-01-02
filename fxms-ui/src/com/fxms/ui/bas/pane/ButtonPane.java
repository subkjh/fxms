package com.fxms.ui.bas.pane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ButtonPane extends HBox {

	public ButtonPane() {
		super(10);
		setPadding(new Insets(3, 3, 3, 10));
		setAlignment(Pos.TOP_RIGHT);
	}
}
