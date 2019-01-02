package com.fxms.ui.dx.item.chart;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.util.StringConverter;

public class TimeSeriesChartRt extends TimeSeriesChart {

	private static final SimpleDateFormat HHMM = new SimpleDateFormat("mm:ss");

	public TimeSeriesChartRt() {
	}

	public void addValue(String seriesName, long mstime, Number value, long showDataTerm) {

		XYChart.Series<Number, Number> series = getSeries(seriesName);
		if (series == null) {
			return;
		}

		Platform.runLater(new Runnable() {
			public void run() {

				ObservableList<Data<Number, Number>> list = series.getData();
				double lowerBound = mstime - showDataTerm;

				// 보여주는 범위 밖이면 지운다.
				while (list.size() > 0) {
					if (list.get(0).getXValue().doubleValue() < lowerBound) {
						list.remove(0);
					} else {
						break;
					}
				}

				getxAxis().setLowerBound(lowerBound);
				getxAxis().setUpperBound(mstime);

				XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(mstime, value);
				series.getData().add(data);
			}
		});
	}

	@Override
	protected StringConverter<Number> getXAxisStringConverter() {
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

	public void initChart(Class<?> classOfChart, long showDataTerm, String yLabel) {
		long mstime = (System.currentTimeMillis() / 1000) * 1000L;
		makeChart(classOfChart, mstime - showDataTerm, mstime, yLabel);
	}

}
