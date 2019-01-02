package com.fxms.ui.bas.window;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class InnerWindow extends BorderPane {

	private final InnerWindowTitle titleBar;

	public void setTitle(String title) {
		titleBar.setTitle(title);
	}

	public InnerWindow(Pane parent, Node node) {

		titleBar = new InnerWindowTitle() {
			@Override
			protected void onCloseCliked() {
				parent.getChildren().remove(InnerWindow.this);
			}
		};

		setTop(titleBar);
		setCenter(node);
	}
}
