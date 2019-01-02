package com.fxms.ui.dx;

import java.util.Map;

/**
 * Stage가 오픈 될 때 호출되는 인터페이스
 * 
 * @author SUBKJH-DEV
 *
 */
public interface FxCallfront {

	/**
	 * 화면이 오픈 될 때 호출된다.
	 * 
	 * @return initData에 인자로 사용되는 값
	 */
	public Map<String, Object> call();

}
