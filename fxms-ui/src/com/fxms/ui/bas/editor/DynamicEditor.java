package com.fxms.ui.bas.editor;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.code.UiOpCodeAttrVo;
import com.fxms.ui.bas.vo.Attr;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class DynamicEditor extends HBox implements FxEditor, EditorCallback<UiCodeVo> {

	private final ComboBox<Attr> comboBox;
	private final TextField textField;

	public DynamicEditor() {
		comboBox = new ComboBox<Attr>();
		textField = new TextField();
		getChildren().add(textField);
	}

	public void setPromptText(String value) {
		textField.setPromptText(value);
	}

	@Override
	public void onSelected(FxEditor editor, UiCodeVo code) {
		if (code != null && code.getFillQry() != null && code.getFillQry().length() > 0) {

			String ss[] = code.getFillQry().split("=");
			if (ss.length == 2) {

				showCombo(true);

				comboBox.getItems().clear();

				if (ss[0].equals("qid")) {
					for (Attr a : CodeMap.getMap().getAttrList(ss[1], null)) {
						comboBox.getItems().add(a);
					}
				} else {
					List<UiCodeVo> list = CodeMap.getMap().getCodeList(ss[1]);
					if (list != null) {
						for (UiCodeVo cd : list) {
							comboBox.getItems().add(new Attr(cd.getCdCode(), cd.getCdName(), cd));
						}
					}
				}
			}
		} else {
			showCombo(false);
		}
	}

	private void showCombo(boolean isShow) {
		if (isShow) {
			getChildren().remove(textField);
			if (getChildren().contains(comboBox) == false) {
				getChildren().add(comboBox);
			}
		} else {
			getChildren().remove(comboBox);
			if (getChildren().contains(textField) == false) {
				getChildren().add(textField);
			}
		}
	}

	@Override
	public String getAttrId() {
		if (getChildren().contains(comboBox)) {
			Attr attr = comboBox.getSelectionModel().getSelectedItem();
			return attr == null ? null : attr.getAttrId();
		} else {
			return textField.getText();
		}
	}

	@Override
	public void clearEditor() {
		textField.setText("");
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		if (comboBox.isVisible()) {
			setComboId(id);
		} else {
			textField.setText(id == null ? "" : id.toString());
		}
	}

	private void setComboId(Object id) {
		if (id instanceof Number) {
			for (Attr cd : comboBox.getItems()) {
				try {
					if (Double.valueOf(cd.getAttrId()).doubleValue() == ((Number) id).doubleValue()) {
						comboBox.getSelectionModel().select(cd);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			String attrId = String.valueOf(id);
			for (Attr cd : comboBox.getItems()) {
				if (attrId.equalsIgnoreCase(cd.getAttrId())) {
					comboBox.getSelectionModel().select(cd);
					return;
				}
			}
		}
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		this.setPrefWidth(width);

	}
}
