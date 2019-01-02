package com.fxms.ui.bas;

import java.util.Map;

import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxNode;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.window.FxWindowTitle;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.dx.FxCallfront;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class FxStage extends Stage {

	class StageEvent implements EventHandler<MouseEvent> {

		private double windowX;
		private double windowY;
		private double x;
		private double y;

		public StageEvent() {
		}

		public void addEvent(Node node) {
			node.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
			node.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		}

		@Override
		public void handle(MouseEvent e) {

			Label label = (Label) e.getSource();
			Window window = label.getScene().getWindow();

			if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.PRIMARY) {
				x = e.getScreenX();
				y = e.getScreenY();
				windowX = window.getX();
				windowY = window.getY();
			} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED && e.getButton() == MouseButton.PRIMARY) {
				window.setX(windowX + (e.getScreenX() - x));
				window.setY(windowY + (e.getScreenY() - y));
			}
		}
	}

	// public static Stage showDialog(Node parent, Node contextNode, UiOpCodeVo
	// opCode, String doneText, String closeText,
	// DxCallback callback) {
	// FxStage stage = new FxStage(contextNode, opCode, doneText, closeText,
	// callback);
	// stage.showDialog(parent);
	// return stage;
	// }

	public static Stage showDialog(Node parent, Node contextNode, String title) {
		FxStage stage = new FxStage(contextNode, title);
		stage.showDialog(parent);
		return stage;
	}

	public static Stage showDialog(Node parent, Node contextNode, UiOpCodeVo opCode, String doneText,
			FxCallfront callfront, DxCallback callback) {
		FxStage stage = new FxStage(contextNode, opCode, doneText, callfront, callback);
		stage.showDialog(parent);
		return stage;
	}

	public static void showMessage(Node node, boolean error, String s) {
		Window window = node.getScene().getWindow();
		if (window instanceof FxStage) {
			FxStage stage = (FxStage) window;
			stage.showMsg(s);
		} else {
			if (error) {
				FxAlert.showAlert(FxAlertType.error, node, "", s);
			}
		}
	}

	public static FxStage showStage(Node parent, Node contextNode, String title) {
		FxStage stage = new FxStage(contextNode, title);
		stage.showStage(parent);
		return stage;
	}

	public static Stage showStage(Node parent, Node contextNode, UiOpCodeVo opCode, String doneText, String closeText,
			DxCallback callback) {
		FxStage stage = new FxStage(contextNode, opCode, doneText, closeText, callback);
		stage.showStage(parent);
		return stage;
	}

	private DxCallback callback;
	private FxCallback<WindowEvent> closeCallback; // 윈도위가 닫힐 때 호출
	private final Border contextBorder = new Border(
			new BorderStroke(Color.rgb(4, 67, 144), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));
	private FxWindowTitle windowTitle;
	private UiOpCodeVo opCode;
	private final Border outBorder = new Border(
			new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
	private BorderPane borderPane = new BorderPane();
	private StackPane mainPane = new StackPane();
	private Node contextNode;
	private boolean inited = false;

	public FxStage(Node contextNode, String title) {

		this.contextNode = contextNode;

		borderPane.setTop(makeTop(title));
		borderPane.setCenter(contextNode);
		borderPane.setPadding(new Insets(0, 0, 0, 0));

		if (contextNode instanceof Region) {
			Region region = ((Region) contextNode);
			region.setPadding(new Insets(1, 3, 3, 3));
		}

		borderPane.setBorder(contextBorder);

		mainPane.getChildren().add(borderPane);
		mainPane.setBorder(outBorder);
		mainPane.setPadding(new Insets(0, 0, 0, 0));
	}

	public FxStage(Node contextNode, UiOpCodeVo opCode, String doneText, FxCallfront callfront, DxCallback callback) {

		this(contextNode, opCode, doneText, (String) null, callback);

		if (callfront != null) {

			contextNode.setDisable(true);

			new Thread() {
				public void run() {
					Map<String, Object> data = callfront.call();
					contextNode.setDisable(false);

					if (data != null) {
						Platform.runLater(new Runnable() {
							public void run() {

								System.out
										.println("------------------------------------------------------------------");
								System.out.println(data);
								System.out.println(contextNode);
								System.out
										.println("------------------------------------------------------------------");

								if (data != null && contextNode instanceof FxUi) {
									((FxUi) contextNode).initData(data);
								}
							}
						});
					}
				}
			}.start();

		}
	}

	public FxStage(Node contextNode, UiOpCodeVo opCode, String doneText, String closeText, DxCallback callback) {

		this(contextNode, "[" + opCode.getOpNo() + "] " + opCode.getOpTitle());

		this.callback = callback;
		this.opCode = opCode;

		Node node = makeBottom(doneText, closeText);

		if (node != null) {
			borderPane.setBottom(node);
		}
	}

	public void setCloseCallback(FxCallback<WindowEvent> closeCallback) {
		this.closeCallback = closeCallback;
	}

	public void showDialog(Node parent) {
		if (parent == null || parent.getScene() == null) {
			show(null, Modality.NONE);

		} else {
			show(parent.getScene().getWindow(), Modality.APPLICATION_MODAL);
		}
	}

	public void showMsg(String msg) {
		if (windowTitle != null) {
			windowTitle.showMsg(msg);
		}
	}

	public void showStage(Window parent) {
		show(parent, Modality.NONE);
	}

	@Override
	public String toString() {
		if (windowTitle != null) {
			return "Stage:" + windowTitle.getTitle();
		}
		return "Stage:" + contextNode.getClass().getName();
	}

	private Node makeBottom(String doneText, String closeText) {

		if (doneText == null && closeText == null) {
			return null;
		}

		HBox hBox = new HBox(8);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(8, 8, 8, 8));

		if (opCode != null && doneText != null && doneText.trim().length() > 0) {

			Button doneButton = new Button(doneText);
			doneButton.getStyleClass().add(CssPointer.FxWindowButton);

			doneButton.setDefaultButton(true);
			doneButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (contextNode instanceof FxEditorNode) {
						Map<String, Object> data = ((FxEditorNode) contextNode).getInputData();

						if (data == null) {
							return;
						}

						if (opCode.isCall()) {
							data = DxAsyncSelector.getSelector().callMethod(contextNode, opCode, data);
						}

						if (callback != null && data != null) {
							callback.onCallback(data);
						}
					}
				}
			});
			hBox.getChildren().add(doneButton);
		}

		if (closeText != null && closeText.trim().length() > 0) {
			Button cancelButton = new Button(closeText);
			cancelButton.getStyleClass().add(CssPointer.FxWindowButton);
			cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					hide();
				}
			});
			hBox.getChildren().add(cancelButton);
		}

		return hBox.getChildren().size() > 0 ? hBox : null;
	}

	private Node makeTop(String title) {
		windowTitle = new FxWindowTitle(title);
		return windowTitle;
	}

	private void show(Window parent, Modality modality) {

		if (inited) {
			show();
			toFront();
			return;
		}

		inited = true;

		initModality(modality);
		initStyle(StageStyle.DECORATED);

		if (parent != null) {
			initOwner(parent);
		}

		Scene scene = new Scene(mainPane);
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

		setScene(scene);

		setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {

				if (contextNode instanceof FxNode) {
					((FxNode) contextNode).onRemovedFromParent();
				}

				if (closeCallback != null) {
					closeCallback.onCallback(we);
				}
			}
		});

		windowTitle.setStage(this);

		if (contextNode instanceof FxNode) {
			((FxNode) contextNode).onAddedInParent();
		}

		//
		widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
//				System.out.println(newSceneWidth);
			}
		});
		heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
//				System.out.println(newSceneHeight.doubleValue());
			}
		});

		show();
		toFront();
	}

	private void showStage(Node parent) {
		if (parent == null || parent.getScene() == null) {
			show(null, Modality.NONE);

		} else {
			show(parent.getScene().getWindow(), Modality.NONE);
		}
	}

}
