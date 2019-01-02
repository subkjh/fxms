package com.fxms.ui.bas.property;

import javafx.scene.Node;

/**
 * 부모를 가지는 화면
 * 
 * @author SUBKJH-DEV
 *
 */
public interface FxUiChild {

	public Node getFxUiParent();

	public void setFxUiParent(Node parent);
}
