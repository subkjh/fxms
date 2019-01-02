package com.fxms.nms.fxactor;

import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.FxActor;
import fxms.bas.mo.child.MoConfig;

public interface NeConfigActor extends FxActor {

	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception;

	public boolean match(Object obj);

}
