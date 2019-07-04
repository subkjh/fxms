package fxms.nms.fxactor;

import fxms.bas.co.exp.FxTimeoutException;
import fxms.bas.fxo.FxActor;
import fxms.bas.mo.child.MoConfig;

/**
 * 장비의 구성정보를 수집하는 클래스 
 * @author subkjh(김종훈)
 *
 */
public interface NeConfigActor extends FxActor {

	/**
	 * 
	 * @param children
	 * @param moClasses
	 * @throws FxTimeoutException
	 * @throws Exception
	 */
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception;

	/**
	 * 장비가 이 클래스에 해당되는지 여부
	 * @param obj
	 * @return
	 */
	public boolean match(Object obj);

}
