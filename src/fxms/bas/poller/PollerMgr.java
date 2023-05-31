package fxms.bas.poller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.api.MoApi;
import fxms.bas.event.FxEvent;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.event.TargetFxEvent;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.FxServiceMember;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;
import fxms.bas.signal.MoPollingSecondChangeSignal;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PollItem;
import fxms.bas.vo.PollItem.Status;
import fxms.bas.vo.PollMo;
import fxms.bas.vo.PollerCfg;
import fxms.bas.vo.PollerStatus;
import fxms.bas.vo.PollingTag;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * POLLER MANAGER
 * 
 * @author subkjh
 * 
 * @param <MO>
 */
public class PollerMgr extends FxThread implements FxServiceMember {

	/** 폴링할 MO */
	private final Map<Long, PollMo> moMap;
	/** 변경될 MO */
	private final Map<Long, PollMo> updateMap;
	/** 폴링주기별 정보 */
	private final Map<Integer, PollerStatus> secMap;

	private final PollerCfg pollerCfg;

	/**
	 * 
	 * @param api           API
	 * @param classOfThread Poller 클래스
	 */
	public PollerMgr(PollerCfg pollerCfg) {

		this.pollerCfg = pollerCfg;
		this.moMap = new HashMap<Long, PollMo>();
		this.updateMap = new HashMap<Long, PollMo>();
		this.secMap = new HashMap<Integer, PollerStatus>();

		setName(getClass().getSimpleName());
	}

	private void addMo(PollMo pollMo) {

		// 수행 주기가 같은 폴러를 찾아 배정합니다.
		if (pollMo.getPollCycle() <= 0) {
			pollMo.setPollCycle(pollerCfg.getCyclePollingSeconds());
		}

		pollMo.setNext(System.currentTimeMillis());
		pollMo.setStatus(Status.Waiting);

		PollMo moOld = null;

		synchronized (moMap) {
			moOld = moMap.get(pollMo.getMo().getMoNo());
			moMap.put(pollMo.getMo().getMoNo(), pollMo);
		}

		Logger.logger.info(pollMo + " " + (moOld == null ? "ADDED" : "UPDATED"));
	}

