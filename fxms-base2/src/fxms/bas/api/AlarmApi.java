package fxms.bas.api;

import java.util.List;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.AlarmFilter;
import fxms.bas.ao.AoCode;
import fxms.bas.ao.AoCode.AlarmLevel;
import fxms.bas.ao.treat.TreatFxThread;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.ao.vo.AlarmCfg;
import fxms.bas.ao.vo.AlarmCode;
import fxms.bas.ao.vo.ClearAlarm;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasAlarmCfg;
import fxms.bas.mo.property.MoOwnership;

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

		try {
			api.reload();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/** 경보필터 */
	private List<AlarmFilter> alarmFilterList;

	/** 처리된 이벤트 수 */
	private long countWork;

	/** 동일한 경보인 경우 새롭게 만들것인지 아니면 수정할 것인지 설정. 기본은 false */
	private boolean isChangeAlarmIfExists = false;

	/**
	 * 이벤트를 분석하여 알람을 제공합니다.<br>
	 * 여러개의 스레드가 동일한 경보에 대해서 처리할 경우가 존재하므로 sychronized를 반드시 합니다.
	 * 
	 * @param event
	 * @return 발생된 경보
	 */
	public synchronized Alarm analyze(AlarmEvent event) {

		countWork++;

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
					doUpdateAlarm(event, FxApi.getDate(0));
					curAlarm.setAlarmLevel(event.getAlarmLevel());
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

				if (event.getClearReason() == null) {
					clearAlarm(curAlarm, User.USER_NO_SYSTEM, AoCode.ClearReason.Auto, null, null);
				} else {
					clearAlarm(curAlarm, User.USER_NO_SYSTEM, event.getClearReason(), null, null);
				}

				curAlarm.setAlarmLevel(AoCode.AlarmLevel.Clear.getNo());
				curAlarm.setStatus(FxEvent.STATUS.deleted);
			}

			// 경보가 발생인 경우
			else if (curAlarm == null && event.getAlarmLevel() != AoCode.AlarmLevel.Clear.getNo()) {

				OccurAlarm occurAlarm = makeOccurAlarm(event, mo, upperMo);

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
					occurAlarm = insertAlarm(event, occurAlarm, mo, upperMo);
					curAlarm = makeBroadcastAlarmClass(occurAlarm);
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
			if (event.isBroadcast()) {
				broadcast(curAlarm);
			}

			return curAlarm;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	/**
	 * 현재 경보를 해제합니다.
	 * 
	 * @param alarm     현재 경보
	 * @param userNo    해제 운용자번호
	 * @param clearType 해제유형
	 * @param clearNo   해제사유번호
	 * @param clearName 해제사유명
	 * @param clearMemo 해제사유메모
	 * @return 해제된 경보
	 * @throws Exception
	 */
	public ClearAlarm clearAlarm(Alarm a, int userNo, AoCode.ClearReason clearRsnNo, String clearName, String clearMemo)
			throws Exception {

		ClearAlarm clsAlarm = makeClearAlarmClass();
		clsAlarm.setClearMemo(clearMemo);
		clsAlarm.setClearRsnName(clearName);
		clsAlarm.setClearRsnNo(clearRsnNo.getNo());
		clsAlarm.setClearDate(FxApi.getDate(System.currentTimeMillis()));
		clsAlarm.setClearUserNo(userNo);
		clsAlarm.setAlarmNo(a.getAlarmNo());

		a.setStatus(FxEvent.STATUS.deleted);
		a.setAlarmLevel(AlarmLevel.Clear.getNo());

		for (AlarmFilter filter : alarmFilterList) {
			try {
				filter.filter(clsAlarm);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		doDeleteAlarm(clsAlarm, a);

		EventApi.getApi().setAlarm(a);

		broadcast(a);

		return clsAlarm;
	}

	/** 경보조치맵, 키:경보조치번호 */
	// private Map<Integer, FaultTreat> treatMap;

	/**
	 * 관리대상이 가지고 있는 현재 경보를 해제합니다.
	 * 
	 * @param moNo      관리대상 번호
	 * @param clearType 해제사유
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

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName() + " ");
		sb.append("FILTER-SIZE(" + (alarmFilterList == null ? 0 : alarmFilterList.size()) + ")");
		sb.append("COUNT-EVENT(" + countWork + ")");

		return sb.toString();
	}

	protected abstract void doDeleteAlarm(ClearAlarm clearAlarm, Alarm curAlarm) throws Exception;

	protected abstract void doInitOccurAlarm(OccurAlarm alarm, AlarmEvent event, AlarmCode alarmCode, Mo mo, Mo upper);

	protected abstract void doInsertAlarm(OccurAlarm alarm) throws Exception;

	protected abstract void doUpdateAlarm(AlarmEvent event, long chgDate) throws Exception;

	@Override
	protected void initApi() throws Exception {
		alarmFilterList = FxActorParser.getParser().getActorList(AlarmFilter.class);
	}

	protected abstract Alarm makeBroadcastAlarmClass(OccurAlarm a);

	protected ClearAlarm makeClearAlarmClass() throws Exception {
		return ObjectUtil.makeObject4Use(ClearAlarm.class);
	}

	protected OccurAlarm makeOccurAlarmClass() throws Exception {
		return ObjectUtil.makeObject4Use(OccurAlarm.class);
	}

	@Override
	protected void reload() throws Exception {
	}

	private void broadcast(Alarm alarm) {
		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(alarm);
		}
	}

	/**
	 * 알람 전반부 필터 처리
	 * 
	 * @param event
	 * @param alarm
	 * @param node
	 * @return
	 */
	private OccurAlarm filter(AlarmEvent event, OccurAlarm alarm, Mo node) {
		OccurAlarm alarmNew = alarm;
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

	private void initOccurAlarm(OccurAlarm alarm, AlarmEvent event, AlarmCode alarmCode, Mo mo, Mo upper) {

		alarm.setMoNo(mo.getMoNo());
		if (upper != null) {
			alarm.setUpperMoNo(upper.getMoNo());
		}
		alarm.setAlcdNo(alarmCode.getAlcdNo());
		alarm.setAlarmKey(event.getAlarmKey());
		alarm.setAlarmLevel(event.getAlarmLevel());
		alarm.setMoInstance(event.getMoInstance());
		alarm.setTreatName(event.getTreatName());

		// set ownership
		if (upper instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) upper).getInloNo());
		} else if (mo instanceof MoOwnership) {
			alarm.setInloNo(((MoOwnership) mo).getInloNo());
		}

		doInitOccurAlarm(alarm, event, alarmCode, mo, upper);
	}

	private OccurAlarm insertAlarm(AlarmEvent event, OccurAlarm alarm, Mo mo, Mo node) throws Exception {

		OccurAlarm alarmNew = filter(event, alarm, node);

		if (alarmNew == null)
			return null;

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

	private OccurAlarm makeOccurAlarm(AlarmEvent event, Mo mo, Mo upperMo) throws Exception {

		AlarmCode alarmCode = EventApi.getApi().getAlarmCodeByNo(event.getAlcdNo());

		if (alarmCode == null) {
			Logger.logger.trace("alarm-code={} not found", event.getAlcdNo());
			return null;
		}

		// 장비정보가 없고 해당 MO가 없거나 비관리이면 경보를 생성하지 않습니다.
		if (upperMo != null && upperMo instanceof HasAlarmCfg) {
			if (upperMo.isMngYn() == false || ((HasAlarmCfg) upperMo).getAlarmCfgNo() == AlarmCfg.NO_ALARM_CFG) {
				return null;
			}
		}

		OccurAlarm alarm = makeOccurAlarmClass();

		initOccurAlarm(alarm, event, alarmCode, mo, upperMo);

		alarm.setStatus(FxEvent.STATUS.added);

		return alarm;
	}

}
