package fxms.bas.impl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.ao.AlarmFireDpo;
import fxms.bas.impl.dpo.ao.AlarmFilterDfo;
import fxms.bas.impl.dpo.ao.AlarmInsertDfo;
import fxms.bas.impl.dpo.ao.AlarmMakeDfo;
import fxms.bas.impl.dpo.ao.AlarmReleaseDfo;
import fxms.bas.impl.dpo.ao.AlCfgSelectDfo;
import fxms.bas.impl.dpo.ao.AlcdSelectDfo;
import fxms.bas.impl.dpo.ao.AlarmCurSelectDfo;
import fxms.bas.impl.dpo.ao.AlarmHstSelectDfo;
import fxms.bas.impl.dpo.ao.AlarmUpdateDfo;
import fxms.bas.mo.FxMo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.OccurAlarm;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * AlarmApi를 구현한 클래스
 * 
 * @author subkjh
 *
 */
public class AlarmApiDfo extends AlarmApi {

	public static void main(String[] args) {
		FxMo mo = new FxMo();
		mo.setMoName("AAAAA1");
		mo.setMoDispName("AAAAA2");
//		OccurAlarm a = new OccurAlarm();
//		try {
//			ObjectUtil.initObject(mo, a);
//		} catch (Exception e) {
//			Logger.logger.error(e);
//		}
//		
//		System.out.println(a.getMoName());
//		System.out.println(a.getMoDispName());
//		System.out.println(a.getMoClass());

		Map<String, Object> map;
		map = ObjectUtil.initMap(mo, new HashMap<String, Object>());
		System.out.println(map);

		FX_AL_ALARM_HST a = new FX_AL_ALARM_HST();
		ObjectUtil.toObject(map, a);
		System.out.println(ObjectUtil.toMap(a));
		FX_AL_ALARM_CUR a1 = new FX_AL_ALARM_CUR();
		ObjectUtil.toObject(map, a1);
		System.out.println(ObjectUtil.toMap(a1));

	}

	@Override
	public Alarm clearAlarm(AlarmClearEvent event) throws Exception {

		Alarm alarm = new AlarmReleaseDfo().releaseAlarm(event);

		if (alarm != null) {

			setCurAlarm(alarm);

			new AlarmFilterDfo().filter(alarm);

			if (event.isBroadcast()) {
				new BroadcastDfo().broadcast(alarm);
			}
		}

		return alarm;
	}

	@Override
	public Alarm fireAlarm(AlarmOccurEvent event, Map<String, Object> etcData) throws Exception {

		if (event == null)
			return null;

		Alarm curAlarm, madeAlarm = null;

		curAlarm = getCurAlarm(event.getAlarmKey());
		if (curAlarm == null) {
			// 신규생성
			OccurAlarm oa = new AlarmMakeDfo().makeAlarm(event);
			madeAlarm = new AlarmInsertDfo().insertAlarm(oa, etcData);
		} else {
			// 업데이트
			madeAlarm = new AlarmUpdateDfo().updateAlarm(curAlarm, event, etcData);
		}

		if (madeAlarm != null) {
			setCurAlarm(madeAlarm);
			if (event.isBroadcast()) {
				new BroadcastDfo().broadcast(madeAlarm);
			}
		}

		return madeAlarm;

	}

	@Override
	public Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws Exception {

		Alarm alarm = new AlarmFireDpo().fireAlarm(moNo, alcdNo, moInstance);

		setCurAlarm(alarm);

		return alarm;

	}

	@Override
	public Alarm getHstAlarm(long alarmNo) throws Exception {
		List<Alarm> list = new AlarmHstSelectDfo().selectAlarmHst(makePara("alarmNo", alarmNo));
		return list != null && list.size() == 1 ? list.get(0) : null;
	}

	@Override
	public List<AlarmCfg> getAlCfgs(Map<String, Object> para) throws Exception {
		return new AlCfgSelectDfo().selectAlarmCfg(para);
	}

	@Override
	public List<AlarmCode> getAlCds() throws Exception {
		return new AlcdSelectDfo().selectAlarmCodeAll();
	}

	@Override
	public List<Alarm> getHstAlarms(long startDate, long endDate, Map<String, Object> para) throws Exception {
		return new AlarmHstSelectDfo().selectAlarmHst(startDate, endDate, para);
	}

	@Override
	public List<Alarm> getCurAlarms(Map<String, Object> para) throws Exception {
		return new AlarmCurSelectDfo().selectAlarmCur(para);
	}

}
