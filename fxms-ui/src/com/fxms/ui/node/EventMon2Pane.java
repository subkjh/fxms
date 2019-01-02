package com.fxms.ui.node;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.event.FxEvent;
import com.fxms.ui.bas.pane.list.ListPaneBase;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ListData;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxEventReceiver;
import com.fxms.ui.dx.DxListener;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

/**
 * 수신된 이벤트 종류를 표시하는 클래스
 * 
 * @author SUBKJH-DEV
 *
 */
public class EventMon2Pane extends FlowPane implements DxListener<FxEvent>, DxNode, EventHandler<MouseEvent> {

	private final Timeline timeline;

	public EventMon2Pane() {

		super(8, 8);

		setPadding(new Insets(4, 4, 4, 4));
		setStyle("-fx-background-color: transparent;");

		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), (ActionEvent actionEvent) -> {
			blurCircle();
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);

		Circle circle = makeCircle("method-call", Color.FORESTGREEN);
		getChildren().add(circle);
		circle = makeCircle("polling-completed", Color.BROWN);
		getChildren().add(circle);
		circle = makeCircle("alarm", Color.RED);
		getChildren().add(circle);
		circle = makeCircle("etc", Color.BLUEVIOLET);
		getChildren().add(circle);

	}

	private synchronized void blurCircle() {
		Circle circle;

		for (Node node : getChildren()) {
			if (node instanceof Circle) {
				circle = (Circle) node;
				if (circle.getOpacity() > 0.1) {
					circle.setOpacity(circle.getOpacity() - 0.01);
				}
				if (circle.getStrokeWidth() > 2) {
					circle.setStrokeWidth(circle.getStrokeWidth() - 0.2);
				}
			}
		}
	}

	private Circle makeCircle(String eventType, Color color) {

		final Circle circle = new Circle(15);

		circle.setId(eventType);
		circle.setStroke(color);
		circle.setStrokeWidth(4);
		circle.setStrokeType(StrokeType.INSIDE);
		circle.setFill(Color.AZURE);
		circle.setOpacity(1);
		circle.setUserData(new ArrayList<FxEvent>());

		circle.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

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

		double radius;

		double height = getHeight();
		double width = getWidth();

		if (vo != null) {
			height = vo.getUiHeight();
			width = vo.getUiWidth();
		}

		if (height > width) {
			// 세로
			radius = (width - getPadding().getLeft() - getPadding().getRight() - 4) / 2;
		} else {
			// 가로
			radius = (height - getPadding().getBottom() - getPadding().getTop() - 4) / 2;
		}

		radius /= getChildren().size();
		if (radius < 15) {
			radius = 15;
		}

		for (Node node : getChildren()) {
			if (node instanceof Circle) {
				((Circle) node).setRadius(radius);
			}
		}

		return true;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void onData(Action action, FxEvent data) {

		if (data == null) {
			return;
		}
		Circle circle = null;

		for (Node node : getChildren()) {
			if (node.getId().equals(data.getEventType().toLowerCase())) {
				circle = (Circle) node;
				break;
			}
		}

		if (circle == null) {
			for (Node node : getChildren()) {
				if (node.getId().equals("etc")) {
					circle = (Circle) node;
					break;
				}
			}
		}

		List<FxEvent> list = (List<FxEvent>) circle.getUserData();
		list.add(0, data);
		if (list.size() > 100) {
			list.remove(list.size() - 1);
		}

		if (circle.getStrokeWidth() < 10) {
			circle.setStrokeWidth(circle.getStrokeWidth() + 1);
		}
		circle.setOpacity(1);
		Tooltip.install(circle, new Tooltip(circle.getId()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(MouseEvent e) {

		Circle node = (Circle) e.getSource();

		if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {

			List<FxEvent> list = (List<FxEvent>) node.getUserData();
			ListData listData = new ListData();
			listData.setColumns(new String[] { "D150_DATE", "S200_EVENT_TYPE", "S100_EVENT_STATUS", "S600_EVENT_MSG" });
			for (FxEvent event : list) {
				listData.getDataList()
						.add(new Object[] { event.getDate(), event.getEventType(), event.getStatus(), event.getTarget() });
			}

			ListPaneBase listPane = new ListPaneBase();
			listPane.setPrefSize(1.6181 * 600, 600);
			listPane.init(CodeMap.getMap().getOpCode(OP_NAME.Etc));
			FxStage stage = new FxStage(listPane, "Event List");
			stage.showDialog(EventMon2Pane.this);
			listPane.showData(listData);

		}
	}
}
