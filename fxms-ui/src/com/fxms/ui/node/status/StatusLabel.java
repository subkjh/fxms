package com.fxms.ui.node.status;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNodeMulti;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.dx.item.chart.TimeSeriesChartPs;

import fxms.client.FxmsClient;
import fxms.client.log.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class StatusLabel extends Label implements DxNodeMulti {

	private final Timeline timeline;
	private Mo mo;
	private UiPsItemVo psItem;

	public StatusLabel() {
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10000), (ActionEvent actionEvent) -> {
			onCycle();
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);

		this.getStyleClass().add("dx-node-status-label");

		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getEventType() == MouseEvent.MOUSE_CLICKED && e.getButton() == MouseButton.PRIMARY) {
					if (e.getClickCount() == 2) {
						showChart();
					}
				}
			}
		});
	}

	@Override
	public String getNodeTag() {

		StringBuffer sb = new StringBuffer();
		if (mo != null) {
			sb.append(mo.getMoAname());
		}
		if (psItem != null) {
			sb.append(", ");
			sb.append(psItem.getPsName());
		}

		return sb.toString();
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number moNo = vo.getPropertyNumber("moNo");
		mo = null;
		if (moNo != null) {
			mo = DxAsyncSelector.getSelector().getMo(moNo.longValue());
		}

		psItem = CodeMap.getMap().getPsItem(vo.getProperty("psCode"));

		if (mo == null || psItem == null) {
			return false;
		}

		return true;
	}

	@Override
	public void onAddedInParent() {
		if (timeline != null) {
			onCycle();
			timeline.stop();
			timeline.play();
		}
	}

	@Override
	public void onRemovedFromParent() {
		if (timeline != null) {
			timeline.stop();
		}
	}

	public void setStatus(Number status) {
		setText(status == null ? "-" : (status.toString() + (psItem != null ? " " + psItem.getPsUnit() : "")));
	}

	private void onCycle() {

		long startDate = FxmsClient.getDate(System.currentTimeMillis() - 90000);
		long endDate = FxmsClient.getDate(System.currentTimeMillis());

		Logger.logger.trace("mo-no={} ps-item={}", mo.getMoNo(), psItem.getPsCode());

		if (mo != null && psItem != null) {

			DxAsyncSelector.getSelector().getPsList(psItem, mo.getMoNo(), "raw", startDate, endDate,
					new FxCallback<PsValueMap>() {
						@Override
						public void onCallback(PsValueMap data) {

							Platform.runLater(new Runnable() {
								public void run() {
									try {
										Number value = data.getLastValue().getValue();
										setStatus(value);
									} catch (Exception e) {
										setStatus(null);
									}
								}
							});
						}
					});
		}

	}

	private void showChart() {
		long startDate = FxmsClient.getDate(System.currentTimeMillis() - 3600000);
		long endDate = FxmsClient.getDate(System.currentTimeMillis());
		TimeSeriesChartPs chart = new TimeSeriesChartPs();
		chart.initChart(AreaChart.class, "raw", startDate, endDate, psItem.getPsUnit());
		chart.addDatas(mo, psItem, startDate, endDate);
		// DxItemPsAreaChart root = new DxItemPsAreaChart();
		// root.viewChart(mo, psItem);
		// root.setDatas("raw", startDate, endDate);
		FxStage.showStage(this, chart, mo.getMoAname() + "," + psItem.getPsName());
	}
}
