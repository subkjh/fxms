package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.OccurAlarm;

/**
 * 알람 생성
 * 
 * @author subkjh
 *
 */
public class AlarmFireDpo implements FxDpo<Void, Alarm> {

	public static void main(String[] args) {
		AlarmFireDpo dpo = new AlarmFireDpo();
		try {
			Alarm alarm = dpo.fireAlarm(1002134, 10000, null);

			System.out.println(FxmsUtil.toJson(alarm));

			new AlarmAckDfo().ackAlarm(alarm, 1);

			Mo mo = MoApi.getApi().getMo(1002134);
			AlarmCode alarmCode = AlcdMap.getMap().getAlarmCode(10000);

			System.out.println(FxmsUtil.toJson(dpo.fireAlarm(mo, alarmCode, null, ALARM_LEVEL.Major, null, null)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Alarm run(FxFact fact, Void data) throws Exception {
		long moNo = fact.getMoNo();
		int alcdNo = fact.getInt("alcdNo");
		String moInstance = fact.getString("moInstance");
		return fireAlarm(moNo, alcdNo, moInstance);
	}

	public Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws Exception {
		Mo mo = MoApi.getApi().getMo(moNo);
		AlarmCode alarmCode = AlcdMap.getMap().getAlarmCode(alcdNo);

		return fireAlarm(mo, alarmCode, moInstance, null, null, null);
	}

	public Alarm fireAlarm(Mo mo, AlarmCode alarmCode, Object moInstance, ALARM_LEVEL alarmLevel, String msg,
			Map<String, Object> etcData) throws Exception {

		AlarmOccurEvent alarmEvent = new MakeAlarmEventDfo().makeAlarmEvent(mo, moInstance, alarmCode, alarmLevel, msg,
				etcData);

		Alarm curAlarm = alarmEvent.getAlarm();
		Alarm newAlarm;

		if (curAlarm != null) {

			// 알람 수정
			// 이미 알람이 존재하면 수정
			newAlarm = new AlarmUpdateDfo().updateAlarm(curAlarm, alarmEvent, etcData);
			newAlarm.setStatus(STATUS.changed);

		} else {

			// 알람 추가

			OccurAlarm oa = new AlarmMakeDfo().makeAlarm(alarmEvent);

			newAlarm = new AlarmInsertDfo().insertAlarm(oa, etcData);
		}

		if (newAlarm != null) {
			new BroadcastDfo().broadcast(newAlarm);
		}

		return newAlarm;
	}

}
