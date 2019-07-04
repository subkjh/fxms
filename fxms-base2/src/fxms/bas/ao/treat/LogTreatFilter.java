package fxms.bas.ao.treat;

import java.util.List;

import fxms.bas.ao.vo.ClearAlarm;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;

public class LogTreatFilter extends TreatFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -271136428205655643L;

	@Override
	public List<AmHstVo> treat(OccurAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {

		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");

		return null;
	}

	@Override
	public List<AmHstVo> treat(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {

		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");
		return null;

	}

}
