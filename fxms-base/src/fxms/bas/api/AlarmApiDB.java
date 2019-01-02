package fxms.bas.api;

import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.DeleteAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.alarm.dbo.UpdateAlarmDbo;
import fxms.bas.dbo.ao.FX_AL_CUR;
import fxms.bas.dbo.ao.FX_AL_HST;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

public class AlarmApiDB extends AlarmApi {

	@Override
	protected void doInsertAlarm(OccurAlarmDbo alarm) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			alarm.setAlarmNo(tran.getNextVal(FX_AL_CUR.FX_SEQ_ALARMNO, Long.class));

			tran.insertOfClass(FX_AL_CUR.class, alarm);
			tran.insertOfClass(FX_AL_HST.class, alarm);

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void onDeleteAlarm(ClearAlarmDbo alarm) throws Exception {
		
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			tran.update(alarm, null);
		
			tran.deleteOfObject(new DeleteAlarmDbo(alarm.getAlarmNo()));

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}

	}

	@Override
	protected void doUpdateAlarm(UpdateAlarmDbo alarm) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			tran.update(alarm, null);

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}
	}

}
