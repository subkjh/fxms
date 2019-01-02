package com.fxms.ui.dx.item.chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiChartVo;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.chart.AreaChart;

public class DxItemAreaChart extends TimeSeriesChartPs implements DxNode, FxChart {

	public DxItemAreaChart() {
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {

	}

	@Override
	public void viewChart(Mo mo, UiChartVo chart) {

		if (mo == null || chart == null) {
			StringBuffer sb = new StringBuffer();
			if (mo == null) {
				sb.append("No Managed Object");
			}
			if (chart == null) {
				if (sb.length() > 0) {
					sb.append(" & ");
				}
				sb.append("No Chart");
			}

			showMessage(sb.toString());
			return;
		}

		List<UiPsItemVo> psItemList = CodeMap.getMap().getPsItemList(chart.getPsCodes());
		if (psItemList.size() == 0) {
			showMessage("No Performance & Status Items");
			return;
		}

		setTitle(chart.getChartName());

		showMessage("Loading Datas...");

		initChart(AreaChart.class, chart.getPsType(), chart.getStartDate(), chart.getEndDate(),
				psItemList.get(0).getPsUnit());
		for (UiPsItemVo psItem : psItemList) {
			addDatas(mo, psItem, chart.getStartDate(), chart.getEndDate());
		}

		this.setTitle(mo.getMoName() + "'s " + chart.getChartName());

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number moNo = vo.getPropertyNumber("moNo");
		String chartId = vo.getProperty("chartId");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("moNo", moNo);
		viewChart(data, chartId);
		return true;
	}

	protected void viewChart(Map<String, Object> data, String chartId) {

		Mo mo = DxAsyncSelector.getSelector().getMo(data);
		
		System.out.println(mo);

		if (mo == null) {
			viewChart((Mo) null, (UiChartVo) null);
		} else {
			UiChartVo chartVo = null;
			if (chartId != null) {
				chartVo = CodeMap.getMap().getChartVo(mo, chartId);
			} else {
				chartVo = CodeMap.getMap().getChartVo(mo);
			}

			viewChart(mo, chartVo);
		}
	}

}
