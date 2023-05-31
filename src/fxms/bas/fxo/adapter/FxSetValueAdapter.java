package fxms.bas.fxo.adapter;

import java.util.Map;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;

public abstract class FxSetValueAdapter<UPPER extends Mo, CHILD extends Mo> extends FxActorImpl implements FxMoAdapter {

	/**
	 * 관리대상에 값을 설정한다.
	 * 
	 * @param mo
	 * @param method
	 * @param parameters
	 * @throws Exception
	 */
	public abstract void setValue(UPPER mo, String method, Map<String, Object> parameters) throws Exception;

	/**
	 * 
	 * @param mo
	 * @throws Exception
	 */
	protected void throwNotImplException(UPPER mo) throws Exception {
		throw new Exception("Not Implement for " + mo);
	}

}