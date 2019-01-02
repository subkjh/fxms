package com.fxms.ui.node;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.event.FxEvent;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxEventReceiver;
import com.fxms.ui.dx.DxListener;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class EventMonPane extends AnchorPane implements DxListener<FxEvent>, DxNode {

	private final PhongMaterial blueMaterial = new PhongMaterial();
	private final PhongMaterial redMaterial = new PhongMaterial();

	private final Timeline timeline;

	public EventMonPane() {

		blueMaterial.setDiffuseColor(Color.BLUE);
		blueMaterial.setSpecularColor(Color.LIGHTBLUE);

		redMaterial.setSpecularColor(Color.ORANGE);
		redMaterial.setDiffuseColor(Color.RED);

		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(20), (ActionEvent actionEvent) -> {
			moveNodes(3);
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);

		setStyle("-fx-background-color: transparent;");
	}

	private synchronized void moveNodes(int speed) {

		Circle node;
		List<Node> toRemoveList = new ArrayList<Node>();
		int size = getChildren().size();

		if (getWidth() > getHeight()) {
			for (int i = 0; i < size; i++) {
				node = (Circle) getChildren().get(i);
				node.setTranslateX(node.getTranslateX() + node.getRadius()*speed);
				if (node.getTranslateX() >= this.getWidth() - node.getRadius() * 2) {
					toRemoveList.add(node);
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				node = (Circle) getChildren().get(i);
				node.setTranslateY(node.getTranslateY() + node.getRadius()*speed);
				if (node.getTranslateY() >= this.getHeight() - node.getRadius() * 2) {
					toRemoveList.add(node);
				}
			}

		}

		for (Node e : toRemoveList) {
			getChildren().remove(e);
		}

	}

	private Circle makeEvent(String type) {
		// Sphere sphere = new Sphere();
		//
		// sphere.setRadius(10.0);
		// sphere.setTranslateX(0);
		// sphere.setTranslateY(getHeight() / 2);
		//
		// if ("test".equals(type)) {
		// sphere.setMaterial(blueMaterial);
		// } else {
		// sphere.setMaterial(redMaterial);
		// }
		//
		// return sphere;

		final Circle circle = new Circle(8);
		if ("method-call".equals(type)) {
			circle.setStroke(Color.FORESTGREEN);
		} else if ("polling-completed".equals(type)) {
			circle.setStroke(Color.BROWN);
		} else if ("alarm".equals(type)) {
			circle.setStroke(Color.RED);
		} else {
			circle.setStroke(Color.BLUEVIOLET);
		}


		circle.setStrokeWidth(2);
		circle.setStrokeType(StrokeType.INSIDE);
		circle.setFill(Color.AZURE);
		circle.setOpacity(.85);

		if (getWidth() > getHeight()) {
			circle.relocate(0, getHeight() / 2);
		} else {
			circle.relocate(getWidth() / 2, 0);

		}
		return circle;
	}

	@Override
	public void onAddedInParent() {
		DxEventReceiver.getReceiver().add(this);
		timeline.play();
	}

	@Override
	public void onRemovedFromParent() {
		timeline.stop();
		DxEventReceiver.getReceiver().remove(this);

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

	@Override
	public void onData(Action action, FxEvent data) {

		if (data == null) {
			return;
		}

		Circle node = makeEvent(data.getEventType().toLowerCase());
		getChildren().add(node);
		moveNodes(1);
	}

}
