package fxms.bas.fxo.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

public abstract class QueueFxThread<E> extends FxThread {

	private final int MAX_SIZE = 10000;
	protected final LinkedBlockingQueue<E> queue;
	private int waitSeconds = 0;

	public QueueFxThread() {
		this(0);
	}

	public QueueFxThread(int waitSeconds) {
		queue = new LinkedBlockingQueue<E>();
		this.waitSeconds = waitSeconds;
	}

	public void put(E e) {

//		Logger.logger.debug("FXEVENT={}", e);

		try {
			queue.put(e);
		} catch (InterruptedException e1) {
			Logger.logger.error(e1);
		}
	}

	public LinkedBlockingQueue<E> getQueue() {
		return queue;
	}

	public int getSizeInQueue() {
		return queue == null ? -1 : queue.size();
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.getState(level) + ",queue-size=" + queue.size();
	}

	public int getWaitSeconds() {
		return waitSeconds;
	}

	private void onTooMany() {

		FxServiceImpl.logger.fail(Lang.get("너무 많은 자료가 큐에 있어 모두 제거합니다."));

		AlarmApi.getApi().fireAlarm(ServiceApi.getApi().getMyServiceMo(), getName(),
				ALARM_CODE.SERVICE_QUEUE_TOO_MANY.getAlcdNo(), null, "TOO MANY DATAS IN QUEUE", null);

		FxServiceImpl.setError(getName(), "Too Many Datas : " + DateUtil.getDtm());

		FxServiceImpl.logger.info("remove queue datas");

		queue.clear();

	}

	@Override
	protected void doWork() {

		E e;
		long ptime;

		while (isContinue()) {

			getCounter().setStatus(FXTHREAD_STATUS.Waiting);

			try {
				if (waitSeconds <= 0) {
					e = queue.take();
				} else {
					e = queue.poll(waitSeconds, TimeUnit.SECONDS);
					if (e == null) {
						onNoDatas(getCounter().getCntTotal());
						continue;
					}
				}
			} catch (InterruptedException e1) {
				continue;
			}

			if (e != null) {
				try {
					getCounter().setStatus(FXTHREAD_STATUS.Running);
					ptime = System.currentTimeMillis();
					doWork(e);
					getCounter().addOk(System.currentTimeMillis() - ptime);
				} catch (Exception e1) {
					FxServiceImpl.logger.error(e1);
					getCounter().addFail();
				}

				if (queue.size() >= MAX_SIZE) {
					onTooMany();
				}
			}
		}
	}

	protected abstract void doWork(E e) throws Exception;

	/**
	 * 데이터가 일정 시간 안 들어오면 호출 되는 메소드
	 */
	protected abstract void onNoDatas(long index);

}
