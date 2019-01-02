package com.fxms.ui.bas.editor;

/**
 * 목록성 에디터
 * 
 * @author SUBKJH-DEV
 *
 */
public interface FxListEditor<DATA> extends FxEditor {

	public void setCallback(EditorCallback<DATA> callback);
	
}
