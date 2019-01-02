package com.fxms.ui.bas.property;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.dx.DxAlarmReceiver;

public interface AlarmRefresher {

	public void onRefresh(DxAlarmReceiver.AlarmFilter alarmFilter);

	public static List<AlarmRefresher> refresherList = new ArrayList<AlarmRefresher>();

	public static void addRefresher(AlarmRefresher refresher) {
		if (refresherList.contains(refresher) == false) {
			refresherList.add(refresher);
		}
	}

	public static void removeRefresher(AlarmRefresher refresher) {
		refresherList.remove(refresher);
	}

	public static void requestRefresh(DxAlarmReceiver.AlarmFilter alarmFilter) {
		for (AlarmRefresher r : refresherList) {
			r.onRefresh(alarmFilter);
		}
	}
}
