package com.fxms.ui.dx.confirm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;

public class MoAliasChange implements Confirmer {

	@Override
	public void confirm(Node node, UiOpCodeVo opCode, Map<String, Object> data, DxCallback callback) {

		Object value = data.get("moNo");
		long moNo = -1;
		try {
			moNo = Double.valueOf(value.toString()).longValue();
		} catch (Exception e) {
		}

		if (moNo < 0) {
			return;
		}

		TextInputDialog dialog = new TextInputDialog(String.valueOf(data.get("moAname")));
		dialog.initOwner(node.getScene().getWindow());
		dialog.setTitle(opCode.getOpTitle());
		dialog.setHeaderText("Change Alias");
		dialog.setContentText("Please enter new alias : ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("moAname", result.get());
			parameters.put("moNo", moNo);
			Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(node, opCode, parameters);
			if (ret != null && callback != null) {
				callback.onCallback(ret);
			}
		}
	}
}
