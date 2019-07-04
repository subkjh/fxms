package fxms.bas.poller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import fxms.bas.api.PsApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.FxEvent.STATUS;
import fxms.bas.co.noti.TargetFxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.bas.mo.Mo;
import fxms.bas.poller.beans.PollerStatus;
import fxms.bas.poller.beans.PollingItem;
import fxms.bas.poller.beans.PollingItem.Status;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.beans.PollingTag;
import fxms.bas.poller.signal.MoPollingSecondChangeSignal;

/**
 * POLLER MANAGER
 * 
 * @author subkjh
 * 
 * @param <MO>
 */
public class PollerMgr<MO extends Mo> extends FxThread implements FxServiceMember {

	/** 폴러 API */
	private PsApi api;
	/** 폴링할 MO */
	private Map<Long, PollingMo> moMap;
	/** 변경될 MO */
	private Map<Long, PollingMo> updateMap;
	/** 폴링주기별 정보 */
	private Map<Integer, PollerStatus> secMap;
	private Class<? extends Poller<MO>> classOfThread;
	private Class<MO> classOfMo;

	/**
	 * 
	 * @param api
	 *            API
	 * @param classOfThread
	 *            Poller 클래스
	 */
	public PollerMgr(Class<MO> classOfMo, Class<? extends Poller<MO>> classOfThread) {

		this.api = PsApi.getApi();
		this.classOfMo = classOfMo;
		this.classOfThread = classOfThread;

		moMap = new HashMap<Long, PollingMo>();
		updateMap = new HashMap<Long, PollingMo>();
		secMap = new HashMap<Integer, PollerStatus>();
	}

	public PsApi getPsApi() {
		return api;
	}

	/**
	 * 입력된 MO를 조회하여 폴링에 추가, 수정, 삭제를 수행합니다.
	 * 
	 * @param mo
	 *            처리할 MO
	 * @param secPolling
	 *            강제로 설정할 수집 주기
	 */
	public void checkMo(long moNo, boolean deleted, int secPolling) {

		Logger.logger.debug("moNo={} to check", moNo);

		if (deleted) {
			removeMo(moNo, "deleted");
			return;
		}

		// 비관리전화으로 없을 경우도 있으므로 새롭게 읽어오지 못했는데
		// 현재 있다면 현재 내용을 제거합니다.
		Map<String, Object> para = getParameters();
		List<MO> moNewList = loadMoList(classOfMo, moNo, para);

		if (moNewList == null || moNewList.size() == 0) {
			removeMo(moNo, "no new");
			return;
		}

		// 새롭게 얻은 MO에 이번에 변경된 MO가 없으면 이전 MO를 제거합니다.
		boolean isExist = false;
		for (MO po : moNewList) {
			if (moNo == po.getMoNo()) {
				isExist = true;
				break;
			}
		}

		if (isExist == false) {
			removeMo(moNo, null);
		}

		PollingMo pmo;

		for (MO po : moNewList) {

			pmo = new PollingMo(po);

			if (secPolling > 0) {
				pmo.setPollingCycle(secPolling);
			}

			if (exist(po.getMoNo())) {
				updateMap.put(po.getMoNo(), pmo);
			} else {
				addMo(pmo);
			}
		}
	}

	/**
	 * 수집대상에서 입력된 관리번호를 찾아 제공합니다.
	 * 
	 * @param moNo
	 *            찾을 대상 관리번호
	 * @return 관리대상
	 */
	public PollingMo getMo(long moNo) {
		synchronized (moMap) {
			return moMap.get(moNo);
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();

		sb.append(counter.toString());
		sb.append(",mo-size=" + moMap.size());
		sb.append(",mo-size-update=" + updateMap.size());
		// sb.append(T);

		Integer secArr[] = secMap.keySet().toArray(new Integer[secMap.size()]);
		for (int sec : secArr) {
			sb.append("\n\t" + sec + " seconds " + secMap.get(sec));
		}

		if (level.contains(LOG_LEVEL.trace)) {
			int index = 1;
			List<PollingMo> moList = getMoList();

			Collections.sort(moList, new Comparator<PollingMo>() {
				public int compare(PollingMo mo1, PollingMo mo2) {
					return (int) (mo1.getMoNo() - mo2.getMoNo());
				}

			});

			for (PollingMo mo : moList) {
				sb.append("\n\t");
				sb.append(index);
				sb.append(")\tmo-no=");
				sb.append(mo.getMo().getMoNo());
				sb.append(",cycle=");
				sb.append(mo.getPollingCycle());
				sb.append(",");
				sb.append(mo.getCount());
				index++;
			}
		}

		return sb.toString();
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof ReloadSignal) {

			ReloadSignal r = (ReloadSignal) noti;

			if (r.contains(ReloadSignal.RELOAD_TYPE_ALL, ReloadSignal.RELOAD_TYPE_MO)) {
				setMoListAll();
			}

		} else if (noti instanceof Mo) {

			Mo mo = (Mo) noti;
			checkMo(mo.getMoNo(), mo.getStatus() == STATUS.deleted, 0);

		} else if (noti instanceof MoPollingSecondChangeSignal) {

			MoPollingSecondChangeSignal chg = (MoPollingSecondChangeSignal) noti;
			if (chg.getMoNo() > 0) {
				if (chg.getSecPolling() > 0) {
					checkMo(chg.getMoNo(), false, chg.getSecPolling());
				} else {
					checkMo(chg.getMoNo(), false, 0);
				}
			}

		}
	}

