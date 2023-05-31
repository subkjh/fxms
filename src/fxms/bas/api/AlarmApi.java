package fxms.bas.api;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.NotFoundException;
import fxms.bas.impl.dpo.ao.AlarmMap;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.impl.dpo.ao.MakeAlarmEventDfo;
import fxms.bas.mo.Moable;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.ExtraAlarm;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 알람 관련 API
 * 
 * @author subkjh
 *
 */
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
			api.reload(ReloadType.Alarm);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/** 경보필터 */

	private final AlarmMap map = AlarmMap.getInstance();
	private final AlcdMap alcdMap = AlcdMap.getInstance();

	/** 처리된 이벤트 수 */
	private long countWork;

	public AlarmApi() {

	}

	/**
	 * 알람를 확인하여 해제한다.
	 * 
	 * @param alarm          알람
	 * @param alarmRlseRsnCd 해제사유코드
	 * @param releaseMemo    해제메모
	 * @param eventTime      이벤트일시
	 * @param userNo         운용자번호
	 * @return
	 */
	public Alarm clearAlarm(Alarm alarm, ALARM_RLSE_RSN_CD alarmRlseRsnCd, String releaseMemo, long eventTime,
			int userNo) throws Exception {
		AlarmClearEvent event = new AlarmClearEvent(alarm.getAlarmNo(), eventTime, alarmRlseRsnCd, releaseMemo, userNo);
		return clearAlarm(event);
	}

	/**
	 * 현재 경보를 해제합니다.
	 * 
	 * @param alarm       알람
	 * @param clearRsnNo  해제사유번호
	 * @param releaseMemo 해제메모
	 * @param eventTime   이벤트시간(millseconds)
	 * @param userNo      사용자번호
	 * @return
	 * @throws Exception
	 */
	public abstract Alarm clearAlarm(AlarmClearEvent event) throws Exception;

	/**
	 * 
	 * @param mo
	 * @param moInstance
	 * @param alcdNo
	 * @param alarmRlseRsnCd
	 * @param releaseMemo
	 * @param eventTime
	 * @param userNo
	 */
	public void clearAlarm(Moable mo, String moInstance, int alcdNo, ALARM_RLSE_RSN_CD alarmRlseRsnCd,
			String releaseMemo, long eventTime, int userNo) {

		Alarm nowAlarm = getCurAlarm(mo, moInstance, alcdNo);

		if (nowAlarm != null) {
			if (mo == null) {
				try {
					mo = MoApi.getApi().getProjectMo();
				} catch (Exception e) {
					Logger.logger.error(e);
					return;
				}
			}

			try {
				clearAlarm(nowAlarm, alarmRlseRsnCd, releaseMemo, eventTime, userNo);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	/**
	 * 이벤트를 분석하여 알람을 발생, 해제한다.
	 * 
	 * @param event
	 * @return
	 * @throws Exception
	 */
	public abstract Alarm fireAlarm(AlarmOccurEvent event) throws Exception;

	/**
	 * 알람을 생성한다.
	 * 
	 * @param moNo       관리대상번호
	 * @param alcdNo     알람코드번호
	 * @param moInstance 인스턴스
	 * @return 생성한 알람
	 * @throws Exception
	 */
	public abstract Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws Exception;

	/**
	 * 동일 알람이 없다면 알람이벤트를 발생하여 알람을 생성한다.
	 * 
	 * @param mo
	 * @param moInstance
	 * @param alarmName
	 * @param alarmLevel
	 * @param msg
	 * @param ext
	 * @return
	 */

	public void fireAlarm(Moable mo, Object moInstance, int alcdNo, ALARM_LEVEL alarmLevel, String msg,
			ExtraAlarm ext) {
		try {
			fireAlarm(makeAlarmEvent(mo, moInstance, alcdNo, alarmLevel, msg, ext));
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	/**
	 * 경보코드 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 경보코드목록
	 */
	public abstract List<AlarmCode> getAlCds() throws Exception;

	/**
	 * 경보발생조건 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws Exception
	 */
	public abstract List<AlarmCfg> getAlCfgs(Map<String, Object> para) throws Exception;

	/**
	 * 알람번호를 이용하여 알람을 제공합니다.
	 * 
	 * @param alarmNo 알람번호
	 * @return 알람
	 */
	public Alarm getCurAlarm(long alarmNo) {
		return this.map.getAlarm(alarmNo);
	}

	/**
	 * 메모리에 가지고 있는 경보 정보를 제공합니다.
	 * 
	 * @param mo       관리MO
	 * @param instance 인스턴스
	 * @param alcdNo   경보코드
	 * @return 경보
	 */
	public Alarm getCurAlarm(Moable mo, String instance, int alcdNo) {
		return this.map.getAlarm(mo, instance, alcdNo);
	}

	/**
	 * 경보키를 이용하여 현재 경보 내용을 제공합니다.
	 * 
	 * @param alarmKey 경보키
	 * @return 현재경보
	 */
	public Alarm getCurAlarm(String alarmKey) {
		return this.map.getAlarm4Key(alarmKey);
	}

	/**
	 * 현재 알람을 조회한다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract List<Alarm> getCurAlarms(Map<String, Object> para) throws Exception;

	/**
	 * 알람을 조회한다. 해제된 알람을 포함한다.
	 * 
	 * @param alarmNo 알람번호
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract Alarm getHstAlarm(long alarmNo) throws Exception;

	/**
	 * 알람 목록을 조회한다. 해제된 알람을 포함한다.
	 * 
	 * @param startDate 발생조회시작일시
	 * @param endDate   발생조회종료일시
	 * @param para      조건
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract List<Alarm> getHstAlarms(long startDate, long endDate, Map<String, Object> para) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(Logger.makeSubString("alarm.size", this.map.size()));
		sb.append(Logger.makeSubString("work.count", this.countWork));
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {
		if (noti instanceof Alarm) {
			setCurAlarm(((Alarm) noti));
		} else {
			super.onEvent(noti);
		}
	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All || type == ReloadType.Alarm) {
			try {
				this.map.load();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}

	public AlarmOccurEvent makeAlarmEvent(Moable mo, Object moInstance, int alcdNo, ALARM_LEVEL alarmLevel, String msg,
			ExtraAlarm ext) {

		try {

			AlarmCode alarmCode = alcdMap.getAlarmCode(alcdNo);

			AlarmOccurEvent event = new MakeAlarmEventDfo().makeAlarmEvent(mo, moInstance, alarmCode, alarmLevel, msg,
					ext);

			return event;

		} catch (NotFoundException e) {
			// 알람코드가 없으때 무시한다.
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return null;
	}

	/**
	 * 알람을 맵에서 등록하거나 제거합니다.<br>
	 * alarm.getBeanStatus() == NotiBean.BEAN_STATUS_DELETE 인경우만 제거합니다. 그외는 추가
	 * 
	 * @param alarm 알람
	 */
	protected void setCurAlarm(Alarm alarm) {

		if (alarm == null)
			return;

		this.map.setAlarm(alarm);

	}

}
