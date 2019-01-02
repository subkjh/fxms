package fxms.bas.alarm;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.EventApi;
import fxms.bas.cron.Cron;
import fxms.bas.define.PS_TYPE;
import fxms.bas.fxo.thread.CycleFxThread;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;

/**
 * 경보가 발생한 후 설정된 시간이 경과되면 자동으로 해제하는 스레드
 * 
 * @author SUBKJH
 * @since Ver 1-0-0
 *
 */
public class AlarmCleaner extends CycleFxThread {

	public AlarmCleaner() throws Exception {
		super(AlarmCleaner.class.getSimpleName(), Cron.CYCLE_30_SECONDS);
	}

	@Override
	protected void doCycle(long mstime) {

		int clearCount = 0;

		AlarmCode alarmCode;
		Alarm alarms[] = EventApi.getApi().getAlarmArray();
		long ocuMsdate;

		for (Alarm alarm : alarms) {
			alarmCode = EventApi.getApi().getAlarmCodeByNo(alarm.getAlcdNo());
			if (alarmCode != null && alarmCode.getAutoClearSec() > 0) {

				ocuMsdate = PS_TYPE.getMstimeByHstime(alarm.getOcuDate());

				if (ocuMsdate + (alarmCode.getAutoClearSec() * 1000L) <= System.currentTimeMillis()) {
					try {
						AlarmApi.getApi().clearAlarm(alarm, User.USER_NO_SYSTEM, AoCode.ClearReason.TimeOver,
								AoCode.ClearReason.TimeOver.name(), AoCode.ClearReason.TimeOver.name());
						clearCount++;
					} catch (Exception e) {
						Logger.logger.error(e);
					}
				}
			}
		}

		Logger.logger.trace("alarm-count={}, auto-clean-alarm-count={}", alarms.length, clearCount);

	}

}