	/**
	 * 폴링에 사용되는 스레드를 실행합니다.
	 */
	private void createPoller(PollerStatus pollerStatus, int max) {

		int sizeNow = pollerStatus.getPollerList().size();

		if (sizeNow >= max)
			return;

		if (max + 1 > pollerCfg.getCountThreadMax()) {
			max = pollerCfg.getCountThreadMax();
		}

		Poller th;
		StringBuffer sb = new StringBuffer();

		for (int i = sizeNow + 1; i <= max; i++) {
			try {
				th = new Poller();
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

	private List<PollMo> getMoList() {
		synchronized (moMap) {
			List<PollMo> list = new ArrayList<PollMo>();
			for (PollMo mo : moMap.values()) {
				list.add(mo);
			}
			return list;
		}
	}

	/**
	 * 폴링주기에 해당되는 큐를 제곻합니다.
	 * 
	 * @param secPolling seconds
	 * @return 폴링주기용 큐
	 */
	private PollerStatus getPollerStatus(int secPolling) {

		PollerStatus data = secMap.get(secPolling);
		if (data == null) {
			data = new PollerStatus(secPolling, new LinkedBlockingQueue<PollItem>());
			secMap.put(secPolling, data);
		}

		return data;
	}

	/**
	 * 적정한 스레드를 생성합니다.
	 * 
	 * @param status POLLER-DATA
	 * @param tag    POLLING-START
	 */
	private void makeSuitableThread(PollerStatus status, PollingTag tag) {

		int cycle = status.getSecPolling();

		if (pollerCfg.getSpp() > 0)
			cycle /= pollerCfg.getSpp();

		float size = (1f * tag.getSize()) / (cycle * (pollerCfg.getLimitRate() / 100F));

		int n;
		if (size < pollerCfg.getCountThreadMin()) {
			n = pollerCfg.getCountThreadMin();
		} else {
			n = (size % 1 == 0 ? (int) (size + 1) : (int) size);
		}

		if (n < pollerCfg.getCountThreadMin())
			n = pollerCfg.getCountThreadMin();

		createPoller(status, n);
	}

	private void removeMo(long moNo, String reason) {

		PollMo moRemoved = null;

		synchronized (moMap) {
			moRemoved = moMap.remove(moNo);
		}

		if (reason != null)
			Logger.logger.debug("moNo={} {}. {}", moNo, moRemoved != null ? "removed" : "not found", reason);

		return;
	}

	private void setMoListAll() {

		List<Mo> list = loadMoList(-1);

		if (list == null) {
			return;
		}

		synchronized (moMap) {
			moMap.clear();
		}

		// 관리대상에 폴링 주기가 설정되어 있으면 추가한다.
		// 폴링 주기가 0이하이면 폴링 대상이 아니다.
		int count = 0;
		for (Mo mo : list) {
			addMo(new PollMo(mo));
			count++;
		}

		Logger.logger.info("MO-SIZE({}) ADDED", count);

	}

	/**
	 * 폴링이 끝나면 호출됩니다.
	 * 
	 * @param pollMo 폴링이 끝난 MO
	 */
	private void toQueue(PollMo pollMo) {

		PollMo moNew = updateMap.remove(pollMo.getMoNo());

		// 수정할 내용에 존재하다는 의미는 삭제할 내용은 제거한 후 추가하기 위한 것입니다.
		if (moNew != null) {

			moNew.setMo(pollMo.getMo());

			addMo(moNew);

		} else {
			pollMo.setNext(System.currentTimeMillis());
			pollMo.setStatus(Status.Waiting);
		}
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		List<PollingTag> secList = new ArrayList<PollingTag>();
		List<PollMo> moList = new ArrayList<PollMo>();
		List<PollMo> poList;
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

				O: for (PollMo pollerMo : moMap.values()) {

					if (pollerMo.onTime(mstime) && pollerMo.getStatus() == Status.Waiting) {

						moList.add(pollerMo);

						pollerMo.addReq();

						for (PollingTag sec : secList) {
							if (sec.getPollCycle() == pollerMo.getPollCycle()) {
								sec.setSize(sec.getSize() + 1);
								continue O;
							}
						}

						item = new PollingTag(pollerMo.getPollCycle());
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
							pollerStatus = getPollerStatus(sec.getPollCycle());
							pollerStatus.addCount();
							sec.setIndex(pollerStatus.getCount());

							makeSuitableThread(pollerStatus, sec);

							Logger.logger.debug(sec + " START");
							pollerStatus.getQueue().put(sec);

						} catch (InterruptedException e) {
						}
					}

					// 실제 데이터 넣음
					for (PollMo pollerMo : poList) {
						pollerStatus = getPollerStatus(pollerMo.getPollCycle());
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

	/**
	 * 관리대상 검색 조건을 제공한다.<br>
	 * 기본 조건으로 아래가 포함된다.<br>
	 * mngYn : 관리여부<br>
	 * fxsvrIpAddr : 관리서버IP주소<br>
	 * 
	 * @return 관리대상 검색조건
	 */
	protected Map<String, Object> getMoPara() {

		Map<String, Object> para = new HashMap<String, Object>();

		return para;
	}

	/**
	 * 수집할 관리대상을 가져온다.
	 * 
	 * @param classOfMo 해당 관리대상 클래스
	 * @param moNo      관리대상 관리번호<br>
	 *                  0보다 작으면 전체를 의미한다.
	 * @return
	 */
	protected List<Mo> loadMoList(long moNo) {

		Map<String, Object> para = getMoPara();

		if (moNo > -1) {
			para.put("moNo", moNo);
		}

		// 폴링이 설정되어 있는 경우
		para.put("pollCycle >", 0);
		para.put("delYn", "N");
		para.put("mngYn", "Y");

		List<Mo> list;
		try {
			list = MoApi.getApi().getMoList(para);
		} catch (Exception e) {
			Logger.logger.error(e);
			list = null;
		}
		return list;
	}

	/**
	 * 입력된 MO를 실제 폴링한 객체로 넘김
	 * 
	 * @param moList MO-LIST
	 * @return
	 */
	protected List<PollMo> makePOList(List<PollMo> moList) {
		List<PollMo> ret = new ArrayList<PollMo>();
		for (PollMo mo : moList) {
			ret.add(mo);
		}
		return ret;
	}

	/**
	 * 폴링이 끝났을때 호출됩니다.
	 * 
	 * @param mo        폴링이 끝난 MO
	 * @param valueList 수집한 값
	 */
	void onPollingCompleted(long pollMstime, boolean success, long spentTime, PollMo... pollMos) {

		if (success) {
			for (PollMo mo : pollMos) {
				mo.addTime(spentTime);
			}
		}

		for (PollMo mo : pollMos) {
			FxServiceImpl.fxService.sendEvent(new TargetFxEvent("polling-completed", String.valueOf(mo.getMoNo())),
					true, true);
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

	/**
	 * 입력된 MO를 조회하여 폴링에 추가, 수정, 삭제를 수행합니다.
	 * 
	 * @param mo         처리할 MO
	 * @param secPolling 강제로 설정할 수집 주기
	 */
	public void checkMo(long moNo, boolean deleted, int secPolling) {

		Logger.logger.debug("moNo={} to check", moNo);

		if (deleted) {
			removeMo(moNo, "deleted");
			return;
		}

		// 비관리전화으로 없을 경우도 있으므로 새롭게 읽어오지 못했는데
		// 현재 있다면 현재 내용을 제거합니다.
		List<Mo> moNewList = loadMoList(moNo);

		if (moNewList == null || moNewList.size() == 0) {
			removeMo(moNo, "no new");
			return;
		}

		// 새롭게 얻은 MO에 이번에 변경된 MO가 없으면 이전 MO를 제거합니다.
		boolean isExist = false;
		for (Mo po : moNewList) {
			if (moNo == po.getMoNo()) {
				isExist = true;
				break;
			}
		}

		if (isExist == false) {
			removeMo(moNo, null);
		}

		PollMo pmo;

		for (Mo po : moNewList) {

			pmo = new PollMo(po);

			if (secPolling > 0) {
				pmo.setPollCycle(secPolling);
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
	 * @param moNo 찾을 대상 관리번호
	 * @return 관리대상
	 */
	public PollMo getMo(long moNo) {
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
			List<PollMo> moList = getMoList();

			Collections.sort(moList, new Comparator<PollMo>() {
				public int compare(PollMo mo1, PollMo mo2) {
					return (int) (mo1.getMoNo() - mo2.getMoNo());
				}

			});

			for (PollMo mo : moList) {
				sb.append("\n\t");
				sb.append(index);
				sb.append(")\tmo-no=");
				sb.append(mo.getMo().getMoNo());
				sb.append(",cycle=");
				sb.append(mo.getPollCycle());
				sb.append(",");
				sb.append(mo.getCount());
				index++;
			}
		}

		return sb.toString();
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		super.onEvent(noti);

		if (noti instanceof ReloadSignal) {

			ReloadSignal r = (ReloadSignal) noti;

			if (r.getReloadType() == ReloadType.All || r.getReloadType() == ReloadType.Mo) {
				setMoListAll();
			}

		} else if (noti instanceof FxMo) {

			FxMo mo = (FxMo) noti;
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

	@Override
	public void startMember() throws Exception {
		start();
	}

}
