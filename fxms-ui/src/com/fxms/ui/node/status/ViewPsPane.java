package com.fxms.ui.node.status;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNodeMulti;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.item.chart.PsAreaLineChart;

import fxms.client.FxmsClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ViewPsPane extends BorderPane implements DxNodeMulti {

	private final Label moLabel;
	private final Label psNameLabel;
	private final Label psValueLabel;
	private PsAreaLineChart chart;

	private final Timeline timer = new Timeline();

	public ViewPsPane() {

		setPadding(new Insets(10, 10, 10, 10));
		setBorder(new Border(
				new BorderStroke(Color.AQUA, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

		moLabel = new Label("Target");
		moLabel.getStyleClass().add("mo-info");

		psNameLabel = new Label("ps-name");
		psNameLabel.getStyleClass().add("ps-info");

		psValueLabel = new Label("ps-value");
		psValueLabel.getStyleClass().add("ps-value");

		Button bClear = new Button("", new ImageView(ImagePointer.getImage("s16x16/refresh.png")));
		bClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				chart.clear();
				showChart();
			}
		});

		BorderPane topBox = new BorderPane();
		VBox nameBox = new VBox(3);
		HBox valBox = new HBox(10);

		nameBox.getChildren().add(moLabel);
		nameBox.getChildren().add(psNameLabel);

		valBox.getChildren().add(bClear);
		valBox.getChildren().add(psValueLabel);

		topBox.setLeft(nameBox);
		topBox.setRight(valBox);

		setTop(topBox);

		//
		// widthProperty().addListener(new ChangeListener<Number>() {
		// @Override
		// public void changed(ObservableValue<? extends Number> observableValue, Number
		// oldSceneWidth,
		// Number newSceneWidth) {
		// System.out.println("Width: " + newSceneWidth + ", " +
		// (newSceneWidth.doubleValue() / 400));
		// setScaleX(newSceneWidth.doubleValue() / ( 1 * 400 ) );
		// }
		// });
		// heightProperty().addListener(new ChangeListener<Number>() {
		// @Override
		// public void changed(ObservableValue<? extends Number> observableValue, Number
		// oldSceneHeight,
		// Number newSceneHeight) {
		//// System.out.println("Height: " + newSceneHeight + ", " +
		// (newSceneHeight.doubleValue() / height));
		// setScaleY(newSceneHeight.doubleValue() / ( 1 * 250 ) );
		//
		// }
		// });
		
	}

	@Override
	public String getNodeTag() {
		return "mo:" + moLabel.getText() + ",ps:" + psNameLabel.getText();
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number moNo = vo.getPropertyNumber("moNo");
		Mo mo = null;
		if (moNo != null) {
			mo = DxAsyncSelector.getSelector().getMo(moNo.longValue());
		}

		UiPsItemVo psItem = CodeMap.getMap().getPsItem(vo.getProperty("psCode"));

		if (mo == null || psItem == null) {
			return false;
		}

		set(mo, psItem);

		return true;
	}

	@Override
	public void onAddedInParent() {
		timer.play();
	}

	@Override
	public void onRemovedFromParent() {
		timer.stop();
	}

	public void set(Mo mo, UiPsItemVo psItem) {

		moLabel.setText(mo.getMoAname());
		psNameLabel.setText(psItem.getPsName());

		PsAreaLineChart.YMaker yMaker = new PsAreaLineChart.YMaker() {
			@Override
			public String makeY(long time) {
				String hhmm = String.valueOf(time).substring(8, 12);
				return hhmm;
			}
		};

		PsAreaLineChart.LastValueReceiver receiver = new PsAreaLineChart.LastValueReceiver() {
			@Override
			public void onLast(long time, Number value) {
				psNameLabel.setText(psItem.getPsName() + "(" + psItem.getPsUnit() + ") : " + time);
				psValueLabel.setText(value + " " + psItem.getPsUnit());
			}
		};

		chart = new PsAreaLineChart(mo, psItem, "raw", yMaker, receiver);

		setCenter(chart);
		showChart();

		timer.getKeyFrames().add(new KeyFrame(Duration.millis(60000), (ActionEvent actionEvent) -> {
			showPsData2();
		}));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.setAutoReverse(true);
	}

	private void showChart() {
		long endDate = FxmsClient.getDate(System.currentTimeMillis() + 600000L); // 10분 후
		long startDate = FxmsClient.getDate(System.currentTimeMillis() - 3000000L); // 50분 전
		long yEndDate = FxmsClient.getDate(System.currentTimeMillis() + 600000L - 86400000L); // 10분 후
		long yStartDate = FxmsClient.getDate(System.currentTimeMillis() - 3000000L - 86400000L); // 50분 전

		chart.addPsDate("yesterday", yStartDate, yEndDate);
		chart.addPsDate("today", startDate, endDate);
	}

	private void showPsData2() {
		long endDate = FxmsClient.getDate(System.currentTimeMillis() + 60000L); // 1분 후
		long startDate = FxmsClient.getDate(System.currentTimeMillis() - 180000L); // 3분 전
		long yEndDate = FxmsClient.getDate(System.currentTimeMillis() + 600000L - 86400000L); // 10분 후
		long yStartDate = FxmsClient.getDate(System.currentTimeMillis() - 180000L - 86400000L); // 3분 전

		chart.addPsDate("yesterday", yStartDate, yEndDate);
		chart.addPsDate("today", startDate, endDate);
	}
}
