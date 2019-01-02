package com.fxms.ui.bas.property;

public interface FxNode {

	/**
	 * 부모에 포함될 때 호출됨
	 */
	public void onAddedInParent();

	/**
	 * 부모에서 제거될 때 호출됨
	 */
	public void onRemovedFromParent();

}
