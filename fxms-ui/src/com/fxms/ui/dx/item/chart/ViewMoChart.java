package com.fxms.ui.dx.item.chart;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxUi;

public class ViewMoChart extends DxItemAreaChart implements FxUi {

	@Override
	public UiOpCodeVo getOpCode() {
		return CodeMap.getMap().getOpCode(OP_NAME.PsMoView);

	}

	@Override
	public void init(UiOpCodeVo opcode) {

	}

	@Override
	public void initData(Map<String, Object> data) {
		this.viewChart(data, null);
	}

}
