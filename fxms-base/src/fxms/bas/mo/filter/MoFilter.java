package fxms.bas.mo.filter;

import java.io.Serializable;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.noti.FxEvent;

public abstract class MoFilter extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8502069485123505280L;

	public abstract MoConfig filter(FxEvent.STATUS status, Mo mo) throws Exception;

	public abstract boolean match(Mo mo);

}
