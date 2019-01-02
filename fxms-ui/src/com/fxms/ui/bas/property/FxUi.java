package com.fxms.ui.bas.property;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;

/**
 * 아래 순서대로 호출해야 한다.<br>
 * 
 * 1. init<br>
 * 2. initData<br>
 * 
 * @author SUBKJH-DEV
 *
 */
public interface FxUi {

	public UiOpCodeVo getOpCode();

	/**
	 * 운용코드를 설정한다.
	 * 
	 * @param opcode
	 *            운용코드
	 */
	public void init(UiOpCodeVo opcode);

	/**
	 * 
	 * 타켓 데이터를 설정한다.
	 * 
	 * @param data
	 *            타켓 데이터
	 */
	public void initData(Map<String, Object> data);

}
