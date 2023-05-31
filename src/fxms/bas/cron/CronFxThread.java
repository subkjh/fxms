package fxms.bas.cron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.LogApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.fxo.thread.CycleFxThread;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;

/**
 * 크론 실행용 스레드
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class CronFxThread extends CycleFxThread implements CronListener {

	private Map<String, CronSubFxThread> workerMap;

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
	public void onFinished(long cronRunNo, Crontab e, Exception ex, int spentTieme) {

		if (ex == null) {
			e.getCounter().addOk(spentTieme);
			LogApi.getApi().closeCronLog(cronRunNo, true, spentTieme, e.getOutPara());
			clearAlarm(e.getName(), Lang.get("Cron ran normally."));
		} else {
			LogApi.getApi().closeCronLog(cronRunNo, false, spentTieme,
					FxApi.makePara("exp", ex.getClass().getName(), "message", ex.getMessage()));
			e.getCounter().addFail();
			fireAlarm(e.getName(), ex.getMessage());
		}

		Logger.logger.info(e + " ** NOW(RESULT({})STIME({}))", ex == null, spentTieme);

	}

	@Override
	public long onStart(Crontab e) {
		long cronRunNo = LogApi.getApi().openCronLog(e.getClass().getName(), e.getInPara());
		Logger.logger.info("{}", e);
		return cronRunNo;
	}

	/**
	 * 
	 * @param name 실행한 크론명
	 * @throws Exception
	 */
	public void runCron(String name) throws Exception {

		for (Crontab ct : getCrontabList()) {
			if (ct.getClass().getName().equals(name)) {
				runCron(ct);
				return;
			}
		}

		throw new Exception("CRON(" + name + ") NOT FOUND");

	}

	private List<Crontab> getCrontabList() {
		return AdapterApi.getApi().getAdapters(Crontab.class);
	}

	/**
	 * 크론을 실행한다.
	 * 
	 * @param ct
	 */
	private void runCron(Crontab ct) {
		try {

			Logger.logger.debug("{}.{}", ct.getGroup(), ct.getClass().getName());

			if (ct.getGroup() == null || ct.getGroup().length() == 0) {
				new Thread() {
					public void run() {
						long cronRunNo = onStart(ct);
						long ptime = System.currentTimeMillis();
						try {
							ct.start();
							onFinished(cronRunNo, ct, null, ((int) (System.currentTimeMillis() - ptime)));
						} catch (Exception e) {
							Logger.logger.error(e);
							onFinished(cronRunNo, ct, e, ((int) (System.currentTimeMillis() - ptime)));
						}
					}
				}.start();
			} else {
				CronSubFxThread th = workerMap.get("CRON-" + ct.getGroup());
				if (th == null) {
					th = new CronSubFxThread("CRON-" + ct.getGroup(), this);
					workerMap.put("CRON-" + ct.getGroup(), th);
					th.start();
				}

				th.put(ct);
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
//		for (Crontab tab : getCrontabList()) {
//			if (tab.isRunInit()) {
//				runCron(tab);
//			}
//		}
	}

	private void clearAlarm(String instance, String message) {
		try {
			AlarmApi.getApi().clearAlarm(MoApi.getApi().getProjectMo(), instance, ALARM_CODE.FXMSERR_CRON.getAlcdNo(),
					ALARM_RLSE_RSN_CD.Release, message, System.currentTimeMillis(), User.USER_NO_SYSTEM);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private void fireAlarm(String instance, String message) {
		try {
			AlarmApi.getApi().fireAlarm(MoApi.getApi().getProjectMo(), instance, ALARM_CODE.FXMSERR_CRON.getAlcdNo(),
					null, message, null);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}
