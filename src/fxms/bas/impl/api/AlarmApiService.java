package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApiServiceTag;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.service.AlarmService;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;

public class AlarmApiService extends AlarmApi implements FxApiServiceTag {

	private AlarmService alarmService = null;

	public AlarmApiService() {
		try {
			setName(FxServiceImpl.serviceName + ":AlarmApi");
		} catch (Exception e) {
			setName("AlarmApiService");
		}
	}

	private synchronized AlarmService getAlarmService() throws FxServiceNotFoundException, Exception {

		if (this.alarmService == null) {
			this.alarmService = ServiceApi.getApi().getService(AlarmService.class);
		}

		// ping을 해봐서 끊겼는지 확인한다.
		try {
			this.alarmService.ping(getName());
		} catch (Exception e) {
			this.alarmService = ServiceApi.getApi().getService(AlarmService.class);
		}

		return this.alarmService;
	}

	@Override
	public Alarm clearAlarm(long alarmNo, long mstime, ALARM_RLSE_RSN_CD cd, String rlseMemo, int userNo)
			throws Exception {

		Alarm alarm = getAlarmService().clearAlarm(alarmNo, mstime, cd, rlseMemo, userNo);

		setCurAlarm(alarm);

		return alarm;
	}

	@Override
	public Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws Exception {
		Alarm alarm = getAlarmService().fireAlarm(moNo, alcdNo, moInstance);

		setCurAlarm(alarm);

		return alarm;
	}

	@Override
	public Alarm getHstAlarm(long alarmNo) throws Exception {
		return getAlarmService().getAlarm(alarmNo);
	}

	@Override
	public List<Alarm> getHstAlarms(long startDate, long endDate, Map<String, Object> para) throws Exception {
		return getAlarmService().getAlarms(startDate, endDate, para);
	}

	@Override
	public List<Alarm> getCurAlarms(Map<String, Object> para) throws Exception {
		return getAlarmService().getCurAlarms(para);
	}

	@Override
	public Alarm fireAlarm(AlarmOccurEvent event, Map<String, Object> etcData) throws Exception {

		if (event == null)
			return null;

		Alarm madeAlarm = getAlarmService().fireAlarm((AlarmOccurEvent) event, etcData);

		setCurAlarm(madeAlarm);

		return madeAlarm;
	}

	@Override
	public List<AlarmCfg> getAlCfgs(Map<String, Object> para) throws Exception {
		return getAlarmService().getAlarmCfgs(para);
	}

	@Override
	public List<AlarmCode> getAlCds() throws Exception {
		return getAlarmService().getAlarmCodes();
	}

}
