package com.fxms.ui.bas.window;

import com.fxms.ui.css.CssPointer;
import com.fxms.ui.css.image.ImagePointer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FxWindowTitle extends BorderPane {

	private final Border contextBorder = new Border(
			new BorderStroke(Color.rgb(4, 67, 144), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

	private final Label labelMsg;
	private final Label labelTitle;
	private final Button bClose;
	private String title;
	private Stage stage;
	private long mstime;
	private Thread thread;
	private String lastMsg;

	public FxWindowTitle(String title) {

		this.title = title;

		bClose = new Button("", new ImageView(ImagePointer.getImage("s24x24/close.png")));
		bClose.setPadding(new Insets(0, 0, 0, 0));
		bClose.getStyleClass().add(CssPointer.FxWindowClose);
		bClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (stage != null) {
					stage.hide();
				}
			}
		});
		bClose.setDisable(true);

		// ImageView icon = new ImageView(ImagePointer.getImage("fx-32-32.jpg"));

		labelTitle = new Label(" :: " + title);
		labelTitle.setPrefHeight(36);
		// labelTitle.prefWidthProperty().bind(this.widthProperty());
		labelTitle.getStyleClass().add(CssPointer.FxWindowTitle);
		labelTitle.setPadding(new Insets(0, 2, 0, 2));

		labelMsg = new Label("");
		labelMsg.setMinSize(100, 20);
		labelMsg.setPadding(new Insets(12, 2, 0, 2));
		labelMsg.getStyleClass().add(CssPointer.FxWindowTitleMsg);
		labelMsg.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mstime = System.currentTimeMillis();
				if ( lastMsg != null) {
					labelMsg.setText(": " + lastMsg);
				}
			}
		});

		setBorder(contextBorder);
		getStyleClass().add(CssPointer.FxWindowTitleBar);

		HBox box = new HBox(5);
		box.setFillHeight(true);
		box.getChildren().addAll(labelTitle, labelMsg);
		new StageEvent().addEvent(box);

		// setLeft(icon);
		setCenter(box);
		setRight(bClose);
		setAlignment(bClose, Pos.CENTER_RIGHT);

	}
	public String getTitle() {
		return title;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
		bClose.setDisable(stage == null);
	}

	public void showMsg(String msg) {

		lastMsg = msg;
		labelMsg.setText(": " + msg);

		mstime = System.currentTimeMillis();

		if (thread == null) {

			thread = new Thread() {
				public void run() {

					while (true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (mstime + 5000 < System.currentTimeMillis()) {
							break;
						}
					}
					Platform.runLater(new Runnable() {
						public void run() {
							labelMsg.setText("");
							thread = null;
						}
					});
				}
			};
			thread.start();
		}
	}

}
