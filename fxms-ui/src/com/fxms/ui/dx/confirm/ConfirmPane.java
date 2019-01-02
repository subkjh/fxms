package com.fxms.ui.dx.confirm;

import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConfirmPane implements Confirmer {

	public ConfirmPane() {

	}

	@Override
	public void confirm(Node node, UiOpCodeVo opCode, Map<String, Object> data, DxCallback callback) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Lang.getText(Type.button, "확인"));
		alert.setHeaderText(Lang.getText(Type.msg, "작업 진행 확인"));
		
		if (opCode.getConfirmMsg() != null && opCode.getConfirmMsg().trim().length() > 0) {
			alert.setContentText(opCode.getConfirmMsg());
		} else {
			alert.setContentText(Lang.getText(Type.msg, "작업을 진행할까요?"));
		}

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(node, opCode, data);
			if (ret != null && callback != null) {
				callback.onCallback(ret);
			}
		}
	}

}
