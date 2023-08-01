package fxms.bas.poller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.co.CoCode.MO_STATUS;
import fxms.bas.exp.FxNotMatchException;
import fxms.bas.exp.FxTimeoutException;
import fxms.bas.exp.PollingTimeoutException;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PollItem;
import fxms.bas.vo.PollItem.Status;
import fxms.bas.vo.PollMo;
import fxms.bas.vo.PollerStatus;
import fxms.bas.vo.PollingTag;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;

/**
 * PollingPool에서 사용하는 스레드로 실제 폴링 작업을 담당합니다.
 * 
 * @author subkjh
 * 
 * @param <BEAN>
 */
public class Poller extends FxThread {

	/** 폴링할 내용이 있는 큐 */
	private PollerStatus pollerStatus;
	private PollerMgr mgr;

	public Poller() {
	}

	/**
	 * 관리대상의 ONLINE 상태를 기록한다.
	 * 
	 * @param moNo
	 * @param status
	 * @param callback
	 * @param pollMsdate
	 */
	protected List<PsVoRaw> addMoOnlineStatus(Mo mo, MO_STATUS status, List<PsVoRaw> valList) {
		PsVoRaw vo = new PsVoRaw(mo.getMoNo(), PsApi.MO_STATUS_PS_ID, status.getNo());
		if (valList == null) {
			valList = new ArrayList<PsVoRaw>();
		}
		valList.add(vo);
		return valList;
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		PollItem pollingItem;
		PollMo pollingMo;
		long ptime;
		long spentTime;
		PollingTag tag = null;
		long pollMstime;

		pollerStatus.getPollerList().add(this);

		while (isContinue()) {

			counter.setStatus(FXTHREAD_STATUS.Waiting);

			try {
				pollingItem = pollerStatus.getQueue().poll(300, TimeUnit.MILLISECONDS);
				if (pollingItem instanceof PollingTag) {
					tag = (PollingTag) pollingItem;
					tag.setMstimeStart(System.currentTimeMillis());
					Logger.logger.debug(pollingItem + " START");
					continue;
				} else if (pollingItem == null) {
					if (tag != null) {
						tag.setMstimeEnd(System.currentTimeMillis());
						mgr.onPollingStatus(tag);
						tag = null;
					}
					continue;
				}
			} catch (Exception e) {
				if (isContinue() == false)
					break;
				Logger.logger.error(e);
				continue;
			}

			pollingMo = (PollMo) pollingItem;
			ptime = System.currentTimeMillis();
			pollMstime = pollingMo.getMstimePoll();

			counter.setStatus(FXTHREAD_STATUS.Running);

			try {
				pollingMo.setStatus(Status.Processing);
				polling(pollingMo.getMstimePoll(), pollingMo);
				mgr.onPollingCompleted(pollMstime, true, System.currentTimeMillis() - ptime, pollingMo);

			} catch (PollingTimeoutException e) {
				mgr.onPollingCompleted(pollMstime, false, 0, pollingMo);

			} catch (Exception e) {
				Logger.logger.error(e);
				mgr.onPollingCompleted(pollMstime, false, 0, pollingMo);
			}

			spentTime = System.currentTimeMillis() - ptime;

			counter.addOk(spentTime);

			pollerStatus.setMstimeLast(System.currentTimeMillis());

		}

		pollerStatus.getPollerList().remove(this);

	}

	/**
	 * 관리대상 하나씩 데이터를 수집한다.
	 * 
	 * @param pollMsdate
	 * @param po
	 * @throws PollingTimeoutException
	 * @throws Exception
	 */
	protected void polling(long pollMsdate, PollMo pollMo) throws FxTimeoutException, Exception {

		Mo mo = pollMo.getMo();
//		boolean online = true;

		List<FxGetValueAdapter> adapterList = AdapterApi.getApi().getAdapters(FxGetValueAdapter.class, mo);
		PsVoRawList valList = new PsVoRawList("Poller", pollMsdate);
		for (FxGetValueAdapter adapter : adapterList) {
			// adapter가 처리하는 MO인지 확인한다.
			try {
				List<PsVoRaw> list = adapter.getValue(mo);
				if (list != null) {
					valList.add(list);
				}
			} catch (FxNotMatchException e) {
			} catch (FxTimeoutException e) {
//				online = false;
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

//		valList.add(new PsVoRaw(mo.getMoNo(), PsApi.MO_STATUS_PS_ID, online ? MO_STATUS.Online.getNo() : MO_STATUS.Offline.getNo()));

		ValueApi.getApi().addValue(valList, true);
	}

	/**
	 * 
	 * @param mgr
	 */
	public void setMgr(PollerMgr mgr) {
		this.mgr = mgr;
	}

	/**
	 * 
	 * @param pollerStatus
	 */
	public void setPollerStatus(PollerStatus pollerStatus) {
		this.pollerStatus = pollerStatus;
	}

}
