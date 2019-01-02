package com.fxms.ui.bas.property;

import java.util.Map;

public interface FxEditorNode {

	/**
	 * 
	 * @return 입력한 데이터 + 기본 데이터
	 */
	public Map<String, Object> getInputData();

	/**
	 * 
	 * @return 기본 데이터
	 */
	public Map<String, Object> getDefaultData();

}
