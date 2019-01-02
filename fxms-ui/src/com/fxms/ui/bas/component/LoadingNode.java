package com.fxms.ui.bas.component;

import javafx.scene.control.Label;

public class LoadingNode extends Label {

	public LoadingNode() {
	}

	public void setErrorMsg(String msg) {
		setText(msg);
		getStyleClass().add("message-error");
	}

	public void setWaitingMsg(String msg) {
		setText(msg);
		getStyleClass().add("message-wait");
	}
}
