package fxms.bas.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fxms.bas.event.FxEvent;
import fxms.bas.event.PsVoListEvent;
import fxms.bas.fxo.filter.PsValuePeeker;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 수집한 값을 방송하는 스레드
 * 
 * @author subkjh
 *
 */
public class PsValueNotifyThread extends QueueFxThread<PsVoList> {

	class ValuePeekerMap extends HashMap<String, List<PsValuePeeker>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2789183549873805265L;

		public ValuePeekerMap() {
		}

		private void addReq(long moNo, String psId, PsValuePeeker peeker) {

			String key = makeKey(moNo, psId);

			List<PsValuePeeker> list = get(key);
			if (list == null) {
				list = new ArrayList<PsValuePeeker>();
				Logger.logger.debug("key=[] added", key);
				put(key, list);
			}

			list.add(peeker);
		}

		private List<PsValuePeeker> getPeekerList(PsVo vo) {

			String req = makeKey(vo.getMo().getMoNo(), vo.getPsItem().getPsId());

			return get(req);
		}

		private String makeKey(long moNo, String psId) {

			StringBuffer sb = new StringBuffer();

			sb.append(moNo);
			sb.append("/");
			sb.append(psId);

			return sb.toString();
		}

		private void removeReq(long moNo, String psId, PsValuePeeker peeker) {

			String key = makeKey(moNo, psId);

			List<PsValuePeeker> list = get(key);
			if (list != null) {
				list.remove(peeker);
				if (list.size() == 0) {
					remove(key);
					Logger.logger.debug("key=[] removed", key);
				}
			}

		}

	}

	private static PsValueNotifyThread notifier;

	public static PsValueNotifyThread getVoNotifier() {

		if (notifier == null) {
			notifier = new PsValueNotifyThread();
			notifier.start();
		}

		return notifier;
	}

	private ValuePeekerMap peekerMap;

	private PsValueNotifyThread() {
		setName(PsValueNotifyThread.class.getSimpleName());
		peekerMap = new ValuePeekerMap();
	}

	@Override
	protected void doInit() {

	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		super.onEvent(noti);

		// 수집값인 경우
		if (noti instanceof PsVoListEvent) {
			put(((PsVoListEvent) noti).getVoList());
		}

	}

	@Override
	protected void doWork(PsVoList voList) {

		// 수집된 값이 들어온다.
		if (voList == null)
			return;

		Logger.logger.trace("{}", voList);

		List<PsValuePeeker> list;
		List<PsValuePeeker> toRemoveList = new ArrayList<PsValuePeeker>();

		for (PsVo e : voList) {
			list = peekerMap.getPeekerList(e);
			if (list != null) {
				toRemoveList.clear();
				for (PsValuePeeker peeker : list) {
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
	protected void onNoDatas(long index) {
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

	/**
	 * 피커를 추가, 삭제한다.
	 * 
	 * @param moNo   관리대상번호
	 * @param psId   성능ID
	 * @param peeker 넘기는 곳
	 * @param add    추가, 삭제
	 */
	public void setPeeker(long moNo, String psId, PsValuePeeker peeker, boolean add) {
		Logger.logger.info("moNo={}, psId={}, peeker={}, add={}", moNo, psId, peeker.getName(), add);
		if (add) {
			peekerMap.addReq(moNo, psId, peeker);
		} else {
			peekerMap.removeReq(moNo, psId, peeker);
		}
	}
}
