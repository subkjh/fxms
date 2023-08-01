package fxms.bas.poller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.ValueApi;
import fxms.bas.cron.Cron;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.FxNotMatchException;
import fxms.bas.exp.FxTimeoutException;
import fxms.bas.exp.PollingTimeoutException;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * 하나의 Adapter를 주기적으로 실생하는 쓰레드
 *
 * @author subkjh
 *
 */
public class AdapterPoller extends FxThread {

	private final FxGetValueAdapter adapter;
	private final Cron cron;
	private long reloadReq = 1;
	private long reloadTime = 0;

	public AdapterPoller(FxGetValueAdapter adapter) {

		this.adapter = adapter;

		this.cron = new Cron();
		try {
			this.cron.setSchedule("period " + this.adapter.getPollCycle());
		} catch (Exception e) {
		}

		setName(this.adapter.getClass().getSimpleName() + "Poller");

	}

	public FxGetValueAdapter getAdapter() {
		return adapter;
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		super.onEvent(noti);

		if (noti instanceof ReloadSignal) {

			ReloadSignal r = (ReloadSignal) noti;

			if (r.getReloadType() == ReloadType.All || r.getReloadType() == ReloadType.Mo) {
				reloadReq = System.currentTimeMillis();
			}

		} else if (noti instanceof FxMo) {

			reloadReq = System.currentTimeMillis();

		}
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		long spentTime;
		long pollMstime;
		List<Mo> moList = null;

		while (isContinue()) {

			counter.setStatus(FXTHREAD_STATUS.Waiting);

			pollMstime = System.currentTimeMillis();

			if (cron.isOnTime(pollMstime) == false) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				continue;
			}

			if (reloadReq > reloadTime) {
				try {
					reloadReq = System.currentTimeMillis() + (5 * 60000L); // 최소 5분 후에 데이터를 다시 읽도록 한다.
					moList = loadMos();
					reloadTime = System.currentTimeMillis();
				} catch (Exception e) {
					Logger.logger.error(e);
					continue;
				}
			}

			Logger.logger.info("start polling... {}, date={}, mo.size={}", this.adapter.getClass().getName(),
					DateUtil.toHstime(pollMstime), moList.size());

			PsVoRawList valList = new PsVoRawList(this.adapter.getClass().getSimpleName(), pollMstime);

			for (Mo mo : moList) {
				try {
					polling(valList, mo);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			if (valList.size() > 0) {
				ValueApi.getApi().addValue(valList, true);
			}

			spentTime = System.currentTimeMillis() - pollMstime;

			counter.addOk(spentTime);

		}

	}

	protected List<Mo> loadMos() throws Exception {
		Map<String, Object> para = new HashMap<>(this.adapter.getMoPara());
		para.put("delYn", "N");
		para.put("mngYn", "Y");
		return MoApi.getApi().getMoList(para);
	}

	/**
	 * 관리대상 하나씩 데이터를 수집한다.
	 *
	 * @param pollMsdate
	 * @param po
	 * @throws PollingTimeoutException
	 * @throws Exception
	 */
	private void polling(PsVoRawList valList, Mo mo) throws FxTimeoutException, Exception {

//		boolean online = true;

		// adapter가 처리하는 MO인지 확인한다.
		try {
			List<PsVoRaw> list = this.adapter.getValue(mo);
			if (list != null) {
				valList.add(list);
			}
		} catch (FxNotMatchException e) {
		} catch (FxTimeoutException e) {
//			online = false;
		} catch (Exception e) {
			Logger.logger.error(e);
		}

//		valList.add(new PsVoRaw(mo.getMoNo(), PsApi.MO_STATUS_PS_ID,
//				online ? MO_STATUS.Online.getNo() : MO_STATUS.Offline.getNo()));

	}

}
