package fxms.bas.fxo.adapter;

import java.util.List;

import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.vo.ClearAlarm;
import fxms.bas.vo.OccurAlarm;

@FxAdapterInfo(service = "AlarmService", descr = "단순한 로그를 남긴다.")
public class AlarmAfterLogAdapter extends AlarmAfterAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -271136428205655643L;

	@Override
	public List<AmHstVo> followUp(OccurAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {

		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");

		return null;
	}

	@Override
	public List<AmHstVo> followUp(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception {

		System.out.println("--- LogTreatFilter --------------------------------------------");
		System.out.println(alarm);
		System.out.println("---------------------------------------------------------------");
		return null;

	}

	@Override
	public String getFpactCd() {
		return "LOG";
	}

}
