package fxms.bas.event;

import java.io.Serializable;

import fxms.bas.fxo.FxObject;

/**
 * FxMS에서 사용되는 이벤트 정의
 * 
 * @author subkjh
 *
 */
public interface FxEvent extends FxObject, Serializable {

	public enum STATUS {

		acked

		, added

		, changed

		, deleted

		, notified

		, raw;

	}
	
	public void setNo(long no);
	
	public long getNo();

	/**
	 * 이벤트 상태
	 * 
	 * @return
	 */
	public STATUS getStatus();

	/**
	 * 이벤트 상태 설정
	 * 
	 * @param status 상태
	 */
	public void setStatus(STATUS status);

}
