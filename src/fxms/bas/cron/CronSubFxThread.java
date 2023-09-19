package fxms.bas.cron;

import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.co.lang.Lang;
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
	public boolean put(Crontab cron) {
		
		if (this.queue.contains(cron)) {
			Logger.logger.debug("{}", Lang.get("The same Cron job exists."));
			return false;
		}

		return super.put(cron);
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(Crontab e) throws Exception {

		String backupName = Thread.currentThread().getName();
		if (e.getName() != null) {
			Thread.currentThread().setName(backupName + "." + e.getName());
		}

		long ptime = System.currentTimeMillis();

		long cronRunNo = listener.onStart(e);

		Logger.logger.info("Cron : {}.{}", cronRunNo, e.getName());

		try {
			e.start();
			listener.onFinished(cronRunNo, e, null, (int) (System.currentTimeMillis() - ptime));
		} catch (Exception e2) {
			listener.onFinished(cronRunNo, e, e2, (int) (System.currentTimeMillis() - ptime));
			throw e2;
		} finally {
			Thread.currentThread().setName(backupName);
		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

}
