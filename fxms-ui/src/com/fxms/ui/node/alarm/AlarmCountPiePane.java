package com.fxms.ui.node.alarm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.UiCode;
import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxListener;

import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AlarmCountPiePane extends StackPane implements DxListener<UiAlarm>, DxNode {

	private final PieChart chart;
	private final Label count;

	public AlarmCountPiePane() {

		chart = new PieChart();
		chart.setLabelsVisible(false);
		chart.setLegendVisible(false);
//		chart.setOpacity(.5);

		count = new Label("-");
		count.setWrapText(true);
		count.setOpacity(.8);
		count.getStyleClass().add("alarm-count-all");
		setMargin(chart, new Insets(8, 8, 8, 8));

		getChildren().add(chart);
		getChildren().add(count);

	}

	@Override
	public void onAddedInParent() {
		countAlarm();
		DxAlarmReceiver.getBorader().add(this);
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		return true;
	}
	
	@Override
	public void onData(Action action, UiAlarm data) {
		if (action != Action.loop) {
			countAlarm();
		}
	}

	private void countAlarm() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn() == false && alarm.getAckDate() == 0;
			}
		});

		Map<Integer, Integer> cntMap = new HashMap<Integer, Integer>();

		Integer cnt;
		for (UiAlarm alarm : alarmList) {
			cnt = cntMap.get(alarm.getAlarmLevel());
			if (cnt == null) {
				cntMap.put(alarm.getAlarmLevel(), 1);
			} else {
				cntMap.put(alarm.getAlarmLevel(), cnt.intValue() + 1);
			}
		}

		int totalCnt = 0;

		chart.getData().clear();
		for (Integer key : cntMap.keySet()) {
			cnt = cntMap.get(key);
			chart.getData().add(new PieChart.Data(UiCode.alarmName[key.intValue()], cnt));
			totalCnt += cnt.intValue();
		}

		count.setText(String.valueOf(totalCnt));
	}

}
