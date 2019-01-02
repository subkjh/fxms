package fxms.bas.api;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.Map;

import fxms.bas.alarm.AlarmCfg;
import fxms.bas.alarm.AlarmCfgMem;
import fxms.bas.alarm.AlarmCode;
import fxms.bas.alarm.AlarmEvent;
import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.app.AppService;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

public class EventApiDB extends EventApi {

	@Override
	public List<AlarmCfg> doSelectAlarmCfgAll(Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			List<AlarmCfg> cfgList = tran.select(AlarmCfg.class, parameters);
			List<AlarmCfgMem> memList = tran.select(AlarmCfgMem.class, parameters);

			for (AlarmCfgMem mem : memList) {
				for (AlarmCfg cfg : cfgList) {
					if (cfg.getAlarmCfgNo() == mem.getAlarmCfgNo()) {
						cfg.add(mem);
						break;
					}
				}
			}

			for (AlarmCfg cfg : cfgList) {
				cfg.sortMember();
			}

			return cfgList;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public List<AlarmCode> doSelectAlarmCodeAll() throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return (List<AlarmCode>) tran.select(AlarmCode.class, null);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}
	
	public OccurAlarmDbo doSelectAlarmHst(long alarmNo) throws Exception 
	{
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return tran.selectOne(OccurAlarmDbo.class, FxDaoExecutor.makePara("alarmNo", alarmNo));
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}


	@Override
	public List<Alarm> doSelectCurAlarmAll(Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return tran.select(Alarm.class, parameters);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected Alarm doSendEvent(AlarmEvent event) {

		try {
			AppService appService = ServiceApi.getApi().getService(AppService.class);
			return appService.onEvent(event);
		} catch (NotBoundException e) {
			Logger.logger.fail("{} {}", e.getClass().getSimpleName(), e.getMessage());
			return null;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

}
