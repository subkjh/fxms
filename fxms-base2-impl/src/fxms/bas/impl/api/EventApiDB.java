package fxms.bas.impl.api;

import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.AoCode;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.ao.vo.AlarmCfg;
import fxms.bas.ao.vo.AlarmCfgMem;
import fxms.bas.ao.vo.AlarmCode;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.api.EventApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.app.AppService;
import fxms.bas.impl.ao.AlarmCodeDbo;
import fxms.bas.impl.ao.AlarmDbo;
import fxms.bas.impl.ao.OccurAlarmDbo;
import fxms.bas.impl.dbo.FX_AL_CFG;
import fxms.bas.impl.dbo.FX_AL_CFG_MEM;

public class EventApiDB extends EventApi {

	@Override
	public List<AlarmCfg> doSelectAlarmCfgAll(Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			List<FX_AL_CFG> cfgList = tran.select(FX_AL_CFG.class, parameters);
			List<FX_AL_CFG_MEM> memList = tran.select(FX_AL_CFG_MEM.class, parameters);

			List<AlarmCfg> list = new ArrayList<AlarmCfg>();
			for (FX_AL_CFG e : cfgList) {
				list.add(new AlarmCfg(e.getAlarmCfgNo(), e.getAlarmCfgName(), e.getMoClass(), e.isBasicCfgYn()));
			}

			for (FX_AL_CFG_MEM e : memList) {
				for (AlarmCfg cfg : list) {
					if (cfg.getAlarmCfgNo() == e.getAlarmCfgNo()) {
						cfg.add(new AlarmCfgMem(e.getAlarmLevel(), e.getAlcdNo(), e.getCompareVal(), e.getRepeatTimes(), e
								.getTreatName(), e.getVerifierJavaClass(), e.getVerifierValue()));
						break;
					}
				}
			}

			for (AlarmCfg cfg : list) {
				cfg.sortMember();
			}

			return list;

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
			List<AlarmCodeDbo> list = tran.select(AlarmCodeDbo.class, null);
			List<AlarmCode> ret = new ArrayList<AlarmCode>();
			for (AlarmCodeDbo a : list) {
				ret.add(new AlarmCode(a.getAlcdNo(), a.getAlcdName(), AoCode.AlarmLevel.getLevel(a.getAlarmLevel()) //
						, a.getPsCode(), a.getCompareCode(), a.getAlarmMsg(), a.getTreatName()//
						, a.getTargetMoClass(), a.getAutoClearSec(), a.isServiceAlarmYn()));
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public OccurAlarm doSelectAlarmHst(long alarmNo) throws Exception {
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
			List<AlarmDbo> list = tran.select(AlarmDbo.class, parameters);
			List<Alarm> ret = new ArrayList<Alarm>();
			for (Alarm a : list) {
				ret.add(a);
			}
			return ret;
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
