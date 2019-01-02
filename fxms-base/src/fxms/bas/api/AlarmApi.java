package fxms.bas.api;

import java.util.List;

import fxms.bas.alarm.AlarmCfg;
import fxms.bas.alarm.AlarmCode;
import fxms.bas.alarm.AlarmEvent;
import fxms.bas.alarm.AlarmFilter;
import fxms.bas.alarm.AoCode;
import fxms.bas.alarm.AoCode.AlarmLevel;
import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.alarm.dbo.UpdateAlarmDbo;
import fxms.bas.alarm.treat.TreatFxThread;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.attr.Model;
import fxms.bas.mo.property.MoModelable;
import fxms.bas.mo.property.MoOwnership;
import fxms.bas.noti.FxEvent;
import fxms.bas.pso.item.PsItem;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public abstract class AlarmApi extends FxApi {

	/** use DBM */
	public static AlarmApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static AlarmApi getApi() {
		if (api != null)
			return api;

		api = makeApi(AlarmApi.class);

		return api;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName() + " ");
		sb.append("FILTER-SIZE(" + (alarmFilterList == null ? 0 : alarmFilterList.size()) + ")");
		sb.append("COUNT-EVENT(" + countWork + ")");

		return sb.toString();
	}

	@Override
	protected void initApi() throws Exception {
		alarmFilterList = FxActorParser.getParser().getActorList(AlarmFilter.class);
	}

	@Override
	protected void reload() throws Exception {
	}

	/** 경보필터 */
	private List<AlarmFilter> alarmFilterList;
	/** 처리된 이벤트 수 */
	private long countWork;
	/** 동일한 경보인 경우 새롭게 만들것인지 아니면 수정할 것인지 설정. 기본은 false */
	private boolean isChangeAlarmIfExists = false;
	/** 경보조치맵, 키:경보조치번호 */
	// private Map<Integer, FaultTreat> treatMap;

	/**
	 * 이벤트를 분석하여 알람을 제공합니다.<br>
	 * 여러개의 스레드가 동일한 경보에 대해서 처리할 경우가 존재하므로 sychronized를 반드시 합니다.
	 * 
	 * @param event
	 * @return 발생된 경보
	 */
	public synchronized Alarm analyze(AlarmEvent event) {

		countWork++;

		boolean isBroadcast = true;
		Mo upperMo = null;
		Mo mo;
		Alarm curAlarm;

		curAlarm = EventApi.getApi().getAlarm4Key(event.getAlarmKey());

		mo = MoApi.getApi().getMo(event.getMoNo());

		if (mo != null && mo.getUpperMoNo() > 0) {
			upperMo = MoApi.getApi().getMo(mo.getUpperMoNo());
		}

		try {

			// 경보등급이 서로 다른 경우
			// 이전 경보를 해제하고 새롭게 추가합니다.
			if (curAlarm != null && event.getAlarmLevel() != curAlarm.getAlarmLevel()
					&& event.getAlarmLevel() != AoCode.AlarmLevel.Clear.getNo()) {

				if (isChangeAlarmIfExists) {
					UpdateAlarmDbo updateAlarm = new UpdateAlarmDbo(event.getAlarmNo());
					updateAlarm.setAlarmLevel(event.getAlarmLevel());
					updateAlarm.setAlarmMsg(event.getAlarmMsg());
					updateAlarm.setChgDate(FxApi.getDate(0));
					updateAlarm(updateAlarm);
					curAlarm.setAlarmLevel(updateAlarm.getAlarmLevel());
					curAlarm.setStatus(FxEvent.STATUS.changed);

				} else {
					AlarmEvent eventClone = (AlarmEvent) event.clone();
					eventClone.setAlarmLevel(AoCode.AlarmLevel.Clear.getNo());
					analyze(eventClone);
					curAlarm = null;
				}
			}

			// 해제인 경우
			if (curAlarm != null && event.getAlarmLevel() == AoCode.AlarmLevel.Clear.getNo()) {

				ClearAlarmDbo clearAlarm = new ClearAlarmDbo(curAlarm.getAlarmNo());
				clearAlarm.setClearUserNo(0);
				clearAlarm.setClearDate(
						FxApi.getDate(event.getMstime() > 0 ? event.getMstime() : System.currentTimeMillis()));
				if (event.getClearReason() == null) {
					clearAlarm.setClearRsnNo(AoCode.ClearReason.Auto.getNo());
				} else {
					clearAlarm.setClearRsnName(event.getClearReason().name());
					clearAlarm.setClearRsnNo(event.getClearReason().getNo());
				}

				clearAlarm(curAlarm, clearAlarm);

				curAlarm.setAlarmLevel(AoCode.AlarmLevel.Clear.getNo());
				curAlarm.setStatus(FxEvent.STATUS.deleted);

			}

			// 경보가 발생인 경우
			else if (curAlarm == null && event.getAlarmLevel() != AoCode.AlarmLevel.Clear.getNo()) {
				OccurAlarmDbo occurAlarm = makeAlarm(event, mo, upperMo);

				// if (alarm.getTreatNo() > 0) {
				// faultTreat = getFaultTreat(alarm.getTreatNo());
				// }
				//
				// if (faultTreat != null) {
				// alarm.setTreatName(faultTreat.getTreatName());
				// alarm.setTreatNo(faultTreat.getTreatNo());
				// }

				// // 발생과 동시에 해제된 경우
				// Object obj = event.getProperty("ALARM_CLEAR_BEAN");
				// if (obj instanceof AlarmClearBean) {
				// AlarmClearBean alarmClearBean = (AlarmClearBean) obj;
				// alarm.setUserNoClear(alarmClearBean.getUserNoClear());
				// alarm.setHstimeClear(alarmClearBean.getHstimeClear());
				// alarm.setClearMemo(alarmClearBean.getClearMemo());
				// alarm.setClearName(alarmClearBean.getClearName());
				// alarm.setClearNo(alarmClearBean.getClearNo());
				// alarm.setClearType(alarmClearBean.getClearType());
				//
				// // 발생/해제가 되면 방송하지 않습니다.
				// isBroadcast = false;
				// }

				try {
					occurAlarm = occurAlarm(event, occurAlarm, mo, upperMo);
					curAlarm = new Alarm(occurAlarm);
					curAlarm.setStatus(FxEvent.STATUS.added);
				} catch (Exception e) {
					Logger.logger.error(e);
					occurAlarm = null;
				}

			}

			if (curAlarm != null && event.getAlarmLevel() != curAlarm.getAlarmLevel()) {
				Logger.logger.debug("ALARM-KEY(" + curAlarm.getAlarmKey() + ") DUP");
			}

			EventApi.getApi().setAlarm(curAlarm);

			// 방송 요청
			if (isBroadcast) {
				broadcast(curAlarm);
			}

			return curAlarm;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	private void broadcast(Alarm alarm) {
		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(alarm);
		}
	}

	private OccurAlarmDbo occurAlarm(AlarmEvent event, OccurAlarmDbo alarm, Mo mo, Mo node) throws Exception {

		OccurAlarmDbo alarmNew = filterPre(event, alarm, node);

		if (alarmNew == null)
			return null;

		if (node != null) {
			// alarm.setDeviceTypeName(node.getDeviceTypeName());
			// alarm.setServiceName(node.getServiceName());
		}

		doInsertAlarm(alarmNew);

		TreatFxThread.getTreatMgr().put(alarmNew);

		for (AlarmFilter filter : alarmFilterList) {
			try {
				filter.filter(alarm);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return alarmNew;

	}

	protected abstract void doInsertAlarm(OccurAlarmDbo alarm) throws Exception;

	/**
	 * 
	 * @param curAlarm
	 * @param alarm
	 * @throws Exception
	 */
	public void clearAlarm(Alarm curAlarm, ClearAlarmDbo alarm) throws Exception {

		onDeleteAlarm(alarm);

		for (AlarmFilter filter : alarmFilterList) {
			try {
				filter.filter(alarm);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}

	protected abstract void onDeleteAlarm(ClearAlarmDbo alarm) throws Exception;

	/**
	 * 현재 경보를 해제합니다.
	 * 
	 * @param alarm
	 *            현재 경보
	 * @param userNo
	 *            해제 운용자번호
	 * @param clearType
	 *            해제유형
	 * @param clearNo
	 *            해제사유번호
	 * @param clearName
	 *            해제사유명
	 * @param clearMemo
	 *            해제사유메모
	 * @return 해제된 경보
	 * @throws Exception
	 */
	public ClearAlarmDbo clearAlarm(Alarm curAlarm, int userNo, AoCode.ClearReason clearRsnNo, String clearName,
			String clearMemo) throws Exception {

		ClearAlarmDbo clsAlarm = new ClearAlarmDbo();
		clsAlarm.setClearMemo(clearMemo);
		clsAlarm.setClearRsnName(clearName);
		clsAlarm.setClearRsnNo(clearRsnNo.getNo());
		clsAlarm.setClearDate(FxApi.getDate(System.currentTimeMillis()));
		clsAlarm.setClearUserNo(userNo);
		clsAlarm.setAlarmNo(curAlarm.getAlarmNo());

		curAlarm.setStatus(FxEvent.STATUS.deleted);
		curAlarm.setAlarmLevel(AlarmLevel.Clear.getNo());

		clearAlarm(curAlarm, clsAlarm);

		EventApi.getApi().setAlarm(curAlarm);

		broadcast(curAlarm);

		return clsAlarm;
	}

	/**
	 * 관리대상이 가지고 있는 현재 경보를 해제합니다.
	 * 
	 * @param moNo
	 *            관리대상 번호
	 * @param clearType
	 *            해제사유
	 */
	public void clearAlarm(long moNo, AoCode.ClearReason clearRsnNo) {

		Logger.logger.trace("mo-no={}", moNo);

		List<Alarm> alarmList = EventApi.getApi().getAlarm4Mo(moNo);
		if (alarmList != null && alarmList.size() > 0) {
			for (Alarm alarm : alarmList) {
				try {
					clearAlarm(alarm, 0, clearRsnNo, null, null);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		Logger.logger.debug("mo-no={}, size={}", moNo, (alarmList == null ? 0 : alarmList.size()));
	}

	private void updateAlarm(UpdateAlarmDbo alarm) throws Exception {

		doUpdateAlarm(alarm);

	}

	protected abstract void doUpdateAlarm(UpdateAlarmDbo alarm) throws Exception;

	/**
	 * 알람 전반부 필터 처리
	 * 
	 * @param event
	 * @param alarm
	 * @param node
	 * @return
	 */
	private OccurAlarmDbo filterPre(AlarmEvent event, OccurAlarmDbo alarm, Mo node) {
		OccurAlarmDbo alarmNew = alarm;
		for (AlarmFilter filter : alarmFilterList) {
			try {
				alarmNew = filter.filter(event, alarmNew, node);
				if (alarmNew == null) {
					Logger.logger.debug(filter.getClass().getSimpleName(), "made null");
					break;
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return alarmNew;
	}

	private OccurAlarmDbo makeAlarm(AlarmEvent event, Mo mo, Mo upperMo) {

		AlarmCode alarmCode = EventApi.getApi().getAlarmCodeByNo(event.getAlcdNo());

		if (alarmCode == null) {
			Logger.logger.trace("alarm-code={} not found", event.getAlcdNo());
			return null;
		}

		// 장비정보가 없고 해당 MO가 없거나 비관리이면 경보를 생성하지 않습니다.
		if (upperMo != null) {
			if (upperMo.isMngYn() == false || upperMo.getAlarmCfgNo() == AlarmCfg.NO_ALARM_CFG) {
				return null;
			}
		}

		OccurAlarmDbo alarm;
		try {
			alarm = ObjectUtil.makeObject4Use(OccurAlarmDbo.class);
		} catch (Exception e) {
			Logger.logger.fail(e.toString());
			alarm = new OccurAlarmDbo();
		}

		alarm.setStatus(FxEvent.STATUS.added);
		return initAlarm(alarm, event, alarmCode, mo, upperMo);
	}

	protected OccurAlarmDbo initAlarm(OccurAlarmDbo alarm, AlarmEvent event, AlarmCode alarmCode, Mo mo, Mo upper) {

		alarm.setMoClass(mo.getMoClass());
		alarm.setMoName(mo.getMoName());
		alarm.setMoAname(mo.getMoAname());
		alarm.setMoNo(mo.getMoNo());

		if (upper != null) {
			alarm.setUpperMoName(upper.getMoName());
			alarm.setUpperMoAname(upper.getMoAname());
			alarm.setUpperMoNo(upper.getMoNo());
		}

		alarm.setAlcdNo(alarmCode.getAlcdNo());
		alarm.setAlcdName(alarmCode.getAlcdName());

		alarm.setAlarmKey(event.getAlarmKey());
		alarm.setAlarmLevel(event.getAlarmLevel());
		alarm.setAlarmMsg(event.getAlarmMsg());
		alarm.setAlarmNo(0);
		alarm.setMoInstance(event.getMoInstance());

		alarm.setRegDate(FxApi.getDate(event.getMstime()));
		alarm.setOcuDate(FxApi.getDate(event.getMstime()));

		if (event.getPsValue() != null)
			alarm.setPsValue(event.getPsValue().doubleValue());
		if (event.getCompareValue() != null)
			alarm.setCompareValue(event.getCompareValue().doubleValue());

		if (event.getPsCode() != null) {
			PsItem item = PsApi.getApi().getItem(event.getPsCode());
			if (item != null) {
				alarm.setPsName(item.getPsName());
			}
			alarm.setPsCode(event.getPsCode());
			alarm.setPsDate(event.getPsDate());
		}

		alarm.setTreatName(event.getTreatName());

		// set ownership
		if (upper instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) upper).getInloNo());
		} else if (mo instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) mo).getInloNo());
		}
		if (alarm.getInloNo() > 0) {
			MoLocation inlo = MoApi.getApi().getMoLocation(alarm.getInloNo());
			alarm.setInloName(inlo == null ? "" : inlo.getInloName());
		}

		MoModelable modelable = mo instanceof MoModelable ? (MoModelable) mo
				: upper instanceof MoModelable ? (MoModelable) upper : null;
		if (modelable != null) {
			Model model = MoApi.getApi().getModel(modelable.getModelNo());
			alarm.setModelName(model == null ? "" : model.getModelName());
			alarm.setModelNo(modelable.getModelNo());
		}

		return alarm;
	}

}
