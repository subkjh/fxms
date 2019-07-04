package fxms.bas.co.cron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.thread.CycleFxThread;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public class CronFxThread extends CycleFxThread implements CronListener {

	public static void main(String[] args) throws Exception {
		CronFxThread trot = new CronFxThread();
		CrontabSample cron = new CrontabSample();
		try {
			cron.setSchedule(Cron.EVERY_MINUTES);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trot.start();
	}

	private Map<String, CronSubFxThread> workerMap;

	private CronListener listener;

	public CronFxThread() throws Exception {

		super("CronFxThread", Cron.CYCLE_1_SECOND);

		workerMap = new HashMap<String, CronSubFxThread>();
	}

	@Override
	public String getState(LOG_LEVEL level) {
		
		StringBuffer sb = new StringBuffer();
		
		List<Crontab> tabList = getCrontabList();

		sb.append("crontab-size=" + tabList.size());
		sb.append(",thread-size=" + workerMap.size());
		
		if (LOG_LEVEL.trace.contains(level)) {
			for (Crontab t : tabList) {
				sb.append("\n\tcrontab=" + t);
			}
		}

		return super.toString() + " " + sb.toString();
	}

	@Override
	public void onFinished(Crontab e, boolean isOk, long stime) {

		if (isOk) {
			e.getCounter().addOk(stime);
		} else {
			e.getCounter().addFail();
		}

		Logger.logger.info(e + " ** NOW(RESULT({})STIME({}))", isOk, stime);

		// if (TROS.tros != null)
		// TROS.tros.addHst(e.getOpcode(), e.getLog(), isOk, stime);

		if (listener != null)
			listener.onFinished(e, isOk, stime);

	}

	@Override
	public void onStart(Crontab e) {

		Logger.logger.info("{}", e);

		if (listener != null)
			listener.onStart(e);

	}

	public void runCron(String name) throws Exception {

		for (Crontab ct : getCrontabList()) {
			if (ct.getName().equals(name)) {
				runCron(ct);
				return;
			}
		}

		throw new Exception("CRON(" + name + ") NOT FOUND");

	}

	private List<Crontab> getCrontabList() {

		if (FxActorParser.getParser().isChanged()) {
			FxActorParser.getParser().reload();
		}

		return FxActorParser.getParser().getActorList(Crontab.class);
	}

	/**
	 * 크론을 실행한다.
	 * 
	 * @param ct
	 */
	private void runCron(Crontab ct) {
		try {

			Logger.logger.debug(ct.getName());

			if (ct.getGroup() == null || ct.getGroup().length() == 0) {
				new Thread() {
					public void run() {
						onStart(ct);
						long ptime = System.currentTimeMillis();
						try {
							ct.cron();
							onFinished(ct, true, System.currentTimeMillis() - ptime);
						} catch (Exception e) {
							Logger.logger.error(e);
							onFinished(ct, false, System.currentTimeMillis() - ptime);
						}
					}
				}.start();
			} else {
				CronSubFxThread trot = workerMap.get("CRON-" + ct.getGroup());
				if (trot == null) {
					trot = new CronSubFxThread("CRON-" + ct.getGroup());
					workerMap.put("CRON-" + ct.getGroup(), trot);
					trot.setListener(this);
					trot.start();
				}

				trot.put(ct);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	protected void doCycle(long mstime) {

		for (final Crontab ct : getCrontabList()) {
			if (ct.getCron().isOnTime(mstime)) {
				runCron(ct);
			}
		}

	}

	@Override
	protected void doInit() {
		for (Crontab tab : getCrontabList()) {
			if (tab.isRunInit()) {
				runCron(tab);
			}
		}
	}

}
