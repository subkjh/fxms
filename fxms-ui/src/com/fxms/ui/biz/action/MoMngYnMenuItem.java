package com.fxms.ui.biz.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class MoMngYnMenuItem extends DataMenuItem<Map<String, Object>> {

	private FxCallback<Map<String, Object>> callback;

	public MoMngYnMenuItem(Node parent, final FxCallback<Map<String, Object>> callback) {
		super(OP_NAME.MoAttrMngUpdate);
		setOwnerPane(parent);
		this.callback = callback;
	}

	@Override
	protected void onAction(Map<String, Object> data) {

		Mo mo = new Mo();
		ObjectUtil.toObject(data, mo);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(getWindow());
		alert.setTitle(mo.getMoNo() + (mo.getMoName() == null ? "" : ". " + mo.getMoName()));
		alert.setHeaderText(getText());
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
		Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(getOwnerPane(), getOpCode(), map);
		if (ret != null && callback != null) {
			callback.onCallback(ret);
		}
	}

}
