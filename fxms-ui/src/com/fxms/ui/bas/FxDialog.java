package com.fxms.ui.bas;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.pane.EditorTabPane;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxNode;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallback;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;

public abstract class FxDialog<DATA> extends Dialog<DATA> {

	public static void showDialog(Node parent, Node node, UiOpCodeVo opCode, String doneText, String cancelText,
			FxCallback<Map<String, Object>> callback) {
		showDialogWindowStyle(parent, node, opCode, doneText, cancelText, callback);
	}

	static void showDialogWindowStyle(Node parent, Node node, UiOpCodeVo opCode, String doneText, String cancelText,
			FxCallback<Map<String, Object>> callback) {

		new FxDialog<Map<String, Object>>(parent, (Node) node, opCode.getOpTitle(), doneText, cancelText, callback) {

			@Override
			public Map<String, Object> getDoneValue() {

				if (node instanceof FxEditorNode) {

					Map<String, Object> data = ((FxEditorNode) node).getInputData();
					if (data == null) {
						return null;
					}

					if (opCode.isCall()) {
						return DxAsyncSelector.getSelector().callMethod(getDialogPane().getContent(), opCode, data);
					} else {
						return data;
					}
				} else {
					return new HashMap<String, Object>();
				}
			}
		};

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static FxDialog showDialog(Node parent, Node node, String headerText) {

		FxDialog dialog = new FxDialog(parent, node, headerText, null, "Close", null) {
			@Override
			public Object getDoneValue() {
				return new HashMap<String, Object>();
			}
		};

		return dialog;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static FxDialog showDialog(Node parent, Node node, String headerText, String doneText) {

		FxDialog dialog = new FxDialog(parent, node, headerText, doneText, "Cancel", null) {
			@Override
			public Object getDoneValue() {
				return new Object();
			}
		};

		return dialog;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void showDialog(Node parent, Node node, UiOpCodeVo opcode, String doneText, String cancelText,
			final DxCallback callback) {
		new FxDialog(parent, node, opcode.getOpTitle(), doneText, cancelText, callback) {
			@Override
			public Object getDoneValue() {
				Map<String, Object> data = ((FxEditorNode) node).getInputData();
				if (data == null) {
					return null;
				}

				if (opcode.isCall()) {
					return DxAsyncSelector.getSelector().callMethod(getDialogPane().getContent(), opcode, data);
				} else {
					return data;
				}
			}
		};
	}

	public static void showEditorDialog(Node parent, UiOpCodeVo opCode, String title, String doneText,
			String cancelText, FxCallback<Map<String, Object>> callback, Map<String, Object> data) {

		// EditorPaneBase pane = new EditorPaneBase(maxCol, opCode);
		EditorTabPane pane = new EditorTabPane();
		pane.init(opCode);
		pane.initData(data);

		new FxDialog<Map<String, Object>>(parent, pane, title, doneText, cancelText, callback) {

			@Override
			public Map<String, Object> getDoneValue() {

				Map<String, Object> data = ((FxEditorNode) pane).getInputData();
				if (data == null) {
					return null;
				}

				if (opCode.isCall()) {
					return DxAsyncSelector.getSelector().callMethod(getDialogPane().getContent(), opCode, data);
				} else {
					return data;
				}
			}
		};

	}

	private ButtonType doneButton = null;

	public FxDialog(Node parent, Node node, String headerText, String doneText, String cancelText,
			final FxCallback<DATA> callback) {

		if (parent != null) {
			initOwner(parent.getScene().getWindow());
		} else {
			initModality(Modality.WINDOW_MODAL);
		}

		if (doneText != null) {
			doneButton = new ButtonType(doneText, ButtonData.OK_DONE);
			getDialogPane().getButtonTypes().add(doneButton);
		}

		if (cancelText != null) {
			getDialogPane().getButtonTypes().add(new ButtonType(cancelText, ButtonData.CANCEL_CLOSE));
		}

		getDialogPane().setContent(node);

		setResultConverter(dialogButton -> {
			if (dialogButton == doneButton) {
				return getDoneValue();
			}
			return null;
		});

		setGraphic(new ImageView(ImagePointer.getImage("s16x16/fx.jpg")));
		setTitle("FxMS Dialog");
		setHeaderText(headerText);

		// Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		getDialogPane().getScene().getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));

		setOnCloseRequest(new EventHandler<DialogEvent>() {
			public void handle(DialogEvent we) {
				if (node instanceof FxNode) {
					((FxNode) node).onRemovedFromParent();
				}
			}
		});

		Optional<DATA> result = showAndWait();

		if (callback != null && result.isPresent() && result.get() != null) {
			callback.onCallback(result.get());
		}
	}

	public abstract DATA getDoneValue();
}
