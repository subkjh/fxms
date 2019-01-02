package com.fxms.ui.dx.item.chart;

import java.util.List;

import com.fxms.ui.bas.code.UiChartVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.component.LoadingNode;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.PsValue;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.bas.window.InnerWindow;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public abstract class DxItemChartBase<XTYPE> extends BorderPane implements DxNode, FxChart {

	private XYChart<XTYPE, Number> chart;
	private String chartTitle;
	private int count;
	private final LoadingNode msgLabel = new LoadingNode();

	public DxItemChartBase() {

		setOpacity(0.95);
		setPrefSize(1.6181 * 350, 350);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number moNo = vo.getPropertyNumber("moNo");
		String chartId = vo.getProperty("chartId");

		if (moNo == null) {
			viewChart(null, null);
		} else {
			Mo mo = DxAsyncSelector.getSelector().getMo(moNo.longValue());
			if (mo == null) {
				viewChart(null, null);
			} else {
				UiChartVo chartVo = CodeMap.getMap().getChartVo(mo, chartId);
				viewChart(mo, chartVo);
			}
		}
		return true;
	}

	@Override
	public void onAddedInParent() {

	}

	@Override
	public void onRemovedFromParent() {

	}

	@Override
	public void viewChart(Mo mo, UiChartVo chartVo) {

		if (mo == null || chartVo == null) {
			msgLabel.setErrorMsg("No Chart");
			show(msgLabel);
			return;
		}

		List<UiPsItemVo> psItemList = CodeMap.getMap().getPsItemList(chartVo.getPsCodes());
		if (psItemList.size() == 0) {
			msgLabel.setErrorMsg("No Performance & Status Items");
			show(msgLabel);
			return;
		}

		msgLabel.setWaitingMsg("Loading Datas...");
		show(msgLabel);

		chart = makeChart(chartVo.getStartDate(), chartVo.getEndDate());

		chartTitle = "'" + mo.getMoAname() + "' " + chartVo.getChartName();
		setTitle(chartTitle);
		chart.getData().clear();

		count = psItemList.size();
		for (UiPsItemVo psItem : psItemList) {
			viewChart(mo, psItem, chartVo.getPsType(), chartVo.getStartDate(), chartVo.getEndDate());
		}

	}

	public void viewChart(Mo mo, UiPsItemVo psItem, String psType, long startDate, long endDate) {

		if (chartTitle == null) {
			chartTitle = "'" + mo.getMoAname() + "' " + psItem.getPsName();
			setTitle(chartTitle);
		}

		chart = makeChart(startDate, endDate);

		NumberAxis yAxis = (NumberAxis) chart.getYAxis();
		yAxis.setLabel(psItem.getPsUnit());
		if (psItem.getValMax() != null && psItem.getValMin() != null) {
			yAxis.setAutoRanging(false);
			yAxis.setLowerBound(psItem.getValMin().doubleValue());
			yAxis.setUpperBound(psItem.getValMax().doubleValue());
			yAxis.setTickUnit((psItem.getValMax().doubleValue() - psItem.getValMin().doubleValue()) / 5);
		}

		DxAsyncSelector.getSelector().getPsList(psItem, mo.getMoNo(), psType, startDate, endDate,
				new FxCallback<PsValueMap>() {

					@Override
					public void onCallback(PsValueMap data) {

						Platform.runLater(new Runnable() {
							public void run() {

								if (data == null) {
									msgLabel.setErrorMsg("error");
									show(msgLabel);
								} else if (data.size() == 0) {
									msgLabel.setErrorMsg("No Datas");
									show(msgLabel);
								} else {
									draw(data);
								}
							}
						});
					}
				});
	}

	private void draw(PsValueMap data) {

		count--;

		for (Object key : data.keySet()) {

			XYChart.Series<XTYPE, Number> series = new XYChart.Series<XTYPE, Number>();

			series.setName(data.getPsItem().getPsName());

			for (PsValue val : data.get(key)) {
				try {
					series.getData().add(new XYChart.Data<XTYPE, Number>(makeXValue(val),
							makeYValue(data.getPsItem(), val.getValue())));
				} catch (Exception e) {
				}
			}

			chart.getData().add(series);

		}

		if (count <= 0) {

			show(chart);

			SERIES: for (Series<XTYPE, Number> serie : chart.getData()) {
				for (XYChart.Data<XTYPE, Number> item : serie.getData()) {
					if (item.getNode() == null) {
						break SERIES;
					}
					item.getNode().setOnMousePressed((MouseEvent event) -> {
						onClicked(serie.getName(), item.getXValue() + "", UiPsItemVo.makeAutoUnit(item.getYValue(), 1));
					});
				}
			}
		}
	}

	private void onClicked(String seriesName, String xValue, String yValue) {
		String msg = seriesName + " " + xValue + "=" + yValue;
		setTitle(msg);
	}

	private void setTitle(String title) {
		if (getParent() instanceof InnerWindow) {
			((InnerWindow) getParent()).setTitle(title);
		} else {
			if (chart != null) {
				chart.setTitle(title);
			}
		}
	}

	private void show(Node node) {
		if (node.equals(getCenter())) {
			return;
		}
		setCenter(node);
	}

	protected abstract XYChart<XTYPE, Number> makeChart(long startDate, long endDate);

	protected abstract XTYPE makeXValue(PsValue val);

	protected Number makeYValue(UiPsItemVo psItem, Number value) {
		try {
			return value.doubleValue();
		} catch (Exception e) {
			return null;
		}
	}

}
