package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;

import com.google.gson.Gson;

import fxms.bas.ao.AoCode;
import fxms.bas.ao.exp.AlarmNotFoundException;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.ao.vo.AlarmCode;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.exp.ExistChildException;
import fxms.bas.co.exp.NotFoundException;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.impl.ao.AoSetReasonDbo;
import fxms.bas.impl.dbo.FX_AL_CFG;
import fxms.bas.impl.dbo.FX_AL_CFG_MEM;
import fxms.bas.impl.dbo.FX_AL_CUR;
import fxms.bas.impl.dbo.FX_AL_HST;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exp.MoNotFoundException;
import fxms.module.restapi.CommHandler;
import fxms.module.restapi.vo.SessionVo;

public class AoHandler extends CommHandler {

	public static void main(String[] args) throws Exception {
		AoHandler handler = new AoHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alarmCfgNo", 111);
		handler.deleteAlarmCfg(null, parameters);
	}

	public Object fireAlarm(SessionVo session, Map<String, Object> parameters) throws Exception {

		long moNo = getLong(parameters, "moNo");
		String alarmName = null;
		int alcdNo = -1;

		try {
			alarmName = getString(parameters, "alarmName");
		} catch (Exception e) {
		}

		try {
			alcdNo = getInt(parameters, "alcdNo");
		} catch (Exception e) {
		}

		String alarmMsg = getString(parameters, "alarmMsg");

		Object moInstance = parameters.get("moInstance");

		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
		AlarmCode ac = null;
		if (alcdNo > 0) {
			ac = EventApi.getApi().getAlarmCodeByNo(alcdNo);
		} else {
			ac = EventApi.getApi().getAlarmCodeByName(alarmName);
		}
		if (ac == null) {
			throw new NotFoundException("alarmName", alarmName);
		}

		return EventApi.getApi().check(mo, moInstance == null ? null : moInstance.toString(), ac.getAlcdNo(), alarmMsg, parameters);
	}

	public Object ackAlarm(SessionVo session, Map<String, Object> parameters) throws Exception {

		long alarmNo = getLong(parameters, "alarmNo");
		long ackDate = FxApi.getDate(System.currentTimeMillis());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alarmNo", alarmNo);
		map.put("ackDate", ackDate);
		map.put("ackUserNo", session.getUserNo());

		Alarm alarm = (Alarm) EventApi.getApi().getAlarm(alarmNo);
		if (alarm == null) {
			throw new AlarmNotFoundException("alarm-no=" + alarmNo);
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.updateOfClass(FX_AL_CUR.class, map);
			tran.updateOfClass(FX_AL_HST.class, map);

			tran.commit();

			alarm.setStatus(FxEvent.STATUS.acked);
			FxServiceImpl.fxService.send(alarm);

			return alarm;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	public Object clearAlarm(SessionVo session, Map<String, Object> parameters) throws Exception {

		long alarmNo = this.getLong(parameters, "alarmNo");
		Alarm alarm = EventApi.getApi().getAlarm(alarmNo);
		if (alarm == null) {
			throw new AlarmNotFoundException("alarm-no=" + alarmNo);
		}

		return EventApi.getApi().clear(alarm, System.currentTimeMillis(), AoCode.ClearReason.ByUser);
	}

	public Object setAlarmReason(SessionVo session, Map<String, Object> parameters) throws Exception {

		Gson son = new Gson();
		AoSetReasonDbo item = son.fromJson(son.toJson(parameters), AoSetReasonDbo.class);
		item.setReasonRegDate(FxApi.getDate(0));
		item.setReasonRegUserNo(session.getUserNo());

		FxConfDao.getDao().update(item, null);

		return item;
	}

	public Object getAlarmCodeList(SessionVo session, Map<String, Object> parameters) throws Exception {
		String alarmClass = getString(parameters, "targetMoClass");
		return EventApi.getApi().getAlarmCodeList(alarmClass);
	}

	public Object getAlarmCur(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_AL_CUR.class, makePara4Ownership(session, parameters));
	}

	public Object getAlarmHst(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_AL_HST.class, makePara4Ownership(session, parameters));
	}

	public Object getAlarm(SessionVo session, Map<String, Object> parameters) throws Exception {
		long alarmNo = getLong(parameters, "alarmNo");
		return FxConfDao.getDao().select(FX_AL_HST.class, FxConfDao.makePara("alarmNo", alarmNo));
	}

