package fxms.bas.ao.treat;

import java.io.Serializable;
import java.util.List;

import fxms.bas.ao.vo.ClearAlarm;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.fxo.FxActorImpl;

public abstract class TreatFilter extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6457460666196483945L;

	public abstract List<AmHstVo> treat(OccurAlarm alarm, List<AmGroupVo> amGroupList) throws Exception;

	public abstract List<AmHstVo> treat(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception;

}
