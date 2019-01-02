package com.fxms.ui.biz.menu.mng;

import java.util.List;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.menu.FxCounterMenuButton;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

public class ViewAlarmHistoryButton extends FxCounterMenuButton implements DxListener<UiAlarm> {

	public ViewAlarmHistoryButton() {

	}

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().add(this);
		onData(Action.update, null);
	}

	@Override
	public void onData(Action action, UiAlarm data) {

		if (Action.loop == action) {
			return;
		}

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn() == false && alarm.getAckDate() == 0;
			}
		});

		getTopLabel().setText("Alarm");
		getCenterButton().setText(String.valueOf(alarmList.size()));
		getBottomLabel().setText("Current ALARMs");

	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.AlarmHstList;
	}

}