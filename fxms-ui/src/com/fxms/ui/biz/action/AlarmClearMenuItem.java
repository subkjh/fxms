package com.fxms.ui.biz.action;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.Node;

public class AlarmClearMenuItem extends FxDataMenuItem {

	public AlarmClearMenuItem(Node parent) {
		super(parent, OP_NAME.AlarmClear, null);
	}

	protected void onAction(Map<String, Object> data) {
		
		long alarmNo = getLong(data, "alarmNo", 0);
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmNo", alarmNo);

		DxAsyncSelector.getSelector().callMethod(getOwnerPane(), getOpCode(), para);
	}

}
