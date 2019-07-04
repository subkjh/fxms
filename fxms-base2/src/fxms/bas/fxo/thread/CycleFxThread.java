package fxms.bas.fxo.thread;

import fxms.bas.api.FxApi;
import fxms.bas.co.cron.Cron;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public abstract class CycleFxThread extends FxThread {

	public static void main(String[] args) throws Exception {
		CycleFxThread trot = new CycleFxThread("TEST", Cron.CYCLE_1_SECOND) {

			@Override
			protected void doCycle(long mstime) {
				System.out.println(FxApi.getDate(0));
				System.out.println(mstime);

			}

		};

		trot.setName("TEST");
		trot.start();
	}

	private Cron cron;
	private boolean overCycleSkip = true;

	public CycleFxThread(String name, String line) throws Exception {
		setName(name);
		cron = new Cron();
		cron.setCron(line);
	}
	
	public CycleFxThread(String name, int seconds, boolean overCycleSkip) {
		setName(name);
		cron = new Cron();
		try {
			cron.setCron("period " + seconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.overCycleSkip = overCycleSkip;
	}

	public CycleFxThread(String name, String line, boolean overCycleSkip) throws Exception {
		cron = new Cron();
		cron.setCron(line);
		this.overCycleSkip = overCycleSkip;
	}

	@Override
	protected void doInit() {

	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.toString() + " " + cron.toString();
	}

	@Override
	protected void doWork() {

		long unixtime = System.currentTimeMillis() / 1000L;
		long stime;

		unixtime++;

		while (isContinue()) {

			stime = (unixtime * 1000L) - System.currentTimeMillis();

			if (stime > 0) {
				getCounter().setStatus(FXTHREAD_STATUS.Waiting);
				try {
					Thread.sleep(stime);
				} catch (InterruptedException e) {
				}
			} else if (stime < 0 && overCycleSkip) {
				unixtime++;
				continue;
			}

			getCounter().setStatus(FXTHREAD_STATUS.Running);
			unixtime++;

			if (cron.isOnTime(unixtime * 1000L)) {
				try {
					doCycle(unixtime * 1000L);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

		}

	}

	protected abstract void doCycle(long mstime);

}
