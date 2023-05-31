package fxms.bas.fxo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.exp.FxNotMatchException;
import fxms.bas.exp.FxTimeoutException;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsVoRaw;

/**
 * 관리대상으로부터 값을 수집하는 ADAPTER
 * 
 * @author subkjh
 *
 * @param <MO>
 */
public abstract class FxGetValueAdapter extends FxAdapterImpl implements FxMoAdapter {

	private final Map<String, Object> moPara;
	private int pollCycle = 0;

	public FxGetValueAdapter() {
		this.moPara = new HashMap<String, Object>();
	}

	public void setMoPara(Map<String, Object> o) {
		this.moPara.clear();
		if (o != null)
			this.moPara.putAll(o);
	}

	/**
	 * 특정 값을 조회한다.
	 * 
	 * @param targetMo
	 * @return
	 * @throws FxTimeoutException
	 * @throws Exception
	 */
	public abstract List<PsVoRaw> getValue(Mo targetMo) throws FxNotMatchException, FxTimeoutException, Exception;

	/**
	 * 
	 * @param mo
	 * @throws Exception
	 */
	protected void throwNotImplException(Mo mo) throws Exception {
		throw new Exception("Not Implement for " + mo);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getNode(Mo targetMo, Class<T> classOfT) throws FxNotMatchException {

		if (classOfT.isInstance(targetMo)) {
			return (T) targetMo;
		}

		throw new FxNotMatchException(targetMo.getMoName());
	}

	@Override
	public void onCreated() throws Exception {

	}

	public int getPollCycle() {
		return pollCycle;
	}

	public void setPollCycle(int pollCycle) {
		this.pollCycle = pollCycle;
	}

	public Map<String, Object> getMoPara() {
		return moPara;
	}

	public boolean match(Mo mo) {
		// TODO
		return true;
	}
}
