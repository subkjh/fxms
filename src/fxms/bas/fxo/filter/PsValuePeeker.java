package fxms.bas.fxo.filter;

import fxms.bas.vo.PsVo;

/**
 * 수집된 데이터를 받아보는 인터페이스
 * 
 * @author subkjh
 *
 */
public interface PsValuePeeker {

	/**
	 * 수집된 값이 전달된다.
	 * 
	 * @param mstime 수집시간
	 * @param vo     수집값
	 * @throws Exception
	 */
	public void onValue(long mstime, PsVo vo) throws Exception;

	public String getName();

}