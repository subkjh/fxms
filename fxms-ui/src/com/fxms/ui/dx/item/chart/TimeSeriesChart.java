package com.fxms.ui.dx.item.chart;

import com.fxms.ui.bas.component.LoadingNode;
import com.fxms.ui.bas.window.InnerWindow;
import com.fxms.ui.css.CssPointer;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

public class TimeSeriesChart extends BorderPane {

	private final LoadingNode msgLabel = new LoadingNode();
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	protected XYChart<Number, Number> chart;

	public TimeSeriesChart() {
		setOpacity(0.95);
		setPrefSize(1.6181 * 480, 480);
		msgLabel.getStylesheets().add(CssPointer.getStyleSheet("chart.css"));
		msgLabel.getStyleClass().add("chart-message");
	}

	public XYChart.Series<Number, Number> addSeries(String name) {
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName(name);
		chart.getData().add(series);

		showChart();

		return series;
	}

	public void setTitle(String title) {
		if (getParent() instanceof InnerWindow) {
			((InnerWindow) getParent()).setTitle(title);
		} else {
			if (chart != null) {
				chart.setTitle(title);
			}
		}
	}

	public void showMessage(String msg) {
		msgLabel.setText(msg);
		if (msgLabel.equals(getCenter()) == false) {
			setCenter(msgLabel);
		}
	}

	protected void showChart() {
		if (chart.equals(getCenter()) == false) {
			setCenter(chart);
		}
	}

	protected XYChart.Series<Number, Number> getSeries(String name) {
		for (XYChart.Series<Number, Number> series : chart.getData()) {
			if (series.getName().equals(name)) {
				return series;
			}
		}
		return null;
	}

	protected NumberAxis getxAxis() {
		return xAxis;
	}

	protected StringConverter<Number> getXAxisStringConverter() {
		return null;
	}

	protected StringConverter<Number> getYAxisStringConverter() {
		return null;
	}

	protected void makeChart(Class<?> classOfChart, long lowerBound, long upperBound, String yLabel) {
		// 이렇게 해야 잘 됨. ㅎㅎ
		xAxis = new NumberAxis(lowerBound, upperBound, (upperBound - lowerBound) / 10);
		xAxis.setAutoRanging(false);
		StringConverter<Number> converter = getXAxisStringConverter();
		if (converter != null) {
			xAxis.setTickLabelFormatter(converter);
		}

		yAxis = new NumberAxis();
		yAxis.setMinorTickCount(0);
		yAxis.setTickMarkVisible(false);
		yAxis.setMinorTickVisible(false);
		if (yLabel != null)
			yAxis.setLabel(yLabel);

		StringConverter<Number> yCconverter = getYAxisStringConverter();
		if (yCconverter != null) {
			yAxis.setTickLabelFormatter(yCconverter);
		}

		if (classOfChart == AreaChart.class) {
			AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
			chart.setVerticalGridLinesVisible(false);
			chart.getStylesheets().add(CssPointer.getStyleSheet("chart.css"));
			chart.setCreateSymbols(true);
			this.chart = chart;
		} else {
			LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
			chart.setVerticalGridLinesVisible(false);
			chart.getStylesheets().add(CssPointer.getStyleSheet("chart.css"));
			chart.setCreateSymbols(true);
			this.chart = chart;
		}
	}

	protected void setYBound(Number lowerBound, Number upperBound) {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(lowerBound.doubleValue());
		yAxis.setUpperBound(upperBound.doubleValue());
		yAxis.setTickUnit((upperBound.doubleValue() - lowerBound.doubleValue()) / 5);
	}

}
