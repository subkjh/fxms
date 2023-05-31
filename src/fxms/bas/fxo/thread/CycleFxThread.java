package fxms.bas.fxo.thread;

import fxms.bas.cron.Cron;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * 주기적으로 수행하는 FxMS 기본 스레드
 * 
 * @author subkjh
 *
 */
public abstract class CycleFxThread extends FxThread {

	public static void main(String[] args) throws Exception {
		CycleFxThread trot = new CycleFxThread("TEST", Cron.CYCLE_1_SECOND) {

			@Override
			protected void doCycle(long mstime) {
				System.out.println(System.currentTimeMillis() + ":" + DateUtil.getDtm());
				System.out.println(mstime + ":" + DateUtil.getDtm(mstime));
				System.out.println();
			}

		};

		trot.setName("TEST");
		trot.start();
	}

	private Cron cron;

	/**
	 * 
	 * @param name          명칭
	 * @param seconds       주기적으로 처리할 초 단위 시간
	 * @param overCycleSkip 처리중 다음 시작 시간이 경과할 경우 무시 여부
	 */
	public CycleFxThread(String name, int seconds) {
		setName(name);
		cron = new Cron();
		try {
			cron.setSchedule("period " + seconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param name     명칭
	 * @param schedule 스케쥴
	 * @throws Exception
	 */
	public CycleFxThread(String name, String schedule) throws Exception {
		setName(name);
		cron = new Cron();
		cron.setSchedule(schedule);
	}

	/**
	 * 주기적으로 처리되는 함수
	 * 
	 * @param mstime milliseconds 단위의 현재 시간
	 */
	protected abstract void doCycle(long mstime);

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		long unixtime = System.currentTimeMillis() / 1000L;
		long stime;
		long mstime;

		unixtime++;

		while (isContinue()) {

			mstime = unixtime * 1000L;

			stime = mstime - System.currentTimeMillis();

			// 정시까지 SLEEP한다.
			if (stime > 0) {
				getCounter().setStatus(FXTHREAD_STATUS.Waiting);
				try {
					Thread.sleep(stime);
				} catch (InterruptedException e) {
				}
			} else if (stime < 0) {
				unixtime++;
				continue;
			}

			getCounter().setStatus(FXTHREAD_STATUS.Running);

			if (cron.isOnTime(mstime)) {
				try {
					doCycle(mstime);
				} catch (Exception e) {
					Logger.logger.error(e);
				}

				unixtime++;
			}

		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.toString() + " " + cron.toString();
	}

}
