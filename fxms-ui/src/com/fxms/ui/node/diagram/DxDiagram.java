package com.fxms.ui.node.diagram;

import java.util.List;
import java.util.Map;

import com.fxms.ui.UiCode.Action;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.DxNode;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.biz.pane.MoDetailPane;
import com.fxms.ui.biz.pane.ShowAlarmDetailPane;
import com.fxms.ui.dx.DxAlarmReceiver;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxListener;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.dx.item.chart.TimeSeriesChartPs;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.node.DiagNodeDiagram;
import com.fxms.ui.node.diagram.node.DiagNodeInlo;
import com.fxms.ui.node.diagram.node.DiagNodeLine;
import com.fxms.ui.node.diagram.node.DiagNodeMo;
import com.fxms.ui.node.diagram.node.DiagNodeStatus;
import com.fxms.ui.node.diagram.vo.DiagMainVo;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import fxms.client.FxmsClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class DxDiagram extends FxDiagram implements DxListener<UiAlarm>, DxNode, FxUi {

	class DiagMenu4Dx extends ContextMenu {

		private DxDiagram dxDiagram;

		public DiagMenu4Dx(DxDiagram dxDiagram) {
			this.dxDiagram = dxDiagram;

			getItems().add(getToEdit());

		}

		private MenuItem getToEdit() {

			MenuItem item = new MenuItem("to edit");
			item.setOnAction((ActionEvent e) -> {
				FxDiagramEdit parent = new FxDiagramEdit();
				parent.setDiagMainVo(dxDiagram.getDiagMainVo());
				parent.setPrefSize(800, 600);
				ScrollPane sp = new ScrollPane();
				sp.setContent(parent);
				sp.setFitToHeight(true);
				sp.setFitToWidth(true);
				sp.setContextMenu(parent.getContextMenu());

				FxDialog.showDialog(DxDiagram.this, sp, "Edit Diagram");
				dxDiagram.setDiagNo(dxDiagram.getDiagMainVo().getDiagNo());
			});

			return item;
		}

	}

	class NodeEvent implements EventHandler<MouseEvent> {

		public NodeEvent() {
		}

		@Override
		public void handle(MouseEvent e) {

			Node node = (Node) e.getSource();

			if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
				node.setScaleX(1.25);
				node.setScaleY(1.25);

			} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
				node.setScaleX(1);
				node.setScaleY(1);
			}

		}

		public void installEvent(Node node) {

			// MO인 경우만 확대해 본다.
			if (node instanceof DiagNodeStatus || node instanceof DiagNodeMo) {
				node.addEventHandler(MouseEvent.MOUSE_ENTERED, this);
				node.addEventHandler(MouseEvent.MOUSE_EXITED, this);
			}
		}
	}

	private DiagMenu4Dx contextMenu;
	private NodeEvent nodeEvent;
	private final Timeline timeline;

	public DxDiagram() {
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10000), (ActionEvent actionEvent) -> {
			onCycle();
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.setAutoReverse(true);

		nodeEvent = new NodeEvent();
		contextMenu = new DiagMenu4Dx(this);
	}

	@Override
	public DiagNode addDiagNode(DiagNodeVo vo) {

		DiagNode addedNode = super.addDiagNode(vo);

		if (addedNode == null) {
			return null;
		}

		if (vo.isRemoved() == false) {

			if (addedNode instanceof DiagNodeMo) {
				DiagNodeMo node = (DiagNodeMo) addedNode;
				checkAlarm(node);
			}

			if (addedNode instanceof Node) {

				Tooltip.install((Node) addedNode, new Tooltip(addedNode.toString()));
				// nodeEvent.installEvent((Node) addedNode);

				((Node) addedNode).setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
							if (addedNode instanceof DiagNodeMo) {
								Mo mo = DxAsyncSelector.getSelector().getMo(((DiagNodeMo) addedNode).getMoNo());
								if (mo == null) {
									FxAlert.showAlert(FxAlertType.error, DxDiagram.this, "MO상세보기", "Not Found");
								} else {
									FxStage.showDialog(DxDiagram.this, new MoDetailPane(mo), "MO상세보기");
								}
							} else if (addedNode instanceof DiagNodeDiagram) {
								DxDiagram diagram = new DxDiagram();
								diagram.setDiagNo(((DiagNodeDiagram) addedNode).getDiagNo());
								FxDialog.showDialog(DxDiagram.this, diagram, "Diagram");
							} else if (addedNode instanceof DiagNodeInlo) {
								// TODO
							} else if (addedNode instanceof DiagNodeStatus) {

								long startDate = FxmsClient.getDate(System.currentTimeMillis() - 3600000);
								long endDate = FxmsClient.getDate(System.currentTimeMillis());

								DiagNodeStatus node = (DiagNodeStatus) addedNode;

								Mo mo = DxAsyncSelector.getSelector().getMo(node.getMoNo());

								TimeSeriesChartPs chart = new TimeSeriesChartPs();
								chart.initChart(AreaChart.class, "raw", startDate, endDate,
										node.getPsItem().getPsUnit());
								chart.addDatas(mo, node.getPsItem(), startDate, endDate);
								FxStage.showStage(DxDiagram.this, chart,
										mo.getMoAname() + "," + node.getPsItem().getPsName());

							}
						}
					}
				});
			}

		}

		return addedNode;
	}

	@Override
	public DiagNodeLine addLine(DiagNode startNode, DiagNode endNode) {

		DiagNodeLine line = super.addLine(startNode, endNode);

		if (line instanceof Node) {
			Tooltip.install((Node) line, new Tooltip(startNode.toString() + " - " + endNode.toString()));
		}

		return line;
	}

	public DiagMenu4Dx getContextMenu() {
		return contextMenu;
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return null;
	}

	@Override
	public void init(UiOpCodeVo opcode) {

	}

	@Override
	public void initData(Map<String, Object> data) {
		loadDiagram(data);
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {

		Number diagNo = vo.getPropertyNumber("diagNo");
		if (diagNo == null) {
			return false;
		}

		setDiagNo(diagNo.intValue());

		return true;
	}

	@Override
	public void onAddedInParent() {

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(new DxAlarmReceiver.AlarmFilter() {
			@Override
			public boolean isOk(UiAlarm alarm) {
				return alarm.isClearYn() == false && alarm.getAckDate() == 0;
			}
		});

		for (UiAlarm alarm : alarmList) {
			onData(Action.add, alarm);
		}

		DxAlarmReceiver.getBorader().add(this);

		if (timeline != null) {
			timeline.stop();
			timeline.play();
		}
	}

	@Override
	public void onData(Action action, UiAlarm alarm) {

		if (alarm != null) {

			Platform.runLater(new Runnable() {
				public void run() {
					DiagNodeMo node = getDiagNodeMo(alarm.getMoNo());
					if (node == null && alarm.getUpperMoNo() > 0) {
						node = getDiagNodeMo(alarm.getUpperMoNo());
					}
					if (node != null) {
						checkAlarm(node);
					}
				}
			});

		}
	}

	@Override
	public void onRemovedFromParent() {
		DxAlarmReceiver.getBorader().remove(this);

		if (timeline != null) {
			timeline.stop();
		}
	}

	@Override
	public void setDiagram(DiagMainVo diagram) {

		super.setDiagram(diagram);

		onCycle();

	}

	private void checkAlarm(DiagNodeMo node) {

		Node text = (Node) getNode(node.getMoNo() + "-alarm");

		UiAlarm alarm = DxAlarmReceiver.getBorader().getTopAlarm(node.getMoNo());

		if (alarm != null) {
			if (text == null) {
				Button button = DiagNode.makeAlarmNode(node, node.getBounds(), alarm.getMoName(),
						alarm.getAlarmLevel());
				button.setId(node.getMoNo() + "-alarm");
				getChildren().add(button);
				button.toFront();

				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						FxDialog.showDialog(DxDiagram.this, new ShowAlarmDetailPane(alarm), "경보상세보기");
					}
				});

			} else {
				text.getStyleClass().clear();
				text.getStyleClass().add("alarm-level-" + alarm.getAlarmLevel());
			}
		} else if (text != null) {
			getChildren().remove(text);
		}
	}

	private void onCycle() {

		List<DiagNodeStatus> sList = getNodeList(DiagNodeStatus.class);

		if (sList != null) {
			long startDate = FxmsClient.getDate(System.currentTimeMillis() - 120000);
			long endDate = FxmsClient.getDate(System.currentTimeMillis());
			for (DiagNodeStatus s : sList) {

				if (s.getMoNo() > 0 && s.getPsItem() != null) {

					DxAsyncSelector.getSelector().getPsList(s.getPsItem(), s.getMoNo(), "raw", startDate, endDate,
							new FxCallback<PsValueMap>() {
								@Override
								public void onCallback(PsValueMap data) {

									Platform.runLater(new Runnable() {
										public void run() {
											try {
												Number value = data.getLastValue().getValue();
												s.setStatus(value);
											} catch (Exception e) {
												s.setStatus(null);
											}
										}
									});
								}
							});
				}
			}
		}
	}

}
