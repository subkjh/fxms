package com.fxms.ui.bas.editor;

import com.fxms.ui.bas.vo.Attr;

import javafx.scene.text.Font;

public class FontEditor extends AttrComboBox {

	public FontEditor() {

	}

	@Override
	public void initList(String attrValueList) {

		for (String item : Font.getFamilies()) {
			if (item.trim().length() > 0) {
				Attr attr = new Attr();
				attr.setAttrId(item);
				attr.setAttrText(item);
				getItems().add(attr);
			}
		}
	}

}
