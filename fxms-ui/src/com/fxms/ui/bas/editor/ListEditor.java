package com.fxms.ui.bas.editor;

public class ListEditor extends AttrComboBox {

	public ListEditor() {

	}

	@Override
	public void initList(String attrValueList) {
		getItems().addAll(FxEditor.makeAttrList(attrValueList));
	}
}
