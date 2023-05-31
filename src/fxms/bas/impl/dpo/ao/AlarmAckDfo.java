package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
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

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.updateOfClass(FX_AL_ALARM_HST.class, datas);
			tran.updateOfClass(FX_AL_ALARM_CUR.class, datas);
			tran.commit();
			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}
