package com.fxms.ui.biz.action.mo;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.pane.list.ListPaneBase;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.biz.action.DataMenuItem;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.Node;

public class MoAlarmMenuItem<DATA> extends DataMenuItem<DATA> {

	public MoAlarmMenuItem(Node parent) {
		super(OP_NAME.AlarmHstMoList);
		this.setOwnerPane(parent);
	}

	@Override
	protected void onAction(DATA data) {
		Mo mo = null;

		if (data instanceof Mo) {
			mo = DxAsyncSelector.getSelector().getMo(((Mo) data).getMoNo());
		} else {
			mo = DxAsyncSelector.getSelector().getMo(getLong(ObjectUtil.toMap(data), "moNo", 0));
		}

		if (mo == null) {
			FxAlert.showAlert(FxAlertType.error, getOwnerPane(), getOpCode().getOpTitle(), "Not Found");
		} else {
			
			ListPaneBase node = new ListPaneBase();
			node.init(getOpCode());
			Map<String, Object> map = new HashMap<>();
			map.put("moNo", mo.getMoNo());
			map.putAll(getOpCode().getConstMap());
			node.initData(map);

			FxStage.showDialog(getOwnerPane(), node, getOpCode().getOpTitle());
			
			node.doSearch();
		}
	}

}