package fxms.bas.pso.filter;

import fxms.bas.fxo.FxActorImpl;

public abstract class UpdateFilter extends FxActorImpl {

	public abstract void updated(long moNo) throws Exception;

}
