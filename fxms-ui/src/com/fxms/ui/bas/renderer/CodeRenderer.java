package com.fxms.ui.bas.renderer;

import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.code.CodeMap;

import javafx.scene.control.TextField;

public class CodeRenderer extends TextField implements FxRenderer {

	public CodeRenderer() {
		getStyleClass().add("text-renderer");
	}

	@Override
	public void setValue(Object value, String type) {

		if (value != null) {
			try {
				UiCodeVo code = CodeMap.getMap().getCode(type, value.toString());
				if (code != null) {
					setText(code.getCdName());
					return;
				}
			} catch (Exception e) {
			}
		}
		setText("");

	}
}
