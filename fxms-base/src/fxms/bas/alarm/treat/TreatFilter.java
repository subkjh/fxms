package fxms.bas.alarm.treat;

import java.io.Serializable;
import java.util.List;

import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.dbo.am.AmGroupVo;
import fxms.bas.dbo.am.AmHstVo;
import fxms.bas.fxo.FxActorImpl;

public abstract class TreatFilter extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6457460666196483945L;

	public abstract List<AmHstVo> treat(OccurAlarmDbo alarm, List<AmGroupVo> amGroupList) throws Exception;

	public abstract List<AmHstVo> treat(ClearAlarmDbo alarm, List<AmGroupVo> amGroupList) throws Exception;

}
