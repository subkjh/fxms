package com.fxms.ui.biz.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.OP_TYPE;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallfront;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class FxDataMenuItem extends DataMenuItem<Map<String, Object>> {

	private DxCallback callback;
	private FxCallfront callfront;

	public FxDataMenuItem(Node parent, OP_NAME opName, DxCallback callback) {

		super(opName);

		setOwnerPane(parent);

		this.callback = callback;
	}

	public FxDataMenuItem(Node parent, UiOpCodeVo opCode, DxCallback callback) {

		super(opCode);

		setOwnerPane(parent);

		this.callback = callback;
	}

	public void setCallfront(FxCallfront callfront) {
		this.callfront = callfront;
	}

	@Override
	protected void onAction(Map<String, Object> data) {

		if (getOpCode().getOpType() == OP_TYPE.delete.getCode()) {

			data.putAll(getOpCode().getConstMap());

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(getWindow());
			alert.setTitle(getOpCode().getOpTitle());
			alert.setHeaderText("선택한 작업을 진행할까요?\n" + getOpCode().getOpTitle());
			alert.setContentText("TARGET\n" + data);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {

				Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(getOwnerPane(), getOpCode(), data);
				if (ret != null && callback != null) {
					callback.onCallback(ret);
				}
			}

		} else {

			Map<String, Object> initData = data == null ? new HashMap<String, Object>()
					: new HashMap<String, Object>(data);

			if (getParameters().size() > 0) {
				initData.putAll(getParameters());
			}

			getOpCode().showScreen(getOwnerPane(), initData, callfront, callback);

		}

	}

}