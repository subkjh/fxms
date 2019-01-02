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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public abstract class InnerWindowTitle extends HBox {

	private final Label titleLabel;
	private final Border contextBorder = new Border(
			new BorderStroke(Color.POWDERBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

	public InnerWindowTitle() {
		super(2);

		titleLabel = new Label("...");
		titleLabel.getStyleClass().add(CssPointer.FxInnerWindowTitle);

		Button close = new Button("", new ImageView(ImagePointer.getImage("s16x16/close.png")));
		close.setPadding(new Insets(0, 0, 0, 0));
		close.setPrefHeight(22);
		close.getStyleClass().add(CssPointer.FxInnerWindowClose);
		close.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				onCloseCliked();
			}
		});

		setAlignment(Pos.CENTER_LEFT);
		setBorder(contextBorder);
		getChildren().add(close);
		getChildren().add(titleLabel);
		getStyleClass().add(CssPointer.FxInnerWindowTitleBar);
	}

	public void setMsg(String msg) {
		titleLabel.setText(msg);
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	protected abstract void onCloseCliked();
}
