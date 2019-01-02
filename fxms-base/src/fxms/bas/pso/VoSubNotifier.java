package fxms.bas.pso;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.pso.filter.ValuePeeker;
import fxms.bas.pso.filter.ValuePeekerMap;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

public class VoSubNotifier extends QueueFxThread<VoList> {

	private static VoSubNotifier notifier;

	private ValuePeekerMap peekerMap;

	public static VoSubNotifier getVoNotifier() {

		if (notifier == null) {
			notifier = new VoSubNotifier();
			notifier.start();
		}

		return notifier;
	}

	private VoSubNotifier() {
		super(VoSubNotifier.class.getSimpleName());

		peekerMap = new ValuePeekerMap();
	}

	public void setPeeker(long moNo, String moInstance, String psCode, ValuePeeker peeker, boolean add) {
		Logger.logger.info("moNo={}, moInstance={}, psCode={}, peeker={}, add={}", moNo, moInstance, psCode, peeker,
				add);
		if (add) {
			peekerMap.addReq(moNo, moInstance, psCode, peeker);
		} else {
			peekerMap.removeReq(moNo, moInstance, psCode, peeker);
		}
	}

	@Override
	protected void doWork(VoList voList) {

		if (voList == null)
			return;

		Logger.logger.trace("{}", voList);

		List<ValuePeeker> list;
		List<ValuePeeker> toRemoveList = new ArrayList<ValuePeeker>();

		for (PsVo e : voList) {
			list = peekerMap.getPeekerList(e);
			if (list != null) {
				toRemoveList.clear();
				for (ValuePeeker peeker : list) {
					try {
						peeker.onValue(voList.getMstime(), e);
					} catch (Exception e1) {
						Logger.logger.error(e1);
						toRemoveList.add(peeker);
					}
				}

				list.removeAll(toRemoveList);
			}
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();
		sb.append(super.getState(level));

		for (String key : peekerMap.keySet()) {
			sb.append("\n\t");
			sb.append(key);
		}

		return sb.toString();

	}

	@Override
	protected void onNoDatas(long index) {
	}

	@Override
	protected void doInit() {

	}

}
