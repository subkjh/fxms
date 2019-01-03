package fxms.bas.fxo.service.property;

import fxms.bas.fxo.FxActor;
import subkjh.bas.log.Loggable;

/**
 * FxService 내에서 하나의 스레드로 실행되는 클래스
 * 
 * @author subkjh
 *
 */
public interface FxServiceMember extends FxActor, Loggable {

	public void start();

}
