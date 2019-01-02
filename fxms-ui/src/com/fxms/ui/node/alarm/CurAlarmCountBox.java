package com.fxms.ui.node.alarm;

import java.util.List;

import com.fxms.ui.UiCode;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.VarMap;
import com.fxms.ui.bas.pane.CounterPane;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CurAlarmCountBox extends CounterPane implements DxListener<UiAlarm>, DxNode {

	private int alarmLevel;

	public CurAlarmCountBox() {

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number level = vo.getPropertyNumber("alarmLevel");

		alarmLevel = (level == null ? 0 : level.intValue());

		counterText.getStyleClass()
				.add("alarm-" + UiAlarm.AlarmLevel.getAlarmLevel(alarmLevel) + "-" + UiAlarm.AlarmKind.cur);
		titleText.getStyleClass()
				.add("alarm-" + UiAlarm.AlarmLevel.getAlarmLevel(alarmLevel) + "-" + UiAlarm.AlarmKind.cur);

		titleText.setText(VarMap.getVarMap().getAlarmName(alarmLevel));
		counterText.setFill(UiCode.getAlarmColor(alarmLevel));

		counterText.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 1) {
					String title = "Alarm List (alarm-kind=" + UiAlarm.AlarmKind.cur + ", alarm-level=" + alarmLevel
							+ ")";

					DxAlarmReceiver.AlarmFilter filter = new DxAlarmReceiver.AlarmFilter() {
						@Override
						public boolean isOk(UiAlarm alarm) {
							return alarm.isClearYn() == false && alarm.getAlarmLevel() == alarmLevel;
						}
					};

					AlarmListPane pane = new AlarmListPane();
					pane.onRefresh(filter);
					FxWindow.showStage(CurAlarmCountBox.this, pane, title);
				}
			}

		});

		return true;
	}

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().add(this);
		onData(Action.add, null);
	}

	@Override
	public void onData(Action action, UiAlarm alarm) {

		if (action == Action.loop) {
			return;
		}

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {

			@Override
			public boolean isOk(UiAlarm alarm) {
				if (alarm.isClearYn() == false && alarm.getAlarmLevel() == alarmLevel) {
					return true;
				}
				return false;
			}
		});

		counterText.setText(String.valueOf(alarmList.size()));
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ", alarm-level=" + alarmLevel;
	}
}