	private void addMo(PollingMo pollingMo) {

		// 수행 주기가 같은 폴러를 찾아 배정합니다.
		if (pollingMo.getPollingCycle() <= 0) {
			pollingMo.setPollingCycle(api.getPollerCfg().getCyclePollingSeconds());
		}

		pollingMo.setNext(System.currentTimeMillis());
		pollingMo.setStatus(Status.Waiting);

		PollingMo moOld = null;

		synchronized (moMap) {
			moOld = moMap.get(pollingMo.getMo().getMoNo());
			moMap.put(pollingMo.getMo().getMoNo(), pollingMo);
		}

		Logger.logger.info(pollingMo + " " + (moOld == null ? "ADDED" : "UPDATED"));
	}

	/**
	 * 폴링에 사용되는 스레드를 실행합니다.
	 */
	private void createThread(PollerStatus pollerStatus, int max) {

		int sizeNow = pollerStatus.getPollerList().size();

		if (sizeNow >= max)
			return;

		if (max + 1 > api.getPollerCfg().getCountThreadMax()) {
			max = api.getPollerCfg().getCountThreadMax();
		}

		Poller<MO> th;
		StringBuffer sb = new StringBuffer();

		for (int i = sizeNow + 1; i <= max; i++) {
			try {
				th = (Poller<MO>) classOfThread.newInstance();
			} catch (Exception e) {
				Logger.logger.error(e);
				return;
			}

			th.setName(getName() + "-" + pollerStatus.getSecPolling() + "-" + String.format("%03d", i));
			th.setPollerStatus(pollerStatus);
			th.setMgr(this);
			th.start();

			sb.append(Logger.makeSubString(0, th.getName(), "started"));
		}

		Logger.logger.info(Logger.makeString("Poller", getClass().getSimpleName(), sb.toString()));

	}

	/**
	 * MO가 현재 관리에 포하되어 있는지 확인합니다.
	 * 
	 * @param mo
	 * @return true이면 폴링대상이고 false이면 없음
	 */
	private boolean exist(long moNo) {
		synchronized (moMap) {
			return moMap.get(moNo) != null;
		}
	}

	/**
	 * 폴링주기에 해당되는 큐를 제곻합니다.
	 * 
	 * @param secPolling
	 *            seconds
	 * @return 폴링주기용 큐
	 */
	private PollerStatus getPollerStatus(int secPolling) {

		PollerStatus data = secMap.get(secPolling);
		if (data == null) {
			data = new PollerStatus(secPolling, new LinkedBlockingQueue<PollingItem>());
			secMap.put(secPolling, data);
		}

		return data;
	}

	private List<PollingMo> getMoList() {
		synchronized (moMap) {
			List<PollingMo> list = new ArrayList<PollingMo>();
			for (PollingMo mo : moMap.values()) {
				list.add(mo);
			}
			return list;
		}
	}

	/**
	 * 적정한 스레드를 생성합니다.
	 * 
	 * @param status
	 *            POLLER-DATA
	 * @param tag
	 *            POLLING-START
	 */
	private void makeSuitableThread(PollerStatus status, PollingTag tag) {

		int cycle = status.getSecPolling();

		if (api.getPollerCfg().getSpp() > 0)
			cycle /= api.getPollerCfg().getSpp();

		float size = (1f * tag.getSize()) / (cycle * (api.getPollerCfg().getLimitRate() / 100F));

		int n;
		if (size < api.getPollerCfg().getCountThreadMin()) {
			n = api.getPollerCfg().getCountThreadMin();
		} else {
			n = (size % 1 == 0 ? (int) (size + 1) : (int) size);
		}

		if (n < api.getPollerCfg().getCountThreadMin())
			n = api.getPollerCfg().getCountThreadMin();

		createThread(status, n);
	}

	private void removeMo(long moNo, String reason) {

		PollingMo moRemoved = null;

		synchronized (moMap) {
			moRemoved = moMap.remove(moNo);
		}

		if (reason != null)
			Logger.logger.debug("moNo={} {}. {}", moNo, moRemoved != null ? "removed" : "not found", reason);

		return;
	}

	private void setMoListAll() {

		List<MO> list = loadMoList(classOfMo, -1, getParameters());

		if (list == null) {
			return;
		}

		synchronized (moMap) {
			moMap.clear();
		}

		for (MO mo : list) {
			addMo(new PollingMo(mo));
		}

		Logger.logger.info("MO-SIZE(" + list.size() + ") ADDED");

	}

