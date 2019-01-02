package com.fxms.ui.node.tree;

import java.util.List;
import java.util.Map;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;
import com.fxms.ui.node.tree.vo.TreeItemVo;
import com.fxms.ui.node.tree.vo.TreeMoVo;

public class FxTreePane extends FxTreeBase implements DxNode, DxListener<UiAlarm>, FxUi {

	public FxTreePane(String moClass) {
		super(moClass);
	}

	public FxTreePane() {
		super(null);
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return null;
	}

	@Override
	public void init(UiOpCodeVo opcode) {

	}

	@Override
	public void initData(Map<String, Object> data) {

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		treeView.getStylesheets().add(CssPointer.getStyleSheet("dx-treeview.css"));
		return true;
	}

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().add(this);
	}

	@Override
	public void onData(Action action, UiAlarm alarm) {

		if (alarm != null) {

			TreeMoVo treeMo = moMap.get(alarm.getMoNo());
			if (treeMo != null) {
				UiAlarm topAlarm = DxAlarmReceiver.getBorader().getTopAlarm(treeMo.getMo().getMoNo());
				treeMo.setTopAlarm(topAlarm);
			}

			treeView.refresh();

		}

	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	private void initAlarm() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return true;
			}
		});

		for (UiAlarm alarm : alarmList) {
			onData(Action.update, alarm);
		}

	}

	@Override
	protected void onMakeCompleted() {
		initAlarm();
	}

	@Override
	protected void onSelected(TreeItemVo treeItemVo) {

	}

}
