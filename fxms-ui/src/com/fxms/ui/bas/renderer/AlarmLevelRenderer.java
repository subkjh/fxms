package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.VarMap;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.UiAlarm.AlarmKind;

import javafx.scene.control.Label;

public class AlarmLevelRenderer extends Label implements FxRenderer {

	public AlarmLevelRenderer() {

	}

	public AlarmLevelRenderer(Object value) {
		setValue(value, null);
	}

	@Override
	public void setValue(Object value, String type) {

		if (value == null) {
			setText("");
			return;
		}

		int alarmLevel = 0;

		if (value instanceof Number) {
			alarmLevel = ((Number) value).intValue();
		} else {
			try {
				alarmLevel = Integer.valueOf(value.toString());
			} catch (Exception e) {
				return;
			}
		}

		String styleClass = "alarm-" + UiAlarm.AlarmLevel.getAlarmLevel(alarmLevel).name() + "-" + AlarmKind.cur;

		getStyleClass().add(styleClass);

		setText(VarMap.getVarMap().getAlarmName(alarmLevel));
	}

}
