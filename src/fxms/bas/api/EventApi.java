package fxms.bas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.exp.NotFoundException;
import fxms.bas.impl.dpo.ao.AlCfgMap;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.mo.Mo;
import fxms.bas.mo.Moable;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmCfgMemMatched;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmEvent;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.PsItem;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;

/**
 * 이벤트를 생성하여 Alarm을 발생, 해제 요청하는 API
 * 
 * @author subkjh
 *
 */
public class EventApi extends FxApi {

	/** use DBM */
	public static EventApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static EventApi getApi() {

		if (api != null)
			return api;

		api = makeApi(EventApi.class);

		try {
			api.reload(ReloadType.All);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	private long checkPsCount = 0L; // 성능 임계 확인 회수
	private Map<String, Integer> repeatMap = new HashMap<String, Integer>();
	private final AlcdMap alcdMap = AlcdMap.getInstance();
	private final AlCfgMap cfgMap = AlCfgMap.getInstance();

	/**
	 * 성능값을 이용하여 경보를 확인합니다.
	 * 
	 * @param mo          관리대상
	 * @param instance    인스턴스
	 * @param psId        성능번호
	 * @param psPrevValue 이전조회된 값
	 * @param psCurValue  현재값
	 * @param pollMsdate  조회일시
	 * @param msgAdd      알람메시지에 추가할 내용
	 * @param para        이벤트에 추가할 내용
	 * @return
	 */
	public void checkValue(Moable mo, String moInstance, PsItem psItem, Number psPrevValue, Number psCurValue,
			long pollMsdate, String msgAdd) {

		Logger.logger.trace("{} {} {} {}", mo, moInstance, psItem.getPsId(), psCurValue);

		checkPsCount++;

		if (mo == null || psCurValue == null) {
			Logger.logger.trace("mo={}, psCurValue={}", mo, psCurValue);
			return;
		}

		try {

			List<AlarmEvent> events = makeEventValue(mo, moInstance, psItem, psPrevValue, psCurValue, pollMsdate);
			if (events == null || events.size() == 0) {
				return;
			}

			for (AlarmEvent event : events) {
				if (event instanceof AlarmOccurEvent) {
					AlarmOccurEvent e = (AlarmOccurEvent) event;
					if (msgAdd != null) {
						e.setAlarmMsg(e.getAlarmMsg() + " " + msgAdd);
					}
					AlarmApi.getApi().fireAlarm(e);
				} else if (event instanceof AlarmClearEvent) {
					AlarmApi.getApi().clearAlarm((AlarmClearEvent) event);
				}

			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append(Logger.makeSubString("alarm.code.size", this.alcdMap.size()));
		sb.append(Logger.makeSubString("alarm.cfg.size", this.cfgMap.size()));
		sb.append(Logger.makeSubString("check.ps.count", checkPsCount));
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {
	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All || type == ReloadType.AlarmCode) {
			try {
				this.alcdMap.load();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		if (type == ReloadType.All || type == ReloadType.AlarmCfg) {
			try {
				this.cfgMap.load();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
	}

	private void clearRepeat(Moable mo, int alcdNo) {
		repeatMap.remove(mo.getMoNo() + "." + alcdNo);
	}

	/**
	 * 알람조건번호를 찾는다.
	 * 
	 * @param mo
	 * @return 알람조건번호
	 */
	private int getAlarmCfgNo(Moable mo) {

		int alarmCfgNo = mo.getAlarmCfgNo();

		if (alarmCfgNo <= 0 && Mo.hasUpper(mo)) {
			try {
				Mo upperMo = MoApi.getApi().getMo(mo.getUpperMoNo());
				alarmCfgNo = upperMo.getAlarmCfgNo();
			} catch (Exception e) {
			}
		}

		return alarmCfgNo;

	}

	/**
	 * 
	 * @param alcdNo
	 * @return 경보코드 설정 정보
	 */
	private AlarmCode getAlarmCode(int alcdNo) {
		try {
			return this.alcdMap.getAlarmCode(alcdNo);
		} catch (NotFoundException e) {
			return null;
		}
	}

	private int getRepeat(Moable mo, int alcdNo) {
		Integer cnt = repeatMap.get(mo.getMoNo() + "." + alcdNo);
		return cnt == null ? 0 : cnt.intValue();
	}

	/**
	 * 이벤트를 생성합니다.
	 * 
	 * @param mo         관리대상
	 * @param moInstance 인스턴스
	 * @param alcdNo     경보코드
	 * @param msg        경보메세지
	 * @param valueBase  비교값
	 * @param preValue   조회한 값(이전)
	 * @param value      조회한 값
	 * @param alarmLevel 경보등급
	 * @param etc        기타값
	 * @return 이벤트
	 */
	private AlarmOccurEvent makeEvent(Moable mo, String moInstance, int alcdNo, String msg, Number valueBase,
			Number psValuePrev, Number psValueCur, int alarmLevel, Number etc) {

		AlarmCode alarmCode = getAlarmCode(alcdNo);

		// 메시지가 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if ((msg == null || msg.length() == 0) && alarmCode != null)
			msg = alarmCode.getAlarmMsg();

		// 경보등급이 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (alarmLevel < 0)
			alarmLevel = alarmCode.getAlarmLevel().getAlarmLevel();

		AlarmOccurEvent event = new AlarmOccurEvent(mo.getMoNo(), moInstance, alcdNo);
		event.setAlarmLevel(alarmLevel);
		event.setAlarmMsg(makeMsg(msg, mo, moInstance, valueBase, psValuePrev, psValueCur, etc));
		event.setCmprVal(valueBase);
		event.setPsVal(psValueCur);

		return event;
	}

	/**
	 * 성능항목에 대한 경보조건이 없을 경우 이미 존재하는 경보조건을 없애기 위한 이벤트 생성
	 * 
	 * @param psId     성능항목번호
	 * @param mo       관리대상
	 * @param instance 인스턴스
	 * @param valueCur 현재값
	 * @return 경보이벤트 목록
	 * @throws Exception
	 */
	private List<AlarmEvent> makeEventClearValue(PsItem psItem, Moable mo, String instance, Number valueCur,
			ALARM_RLSE_RSN_CD clearReason) throws Exception {

		Alarm alarm;
		List<Integer> alcdNos = this.alcdMap.getAlcdNos(psItem.getPsId());

		if (alcdNos == null)
			return null;

		List<AlarmEvent> eventList = new ArrayList<>();

		for (int alcdNo : alcdNos) {

			alarm = AlarmApi.getApi().getCurAlarm(mo, instance, alcdNo);

			if (alarm != null) {
				eventList.add(new AlarmClearEvent(alarm.getAlarmNo(), System.currentTimeMillis(), clearReason,
						String.valueOf(valueCur), User.USER_NO_SYSTEM));
			}

		}

		return eventList.size() > 0 ? eventList : null;

	}

	/**
	 * 입력된 내용으로 이벤트를 생성합니다.
	 * 
	 * @param cfg
	 * @param mo
	 * @param instance
	 * @param alarmCode
	 * @param psId
	 * @param valuePre
	 * @param valueCur
	 * @return
	 * @throws Exception
	 */
	private AlarmEvent makeEventValue(AlarmCfg cfg, Moable mo, String instance, AlarmCode alarmCode, PsItem psItem,
			Number valuePre, Number valueCur) throws Exception {

		int alcdNo = alarmCode.getAlcdNo();
		String psId = psItem.getPsId();
		AlarmCfgMemMatched cfgMem;
		Alarm alarmOld;

		alarmOld = AlarmApi.getApi().getCurAlarm(mo, instance, alcdNo);

		cfgMem = cfg.getMatchMember(psId, alcdNo, valuePre, valueCur, mo);

		Logger.logger.trace("alarm-old={}, alarm-cfg-mem={}", alarmOld, cfgMem);

		// 임계 비교 결과 해당 사항이 없으면 누적 카운트를 떨고, 현재 경보가 존재하면
		// 일정시간이 지난 후 시스템이 자동으로 해제하는 경보가 아닌 경우는 이벤트를 발생하여 경보를 해제한다.
		if (cfgMem == null) {
			clearRepeat(mo, alcdNo);
			if (alarmOld != null) {
				return new AlarmClearEvent(alarmOld.getAlarmNo(), System.currentTimeMillis(),
						ALARM_RLSE_RSN_CD.NotFoundAlarmCfg, String.valueOf(valueCur), User.USER_NO_SYSTEM);
			}
		}

		// 신규 발생 경보인 경우
		else if (cfgMem != null && alarmOld == null) {
			if (cfgMem.getRepeatTimes() > 1) {
				int repeat = getRepeat(mo, alcdNo);
				repeat++;
				if (repeat < cfgMem.getRepeatTimes()) {
					setRepeat(mo, alcdNo, repeat);
				}
			}
			AlarmOccurEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCmprVal(), //
					valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.getPsVal());
			event.setAlarmCfgNo(cfg.getAlarmCfgNo());
			event.setFpactCd(cfgMem.getFpactCd());
			event.setPsId(psId);
			return event;
		}

		// 경보 등급이 변경되었을 경우
		else if (cfgMem != null && alarmOld != null && cfgMem.getAlarmLevel() != alarmOld.getAlarmLevel()) {

			// 동일 경보이나 등급이 다른 경우 이전 내용을 제거하고 새롭게 보냅니다.
			AlarmOccurEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCmprVal(), //
					valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.getPsVal());
			event.setAlarmCfgNo(cfg.getAlarmCfgNo());
			event.setFpactCd(cfgMem.getFpactCd());
			event.setPsId(psId);
			event.setAlarm(alarmOld);
			return event;

		}

		return null;

	}

	/**
	 * 
	 * @param mo
	 * @param instance
	 * @param psItem
	 * @param psPrevValue
	 * @param psCurValue
	 * @param mstimePerf
	 * @return
	 * @throws Exception
	 */
	private List<AlarmEvent> makeEventValue(Moable mo, String instance, PsItem psItem, Number psPrevValue,
			Number psCurValue, long mstimePerf) throws Exception {

		long psDate = DateUtil.getDtm(mstimePerf);
		int alarmCfgNo = getAlarmCfgNo(mo);

		AlarmCode alarmCode;
		AlarmEvent event;

		AlarmCfg cfg = cfgMap.getAlarmCfg(alarmCfgNo);
		if (cfg == null) {
			alarmCfgNo = cfgMap.getAlarmCfgNo(mo.getMoClass(), mo.getMoType());
			cfg = cfgMap.getAlarmCfg(alarmCfgNo);
		}

		if (cfg == null || cfg.getMemSize() == 0) {
			Logger.logger.trace("alarm-cfg={} not found or no conditions", alarmCfgNo);
			return makeEventClearValue(psItem, mo, instance, psCurValue, ALARM_RLSE_RSN_CD.NotFoundAlarmCfg);
		}

		// 성능과 관련된 경보코드가 없을 경우 이미 존재한 경보를 해제합니다.
		List<Integer> alcdNos = cfg.getAlcdNo(psItem.getPsId());

		if (alcdNos == null || alcdNos.size() == 0) {

			return makeEventClearValue(psItem, mo, instance, psCurValue, ALARM_RLSE_RSN_CD.NotFoundAlarmCfg);

		} else {

			List<AlarmEvent> eventList = new ArrayList<>();

			for (int alcdNo : alcdNos) {
				alarmCode = getAlarmCode(alcdNo);

				if (alarmCode == null)
					continue;

				event = makeEventValue(cfg, mo, instance, alarmCode, psItem, psPrevValue, psCurValue);
				if (event == null)
					continue;

				if (event instanceof AlarmOccurEvent) {
					((AlarmOccurEvent) event).setPsDate(psDate);
				}

				eventList.add(event);
			}

			return eventList.size() == 0 ? null : eventList;
		}
	}

	/**
	 * 이벤트 메시지를 만든다.<br>
	 * 
	 * @param orgMsg     원시 메시지
	 * @param mo         관리대상
	 * @param moInstance 발생위치
	 * @param baseValue  기준값
	 * @param preValue   이전 조회값
	 * @param value      조회값
	 * @return 만들어진 이벤트 메시지
	 */
	private String makeMsg(String orgMsg, Moable mo, String moInstance, Object baseValue, Object preValue, Object value,
			Object etc) {

		if (orgMsg == null)
			return mo.getMoName() + " AlarmEvent";

		String ret = orgMsg.replaceAll("%value%", toString(value));
		ret = ret.replaceAll("%valueBase%", toString(baseValue));
		ret = ret.replaceAll("%valuePre%", toString(preValue));
		ret = ret.replaceAll("%valueCur%", toString(value));
		ret = ret.replaceAll("%etc%", toString(etc));
		ret = ret.replaceAll("%moInstance%", toString(moInstance));

		return ret;
	}

	/**
	 * 
	 * @param mo
	 * @param alcdNo
	 * @param count
	 */
	private void setRepeat(Moable mo, int alcdNo, int count) {
		repeatMap.put(mo.getMoNo() + "." + alcdNo, count);
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	private String toString(Object val) {
		if (val == null)
			return "";
		else if (val instanceof Float)
			return ((Float) val).longValue() + "";
		else if (val instanceof Double)
			return ((Double) val).longValue() + "";
		else if (val instanceof Integer)
			return ((Integer) val).intValue() + "";
		else if (val instanceof Byte)
			return ((Byte) val).intValue() + "";
		else if (val instanceof Number)
			return ((Number) val).longValue() + "";
		else
			return val.toString();
	}

}
