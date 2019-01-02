package fxms.bas.fxo.adapter;

import java.util.List;
import java.util.Map;

import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.pso.PsVo;

public abstract class Adapter<MO> extends FxActorImpl {

	public abstract void getConfigChildren(MoConfig children, String... moClasses)
			throws FxTimeoutException, Exception;

	public abstract List<PsVo> getValue(MO mo, String psCodes[]) throws FxTimeoutException, Exception;

	public abstract void setValue(MO mo, String method, Map<String, Object> parameters) throws Exception;

	protected void throwNotImplException(MO mo) throws Exception {
		throw new Exception("Not Implement for " + mo);
	}

}
