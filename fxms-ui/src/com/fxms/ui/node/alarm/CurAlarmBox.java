package com.fxms.ui.node.alarm;

import java.util.List;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.biz.pane.ShowAlarmDetailPane;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class CurAlarmBox extends ScrollPane implements DxListener<UiAlarm>, DxNode {

	private final FlowPane pane;

	public CurAlarmBox() {

		pane = new FlowPane();
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setVgap(4);
		pane.setHgap(4);
		pane.setOrientation(Orientation.HORIZONTAL);

		setContent(pane);
		setFitToHeight(true);
		setFitToWidth(true);
		setStyle("-fx-background-color: transparent;");

		setBorder(new Border(
				new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

	}

	@Override
	public void onData(Action action, final UiAlarm alarm) {

		if (action == Action.add) {

			String id = String.valueOf(alarm.getAlarmNo());

			Button btn = new Button(alarm.getMoName() + " " + alarm.getAlcdName());
			btn.setId(id);
			btn.setPrefSize(200, 20);
			btn.setUserData(alarm);
			btn.getStyleClass().add("alarm-level-" + alarm.getAlarmLevel());
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					FxDialog.showDialog(CurAlarmBox.this, new ShowAlarmDetailPane(alarm), "경보상세보기");
				}
			});

			pane.getChildren().add(0, btn);

		} else if (action == Action.remove || action == Action.update) {

			Node toRemove = null;
			String id = String.valueOf(alarm.getAlarmNo());

			for (Node e : pane.getChildren()) {
				if (id.equals(e.getId())) {
					toRemove = e;
					break;
				}
			}

			if (toRemove != null) {
				pane.getChildren().remove(toRemove);
			}
		}
	}

	@Override
	public void onAddedInParent() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn() == false && alarm.getAckDate() == 0;
			}
		});

		for (UiAlarm alarm : alarmList) {
			onData(Action.add, alarm);
		}

		DxAlarmReceiver.getBorader().add(this);

	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

}
