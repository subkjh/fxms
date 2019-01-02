package com.fxms.ui.biz.action;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlarmAckMenuItem extends FxDataMenuItem {

	public AlarmAckMenuItem(Node parent) {
		super(parent, OP_NAME.AlarmAck, null);
	}

	@Override
	protected void onAction(Map<String, Object> data) {

		long alarmNo = getLong(data, "alarmNo", 0);
		if (alarmNo <= 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Ack Information");
			alert.setHeaderText("Alarm Ack Fail");
			alert.setContentText("alarm-no = " + alarmNo);
			alert.showAndWait();
			return;
		}

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmNo", alarmNo);

		DxAsyncSelector.getSelector().callMethod(getOwnerPane(), getOpCode(), para);

	}

}
