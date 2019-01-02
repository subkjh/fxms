package com.fxms.ui.bas.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class EditorPane extends BorderPane implements FxEditorNode, FxUi {

	private ScrollPane scroll;
	private DxCallback callback;
	private Button btnAction;
	private Button btnCancel;
	private EditData editorData;
	private boolean visibleGroup = true;
	private UiOpCodeVo opCode;

	public EditorPane() {
	}

	public EditorPane(String action, String cancelAction, boolean visibleGroup) {
		this.visibleGroup = visibleGroup;
		setButton(action, cancelAction);
		setMinSize(300, 200);

	}

	public void setCallback(DxCallback callback) {
		this.callback = callback;
	}

	public EditData getEditorData() {
		return editorData;
	}

	@Override
	public Map<String, Object> getInputData() {
		return editorData.getInputData(EditorPane.this);
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public Map<String, Object> getDefaultData() {
		return editorData.getParameters();
	}

	@Override
	public void init(UiOpCodeVo opcode) {

		this.opCode = opcode;
		editorData = new EditData(opcode);

		if (opcode != null) {
			scroll = new ScrollPane();
			HBox hBox = new HBox(8);
			if (visibleGroup) {
				List<String> groupList = opcode.getAttrGroupList();
				for (String groupName : groupList) {
					hBox.getChildren().add(editorData.makePane(groupName, true));
				}
			} else {
				hBox.getChildren().add(editorData.makePane(null, false));
			}
			scroll.setContent(hBox);
			setCenter(scroll);
		} else {
			setCenter(new Label("op-code is null"));
		}

	}

	@Override
	public void initData(Map<String, Object> data) {

		System.out.println(getClass().getSimpleName() + " :: " + data);

		editorData.initEditor(data);
	}

	private void setButton(String action, String cancelAction) {

		HBox hbox = new HBox(10);
		hbox.setPadding(new Insets(8, 8, 8, 8));
		hbox.setAlignment(Pos.CENTER_RIGHT);

		if (action != null) {

			btnAction = new Button(action);
			hbox.getChildren().add(btnAction);

			btnAction.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {

					Map<String, Object> data = getInputData();
					if (data == null) {
						return;
					}

					if (editorData.getOpCode().isCall()) {
						Map<String, Object> ret;
						ret = DxAsyncSelector.getSelector().callMethod(btnAction, editorData.getOpCode(), data);

						if (ret == null) {
							return;
						}

						if (callback != null) {
							callback.onCallback(ret);
						}
					} else {
						if (callback != null) {
							callback.onCallback(data);
						}
					}

				}
			});
		}

		if (cancelAction != null) {
			btnCancel = new Button(cancelAction);
			hbox.getChildren().add(btnCancel);

			btnCancel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					EditorPane.this.getScene().getWindow().hide();
				}
			});

		}

		if (hbox.getChildren().size() > 0) {
			setBottom(hbox);
		}
	}

}
