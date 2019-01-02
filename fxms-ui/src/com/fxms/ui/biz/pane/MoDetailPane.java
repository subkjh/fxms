package com.fxms.ui.biz.pane;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.pane.ShowDetailPane;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.DxAsyncSelector;

public class MoDetailPane extends ShowDetailPane {

	public MoDetailPane(Mo mo) {

		UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.MoDetailShow);
		UiOpCodeVo op2 = CodeMap.getMap().getOpCode("mo-" + mo.getMoClass().toLowerCase() + "-detail-show");

		if (op2 == null) {
			init(opCode);
		} else {
			init(UiOpCodeVo.merge(opCode, op2));
		}

		Map<String, Object> map = ObjectUtil.toMap(mo);
		
		super.initData(map);

	}

	public MoDetailPane() {

	}

	@Override
	public void initData(Map<String, Object> data) {

		Mo mo;
		try {
			mo = DxAsyncSelector.getSelector().getMo(data);
		} catch (Exception e) {
			FxAlert.showAlert(FxAlertType.error, this, "Not Found", e.getMessage());
			return;
		}

		Map<String, Object> map = ObjectUtil.toMap(mo);

		super.initData(map);

	}

}
