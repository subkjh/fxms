package fxms.bas.fxo.service;

import fxms.bas.fxo.FxActor;
import subkjh.bas.co.log.Loggable;

/**
 * 서비스의 스레드로 서비스가 실행시켜 준다.
 * 
 * @author subkjh(김종훈)
 *
 */
public interface FxServiceMember extends FxActor, Loggable {

	/**
	 * 스레드 시작
	 * 
	 * @throws Exception
	 */
	public void startMember() throws Exception;

}
