package com.fxms.ui.bas.pane;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CounterPane extends BorderPane {

	protected final Label titleText;
	protected final Text counterText;

	public CounterPane() {
		titleText = new Label();
		// title.getStyleClass().add("alarm-level-" + level + "-text");
		titleText.setText("-");
		titleText.setAlignment(Pos.CENTER);
		// title.setFill(UiCode.getAlarmColor(level));
		titleText.setFont(Font.font(null, FontWeight.BOLD, 12));
		// titleText.setX(25);
		// titleText.setY(65);

		counterText = new Text();
		// title.getStyleClass().add("alarm-level-" + level + "-text");
		counterText.setText("-");
		counterText.setTextAlignment(TextAlignment.CENTER);
		// title.setFill(UiCode.getAlarmColor(level));
		counterText.setFont(Font.font(null, FontWeight.BOLD, 24));
		// counter.setX(25);
		// counter.setY(65);

		counterText.setEffect(new Glow());

		setTop(titleText);
		setCenter(counterText);
	}

	public void setText(String text) {
		titleText.setText(text);
	}

	public void setCounter(int counter) {
		this.counterText.setText(String.valueOf(counter));
	}
}
