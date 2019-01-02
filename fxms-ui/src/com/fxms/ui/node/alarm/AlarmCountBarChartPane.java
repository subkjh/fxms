package com.fxms.ui.node.alarm;

import java.util.Arrays;

import com.fxms.ui.UiCode;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.UiAlarm.AlarmKind;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

import fxms.client.FxmsClient;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class AlarmCountBarChartPane extends BorderPane implements DxListener<UiAlarm>, DxNode {

	final CategoryAxis xAxis = new CategoryAxis();
	final NumberAxis yAxis = new NumberAxis();
	final StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);
	final XYChart.Series<String, Number> seriesAcked = new XYChart.Series<>();
	final XYChart.Series<String, Number> seriesCleared = new XYChart.Series<>();
	final XYChart.Series<String, Number> seriesNew = new XYChart.Series<>();

	@SuppressWarnings("unchecked")
	public AlarmCountBarChartPane() {

		// xAxis.setLabel("Level");
		xAxis.setCategories(FXCollections.<String>observableArrayList(
				Arrays.asList(UiCode.alarmName[1], UiCode.alarmName[2], UiCode.alarmName[3], UiCode.alarmName[4])));
		// yAxis.setLabel("Count");

		seriesAcked.setName("Acked");
		seriesAcked.getData().add(new XYChart.Data<>(UiCode.alarmName[1], 0));
		seriesAcked.getData().add(new XYChart.Data<>(UiCode.alarmName[2], 0));
		seriesAcked.getData().add(new XYChart.Data<>(UiCode.alarmName[3], 0));
		seriesAcked.getData().add(new XYChart.Data<>(UiCode.alarmName[4], 0));

		seriesNew.setName("New");
		seriesNew.getData().add(new XYChart.Data<>(UiCode.alarmName[1], 0));
		seriesNew.getData().add(new XYChart.Data<>(UiCode.alarmName[2], 0));
		seriesNew.getData().add(new XYChart.Data<>(UiCode.alarmName[3], 0));
		seriesNew.getData().add(new XYChart.Data<>(UiCode.alarmName[4], 0));

		seriesCleared.setName("Cleared");
		seriesCleared.getData().add(new XYChart.Data<>(UiCode.alarmName[1], 0));
		seriesCleared.getData().add(new XYChart.Data<>(UiCode.alarmName[2], 0));
		seriesCleared.getData().add(new XYChart.Data<>(UiCode.alarmName[3], 0));
		seriesCleared.getData().add(new XYChart.Data<>(UiCode.alarmName[4], 0));

		chart.setTitle("Alarm Summary");
		chart.getData().addAll(seriesCleared, seriesAcked, seriesNew);

		chart.setLegendVisible(false);

		setCenter(chart);

	}

	private void makeTooltip() {

		EventH e = new EventH(AlarmCountBarChartPane.this);

		int index = 0;
		String name[] = new String[] { "alarm-critical", "alarm-major", "alarm-minor", "alarm-warning" };

		index = 0;
		for (XYChart.Data<String, Number> d : seriesNew.getData()) {
			d.getNode().setOnMouseClicked(e);
			d.getNode().getStyleClass().add(name[index++] + "-cur");
			Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "new : " + d.getYValue()));
		}
		
		index = 0;
		for (XYChart.Data<String, Number> d : seriesAcked.getData()) {
			d.getNode().setOnMouseClicked(e);
			d.getNode().getStyleClass().add(name[index++] + "-acked");
			Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "acked : " + d.getYValue()));
		}

		index = 0;
		for (XYChart.Data<String, Number> d : seriesCleared.getData()) {
			d.getNode().setOnMouseClicked(e);
			d.getNode().getStyleClass().add(name[index++] + "-cleared");
			Tooltip.install(d.getNode(), new Tooltip(d.getXValue().toString() + "\n" + "cleared : " + d.getYValue()));
		}

	
	}

	class EventH implements EventHandler<MouseEvent> {
		
		private Parent parent;
		
		public EventH(Parent parent)
		{
			this.parent = parent;
		}

		@Override
		public void handle(MouseEvent event) {

			if (event.getClickCount() == 2 && event.getSource() instanceof Node) {
				String ss[] = ((Node) event.getSource()).getId().split(",");
				AlarmKind alarmKind = AlarmKind.valueOf(ss[0]);
				int alarmLevel = Integer.valueOf(ss[1]);
				String title = "Alarm List (alarm-kind=" + alarmKind + ", alarm-level=" + alarmLevel + ")";

				DxAlarmReceiver.AlarmFilter filter = new DxAlarmReceiver.AlarmFilter() {

					@Override
					public boolean isOk(UiAlarm alarm) {
						// TODO Auto-generated method stub
						return alarm.getAlarmKind() == alarmKind && alarm.getAlarmLevel() == alarmLevel;
					}

				};

				AlarmListPane pane = new AlarmListPane();
				pane.onRefresh(filter);
//				FxDialog.showDialog(pane, title);
				FxWindow.showStage(parent, pane, title);
			}
		}

	}

	@Override
	public void onData(Action action, UiAlarm alarm) {

		if (action == Action.loop) {
			return;
		}

		for (XYChart.Series<String, Number> series : chart.getData()) {
			for (XYChart.Data<String, Number> data : series.getData()) {
				data.setYValue(0);
			}
		}

		long ocuDate = FxmsClient.getDate(System.currentTimeMillis() - 3600000L);

		DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {

			private XYChart.Data<String, Number> data;

			@Override
			public boolean isOk(UiAlarm alarm) {
				if (alarm.getOcuDate() >= ocuDate) {
					if (alarm.isClearYn()) {
						data = seriesCleared.getData().get(alarm.getAlarmLevel() - 1);
						data.getNode().setId(AlarmKind.cleared + "," + alarm.getAlarmLevel());
					} else if (alarm.getAckDate() > 0) {
						data = seriesAcked.getData().get(alarm.getAlarmLevel() - 1);
						data.getNode().setId(AlarmKind.acked + "," + alarm.getAlarmLevel());
					} else {
						data = seriesNew.getData().get(alarm.getAlarmLevel() - 1);
						data.getNode().setId(AlarmKind.cur + "," + alarm.getAlarmLevel());
					}
					data.setYValue(data.getYValue().intValue() + 1);
				}
				return false;
			}
		});

		makeTooltip();

	}

	@Override
	public void onAddedInParent() {
		DxAlarmReceiver.getBorader().add(this);
		onData(Action.add, null);
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}
	
}
