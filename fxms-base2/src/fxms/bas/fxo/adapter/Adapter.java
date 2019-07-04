package fxms.bas.fxo.adapter;

import java.util.List;
import java.util.Map;

import fxms.bas.co.exp.FxTimeoutException;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.po.PsVo;

/**
 * 아답터(Adapter)<br>
 * 관리대상과 연동하여 구성정보, 성능, 값 설정을 처리하는 클래스
 * 
 * @author subkjh(김종훈)
 *
 * @param <MO>
 */
public abstract class Adapter<MO> extends FxActorImpl {

	/**
	 * 관리대상으로부터 구성정보를 조회한다.
	 * 
	 * @param children
	 * @param moClasses
	 * @throws FxTimeoutException
	 * @throws Exception
	 */
	public abstract void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception;

	/**
	 * 특정 값을 조회한다.
	 * 
	 * @param mo
	 * @param psCodes
	 * @return
	 * @throws FxTimeoutException
	 * @throws Exception
	 */
	public abstract List<PsVo> getValue(MO mo, String psCodes[]) throws FxTimeoutException, Exception;

	/**
	 * 관리대상에 값을 설정한다.
	 * 
	 * @param mo
	 * @param method
	 * @param parameters
	 * @throws Exception
	 */
	public abstract void setValue(MO mo, String method, Map<String, Object> parameters) throws Exception;

	/**
	 * 
	 * @param mo
	 * @throws Exception
	 */
	protected void throwNotImplException(MO mo) throws Exception {
		throw new Exception("Not Implement for " + mo);
	}

}