	/**
	 * 폴링이 끝나면 호출됩니다.
	 * 
	 * @param pollingMo
	 *            폴링이 끝난 MO
	 */
	private void toQueue(PollingMo pollingMo) {

		PollingMo moNew = updateMap.remove(pollingMo.getMoNo());

		// 수정할 내용에 존재하다는 의미는 삭제할 내용은 제거한 후 추가하기 위한 것입니다.
		if (moNew != null) {

			moNew.setMo(pollingMo.getMo());

			addMo(moNew);

		} else {
			pollingMo.setNext(System.currentTimeMillis());
			pollingMo.setStatus(Status.Waiting);
		}
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		List<PollingTag> secList = new ArrayList<PollingTag>();
		List<PollingMo> moList = new ArrayList<PollingMo>();
		List<PollingMo> poList;
		PollingTag item;
		long mstime;
		PollerStatus pollerStatus;

		while (isContinue()) {

			if (moMap.size() == 0) {
				// 자료가 없으면 5초 후에 다시 조회합니다.
				try {
					Thread.sleep(5000);
					setMoListAll();
				} catch (InterruptedException e) {
				}
			}

			secList.clear();
			moList.clear();

			mstime = System.currentTimeMillis();

			counter.setStatus(FXTHREAD_STATUS.Running);

			synchronized (moMap) {

				O: for (PollingMo pollerMo : moMap.values()) {

					if (pollerMo.onTime(mstime) && pollerMo.getStatus() == Status.Waiting) {

						moList.add(pollerMo);

						pollerMo.addReq();

						for (PollingTag sec : secList) {
							if (sec.getPollingCycle() == pollerMo.getPollingCycle()) {
								sec.setSize(sec.getSize() + 1);
								continue O;
							}
						}

						item = new PollingTag(pollerMo.getPollingCycle());
						item.setMstime(mstime);
						item.setSize(1);
						secList.add(item);
					}
				}
			}

			if (moList.size() > 0) {

				poList = makePOList(moList);

				if (poList != null && poList.size() > 0) {

					// 시작함을 알림
					for (PollingTag sec : secList) {
						try {
							pollerStatus = getPollerStatus(sec.getPollingCycle());
							pollerStatus.addCount();
							sec.setIndex(pollerStatus.getCount());

							makeSuitableThread(pollerStatus, sec);

							Logger.logger.debug(sec + " START");
							pollerStatus.getQueue().put(sec);

						} catch (InterruptedException e) {
						}
					}

					// 실제 데이터 넣음
					for (PollingMo pollerMo : poList) {
						pollerStatus = getPollerStatus(pollerMo.getPollingCycle());
						pollerMo.setStatus(Status.InQueue);
						try {
							pollerStatus.getQueue().put(pollerMo);
						} catch (InterruptedException e) {
						}
					}

				}
			}

			counter.setStatus(FXTHREAD_STATUS.Waiting);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

		}

	}

	protected Map<String, Object> getParameters() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("mngYn", "Y");

		String msIps = PsApi.getApi().getPsMsIpaddr().getSqlString();
		if (msIps != null && msIps.length() > 0) {
			para.put("msIpaddr", msIps);
		}

		return para;
	}

	protected List<MO> loadMoList(Class<MO> classOfMo, long moNo, Map<String, Object> parameters) {
		return api.getMoAll(classOfMo, parameters);
	}

	/**
	 * 입력된 MO를 실제 폴링한 객체로 넘김
	 * 
	 * @param moList
	 *            MO-LIST
	 * @return
	 */
	protected List<PollingMo> makePOList(List<PollingMo> moList) {
		List<PollingMo> ret = new ArrayList<PollingMo>();
		for (PollingMo mo : moList) {
			ret.add(mo);
		}
		return ret;
	}

	/**
	 * 폴링이 끝났을때 호출됩니다.
	 * 
	 * @param mo
	 *            폴링이 끝난 MO
	 * @param valueList
	 *            수집한 값
	 */
	void onPollingCompleted(long pollMstime, boolean success, long spentTime, PollingMo... pollingMos) {

		if (success) {
			for (PollingMo mo : pollingMos) {
				mo.addTime(spentTime);
			}
		}

		for (PollingMo mo : pollingMos) {
			FxServiceImpl.fxService.send(new TargetFxEvent("polling-completed", String.valueOf(mo.getMoNo())));
			toQueue(mo);
		}
	}

	/**
	 * POLLER가 현재 처리할 내용이 없을 때 호출됨
	 * 
	 * @param ps
	 */
	void onPollingStatus(PollingTag ps) {

		Logger.logger.debug(String.valueOf(ps));

		if (ps.getMstimeEnd() >= ps.getMstimeStart()) {
			counter.addOk(ps.getMstimeEnd() - ps.getMstimeStart());
		}

	}

	@Override
	public void startMember() throws Exception {
		start();
	}

}
