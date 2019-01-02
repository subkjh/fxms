package com.fxms.ui.biz.action;

import java.util.List;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.UiChartVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.NeedSelectedData;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.item.chart.FxChart;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class MoChartMenu extends Menu implements NeedSelectedData<Object> {

	private Node parent;

	public MoChartMenu(Node parent) {
		super("Show Chart");
		this.parent = parent;
	}

	@Override
	public void onSelectedData(Object data) {

		Mo mo = null;

		if (data instanceof Mo) {
			mo = (Mo) data;
		} else {
			mo = DxAsyncSelector.getSelector().getMo(Long.valueOf(ObjectUtil.toMap(data).get("moNo").toString()));
		}

		getItems().clear();

		final Mo mo2 = mo;

		List<UiChartVo> chartList = CodeMap.getMap().getChartList(mo);
		for (UiChartVo chart : chartList) {
			MenuItem item = new MenuItem(chart.getChartName());
			item.setOnAction((ActionEvent e) -> {
				Pane pane = FxChart.makeChart(mo2, chart);
				FxStage.showStage(parent, pane, getText());
			});
			getItems().add(item);
		}
		
		setDisable(getItems().size() == 0);
	}
}
