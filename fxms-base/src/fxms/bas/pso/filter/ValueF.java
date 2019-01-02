package fxms.bas.pso.filter;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.pso.VoList;

public abstract class ValueF extends FxActorImpl {

	public abstract void send(VoList volist) throws Exception;

}
