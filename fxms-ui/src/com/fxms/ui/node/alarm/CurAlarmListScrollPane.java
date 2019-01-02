package com.fxms.ui.node.alarm;

import java.util.Comparator;
import java.util.List;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.property.AlarmRefresher;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

public class CurAlarmListScrollPane extends AlarmListPane implements DxListener<UiAlarm>, DxNode {

	public CurAlarmListScrollPane() {
		table.getStylesheets().add(CssPointer.getStyleSheet("dx-table.css"));
	}

	@Override
	public void onAddedInParent() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn() == false && alarm.getAckDate() == 0;
			}
		});

		alarmList.sort(new Comparator<UiAlarm>() {
			@Override
			public int compare(UiAlarm arg0, UiAlarm arg1) {
				return (int) (arg0.getAlarmNo() - arg1.getAlarmNo());
			}
		});

		for (UiAlarm alarm : alarmList) {
			onData(Action.add, alarm);
		}

		DxAlarmReceiver.getBorader().add(this);
		AlarmRefresher.addRefresher(this);
	}

	@Override
	public void onData(Action action, final UiAlarm alarm) {

		if (action == Action.add) {

			table.getItems().add(0, alarm);

		} else if (action == Action.remove) {

			UiAlarm toRemove = findAlarm(alarm.getAlarmNo());
			if (toRemove != null) {
				table.getItems().remove(toRemove);
				table.refresh();
			}

		} else if (action == Action.acked) {

			UiAlarm toChange = findAlarm(alarm.getAlarmNo());
			if (toChange != null) {
				toChange.setAckDate(alarm.getAckDate());
				toChange.setAckUserNo(alarm.getAckUserNo());
				table.refresh();
			}
		} else if (action == Action.update) {

			UiAlarm toChange = findAlarm(alarm.getAlarmNo());
			if (toChange != null) {
				ObjectUtil.toCopy(alarm, toChange);
				table.refresh();
			}
		} else if (action == Action.loop) {
			table.refresh();
		}
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
		AlarmRefresher.removeRefresher(this);
	}

}
