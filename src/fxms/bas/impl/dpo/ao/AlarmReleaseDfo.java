package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.exp.AlarmNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.ClearAlarm;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 알람 해제
 * 
 * @author subkjh
 *
 */
public class AlarmReleaseDfo implements FxDfo<ClearAlarm, Alarm> {

	public static void main(String[] args) {
		AlarmReleaseDfo dfo = new AlarmReleaseDfo();
		try {
			ClearAlarm data = new ClearAlarm();
			data.setRlseDtm(DateUtil.getDtm());
			data.setAlarmNo(13027228L);
			System.out.println(FxmsUtil.toJson(dfo.releaseAlarm(data)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Alarm call(FxFact fact, ClearAlarm data) throws Exception {
		return releaseAlarm(data);
	}

	public Alarm releaseAlarm(ClearAlarm data) throws Exception {
		return releaseAlarm(data.getAlarmNo(), ObjectUtil.toMap(data));
	}

	public Alarm releaseAlarm(AlarmClearEvent event) throws Exception {
		Map<String, Object> datas = new HashMap<>();
		datas.put("rlseYn", "Y");
		datas.put("rlseDtm", DateUtil.toHstime(event.getEventMstime()));
		datas.put("alarmRlseRsnCd", event.getAlarmRlseRsnCd().getCd());
		datas.put("alarmRlseRsnName", event.getAlarmRlseRsnCd().name());
		datas.put("rlseMemo", event.getReleaseMemo());

		return releaseAlarm(event.getAlarmNo(), datas);
	}

	public Alarm releaseAlarm(long alarmNo, Map<String, Object> datas) throws AlarmNotFoundException, Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		if (datas == null) {
			datas = new HashMap<>();
		}

		datas.put("alarmNo", alarmNo);
		datas.put("rlseYn", "Y");
		if (datas.get("rlseDtm") == null) {
			datas.put("rlseDtm", DateUtil.getDtm());
		}

		try {
			tran.start();

			Map<String, Object> para = FxApi.makePara("alarmNo", alarmNo);

			FX_AL_ALARM_HST hst = tran.selectOne(FX_AL_ALARM_HST.class, para);
			if (hst != null) {
				tran.updateOfClass(FX_AL_ALARM_HST.class, datas);
				tran.deleteOfClass(FX_AL_ALARM_CUR.class, para);
				tran.commit();

				Alarm alarm = tran.selectOne(FX_AL_ALARM_HST.class, para, Alarm.class);
				alarm.setStatus(STATUS.deleted);
				return alarm;
			}
			throw new AlarmNotFoundException(alarmNo);
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}
