package fxms.bas.alarm.treat;

import java.util.List;

import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.dbo.am.AmGroupVo;
import fxms.bas.dbo.am.AmHstVo;

public class LogTreatFilter extends TreatFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -271136428205655643L;

	@Override
	public List<AmHstVo> treat(OccurAlarmDbo alarm, List<AmGroupVo> amGroupList) throws Exception {

		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");

		return null;
	}

	@Override
	public List<AmHstVo> treat(ClearAlarmDbo alarm, List<AmGroupVo> amGroupList) throws Exception {
		
		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");
		return null;

	}

}
