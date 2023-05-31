package fxms.bas.fxo.adapter;

import java.util.Map;

import fxms.bas.fxo.FxObject;

/**
 * 
 * @author subkjh
 *
 */
public interface FxAdapter extends FxObject {

	/**
	 * 클래스가 생성되고 인수 및 조건이 모두 적용된 후에 호출된다.
	 */
	public void onCreated() throws Exception;

	/**
	 * 아답터의 속성 지정
	 * 
	 * @param para
	 */
	public void setPara(Map<String, Object> para);

}
