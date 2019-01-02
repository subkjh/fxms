package com.fxms.ui.biz.action.mo;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.biz.action.DataMenuItem;
import com.fxms.ui.biz.pane.MoDetailPane;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.Node;

public class MoShowDetailMenuItem<DATA> extends DataMenuItem<DATA> {

	public MoShowDetailMenuItem(Node parent) {
		super(OP_NAME.MoDetailShow);
		this.setOwnerPane(parent);
	}

	@Override
	public void setOpCode(UiOpCodeVo opcode) {
		super.setOpCode(opcode);
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
			FxStage.showDialog(getOwnerPane(), new MoDetailPane(mo), getOpCode().getOpTitle());
		}
	}

}
