package com.fxms.ui.dx.item.chart;

import com.fxms.ui.bas.vo.PsValue;
import com.fxms.ui.css.CssPointer;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

public class DxItemStackedBarChart extends DxItemChartBase<String> {

	private StackedBarChart<String, Number> chart;

	public DxItemStackedBarChart() {
		super();
	}

	protected StringConverter<Number> getYAxisStringConverter() {
		return null;
	}

	protected StringConverter<Number> getXAxisStringConverter() {
		return null;
	}

	@Override
	protected XYChart<String, Number> makeChart(long startDate, long endDate) {

		if (chart == null) {

			CategoryAxis xAxis = new CategoryAxis();
			xAxis.setLabel("Time");
			xAxis.setTickMarkVisible(false);
			xAxis.setTickLabelsVisible(false);
			xAxis.setVisible(false);

			NumberAxis yAxis = new NumberAxis();
			yAxis.setAutoRanging(true);
			yAxis.setMinorTickCount(0);
			yAxis.setTickMarkVisible(false);
			yAxis.setMinorTickVisible(false);

			StringConverter<Number> converter = getYAxisStringConverter();
			if (converter != null) {
				yAxis.setTickLabelFormatter(converter);
			}

			chart = new StackedBarChart<>(xAxis, yAxis);
			chart.setCategoryGap(.5);
			chart.setVerticalGridLinesVisible(false);
			chart.getStylesheets().add(CssPointer.getStyleSheet("chart.css"));

		}

		return chart;
	}

	@Override
	protected String makeXValue(PsValue val) {
		return val.getTime()+"";
	}

}
