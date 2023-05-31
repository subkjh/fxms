package fxms.bas.thread;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxService;
import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * NotiService에 등록된 서비스에게 Noti를 보내는 스레드<br>
 * 같은 서비스가 재등록되면 NotiService는 이 스레드는 종료시킨다.
 * 
 * @author SUBKJH
 *
 */
public class NotiPeerThread extends QueueFxThread<FxEvent> {

	private NotiFilter filter;
	private FxService service;
	private String fxServiceId;
	private FxEvent lastNoti;
	private long lastCallMstime;

	/** 더이상 관련 서비스에 노티를 보낼 수 없는지 여부 */
	private boolean cantSend = false;

	public NotiPeerThread(String fxServiceId, FxService service, NotiFilter filter) {
		super(FxCfg.ALIVE_SIGNAL_CYCLE);
		setName("::" + fxServiceId);
		this.service = service;
		this.filter = filter;
		this.fxServiceId = fxServiceId;
	}

	public String getFxServiceId() {
		return fxServiceId;
	}

	/**
	 * 
	 * @return 보내지 못한 노티 목록
	 */
	public List<FxEvent> getNotiList() {

		lastCallMstime = System.currentTimeMillis();

		if (cantSend == false) {
			return null;
		}

		List<FxEvent> notiList = new ArrayList<FxEvent>();
		if (lastNoti != null) {
			notiList.add(lastNoti);
			lastNoti = null;
		}

		FxEvent noti;
		while (true) {
			noti = getQueue().poll();
			if (noti == null)
				break;
			notiList.add(noti);
		}

		return notiList;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.getState(level) + (cantSend ? " CANT-SEND" : "");
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(FxEvent noti) throws Exception {

		// 필터된 경우 보내지 않는다.
		if (filter.isNoti(noti) == false) {
			Logger.logger.trace("{} filtered", noti);
			return;
		}

		try {
			lastNoti = noti;
			service.onNotify(noti);
			Logger.logger.debug("send {}", noti);
			return;
		} catch (Exception e) {
			Logger.logger.error(e);
			cantSend = true;

			Logger.logger.info(Logger.makeString(getName(), "CANT-SEND"));

			lastCallMstime = System.currentTimeMillis();

			// 상대가 아무런 호출이 없지 않은한 자체적으로 종료하지는 않는다.
			while (isContinue()) {
				Thread.sleep(1000);
				if (lastCallMstime + (getWaitSeconds() * 2000L) < System.currentTimeMillis()) {
					this.stop("disconnected");
					break;
				}
			}
		}

	}

	@Override
	protected boolean isAddToService() {

		// 이 스레드는 이벤트를 받지 않으므로 false로 리턴한다.
		return false;
	}

	@Override
	protected void onNoDatas(long index) {
	}

}
