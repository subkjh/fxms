package fxms.bas.fxo.service.mo;

import fxms.bas.co.noti.ExAliveNotiFilter;
import fxms.bas.po.noti.NotiReqMakeStat;

public class MoServiceNotiFilter extends ExAliveNotiFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2556876260213576640L;

	public MoServiceNotiFilter() {

		super();

		addExclude(NotiReqMakeStat.class);

	}
}
