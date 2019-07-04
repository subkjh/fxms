package fxms.bas.ao.treat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.api.CoApi;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.thread.QueueFxThread;

public class TreatFxThread extends QueueFxThread<OccurAlarm> {

	private static TreatFxThread fxThread;

	public static TreatFxThread getTreatMgr() {
		if (fxThread == null) {
			fxThread = new TreatFxThread();
			fxThread.start();
		}

		return fxThread;
	}

	private Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

	public TreatFxThread() {
		super(0);
		setName(TreatFxThread.class.getSimpleName());
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof ReloadSignal) {
			loadAmGroup();
		}
	}

	private void loadAmGroup() {
		try {
			this.moAmMap = CoApi.getApi().getMoAmMap();
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

		if (e.getTreatName() == null || e.getTreatName().length() == 0) {
			return;
		}

		List<AmGroupVo> groupList = moAmMap.get(e.getMoNo());
		if (groupList == null) {
			groupList = moAmMap.get(e.getUpperMoNo());
		}

		List<TreatFilter> filterList = FxActorParser.getParser().getActorList(TreatFilter.class);
		List<AmHstVo> hstList = new ArrayList<AmHstVo>();
		List<AmHstVo> entry;

		for (TreatFilter filter : filterList) {
			if (filter.getName().equals(e.getTreatName())) {
				entry = filter.treat(e, groupList);
				if (entry != null) {
					hstList.addAll(entry);
				}
			}
		}

		if (hstList.size() > 0) {
			CoApi.getApi().logAmHst(hstList);
		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

}
