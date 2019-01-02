package com.fxms.ui.bas.editor;

import java.util.Map;

import javafx.scene.control.PasswordField;

public class PwdEditor extends PasswordField implements FxEditor {

	public String getAttrId() {
		return this.getText();
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		this.setText(id == null ? "" : id.toString());
	}

	@Override
	public void clearEditor() {
		this.clear();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		setPrefWidth(width);
	}
}
