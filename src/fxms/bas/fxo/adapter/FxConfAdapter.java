package fxms.bas.fxo.adapter;

import fxms.bas.exp.FxTimeoutException;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.SyncMo;

/**
 * 관리대상의 구성정보를 가져오는 ADAPTER
 * 
 * @author subkjh
 *
 */
public abstract class FxConfAdapter extends FxAdapterImpl implements FxMoAdapter {

	/**
	 * 관리대상으로부터 구성정보를 조회한다.
	 * 
	 * @param syncMo
	 * @throws FxTimeoutException
	 * @throws Exception
	 */
	public abstract void sync(SyncMo syncMo) throws FxTimeoutException, Exception;

	/**
	 * 
	 * @param mo
	 * @throws Exception
	 */
	protected void throwNotImplException(FxMo mo) throws Exception {
		throw new Exception("Not Implement for " + mo);
	}

	@Override
	public void onCreated() throws Exception {

	}

	public boolean match(Mo mo) {
		return true;
	}

}
