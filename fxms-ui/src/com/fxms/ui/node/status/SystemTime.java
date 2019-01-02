package com.fxms.ui.node.status;

import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;

import FX.MS.UiConfig;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SystemTime extends BorderPane implements DxNode {

	private final Timeline timeline;
	private Text text;
	private final double WIDTH = 235;
	private final double HEIGHT = 64;

	public SystemTime() {

		text = new Text("time");
		text.setId("system-time");

		setPadding(new Insets(0, 0, 0, 0));
		setStyle("-fx-background-color: transparent");
		setCenter(text);

		onCycle();

		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), (ActionEvent actionEvent) -> {
			onCycle();
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		int height = vo.getUiHeight();
		int width = vo.getUiWidth();

		if (height > 0 && width > 0) {
			setScaleY(height / HEIGHT);
			setScaleX(width / WIDTH);
		}

		return true;
	}

	@Override
	public void onAddedInParent() {
		if (timeline != null) {
			onCycle();
			timeline.play();
		}
	}

	@Override
	public void onRemovedFromParent() {
		if (timeline != null) {
			timeline.stop();
		}
	}

	private void onCycle() {
		text.setText(UiConfig.getConfig().getNowSystemTime());
	}

}
