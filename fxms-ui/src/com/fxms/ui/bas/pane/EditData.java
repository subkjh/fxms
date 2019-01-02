package com.fxms.ui.bas.pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.editor.CodeEditor;
import com.fxms.ui.bas.editor.DynamicEditor;
import com.fxms.ui.bas.editor.FxEditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class EditData {

	private UiOpCodeVo opCode;
	private final Map<String, Object> dataMap = new HashMap<String, Object>();
	private final List<Node> attrNodeList = new ArrayList<Node>();

	public EditData(UiOpCodeVo opCode) {
		this.opCode = opCode;
	}

	public Map<String, Object> getParameters() {
		return dataMap;
	}

	public Map<String, Object> getInputData(Node parent) {

		UiOpCodeAttrVo attr;
		FxEditor editor;
		StringBuffer errmsg = new StringBuffer();
		Map<String, Object> ret = new HashMap<String, Object>(dataMap);

		for (Node node : attrNodeList) {
			if ((node instanceof FxEditor) == false) {
				continue;
			}

			editor = (FxEditor) node;

			if (node.getUserData() instanceof UiOpCodeAttrVo) {
				attr = (UiOpCodeAttrVo) node.getUserData();

				if (attr.isNullableYn() == false) {
					if (editor.getAttrId() == null || editor.getAttrId().length() == 0) {
						if (errmsg.length() > 0) {
							errmsg.append(", ");
						}
						errmsg.append(attr.getAttrDisp());
					}
				}

				ret.put(attr.getAttrName(), editor.getAttrId());
			}
		}

		ret.putAll(opCode.getConstMap());

		for (UiOpCodeAttrVo a : opCode.getChildren()) {
			if (a.getAttrType().equals(FxEditor.EDITOR_TYPE.var.name())) {
				ret.put(a.getAttrName(), ret.get(a.getAttrValueList()));
			}
		}

		if (errmsg.length() > 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(parent.getScene().getWindow());
			alert.setTitle("Check Data");
			alert.setHeaderText("아래 항목이 입력되지 않았습니다.");
			alert.setContentText(errmsg.toString());
			alert.showAndWait();
			return null;
		}

		return ret;
	}

	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	public void initEditor(Map<String, Object> data) {

		if (data != null) {

			dataMap.putAll(data);

			UiOpCodeAttrVo attr;
			Object value;
			for (Node node : attrNodeList) {
				if (node.getUserData() instanceof UiOpCodeAttrVo) {
					attr = (UiOpCodeAttrVo) node.getUserData();
					if (node instanceof FxEditor) {
						value = data == null ? null : data.get(attr.getAttrName());
						if (value != null) {
							((FxEditor) node).setAttrId(value == null ? "" : value, data);
						}
					}
				}
			}

		} else {
			for (Node node : attrNodeList) {
				if (node instanceof FxEditor) {
					((FxEditor) node).setAttrId(null, null);
				}
			}
		}
	}

	public GridPane makePane(String groupName, boolean showTitle) {

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(12, 12, 12, 12));
		gridPane.setVgap(5);
		gridPane.setHgap(10);

		if (showTitle) {
			final Label groupLabel = new Label(groupName == null ? "..." : groupName);
			groupLabel.getStyleClass().add("editor-group");
			GridPane.setConstraints(groupLabel, 0, 0, 2, 1);
			gridPane.getChildren().add(groupLabel);
		}

		int col = 0;
		int row = 0;
		int seqBy = -1;
		Node node;

		for (UiOpCodeAttrVo attr : opCode.getChildren(groupName)) {

			if (attr.getAttrType().equals(FxEditor.EDITOR_TYPE.Const.name())) {
				dataMap.put(attr.getAttrName(), attr.getAttrDefaultValue());
				continue;
			}

			if (attr.getAttrType().equals(FxEditor.EDITOR_TYPE.var.name())) {
				continue;
			}

			if (seqBy / 10 == attr.getSeqBy() / 10) {
				col += 2;
			} else {
				col = 0;
				row++;
			}

			node = addEditor(gridPane, col, row, attr);

			attrNodeList.add(node);

			seqBy = attr.getSeqBy();

		}

		UiOpCodeAttrVo attr;
		for (Node e : attrNodeList) {
			if (e instanceof DynamicEditor) {
				attr = (UiOpCodeAttrVo) e.getUserData();
				for (Node e1 : attrNodeList) {
					if (e1 instanceof CodeEditor
							&& ((UiOpCodeAttrVo) e1.getUserData()).getAttrName().equals(attr.getAttrValueList())) {
						((CodeEditor) e1).valueProperty().addListener(new ChangeListener<UiCodeVo>() {
							@Override
							public void changed(ObservableValue<? extends UiCodeVo> observable, UiCodeVo oldValue,
									UiCodeVo newValue) {
								((DynamicEditor) e).onSelected(null, newValue);
							}
						});
					}
				}
			}
		}

		return gridPane;

	}

	private Node addEditor(GridPane grid, int col, int row, UiOpCodeAttrVo attr) {

		final Node editor = attr.makeEditor();
		editor.setUserData(attr);

		if (editor instanceof EditorTabPane) {
			GridPane.setConstraints(editor, col, row);
			grid.getChildren().add(editor);
		} else {

			final Label nameLabel = new Label(attr.getAttrDisp() + " :");
			if (attr.isNullableYn()) {
				nameLabel.getStyleClass().add("editor-item");
			} else {
				nameLabel.getStyleClass().add("editor-item-not-null");
			}

			if (col == 0) {
				// nameLabel.setPrefWidth(100);
				nameLabel.setPrefHeight(26);

				if (editor instanceof Region) {
					if (attr.getAttrWidth() <= 10) {
						((Region) editor).setPrefWidth(150);
					} else {
						((Region) editor).setPrefWidth(attr.getAttrWidth());
					}
				}
			}

			GridPane.setConstraints(nameLabel, col, row);
			grid.getChildren().add(nameLabel);

			GridPane.setConstraints(editor, col + 1, row);
			grid.getChildren().add(editor);
		}

		return editor;
	}
}
