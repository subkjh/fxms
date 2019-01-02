package com.fxms.ui.dx.confirm;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxCallback;

import javafx.scene.Node;

public interface Confirmer {
	
	public void confirm(Node node, UiOpCodeVo opCode, Map<String, Object> data, DxCallback callback);
	

}
