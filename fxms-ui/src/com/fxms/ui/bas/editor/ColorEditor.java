package com.fxms.ui.bas.editor;

import java.util.Map;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorEditor extends ColorPicker implements FxEditor {

	public ColorEditor() {
	}

	@Override
	public void clearEditor() {
	}

	@Override
	public String getAttrId() {
		return super.getValue().toString();
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		if (id instanceof Color) {
			setValue((Color) id);
		} else {
			try {
				setValue(Color.valueOf(id.toString()));
			} catch (Exception e) {
			}
		}

	}

	@Override
	public void init(String attrValueList, String promptText, int width) {
		setPromptText(promptText);
		setPrefWidth(width);
	}

}
