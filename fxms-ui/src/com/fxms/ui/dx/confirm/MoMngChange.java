package com.fxms.ui.dx.confirm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class MoMngChange implements Confirmer {

	@Override
	public void confirm(Node node, UiOpCodeVo opCode, Map<String, Object> data, DxCallback callback) {

		Mo mo = new Mo();
		ObjectUtil.toObject(data, mo);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(node.getScene().getWindow());
		alert.setTitle(mo.getMoNo() + (mo.getMoName() == null ? "" : ". " + mo.getMoName()));
		alert.setHeaderText(opCode.getOpTitle());
		alert.setContentText("Choose your option.");

		ButtonType bMngY = new ButtonType("Managed");
		ButtonType bMngN = new ButtonType("Unmanaged");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(bMngY, bMngN, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == bMngY) {
			mo.setMngYn(true);
		} else if (result.get() == bMngN) {
			mo.setMngYn(false);
		} else {
			return;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("moNo", mo.getMoNo());
		map.put("mngYn", mo.isMngYn() ? "Y" : "N");
		Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(node, opCode, map);
		if (ret != null && callback != null) {
			callback.onCallback(ret);
		}
	}
}
