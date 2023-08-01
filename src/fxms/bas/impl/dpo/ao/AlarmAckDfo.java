package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 알람 해제
 * 
 * @author subkjh
 *
 */
public class AlarmAckDfo implements FxDfo<Alarm, Boolean> {

	@Override
	public Boolean call(FxFact fact, Alarm alarm) throws Exception {
		return ackAlarm(alarm, fact.getUserNo());
	}

	public boolean ackAlarm(Alarm alarm, int ackUserNo) throws Exception {

		Map<String, Object> datas = new HashMap<>();
		datas.put("alarmNo", alarm.getAlarmNo());
		datas.put("ackDtm", DateUtil.getDtm());
		datas.put("ackUserNo", ackUserNo);

		return ackAlarm(datas);
	}

	public boolean ackAlarm(Map<String, Object> datas) throws Exception {
		ClassDaoEx.open() //
				.updateOfClass(FX_AL_ALARM_HST.class, datas) //
				.updateOfClass(FX_AL_ALARM_CUR.class, datas)//
				.close();
		return true;
	}

}
