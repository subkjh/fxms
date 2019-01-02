package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.CodeMap;

public class AlarmCodeRenderer extends TextRenderer {

	@Override
	public void setValue(Object value, String type) {

		if (value != null) {
			try {
				int alcdNo = FxRenderer.getInt(value, -1);
				if (alcdNo > 0) {
					setText(CodeMap.getMap().getAlarmCodeName(alcdNo));
					return;
				}
			} catch (Exception e) {
			}
		}
		setText("");

	}
}
