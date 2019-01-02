package com.fxms.ui.node.network;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

public class NetworkMonitorPane extends NetworkDrawPane implements DxNode, DxListener<UiAlarm> {

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().add(this);
		showAlarm();
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}

	@Override
	public void redraw() {
		
		super.redraw();
		
		showAlarm();
	
	}

	private void showAlarm() {
		for (NetworkNode node : getNetworkNodeList()) {
			DxAlarmReceiver.getBorader().setAlarmNode(node, node.getMoNo(), box);
		}
	}

	@Override
	public void onData(Action action, UiAlarm data) {

		if (Action.loop == action) {
			return;
		}

		showAlarm();

	}

}
