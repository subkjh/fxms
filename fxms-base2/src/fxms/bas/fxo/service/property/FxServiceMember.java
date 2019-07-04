package fxms.bas.fxo.service.property;

import subkjh.bas.co.log.Loggable;
import fxms.bas.fxo.FxActor;

/**
 * 서비스의 스레드로 서비스가 실행시켜 준다.
 * 
 * @author subkjh(김종훈)
 *
 */
public interface FxServiceMember extends FxActor, Loggable {

	public void startMember() throws Exception;

}
