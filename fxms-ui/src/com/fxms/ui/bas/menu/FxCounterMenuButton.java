package com.fxms.ui.bas.menu;

import com.fxms.ui.bas.property.DxNode;

import javafx.scene.control.Label;

public abstract class FxCounterMenuButton extends FxMenuButton implements DxNode {

	private Label top;
	private Label bottom;

	public FxCounterMenuButton() {
		super();
	}

	protected Label getBottomLabel() {
		if (bottom == null) {
			bottom = new Label();
			bottom.getStyleClass().add("dx-node-data-count-menu-bottom");
			bottom.setPrefWidth(this.getPrefWidth());
			setBottom(bottom);

			button.getStyleClass().clear();
			button.getStyleClass().add("dx-node-data-count-menu-center-number");
		}
		return bottom;
	}

	protected Label getTopLabel() {
		if (top == null) {
			top = new Label();
			top.getStyleClass().add("dx-node-data-count-menu-top");
			setTop(top);
		}
		return top;
	}

}