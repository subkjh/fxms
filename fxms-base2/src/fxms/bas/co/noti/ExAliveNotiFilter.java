package fxms.bas.co.noti;

import fxms.bas.co.signal.AliveSignal;

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
