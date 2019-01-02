package com.fxms.ui.bas.editor;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.vo.Attr;

public class PsCodeEditor extends AttrComboBox {

	public PsCodeEditor() {
	}

	@Override
	public void initList(String attrValueList) {
		for (UiPsItemVo psitem : CodeMap.getMap().getPsItemMap().values()) {
			getItems().add(new Attr(psitem.getPsCode(), psitem.getPsName(), psitem));
		}
	}
}