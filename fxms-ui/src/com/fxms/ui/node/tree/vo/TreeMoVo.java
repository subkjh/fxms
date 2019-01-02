package com.fxms.ui.node.tree.vo;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.UiAlarm;

public class TreeMoVo extends TreeItemVo {

	public TreeMoVo(Mo mo) {
		super(mo);
	}

	public Mo getMo() {
		return (Mo) getSource();
	}

	public void setTopAlarm(UiAlarm alarm) {
		if (alarm == null) {
			setTopAlarmLevel(-1);
		} else {
			setTopAlarmLevel(alarm.getAlarmLevel());
		}
	}

}
