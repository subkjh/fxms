package com.fxms.ui.bas;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxNode;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FxWindow {

	public static Stage showStage(Node parent, Node node, UiOpCodeVo opCode, String doneText, String cancelText,
			FxCallback<Map<String, Object>> callback) {

		Stage stage = new Stage() {
			@Override
			public String toString() {
				return node.getClass().getName();
			}
		};

		stage.setTitle(opCode.getOpTitle());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.getIcons().add(ImagePointer.getImage("fxms.png"));
		if (parent != null) {
			stage.initOwner(parent.getScene().getWindow());
		}

		HBox hBox = new HBox(8);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		hBox.setPadding(new Insets(8, 8, 8, 8));

		if (doneText != null) {
			Button doneButton = new Button(doneText);
			doneButton.setDefaultButton(true);
			doneButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (node instanceof FxEditorNode) {
						Map<String, Object> data = ((FxEditorNode) node).getInputData();
						if (data == null) {
							return;
						}

						if (callback != null) {
							if (opCode.isCall()) {
								data = DxAsyncSelector.getSelector().callMethod(node, opCode, data);
							}
							if (data != null) {
								callback.onCallback(data);
							}
						}
					}
				}
			});
			hBox.getChildren().add(doneButton);
		}

		Button cancelButton = new Button(cancelText);

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				stage.hide();
			}
		});
		hBox.getChildren().add(cancelButton);

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(8, 8, 8, 8));
		pane.setCenter(node);
		pane.setBottom(hBox);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

		stage.setScene(scene);

		if (node instanceof FxNode) {
			((FxNode) node).onAddedInParent();
		}

		// 다이얼로그를 보여주고 사용자가 닫을 때까지 기다린다.
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (node instanceof FxNode) {
					((FxNode) node).onRemovedFromParent();
				}
			}
		});

		return stage;

	}

	public static Stage showStage(Node parent, Parent node, String title) {
		return showStageWindowStyle(parent, node, title);
	}

	static Stage showStageFxStyle(Node parent, Parent node, String title) {

		if (node instanceof FxNode) {
			((FxNode) node).onAddedInParent();
		}

		FxStage stage = new FxStage(node, title);
		stage.showStage(parent.getScene().getWindow());
		stage.setOpacity(.98);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (node instanceof FxNode) {
					((FxNode) node).onRemovedFromParent();
				}
			}
		});

		return stage;
	}

	static Stage showStageWindowStyle(Node parent, Parent node, String title) {

		// if ( node.getScene() != null && node.getScene().getWindow() != null) {
		// node.getScene().getWindow();
		// }

		Stage stage = new Stage();
		stage.setTitle(title);
		stage.getIcons().add(ImagePointer.getImage("s16x16/fx.jpg"));
		stage.initModality(Modality.NONE);

		if (parent != null) {
			stage.initOwner(parent.getScene().getWindow());
		}

		Scene scene = new Scene(node);
		scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));
		stage.setScene(scene);

		if (node instanceof FxNode) {
			((FxNode) node).onAddedInParent();
		}

		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (node instanceof FxNode) {
					((FxNode) node).onRemovedFromParent();
				}
			}
		});

		return stage;
	}

}
