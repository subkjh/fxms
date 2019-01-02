package com.fxms.ui.dx.item.chart;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.PsValue;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.application.Platform;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class PsAreaLineChart extends VBox {

	public static interface LastValueReceiver {
		public void onLast(long time, Number value);
	}

	public static interface YMaker {
		public String makeY(long time);
	}
	
	class PsData {
		XYChart.Series<String, Number> series;
		PsValueMap valueMap;
		long startDate;
		long endDate;
	}

	private final CategoryAxis xAxis;
	private final NumberAxis yAxis;
	private final AreaChart<String, Number> chart;
	// private final LineChart<String, Number> chart;

	private UiPsItemVo psItem;
	private String psType;
	private Mo mo;
	private List<PsData> seriesList;
	private PsAreaLineChart.YMaker yMaker;
	private PsAreaLineChart.LastValueReceiver receiver;
	private XYChart.Data<String, Number> lastData;
	private final Label msgLabel = new Label();
	private final ProgressBar loadBar = new ProgressBar();

	public PsAreaLineChart(Mo mo, UiPsItemVo psItem, String psType, PsAreaLineChart.YMaker yMaker,
			PsAreaLineChart.LastValueReceiver receiver) {
		this.mo = mo;
		this.psItem = psItem;
		this.psType = psType;
		this.yMaker = yMaker;
		this.receiver = receiver;

		seriesList = new ArrayList<PsData>();

		xAxis = new CategoryAxis();
		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		// xAxis.setLabel("Time");

		yAxis = new NumberAxis();
		// yAxis.setLabel(psItem.getPsUnit());

		chart = new AreaChart<String, Number>(xAxis, yAxis);
		// chart = new LineChart<String, Number>(xAxis, yAxis);
		chart.setCreateSymbols(false); // hide dots
		// chart.setLegendSide(Side.TOP);
		chart.setHorizontalGridLinesVisible(false);
		chart.setVerticalGridLinesVisible(false);
		chart.setLegendVisible(false);

		// getChildren().add(chart);

		loadBar.setProgress(-1);
		loadBar.setPrefSize(360, 18);
		getChildren().add(loadBar);

		msgLabel.setText("Empty");
	}

	public void clear() {
		chart.getData().clear();
		seriesList.clear();
		lastData = null;
	}

	public void addPsDate(String name, long startDate, long endDate) {

		PsData data = null;

		for (PsData e : seriesList) {
			if (e.series.getName().equals(name)) {
				data = e;
				break;
			}
		}

		if (data == null) {
			data = new PsData();
			data.series = new XYChart.Series<String, Number>();
			data.series.setName(name);
			seriesList.add(data);
			chart.getData().add(data.series);
		}

		data.startDate = startDate;
		data.endDate = endDate;

		viewLine(data);
	}

	private void draw(PsValueMap valueMap, PsData psData) {

		XYChart.Series<String, Number> series = psData.series;

		for (Object key : valueMap.keySet()) {

			for (PsValue val : valueMap.get(key)) {

				if (psData.valueMap == null) {
					add(series, val);
				} else if (psData.valueMap.exist(key, val) == false) {
					add(series, val);
				}
			}

		}

		psData.valueMap = valueMap;

		// for (int i = series.getData().size() - 50; i >= 0; i--) {
		// series.getData().remove(0);
		// }

		if (getChildren().contains(chart) == false) {
			getChildren().add(chart);
		}
	}

	private void add(XYChart.Series<String, Number> series, PsValue val) {

		String x = getTime(val.getTime());

		XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(x, val.getValue().doubleValue());
		data.setExtraValue(val);
		series.getData().add(data);

		if (data.getNode() != null) {
			Tooltip.install(data.getNode(),
					new Tooltip("Time : " + val.getTime() + "\n" + "Value : " + data.getYValue()));
		}

		PsValue lastValue = (lastData == null ? null : (PsValue) lastData.getExtraValue());

		if (lastValue == null || lastValue.getTime() < val.getTime()) {
			if (receiver != null) {
				receiver.onLast(val.getTime(), val.getValue());
				lastData = data;
			}
		}
	}

	private String getTime(long time) {
		if (yMaker != null) {
			return yMaker.makeY(time);
		}
		return String.valueOf(time);
	}

	private void viewLine(PsData series) {

		DxAsyncSelector.getSelector().getPsList(psItem, mo.getMoNo(), psType, series.startDate,
				series.endDate, new FxCallback<PsValueMap>() {

					@Override
					public void onCallback(PsValueMap data) {

						Platform.runLater(new Runnable() {
							public void run() {

								getChildren().remove(loadBar);

								if (data == null) {
									getChildren().remove(chart);
									msgLabel.setText("error");
									msgLabel.getStyleClass().add("message-error");
									if (getChildren().contains(msgLabel) == false) {
										getChildren().add(msgLabel);
									}
								} else if (data.size() == 0) {
									getChildren().remove(chart);
									msgLabel.setText("No Datas");
									msgLabel.getStyleClass().add("message-error");
									if (getChildren().contains(msgLabel) == false) {
										getChildren().add(msgLabel);
									}
								} else {
									getChildren().remove(msgLabel);
									draw(data, series);
								}
							}
						});
					}
				});

	}

}