	public Object getAlarmCfgList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_AL_CFG.class, parameters);
	}

	public Object addAlarmCfg(SessionVo session, Map<String, Object> parameters) throws Exception {

		Gson son = new Gson();
		FX_AL_CFG item = son.fromJson(son.toJson(parameters), FX_AL_CFG.class);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			int alarmCfgNo = tran.getNextVal(FX_AL_CFG.FX_SEQ_ALARMCFGNO, Integer.class);

			item.setRegUserNo(session.getUserNo());
			item.setRegDate(FxApi.getDate(0));
			item.setChgDate(item.getRegDate());
			item.setChgUserNo(item.getRegUserNo());

			item.setAlarmCfgNo(alarmCfgNo);
			tran.insertOfClass(item.getClass(), item);
			// TODO
			// tran.insertOfClass(FX_AL_CFG_MEM.class, item.getMemList());

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
			}

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object copyAlarmCfg(SessionVo session, Map<String, Object> parameters) throws Exception {

		int oldAlarmCfgNo = getInt(parameters, "oldAlarmCfgNo");
		String newAlarmCfgName = getString(parameters, "newAlarmCfgName");

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("alarmCfgNo", oldAlarmCfgNo);
			List<FX_AL_CFG> cfgList = tran.select(FX_AL_CFG.class, para);
			List<FX_AL_CFG_MEM> memList = tran.select(FX_AL_CFG_MEM.class, para);

			FX_AL_CFG item = cfgList.get(0);

			int alarmCfgNo = tran.getNextVal(FX_AL_CFG.FX_SEQ_ALARMCFGNO, Integer.class);

			item.setAlarmCfgName(newAlarmCfgName);
			item.setRegUserNo(session.getUserNo());
			item.setRegDate(FxApi.getDate(0));
			item.setChgDate(item.getRegDate());
			item.setChgUserNo(item.getRegUserNo());
			item.setAlarmCfgNo(alarmCfgNo);

			tran.insertOfClass(item.getClass(), item);

			if (memList != null && memList.size() > 0) {
				for (FX_AL_CFG_MEM e : memList) {
					e.setAlarmCfgNo(alarmCfgNo);
				}
				tran.insertOfClass(FX_AL_CFG_MEM.class, memList);
			}

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
			}

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object addAlarmCfgMem(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_AL_CFG_MEM mem = new FX_AL_CFG_MEM();
		ObjectUtil.toObject(parameters, mem);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.insertOfClass(mem.getClass(), mem);

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
			}

			return mem;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object delAlarmCfgMem(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_AL_CFG_MEM mem = new FX_AL_CFG_MEM();
		ObjectUtil.toObject(parameters, mem);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.deleteOfClass(mem.getClass(), mem, null);

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
			}

			return mem;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object updateAlarmCfg(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_AL_CFG item = new FX_AL_CFG();
		ObjectUtil.toObject(parameters, item);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			item.setChgUserNo(session.getUserNo());
			item.setChgDate(FxApi.getDate(0));
			item.setAlarmCfgNo(item.getAlarmCfgNo());
			tran.updateOfClass(item.getClass(), item, null);
			// tran.deleteOfClass(FX_AL_CFG_MEM.class,
			// tran.makaParameters("alarmCfgNo",
			// item.getAlarmCfgNo()));
			// tran.insertList(item.getMemList());

			tran.commit();

			// 마스터만 변경된 경우이므로 RELOAD할 필요 없음.
			// if (FxServiceImpl.fxService != null) {
			// FxServiceImpl.fxService.send(new
			// ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
			// }

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object deleteAlarmCfg(SessionVo session, Map<String, Object> parameters) throws Exception {
		Number alarmCfgNo = null;
		if (parameters != null) {
			alarmCfgNo = getNumber(parameters, "alarmCfgNo");
		}

		if (alarmCfgNo == null) {
			throw new Exception("'alarmCfgNo' not defined");
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> map = FxDaoExecutor.makaParameters("alarmCfgNo", alarmCfgNo);

			int setCnt = tran.getInt(
					tran.selectSql("select count(1) from FX_MO where ALARM_CFG_NO = ?", new Object[] { alarmCfgNo }), 0);

			if (setCnt > 0) {
				throw new ExistChildException("alarm-cfg", alarmCfgNo);
			}

			List<FX_AL_CFG> list = tran.select(FX_AL_CFG.class, map);

			if (list != null && list.size() == 1) {

				tran.deleteOfClass(FX_AL_CFG_MEM.class, map, null);
				tran.deleteOfClass(FX_AL_CFG.class, map, null);

				tran.commit();

				if (FxServiceImpl.fxService != null) {
					FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_ALARM));
				}

				return list.get(0);
			}

			throw new Exception("alarm-cfg not found");

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}
