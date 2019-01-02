package com.fxms.ui.dx;

/**
 * 
 * @author SUBKJH-DEV
 *
 * @param <DATA>
 */
public interface FxCallback<DATA> {

	/**
	 * 
	 * @param data
	 *            callback 데이터
	 */
	public void onCallback(DATA data);

}
