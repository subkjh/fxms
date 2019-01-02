package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.menu.FxMenuButton;

public class ViewAlarmConfigButton extends FxMenuButton {

	public ViewAlarmConfigButton() {
	}

	@Override
	protected OP_NAME getContentOpName() {
		return OP_NAME.AlarmCfgList;
	}
//
//	@Override
//	protected Parent makeViewPane(UiOpCodeVo op) {
//
//		SearchListPane pane = new SearchListPane();
//		pane.init(op);
//
//		ListPaneBase listPane = pane.getListPane();
//
//		BasCurMenuItem miShowMos = new BasCurMenuItem(listPane, OP_NAME.AlarmCfgAppliedMoList, null) {
//			@Override
//			protected void onAction(Map<String, Object> data) {
//				getParameters().put("alarmCfgNo", data.get("alarmCfgNo"));
//				super.onAction(data);
//			}
//		};
//
//		listPane.getTableContextMenu().getItems().addAll( //
//				miShowMos//
//				, new BasCurMenuItem(listPane, OP_NAME.AlarmCfgUpdate, listPane.getSearchCallback()) //
//				, new SeparatorMenuItem() //
//				, new BasDelMenuItem(listPane, OP_NAME.AlarmCfgDelete, listPane.getSearchCallback()) //
//				, new SeparatorMenuItem() //
//				, new BasNewMenuItem(listPane, OP_NAME.AlarmCfgAdd, listPane.getSearchCallback()) //
//				, new AlarmCfgCopyMenuItem(listPane, listPane.getSearchCallback()));
//		return pane;
//	}

}