package fxms.bas.impl.cron;

import java.util.List;

import fxms.bas.api.AlarmApi;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;

/**
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AlarmService", descr = "경보가 발생한 후 설정된 시간이 경과되면 자동으로 해제한다.")
public class AlarmReleaseCron extends Crontab {

	@FxAttr(name = "schedule", description = "실행계획", value = "* * * * *")
	private String schedule;

	public AlarmReleaseCron() {

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}

	@Override
	public void start() throws Exception {

		int clearCount = 0;
		long ocuMsdate;
		AlarmCode alarmCode;
		List<Alarm> alarmList;

		// 현재 장애 목록을 가져온다.
		try {
			alarmList = AlarmApi.getApi().getCurAlarms(null);
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}

		for (Alarm alarm : alarmList) {

			try {
				alarmCode = AlcdMap.getMap().getAlarmCode(alarm.getAlcdNo());
			} catch (Exception e) {
				Logger.logger.fail(e.getMessage());
				continue;
			}

			if (alarmCode != null && alarmCode.getAutoClearSec() > 0) {

				ocuMsdate = DateUtil.toMstime(alarm.getOccurDtm());

				if (ocuMsdate + (alarmCode.getAutoClearSec() * 1000L) <= System.currentTimeMillis()) {
					try {
						AlarmApi.getApi().clearAlarm(new AlarmClearEvent(alarm.getAlarmNo(), System.currentTimeMillis(),
								ALARM_RLSE_RSN_CD.TimeOver, "시간경과자동해제", User.USER_NO_SYSTEM));
						clearCount++;
					} catch (Exception e) {
						Logger.logger.error(e);
					}
				}
			}
		}

		Logger.logger.trace("alarm-count={}, auto-clean-alarm-count={}", alarmList.size(), clearCount);

	}

}
