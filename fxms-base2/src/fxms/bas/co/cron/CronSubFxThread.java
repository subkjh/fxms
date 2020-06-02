package fxms.bas.co.cron;

import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.co.log.Logger;

/**
 * CronFxThread의 하위 스레드
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class CronSubFxThread extends QueueFxThread<Crontab> {

	private CronFxThread listener;

	CronSubFxThread(String name, CronFxThread listener) {
		super(0);
		setName(name);
		this.listener = listener;
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(Crontab e) throws Exception {

		String name = Thread.currentThread().getName();
		if (e.getName() != null) {
			Thread.currentThread().setName(name + "." + e.getName());
		}

		Logger.logger.info(e.getName());

		long ptime = System.currentTimeMillis();

		listener.onStart(e);

		try {
			e.cron();
			listener.onFinished(e, true, System.currentTimeMillis() - ptime);
		} catch (Exception e2) {
			listener.onFinished(e, false, System.currentTimeMillis() - ptime);
			throw e2;
		} finally {
			Thread.currentThread().setName(name);
		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

}
