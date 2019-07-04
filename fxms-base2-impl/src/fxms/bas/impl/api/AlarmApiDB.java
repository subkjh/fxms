package fxms.bas.impl.api;

import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.ao.vo.AlarmCode;
import fxms.bas.ao.vo.ClearAlarm;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.ao.AlarmDbo;
import fxms.bas.impl.ao.ClearAlarmDbo;
import fxms.bas.impl.ao.DeleteAlarmDbo;
import fxms.bas.impl.ao.OccurAlarmDbo;
import fxms.bas.impl.ao.UpdateAlarmDbo;
import fxms.bas.impl.dbo.FX_AL_CUR;
import fxms.bas.impl.dbo.FX_AL_HST;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.attr.Model;
import fxms.bas.mo.property.MoModelable;
import fxms.bas.mo.property.MoOwnership;
import fxms.bas.po.item.PsItem;

public class AlarmApiDB extends AlarmApi {

	@Override
	protected void doDeleteAlarm(ClearAlarm clearAlarm, Alarm curAlarm) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(ClearAlarmDbo.class, clearAlarm, null);

			tran.deleteOfClass(DeleteAlarmDbo.class, new DeleteAlarmDbo(clearAlarm.getAlarmNo()), null);

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doInsertAlarm(OccurAlarm alarm) throws Exception {

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
	protected void doUpdateAlarm(AlarmEvent event, long chgDate) throws Exception {

		UpdateAlarmDbo a = new UpdateAlarmDbo(event.getAlarmNo());
		a.setAlarmLevel(event.getAlarmLevel());
		a.setAlarmMsg(event.getAlarmMsg());
		a.setChgDate(FxApi.getDate(0));

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(UpdateAlarmDbo.class, a, null);

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doInitOccurAlarm(OccurAlarm alarm, AlarmEvent event, AlarmCode alarmCode, Mo mo, Mo upper) {

		// set ownership
		if (upper instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) upper).getInloNo());
		} else if (mo instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) mo).getInloNo());
		}

		OccurAlarmDbo a = (OccurAlarmDbo) alarm;
		a.setMoClass(mo.getMoClass());
		a.setMoName(mo.getMoName());
		a.setMoAname(mo.getMoName());

		if (upper != null) {
			a.setUpperMoName(upper.getMoName());
			a.setUpperMoAname(upper.getMoName());
		}

		a.setAlcdName(alarmCode.getAlcdName());

		a.setAlarmMsg(event.getAlarmMsg());
		a.setAlarmNo(0);

		a.setRegDate(FxApi.getDate(event.getMstime()));
		a.setOcuDate(FxApi.getDate(event.getMstime()));

		if (event.getPsValue() != null)
			a.setPsValue(event.getPsValue().doubleValue());
		if (event.getCompareValue() != null)
			a.setCompareValue(event.getCompareValue().doubleValue());

		if (event.getPsCode() != null) {
			PsItem item = PsApi.getApi().getItem(event.getPsCode());
			if (item != null) {
				a.setPsName(item.getPsName());
			}
			a.setPsCode(event.getPsCode());
			a.setPsDate(event.getPsDate());
		}

		// set ownership
		if (upper instanceof MoOwnership) {
			a.setInloNo(((MoOwnership) upper).getInloNo());
		} else if (mo instanceof MoOwnership) {
			a.setInloNo(((MoOwnership) mo).getInloNo());
		}
		if (a.getInloNo() > 0) {
			MoLocation inlo = MoApi.getApi().getMoLocation(a.getInloNo());
			a.setInloName(inlo == null ? "" : inlo.getInloName());
		}

		MoModelable modelable = mo instanceof MoModelable ? (MoModelable) mo : upper instanceof MoModelable ? (MoModelable) upper
				: null;
		if (modelable != null) {
			Model model = MoApi.getApi().getModel(modelable.getModelNo());
			a.setModelName(model == null ? "" : model.getModelName());
			a.setModelNo(modelable.getModelNo());
		}

	}

	@Override
	protected ClearAlarm makeClearAlarmClass() throws Exception {
		return new ClearAlarmDbo();
	}

	@Override
	protected OccurAlarm makeOccurAlarmClass() throws Exception {
		return new OccurAlarmDbo();
	}

	@Override
	protected Alarm makeBroadcastAlarmClass(OccurAlarm s) {
		OccurAlarmDbo a = (OccurAlarmDbo) s;
		AlarmDbo alarm = new AlarmDbo();

		alarm.setAlarmKey(a.getAlarmKey());
		alarm.setAlarmNo(a.getAlarmNo());
		alarm.setAlcdNo(a.getAlcdNo());
		alarm.setMoName(a.getMoName());
		alarm.setMoNo(a.getMoNo());
		alarm.setMoInstance(a.getMoInstance());
		alarm.setUpperMoNo(a.getUpperMoNo());
		alarm.setUpperMoName(a.getUpperMoName());
		alarm.setAlarmLevel(a.getAlarmLevel());
		alarm.setOcuDate(a.getOcuDate());
		alarm.setInloNo(a.getInloNo());
		alarm.setStatus(a.getStatus());

		return alarm;

	}

}
