package fxms.bas.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.event.FxEvent;
import fxms.bas.fxo.adapter.AlarmAfterAdapter;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.vo.OccurAlarm;
import subkjh.bas.co.log.Logger;

/**
 * 발생된 알람에 대한 후속조치를 하는 스레드
 * 
 * @author subkjh
 *
 */
public class AlarmTreatThread extends QueueFxThread<OccurAlarm> {

	private static AlarmTreatThread fxThread;

	public static AlarmTreatThread getTreatMgr() {
		if (fxThread == null) {
			fxThread = new AlarmTreatThread();
			fxThread.start();
		}

		return fxThread;
	}

	private Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

	public AlarmTreatThread() {
		super(0);
		setName(AlarmTreatThread.class.getSimpleName());
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		super.onEvent(noti);

		if (noti instanceof ReloadSignal) {
			loadAmGroup();
		}
	}

	private void loadAmGroup() {
		try {
			// this.moAmMap = VarApi.getApi().getMoAmMap();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	protected void doInit() {
		loadAmGroup();
	}

	@Override
	protected void doWork(OccurAlarm e) throws Exception {

		if (e.getFpactCd() == null || e.getFpactCd().length() == 0) {
			return;
		}

		List<AmGroupVo> groupList = moAmMap.get(e.getMoNo());
		if (groupList == null) {
			groupList = moAmMap.get(e.getUpperMoNo());
		}

		List<AlarmAfterAdapter> filterList = AdapterApi.getApi().getAdapters(AlarmAfterAdapter.class);
		List<AmHstVo> hstList = new ArrayList<AmHstVo>();
		List<AmHstVo> entry;

		for (AlarmAfterAdapter filter : filterList) {
			if (filter.getFpactCd().equals(e.getFpactCd())) {
				entry = filter.followUp(e, groupList);
				if (entry != null) {
					hstList.addAll(entry);
				}
			}
		}

//		if (hstList.size() > 0) {
//			CoApi.getApi().logAmHst(hstList);
//		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

}
