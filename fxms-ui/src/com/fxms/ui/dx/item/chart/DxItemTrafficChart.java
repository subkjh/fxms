package com.fxms.ui.dx.item.chart;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.property.FxUi;

import javafx.util.StringConverter;

public class DxItemTrafficChart extends DxItemAreaChart implements DxNode, FxChart, FxUi {

	public DxItemTrafficChart() {
	}

	@Override
	protected Number makeYValue(UiPsItemVo psItem, Number value) {
		try {
			if (psItem.getPsCode().startsWith("IfIn")) {
				return value.doubleValue() * -1;
			}
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected StringConverter<Number> getYAxisStringConverter() {
		return new StringConverter<Number>() {

			@Override
			public Number fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Number num) {
				return UiPsItemVo.makeAutoUnit(num.doubleValue() < 0 ? num.doubleValue() * -1 : num.doubleValue(), 0);
			}
		};
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return CodeMap.getMap().getOpCode(OP_NAME.PsMoIfView);
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		
	}

	@Override
	public void initData(Map<String, Object> data) {
		this.viewChart(data, "NeIfBytes");
	}

}
