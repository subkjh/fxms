package com.fxms.ui.dx.confirm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiAlarmCfgVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;

public class MoAlarmChgChange implements Confirmer {

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

		Mo mo = DxAsyncSelector.getSelector().getMo(moNo);

		if (mo == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(node.getScene().getWindow());
			alert.setTitle("MO");
			alert.setHeaderText("MO Not Found");
			alert.showAndWait();
			return;
		}

		UiAlarmCfgVo defCfg = CodeMap.getMap().getAlarmCfg(mo.getAlarmCfgNo());
		List<UiAlarmCfgVo> choices = new ArrayList<>();
		choices.add(new UiAlarmCfgVo(-1, "미정의"));
		choices.addAll(CodeMap.getMap().getAlarmCfgList());

		ChoiceDialog<UiAlarmCfgVo> dialog = new ChoiceDialog<>(defCfg, choices);
		dialog.initOwner(node.getScene().getWindow());
		dialog.setTitle(mo.toString());
		dialog.setHeaderText("Alarm Config Property");
		dialog.setContentText("Choose Alarm Config : ");

		// Traditional way to get the response value.
		Optional<UiAlarmCfgVo> result = dialog.showAndWait();
		if (result.isPresent()) {
			mo.setAlarmCfgNo(result.get().getAlarmCfgNo());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("moNo", mo.getMoNo());
			map.put("alarmCfgNo", mo.getAlarmCfgNo());

			Map<String, Object> ret = DxAsyncSelector.getSelector().callMethod(node, opCode, map);
			if (ret != null && callback != null) {
				callback.onCallback(ret);
			}
		}
	}

}
