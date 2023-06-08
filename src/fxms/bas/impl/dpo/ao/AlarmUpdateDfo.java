package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmOccurEvent;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 알람 해제
 * 
 * @author subkjh
 *
 */
public class AlarmUpdateDfo implements FxDfo<Void, Alarm> {

	@Override
	public Alarm call(FxFact fact, Void data) throws Exception {
		return null;
	}

	public Alarm updateAlarm(Alarm alarm, AlarmOccurEvent event, Map<String, Object> etcData) throws Exception {

		Map<String, Object> datas = new HashMap<>();
		datas.put("alarmNo", alarm.getAlarmNo());
		datas.put("alarmLevel", event.getAlarmLevel());
		datas.put("alarmMsg", event.getAlarmMsg());
		datas.put("chgDtm", DateUtil.getDtm());

		if (event.getOccurCnt() > 0) {
			datas.put("occurCnt", event.getOccurCnt());
		}
		
		if ( etcData != null) {
			datas.putAll(etcData);
		}

		return reoccurAlarm(alarm.getAlarmNo(), datas);
	}

	/**
	 * 계속 발생하여 발생회수를 증가시킨다.
	 * 
	 * @param alarm
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public Alarm reoccurAlarm(long alarmNo, Map<String, Object> datas) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		Map<String, Object> para = FxApi.makePara("alarmNo", alarmNo);
		datas.put("alarmNo", alarmNo);

		try {
			tran.start();

			FX_AL_ALARM_HST hst = tran.selectOne(FX_AL_ALARM_HST.class, para);
			if (datas.get("occurCnt") == null) {
				datas.put("occurCnt", hst.getOccurCnt() + 1);
			}

			tran.updateOfClass(FX_AL_ALARM_HST.class, datas);
			tran.updateOfClass(FX_AL_ALARM_CUR.class, datas);
			tran.commit();

			Alarm newAlarm = tran.selectOne(FX_AL_ALARM_HST.class, para, Alarm.class);
			newAlarm.setStatus(STATUS.changed);
			return newAlarm;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 알람 데이터의 일부 속성을 업데이트 한다.
	 * 
	 * @param alarm
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public Alarm updateAlarm(long alarmNo, Map<String, Object> datas) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		Map<String, Object> para = FxApi.makePara("alarmNo", alarmNo);
		datas.put("alarmNo", alarmNo);

		try {
			tran.start();

			tran.updateOfClass(FX_AL_ALARM_HST.class, datas);
			tran.updateOfClass(FX_AL_ALARM_CUR.class, datas);
			tran.commit();

			Alarm newAlarm = tran.selectOne(FX_AL_ALARM_HST.class, para, Alarm.class);
			newAlarm.setStatus(STATUS.changed);
			return newAlarm;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}
