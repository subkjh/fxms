package fxms.bas.fxo.filter;

import fxms.bas.fxo.FxActorImpl;

/**
 * 수집값이 변경되었을 때 이후 작업을 위한 필터이다.
 * 
 * @author subkjh
 *
 */
public abstract class PsUpdateFilter extends FxActorImpl {

	/**
	 * 수집값이 변경되었을때 호출된다.
	 * 
	 * @param moNo   MO번호
	 * @param psId 성능항목
	 * @param value  현재 수집된 값
	 * @throws Exception
	 */
	public abstract void updated(long moNo, String psId, Object value) throws Exception;

}
