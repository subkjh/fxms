package com.fxms.ui.dx.item.chart;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxRtPsRequestor;

import fxms.client.log.Logger;
import javafx.scene.chart.AreaChart;

public class DxItemRtPsAreaChart extends TimeSeriesChartRt implements DxRtPsRequestor.PsListener, DxNode {

	private DxRtPsRequestor psRequestor;
	private final long SHOW_DATA_TERM = 10 * 60 * 1000L; // 10분

	public DxItemRtPsAreaChart() {

	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number moNo = vo.getPropertyNumber("moNo");
		String psCode = vo.getProperty("psCode");

		if (moNo == null || psCode == null) {
			return false;
		}

		Mo mo = DxAsyncSelector.getSelector().getMo(moNo.longValue());
		UiPsItemVo psItem = CodeMap.getMap().getPsItem(psCode);

		if (mo == null || psItem == null) {
			return false;
		}

		try {
			psRequestor = new DxRtPsRequestor(this, mo.getMoNo(), psItem.getPsCode());
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}

		initChart(AreaChart.class, SHOW_DATA_TERM, psItem.getPsUnit());
		addSeries(psItem.getPsCode());

		return true;
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {
		if (psRequestor != null) {
			psRequestor.stop();
		}
	}

	@Override
	public void onValue(String psCode, long mstime, Number value) {
		addValue(psCode, mstime, value, SHOW_DATA_TERM);
	}

}
