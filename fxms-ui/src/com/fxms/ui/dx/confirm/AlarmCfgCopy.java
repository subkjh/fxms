package com.fxms.ui.dx.confirm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;

public class AlarmCfgCopy implements Confirmer {

	@Override
	public void confirm(Node node, UiOpCodeVo opCode, Map<String, Object> data, DxCallback callback) {
		TextInputDialog dialog = new TextInputDialog(data.get("alarmCfgName") + " 2");
		dialog.initOwner(node.getScene().getWindow());
		dialog.setTitle(opCode.getOpTitle());
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Please enter new alarm-config-name : ");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("oldAlarmCfgNo", data.get("alarmCfgNo"));
			parameters.put("newAlarmCfgName", result.get());

			Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(node, opCode, parameters);
			if (ret != null && callback != null) {
				callback.onCallback(ret);
			}

		}
	}

}
