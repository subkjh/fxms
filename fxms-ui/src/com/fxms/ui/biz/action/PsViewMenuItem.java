package com.fxms.ui.biz.action;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.item.chart.FxChart;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PsViewMenuItem<DATA> extends DataMenuItem<DATA> {

	public PsViewMenuItem(Node parent) {
		super(OP_NAME.MoChartShow);
		setOwnerPane(parent);
	}

	@Override
	protected void onAction(DATA data) {

		Mo mo = null;

		if (data instanceof Mo) {
			mo = (Mo) data;
		} else {
			mo = DxAsyncSelector.getSelector().getMo(getLong(ObjectUtil.toMap(data), "moNo", 0));
		}
	
		Pane pane = FxChart.makeChart(mo);
		
		FxStage.showStage(getOwnerPane(), pane, getText());
	}

}