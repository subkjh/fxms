package fxms.bas.noti;

import fxms.bas.signal.AliveSignal;

public class ExAliveNotiFilter extends NotiFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5266829873848575986L;

	@SuppressWarnings("unchecked")
	public ExAliveNotiFilter() {
		
		super();

		addExclude(AliveSignal.class);
	}
}
