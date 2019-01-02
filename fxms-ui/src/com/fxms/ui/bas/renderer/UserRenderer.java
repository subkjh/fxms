package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.CodeMap;

import javafx.scene.control.TextField;

public class UserRenderer extends TextField implements FxRenderer {

	public UserRenderer() {
		getStyleClass().add("text-renderer");
	}

	@Override
	public void setValue(Object value, String type) {
		
		int userNo = FxRenderer.getInt(value, -1);
		if ( userNo <= 0) {
			setText("-"); 
		} else {
			setText(CodeMap.getMap().getUserName(userNo));
		}
		
	}

}
