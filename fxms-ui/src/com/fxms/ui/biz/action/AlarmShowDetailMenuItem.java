package com.fxms.ui.biz.action;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.biz.pane.ShowAlarmDetailPane;

import javafx.scene.Node;

public class AlarmShowDetailMenuItem extends FxDataMenuItem {

	public AlarmShowDetailMenuItem(Node parent) {
		super(parent, OP_NAME.AlarmDetailShow, null);
	}

	@Override
	protected void onAction(Map<String, Object> data) {

		UiAlarm alarm = new UiAlarm();
		ObjectUtil.toObject(data, alarm);
		FxStage.showDialog(getOwnerPane(), new ShowAlarmDetailPane(alarm), getText());
	}

}
