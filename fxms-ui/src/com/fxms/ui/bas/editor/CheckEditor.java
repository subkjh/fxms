package com.fxms.ui.bas.editor;

import java.util.Map;

import javafx.scene.control.CheckBox;

public class CheckEditor extends CheckBox implements FxEditor {

	private boolean defValue = false;

	public CheckEditor() {

	}

	@Override
	public void clearEditor() {
		setSelected(defValue);
	}

	public void setDefValue(String defString) {

		if (defString == null) {
			this.defValue = false;
		} else {
			this.defValue = defString.toString().equalsIgnoreCase("y") || defString.toString().equalsIgnoreCase("true")
					|| defString.toString().equals("1");
		}
	}

	@Override
	public String getAttrId() {
		return this.isSelected() ? "Y" : "N";
	}

	@Override
	public void setAttrId(Object id, Map<String, Object> objectData) {
		if (id == null) {
			setSelected(false);
		} else if (id instanceof Boolean) {
			this.setSelected((Boolean) id);
		} else {
			this.setSelected(id.toString().equalsIgnoreCase("y") || id.toString().equalsIgnoreCase("true")
					|| id.toString().equals("1"));
		}

	}

	@Override
	public void init(String attrValueList, String promptText, int width) {

		// this.setText(value);
		this.setPrefWidth(width);
	}

}
