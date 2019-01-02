package com.fxms.ui.node.alarm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.grid.FxGridPane;
import com.fxms.ui.bas.grid.GridCfg;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.biz.pane.ShowAlarmDetailPane;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

import fxms.client.FxmsClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ClearAlarmBox extends FxGridPane implements DxListener<UiAlarm>, DxNode {

	public ClearAlarmBox() {

		super("Recently-cleared Alarms", new GridCfg(5, 70, 20));

		setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
	}

	@Override
	public void onData(Action action, final UiAlarm data) {

		if (action == Action.remove) {

			String id = String.valueOf(data.getAlarmNo());

			Button btn = new Button(id);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					FxDialog.showDialog(ClearAlarmBox.this, new ShowAlarmDetailPane(data), "경보상세보기");
				}
			});

			btn.setUserData(data);
			btn.getStyleClass().add("alarm-level-" + data.getAlarmLevel() + "-cleared");

			add(id, btn);

		} else if (action == Action.loop) {

			Object userData;

			UiAlarm alarm;
			List<Node> list = new ArrayList<Node>();
			for (Node e : grid.getChildren()) {
				userData = e.getUserData();
				if (userData instanceof UiAlarm) {
					alarm = (UiAlarm) userData;
					if (alarm.getOcuDate() < FxmsClient.getDate(
							System.currentTimeMillis() - (DxAlarmReceiver.SHOW_CLEAR_ALARM_MINUTES * 60000L))) {
						list.add(e);
					}
				}
			}

			for (Node e : list) {
				this.remove(e.getId());
			}
		}
	}

	@Override
	protected void sort(List<Node> nodeList) {
		nodeList.sort(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o2.getId().compareTo(o1.getId());
			}

		});
	}

	@Override
	public void onAddedInParent() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn();
			}
		});

		for (UiAlarm alarm : alarmList) {
			onData(Action.remove, alarm);
		}

		DxAlarmReceiver.getBorader().add(this);
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return false;
	}
}
