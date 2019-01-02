package fxms.bas.cron;

import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.log.Logger;

public class CronSubFxThread extends QueueFxThread<Crontab> {

	private CronListener listener;

	public void setListener(CronListener listener) {
		this.listener = listener;
	}

	public CronSubFxThread(String name) {
		super(name, 0);
	}

	@Override
	protected void doWork(Crontab e) throws Exception {

		String name = Thread.currentThread().getName();
		if (e.getName() != null) {
			Thread.currentThread().setName(e.getName());
		}

		Logger.logger.info(e.getName());

		long ptime = System.currentTimeMillis();

		try {
			if (listener != null)
				listener.onStart(e);

			e.cron();

			if (listener != null)
				listener.onFinished(e, true, System.currentTimeMillis() - ptime);
		} catch (Exception e2) {
			if (listener != null)
				listener.onFinished(e, false, System.currentTimeMillis() - ptime);
			throw e2;
		} finally {
			Thread.currentThread().setName(name);
		}

	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void onNoDatas(long index) {

	}

}
