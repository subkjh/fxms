package com.fxms.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.property.ProgressIndicator;
import com.fxms.ui.bas.vo.UiAlarm;

import javafx.scene.paint.Color;

public class UiCode {

	public static String getAlarmStyleClass(UiAlarm alarm) {
		if (alarm == null)
			return null;

		String state = "cur";
		if (alarm.isClearYn()) {
			state = "cleared";
		} else if (alarm.getAckDate() > 0) {
			state = "acked";
		}

		switch (alarm.getAlarmLevel()) {
		case 1:
			return "alarm-critical-" + state;
		case 2:
			return "alarm-major-" + state;
		case 3:
			return "alarm-minor-" + state;
		case 4:
			return "alarm-warning-" + state;
		default:
			return null;
		}
	}

	public static Color getAlarmColor(int level) {

		switch (level) {
		case 1:
			return Color.ORANGERED;
		case 2:
			return Color.ORANGE;
		case 3:
			return Color.NAVAJOWHITE;
		case 4:
			return Color.MINTCREAM;
		default:
			return Color.BLACK;
		}
	}

	public static final String alarmName[] = new String[] { "", "Critical", "Major", "Minor", "Warning", "", "", "", "",
			"" };

	public static void reload(ProgressIndicator indicator) {

		Lang.load(indicator);

		CodeMap.getMap().reload(indicator);

		while (CodeMap.getMap().isLoaded() == false) {
			Thread.yield();
		}

		List<UiCodeVo> alarmLevelList = CodeMap.getMap().getCodeList("ALARM_LEVEL");
		if (alarmLevelList != null) {
			for (UiCodeVo code : alarmLevelList) {
				alarmName[Integer.valueOf(code.getCdCode())] = code.getCdName();
			}
		}
	}

	public enum Action {
		add, update, remove, loop, acked;
	}

	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");

	public synchronized static String getDate(long mstime) {
		if (mstime <= 0) {
			mstime = System.currentTimeMillis();
		}
		return HHMMSS.format(new Date(mstime));
	}

}
