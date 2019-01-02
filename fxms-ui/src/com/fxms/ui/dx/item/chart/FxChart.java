package com.fxms.ui.dx.item.chart;

import com.fxms.ui.bas.code.UiChartVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.mo.Mo;

import javafx.scene.layout.Pane;

public interface FxChart {

	public void viewChart(Mo mo, UiChartVo chart);

	public static Pane makeChart(Mo mo) {
		UiChartVo chartVo = CodeMap.getMap().getChartVo(mo);
		return makeChart(mo, chartVo);
	}

	public static Pane makeChart(Mo mo, UiChartVo chartVo) {
		FxChart chart = chartVo.makeChart();
		chart.viewChart(mo, chartVo);

		Pane pane = (Pane) chart;
		pane.setPrefSize(1.6181 * 500, 500);

		return pane;
	}

}
