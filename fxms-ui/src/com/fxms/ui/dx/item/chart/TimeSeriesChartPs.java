package com.fxms.ui.dx.item.chart;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fxms.ui.PS_TYPE;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.PsValue;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class TimeSeriesChartPs extends TimeSeriesChart {

	private static final SimpleDateFormat DDHH = new SimpleDateFormat("dd일 HH시");
	private static final SimpleDateFormat HHMM = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat MMDD = new SimpleDateFormat("MM월 dd일");
	private static final SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	private String psType;

	public TimeSeriesChartPs() {
	}

	public void addDatas(Mo mo, UiPsItemVo psItem, long startDate, long endDate) {

		showMessage("loading...");

		DxAsyncSelector.getSelector().getPsList(psItem, mo.getMoNo(), psType, startDate, endDate,
				new FxCallback<PsValueMap>() {

					@Override
					public void onCallback(PsValueMap data) {

						Platform.runLater(new Runnable() {
							public void run() {

								if (data == null) {
									showMessage("error");
								} else if (data.size() == 0) {
									showMessage("No Datas");
								} else {
									showData(data);
								}
							}
						});
					}
				});
	}

	public void initChart(Class<?> classOfChart, String psType, long startDate, long endDate, String yLabel) {
		this.psType = psType;
		makeChart(classOfChart, PS_TYPE.getMstimeByHstime(startDate), PS_TYPE.getMstimeByHstime(endDate), yLabel);
	}

	protected Number makeXValue(PsValue val) {
		return PS_TYPE.getMstimeByHstime(val.getTime());
	}

	protected Number makeYValue(UiPsItemVo psItem, Number value) {
		try {
			return value.doubleValue();
		} catch (Exception e) {
			return null;
		}
	}

	private void showData(PsValueMap data) {

		for (Object key : data.keySet()) {

			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

			series.setName(data.getPsItem().getPsName());

			for (PsValue val : data.get(key)) {
				try {
					series.getData().add(new XYChart.Data<Number, Number>(makeXValue(val),
							makeYValue(data.getPsItem(), val.getValue())));

				} catch (Exception e) {
				}
			}

			chart.getData().add(series);
		}

		showChart();

		SERIES: for (Series<Number, Number> serie : chart.getData()) {
			for (XYChart.Data<Number, Number> item : serie.getData()) {
				if (item.getNode() == null) {
					break SERIES;
				}
				item.getNode().setOnMousePressed((MouseEvent event) -> {
					String msg = serie.getName() + " " + YYYYMMDDHHMM.format(new Date(item.getXValue().longValue()))
							+ "=" + UiPsItemVo.makeAutoUnit(item.getYValue(), 1);
					setTitle(msg);
				});
			}
		}

	}

	@Override
	protected StringConverter<Number> getXAxisStringConverter() {

		if (psType.equals("1d")) {
			return new StringConverter<Number>() {
				@Override
				public Number fromString(String arg0) {
					return null;
				}

				@Override
				public String toString(Number num) {
					return MMDD.format(new Date(num.longValue()));
				}
			};
		} else if (psType.equals("1h")) {
			return new StringConverter<Number>() {

				@Override
				public Number fromString(String arg0) {
					return null;
				}

				@Override
				public String toString(Number num) {
					return DDHH.format(new Date(num.longValue()));
				}
			};
		} else {
			return new StringConverter<Number>() {
				@Override
				public Number fromString(String arg0) {
					return null;
				}

				@Override
				public String toString(Number num) {
					return HHMM.format(new Date(num.longValue()));
				}
			};
		}

	}

}
