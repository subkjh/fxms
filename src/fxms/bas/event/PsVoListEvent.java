package fxms.bas.event;

import fxms.bas.vo.PsVoList;

public class PsVoListEvent extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2625242693782952785L;
	private PsVoList voList;

	public PsVoListEvent(PsVoList voList) {
		this.voList = voList;
	}

	public PsVoList getVoList() {
		return voList;
	}

}
