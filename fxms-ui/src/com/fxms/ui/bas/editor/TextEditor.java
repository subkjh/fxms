package com.fxms.ui.bas.editor;

import java.util.Map;

import javafx.scene.control.TextField;

/**
 * 문자열 입력 에디터
 * 
 * @author SUBKJH-DEV
 *
 */
public class TextEditor extends TextField implements FxEditor {
	
	public TextEditor()
	{
	}

	@Override
	public void clearEditor() {
		this.clear();
	}

	@Override
	public String getAttrId() {
		return this.getText();
	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		if (width > 0) {
			setPrefWidth(width);
		}
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		this.setText(id == null ? "" : id.toString());
	}

}
