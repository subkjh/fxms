package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.event.FxEvent.STATUS;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.OccurAlarm;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 알람 해제
 * 
 * @author subkjh
 *
 */
public class AlarmInsertDfo implements FxDfo<Map<String, Object>, Alarm> {

	@Override
	public Alarm call(FxFact fact, Map<String, Object> data) throws Exception {
		return insertAlarm(data);
	}

	public Alarm insertAlarm(OccurAlarm data) throws Exception {
		return insertAlarm(ObjectUtil.toMap(data));
	}

	public Alarm insertAlarm(Map<String, Object> datas) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			long alarmNo = tran.getNextVal(FX_AL_ALARM_CUR.FX_SEQ_ALARMNO, Long.class);
			datas.put("alarmNo", alarmNo);
			if (datas.get("occurCnt") == null) {
				datas.put("occurCnt", 1);
			}

			// 이력에 우선 기록하고 ( CUR -> HST 키 관계임 )
			tran.insertOfClass(FX_AL_ALARM_HST.class, datas);

			// 현재에 기록한다.
			tran.insertOfClass(FX_AL_ALARM_CUR.class, datas);

			tran.commit();
			
			Alarm alarm = tran.selectOne(FX_AL_ALARM_HST.class, FxApi.makePara("alarmNo", alarmNo), Alarm.class);
			alarm.setStatus(STATUS.added);
			return alarm;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}
