package com.fxms.ui.node.alarm;

import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAlarmReceiver;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class AlarmReceiverStatusDxNode extends Label implements DxNode {

	public class ErrorPane extends Label {

		public ErrorPane(Scene scene) {

			setPrefSize(600, 80);

			setBorder(new Border(
					new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

			setLayoutX(scene.getWidth() / 2 - getPrefWidth() / 2);
			setLayoutY(scene.getHeight() / 2 - getPrefHeight() / 2);

			setId("dx-error-dialog");

		}

		public void addMessage(String msg) {
			setText(msg);
			toFront();
		}
	}

	private ErrorPane errorPane;

	public AlarmReceiverStatusDxNode() {
		setId("dx-node-alarm-receiver-status");
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().setLinkStatus(this);

	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().setLinkStatus(null);
	}

	public void setStatus(boolean connected) {
		Platform.runLater(new Runnable() {
			public void run() {
				
				setText(connected ? "ON-LINE" : "OFF-LINE");
				
				if ((getParent() instanceof Pane) == false) {
					return;
				}

				Pane pane = (Pane) getParent();

				if (connected == false) {
					if (errorPane == null) {
						errorPane = new ErrorPane(getScene());
					}
					if (pane.getChildren().contains(errorPane) == false) {
						pane.getChildren().add(errorPane);
						errorPane.setText("Alarm Receiver Off-line Error!!!");
						pane.setDisable(true);
					}
				} else if (errorPane != null) {
					if (pane.getChildren().contains(errorPane)) {
						pane.getChildren().remove(errorPane);
						pane.setDisable(false);
					}
				}
			}
		});
	}
}
