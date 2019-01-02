package fxms.bas.poller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.bas.mo.Mo;
import fxms.bas.noti.FxEvent;
import fxms.bas.poller.beans.PollerStatus;
import fxms.bas.poller.beans.PollingItem;
import fxms.bas.poller.beans.PollingItem.Status;
import fxms.bas.poller.beans.PollingMo;
import fxms.bas.poller.beans.PollingTag;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import fxms.bas.pso.ValueApi;
import fxms.bas.pso.VoList;
import subkjh.bas.log.Logger;

/**
 * PollingPool에서 사용하는 스레드로 실제 폴링 작업을 담당합니다.
 * 
 * @author subkjh
 * 
 * @param <BEAN>
 */
public abstract class Poller<MO extends Mo> extends FxThread implements PollCallback {

	/** 폴링할 내용이 있는 큐 */
	private PollerStatus pollerStatus;
	private PollerMgr<MO> mgr;
	private boolean groupProcess = false;

	/**
	 * 상태 조회에 추가합니다.
	 */
	public Poller(String name, boolean groupProcess) {
		super(name);
		this.groupProcess = groupProcess;
	}

	@Override
	protected void doWork() {

		PollingItem pollingItem;
		PollingMo pollingMo;
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

			pollingMo = (PollingMo) pollingItem;
			ptime = System.currentTimeMillis();
			pollMstime = pollingMo.getMstimePoll();

			counter.setStatus(FXTHREAD_STATUS.Running);

			if (groupProcess == false) {
				try {
					pollingMo.setStatus(Status.Processing);
					polling(pollingMo.getMstimePoll(), pollingMo, this);
					mgr.onPollingCompleted(pollMstime, true, System.currentTimeMillis() - ptime, pollingMo);
				} catch (PollingTimeoutException e) {
					mgr.onPollingCompleted(pollMstime, false, 0, pollingMo);
				} catch (Exception e) {
					Logger.logger.error(e);
					mgr.onPollingCompleted(pollMstime, false, 0, pollingMo);
				}
			} else {

				List<PollingMo> moList = new ArrayList<PollingMo>();
				moList.add(pollingMo);
				pollingMo.setStatus(Status.Processing);

				while (true) {
					pollingItem = pollerStatus.getQueue().poll();
					if (pollingItem == null) {
						break;
					} else if (pollingItem instanceof PollingMo) {
						((PollingMo) pollingItem).setStatus(Status.Processing);
						moList.add((PollingMo) pollingItem);
					}
				}

				try {
					pollingGroup(pollingMo.getMstimePoll(), moList, this);
					mgr.onPollingCompleted(pollMstime, true, System.currentTimeMillis() - ptime,
							moList.toArray(new PollingMo[moList.size()]));
				} catch (PollingTimeoutException e) {
					mgr.onPollingCompleted(pollMstime, false, 0, moList.toArray(new PollingMo[moList.size()]));
				} catch (Exception e) {
					Logger.logger.error(e);
					mgr.onPollingCompleted(pollMstime, false, 0, moList.toArray(new PollingMo[moList.size()]));
				}
			}

			spentTime = System.currentTimeMillis() - ptime;

			counter.addOk(spentTime);

			pollerStatus.setMstimeLast(System.currentTimeMillis());

		}

		pollerStatus.getPollerList().remove(this);

	}

	protected abstract void polling(long pollMsdate, PollingMo po, PollCallback callback)
			throws PollingTimeoutException, Exception;

	protected abstract void pollingGroup(long pollMsdate, List<PollingMo> moList, PollCallback callback)
			throws PollingTimeoutException, Exception;

	public void setPollerStatus(PollerStatus pollerStatus) {
		this.pollerStatus = pollerStatus;
	}

	public void setMgr(PollerMgr<MO> mgr) {
		this.mgr = mgr;
	}

	@Override
	public void onValue(String owner, long pollMsdate, List<PsVo> valueList, FxEvent sign) {
		if (valueList != null && valueList.size() > 0) {
			ValueApi.getApi().addValue(new VoList(owner, pollMsdate, valueList, sign), true);
		}
	}

}
