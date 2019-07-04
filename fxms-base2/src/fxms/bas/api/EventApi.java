package fxms.bas.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.bas.ao.AlarmEvent;
import fxms.bas.ao.AoCode;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.ao.vo.AlarmCfg;
import fxms.bas.ao.vo.AlarmCfgMem;
import fxms.bas.ao.vo.AlarmCode;
import fxms.bas.ao.vo.OccurAlarm;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasAlarmCfg;

public abstract class EventApi extends FxApi {

	/** use DBM */
	public static EventApi api;

	/** 경보등급 */
	public static final String PARA_ALARM_LEVEL = "alarmLevel";
	/** 비교 값 */
	public static final String PARA_VALUE_BASE = "valueBase";
	/** 수집 값 */
	public static final String PARA_VALUE = "value";
	/** 경보조치명 */
	public static final String PARA_TREAT_NAME = "treatName";
	/** 알람 키 */
	public static final String PARA_ALARM_KEY = "alarmKey";
	/** 이벤트 발생시간 */
	public static final String PARA_MSTIME_EVENT = "mstimeEvent";
	/** 이벤트 발생시간 ( 외부 시스템에서 ) */
	public static final String PARA_MSTIME_EVENT_IN_NODE = "mstimeEventInNode";

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
			api.reload();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/** 현재 경보 ( key : entity ) */
	private Map<String, Alarm> keyMap;
	/** 현재 경보 ( key : eventNo ) */
	private Map<Long, Alarm> noMap;

	/** 경보 코드. key=경보번호 */
	private Map<Integer, AlarmCode> codeMap;
	/** 경보코드. key=경보명 */
	private Map<String, AlarmCode> nameMap;

	/** 성능 임계 확인 회수 */
	private long checkPsCount = 0L;
	/** 경보명 대소문자 구분여부. true이면 구분. */
	private boolean isCaseSensitive = true;
	/**
	 * PERF_CODE = List<ALARM_CODE>
	 */
	private Map<String, List<Integer>> psAlcdMap;

	private Map<String, Integer> repeatMap = new HashMap<String, Integer>();

	/** 경보조건템플릿맵. 키:경보조건템플릿번호 */
	private Map<Integer, AlarmCfg> alarmCfgMap;
	private Map<String, AlarmCfg> moClassAlarmCfgMap;

	public EventApi() {

		keyMap = Collections.synchronizedMap(new HashMap<String, Alarm>());
		noMap = Collections.synchronizedMap(new HashMap<Long, Alarm>());
		codeMap = Collections.synchronizedMap(new HashMap<Integer, AlarmCode>());
		nameMap = Collections.synchronizedMap(new HashMap<String, AlarmCode>());
		moClassAlarmCfgMap = new HashMap<String, AlarmCfg>();
		alarmCfgMap = new HashMap<Integer, AlarmCfg>();
		psAlcdMap = Collections.synchronizedMap(new HashMap<String, List<Integer>>());

	}

	public List<Alarm> getAlarmList4Mo(long moNo) {
		List<Alarm> list = new ArrayList<Alarm>();
		for (Alarm a : noMap.values()) {
			if (a.getMoNo() == moNo || a.getUpperMoNo() == moNo) {
				list.add(a);
			}
		}

		return list;
	}

	public Alarm check(Mo mo, String moInstance, Enum<?> alarmCodeName, String msg, Map<String, Object> para) {

		AlarmCode alarmCode = getAlarmCodeByName(alarmCodeName.name());

		if (alarmCode == null) {
			Logger.logger.trace("alarm-name='{}' NOT FOUND", alarmCodeName);
			return null;
		}
		return check(mo, moInstance, alarmCode, msg, para);

	}

	public Alarm check(Mo mo, String moInstance, String alarmCodeName, String msg, Map<String, Object> para) {

		AlarmCode alarmCode = getAlarmCodeByName(alarmCodeName);

		if (alarmCode == null) {
			Logger.logger.trace("alarm-name='{}' NOT FOUND", alarmCodeName);
			return null;
		}
		return check(mo, moInstance, alarmCode, msg, para);

	}

	public Alarm check(Mo mo, String moInstance, int alcdNo, String msg, Map<String, Object> para) {

		AlarmCode alarmCode = getAlarmCodeByNo(alcdNo);

		if (alarmCode == null) {
			Logger.logger.trace("alarm-code='{}' NOT FOUND", alcdNo);
			return null;
		}

		return check(mo, moInstance, alarmCode, msg, para);

	}

	private Alarm check(Mo mo, String moInstance, AlarmCode alarmCode, String msg, Map<String, Object> para) {

		if (mo == null) {
			Logger.logger.trace("MO NOT DEFIND INSTANCE({}) ALARM-NAME({})", moInstance, alarmCode.getAlcdName());
			return null;
		}

		if (mo.isMngYn() == false) {
			Logger.logger.trace("MO({}) UNMANAGED", mo);
			return null;
		}

		// 메시지가 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (msg == null || msg.length() == 0)
			msg = alarmCode.getAlarmMsg();

		AoCode.AlarmLevel foLevel = null;
		Object valueBase = null;
		Object value = null;
		String treatName = null;
		String alarmKey = null;
		long mstimeEvent = 0L;

		if (para != null) {
			Object tmp = para.get(PARA_ALARM_LEVEL);
			foLevel = (tmp == null ? null : (tmp instanceof AoCode.AlarmLevel ? (AoCode.AlarmLevel) tmp : null));
			valueBase = para.get(PARA_VALUE_BASE);
			value = para.get(PARA_VALUE);
			treatName = getString(para, PARA_TREAT_NAME);
			alarmKey = getString(para, PARA_ALARM_KEY);
			mstimeEvent = getLong(para, PARA_MSTIME_EVENT, 0L);
		}

		// 경보등급이 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (foLevel == null)
			foLevel = alarmCode.getAlarmLevel();

		Alarm alarm = getAlarm(mo, moInstance, alarmCode.getAlcdNo());

		AlarmEvent event = makeEvent(mo.getMoNo(), moInstance, alarmCode.getAlcdNo());
		event.setAlarmLevel(foLevel.getNo());

		if (alarm != null) {
			event.setAlarmNo(alarm.getAlarmNo());
		}

		// a0.setMstimeInNode(mstimeEventInNode);
		event.setAlarmMsg(makeEventMsg(msg, mo, moInstance, valueBase, value, value, null));
		event.setTreatName(treatName != null ? treatName : alarmCode.getTreatName());

		if (alarmKey != null)
			event.setAlarmKey(alarmKey);

		event.setPsValue(0);
		event.setCompareValue(0);
		event.setMstime(mstimeEvent <= 0 ? System.currentTimeMillis() : mstimeEvent);
		event.setMap(para);

		if (alarm != null && alarm.getAlarmLevel() == foLevel.getNo()) {
			Logger.logger.trace("alarm={} exists", alarm.getAlarmKey());
			return null;
		}

		// 경보이벤트를 보내고 경보를 받습니다.
		Alarm alarmNew;
		try {
			alarmNew = sendEvent(event);
			return alarmNew;
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return null;
	}

	/**
	 * MO에 해당 경보코드를 가지는 모든 경보를 해제합니다.
	 * 
	 * @param mo
	 * @param alcdNo
	 */
	public void checkClear(Mo mo, int alcdNo, AoCode.ClearReason clearReason) {

		if (mo == null)
			return;

		try {
			Alarm alarmArr[] = getAlarmArray();

			for (Alarm a : alarmArr) {
				if (a.getMoNo() == mo.getMoNo() && a.getAlcdNo() == alcdNo) {
					clear(a, 0, clearReason);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	public void checkClear(Mo mo, String alarmCodeName, AoCode.ClearReason clearReason) {

		if (mo == null)
			return;

		AlarmCode alarmCode = getAlarmCodeByName(alarmCodeName);

		if (alarmCode == null) {
			Logger.logger.trace("alarm-name='{}' NOT FOUND", alarmCodeName);
			return;
		}

		try {
			Alarm alarmArr[] = getAlarmArray();

			for (Alarm a : alarmArr) {
				if (a.getMoNo() == mo.getMoNo() && a.getAlcdNo() == alarmCode.getAlcdNo()) {
					clear(a, 0, clearReason);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 해당 경보가 존재하면 해제 이벤트를 보내고 해제된 내용을 메모리에서 제거합니다.
	 * 
	 * @param mo
	 *            발생MO
	 * @param instance
	 *            세부대상
	 * @param alcdNo
	 *            경보코드
	 */
	public Alarm checkClear(Mo mo, String moInstance, int alcdNo, AoCode.ClearReason clearReason) {
		if (mo == null)
			return null;
		return checkClear4Key(AlarmEvent.getAlarmKey(mo.getMoNo(), moInstance, alcdNo), clearReason);
	}

	public Alarm checkClear(Mo mo, String moInstance, String alarmCodeName, AoCode.ClearReason clearReason) {
		if (mo == null)
			return null;

		AlarmCode alarmCode = getAlarmCodeByName(alarmCodeName);

		if (alarmCode == null) {
			Logger.logger.trace("alarm-name='{}' NOT FOUND", alarmCodeName);
			return null;
		}

		return checkClear4Key(AlarmEvent.getAlarmKey(mo.getMoNo(), moInstance, alarmCode.getAlcdNo()), clearReason);
	}

	/**
	 * 
	 * @param alarmKey
	 */
	public Alarm checkClear4Key(String alarmKey, AoCode.ClearReason clearReason) {
		return checkClear4Key(alarmKey, 0, clearReason);
	}

	/**
	 * 해제시간 지정 기능 추가
	 * 
	 * @param alarmKey
	 *            경보키
	 * @param mstimeClear
	 *            해제시간
	 * @return 해제된 경보
	 */
	public Alarm checkClear4Key(String alarmKey, long mstimeClear, AoCode.ClearReason clearReason) {

		if (alarmKey == null)
			return null;

		Alarm alarm = getAlarm4Key(alarmKey);
		if (alarm != null) {

			AlarmEvent event = makeClearEvent(alarm, clearReason);
			if (mstimeClear > 0) {
				event.setMstime(mstimeClear);
			}

			if (event != null) {
				Alarm alarmOld = sendEvent(event);
				return alarmOld;
			} else {
				Logger.logger.fail("ALARM({}) CANNOT CLEAR", alarmKey);
			}
		}

		return null;
	}

	/**
	 * 성능값을 이용하여 경보를 확인합니다.
	 * 
	 * @param mo
	 *            관리대상
	 * @param instance
	 *            인스턴스
	 * @param psCode
	 *            성능번호
	 * @param psPrevValue
	 *            이전조회된 값
	 * @param psCurValue
	 *            현재값
	 * @param pollMsdate
	 *            조회일시
	 * @return 관련된 경보 목록
	 */
	public List<Alarm> checkPsValue(Mo mo, String instance, String psCode, Number psPrevValue, Number psCurValue, long pollMsdate) {
		return checkPsValue(mo, instance, psCode, psPrevValue, psCurValue, pollMsdate, 0, 0, null, null);
	}

	public List<Alarm> checkPsValue(Mo mo, String instance, Enum<?> psCode, Number psPrevValue, Number psCurValue, long pollMsdate) {
		return checkPsValue(mo, instance, psCode.name(), psPrevValue, psCurValue, pollMsdate, 0, 0, null, null);
	}

	public List<Alarm> checkPsValue(Mo mo, String moInstance, String psCode, Number psPrevValue, Number psCurValue,
			long pollMsdate, int dpPerfNo, double dpValue, Map<String, Object> para, String msgAdd) {

		Logger.logger.trace("{} {} {} {}", mo, moInstance, psCode, psCurValue);

		checkPsCount++;

		if (mo == null) {
			Logger.logger.trace("NOT FOUND MO");
			return null;
		}

		// 값이 설정되지 않은 경우 null 제공
		if (psCurValue == null) {
			Logger.logger.trace("MO({}) DO NOT HAVE SOME VALUE", mo);
			return null;
		}

		try {

			List<AlarmEvent> events = makeEventPs(mo, moInstance, psCode, psPrevValue, psCurValue, pollMsdate, dpPerfNo, dpValue);
			if (events == null || events.size() == 0)
				return null;

			Alarm alarm;

			List<Alarm> alarmList = null;

			for (AlarmEvent event : events) {

				event.setMap(para);

				if (msgAdd != null) {
					event.setAlarmMsg(event.getAlarmMsg() + " " + msgAdd);
				}

				Logger.logger.trace(event.toString());

				alarm = doSendEvent(event);
				if (alarm != null) {

					setAlarm(alarm);

					if (alarmList == null)
						alarmList = new ArrayList<Alarm>();

					alarmList.add(alarm);

				}
			}

			return alarmList;

		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return null;
	}

	public List<Alarm> checkPsValue(Mo mo, String instance, String psCode, Number valuePrev, Number valueCur, long pollMsdate,
			String msgAdd) {
		return checkPsValue(mo, instance, psCode, valuePrev, valueCur, pollMsdate, 0, 0, null, msgAdd);
	}

	/**
	 * 경보를 해제합니다.
	 * 
	 * @param alarm
	 *            해제할 경보
	 * @param mstimeClear
	 *            해제시간
	 * @return 해제된 경보
	 */
	public Alarm clear(Alarm alarm, long mstimeClear, AoCode.ClearReason clearReason) {

		if (alarm != null) {

			AlarmEvent event = makeClearEvent(alarm, clearReason);
			if (mstimeClear > 0) {
				event.setMstime(mstimeClear);
			}

			Alarm alarmOld = sendEvent(event);
			return alarmOld;
		}

		return null;
	}

	/**
	 * 경보발생조건 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 경보발생조건 목록
	 */
	public abstract List<AlarmCfg> doSelectAlarmCfgAll(Map<String, Object> parameters) throws Exception;

	/**
	 * 경보코드 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 경보코드목록
	 */
	public abstract List<AlarmCode> doSelectAlarmCodeAll() throws Exception;

	/**
	 * 미해제 경보 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 현재 경보 목록
	 */
	public abstract List<Alarm> doSelectCurAlarmAll(Map<String, Object> parameters) throws Exception;

	/**
	 * 알람번호를 이용하여 알람을 제공합니다.
	 * 
	 * @param alarmNo
	 *            알람번호
	 * @return 알람
	 */
	public Alarm getAlarm(long alarmNo) {
		return noMap.get(alarmNo);
	}

	public abstract OccurAlarm doSelectAlarmHst(long alarmNo) throws Exception;

	/**
	 * 메모리에 가지고 있는 경보 정보를 제공합니다.
	 * 
	 * @param mo
	 *            관리MO
	 * @param instance
	 *            인스턴스
	 * @param alcdNo
	 *            경보코드
	 * @return 경보
	 */
	public Alarm getAlarm(Mo mo, String instance, int alcdNo) {
		return keyMap.get(AlarmEvent.getAlarmKey(mo.getMoNo(), instance, alcdNo));
	}

	/**
	 * 경보코드로 발생된 현재 경보를 찾아 제공합니다.
	 * 
	 * @param alcdNo
	 *            찾을 경보 코드
	 * @return 경보코드 목록
	 */
	public List<Alarm> getAlarm4Code(int alcdNo) {

		List<Alarm> alarmList = new ArrayList<Alarm>();

		Alarm alarmArr[] = getAlarmArray();

		for (Alarm alarm : alarmArr) {
			if (alarm.getAlcdNo() == alcdNo) {
				alarmList.add(alarm);
			}
		}

		return alarmList;
	}

	/**
	 * 경보키를 이용하여 현재 경보 내용을 제공합니다.
	 * 
	 * @param alarmKey
	 *            경보키
	 * @return 현재경보
	 */
	public Alarm getAlarm4Key(String alarmKey) {
		return keyMap.get(alarmKey);
	}

	/**
	 * 관리대상의 경보목록을 조회합니다.
	 * 
	 * @param moNo
	 *            관리대상번호
	 * @return 경보목록
	 */
	public List<Alarm> getAlarm4Mo(long moNo) {

		List<Alarm> alarmList = new ArrayList<Alarm>();

		Alarm alarmArr[] = getAlarmArray();

		for (Alarm alarm : alarmArr) {
			if (alarm.getMoNo() == moNo || alarm.getUpperMoNo() == moNo) {
				alarmList.add(alarm);
			}
		}

		return alarmList;
	}

	/**
	 * 메모리에 가지고 있는 현재 경보 목록을 제공합니다.
	 * 
	 * @return 현재경보목록
	 */
	public Alarm[] getAlarmArray() {
		return keyMap.values().toArray(new Alarm[keyMap.size()]);
	}

	/**
	 * 경보발생조건을 조회합니다.
	 * 
	 * @param alarmCfgNo
	 *            경보조건번호
	 * @return 경보조건
	 */
	public AlarmCfg getAlarmCfg(int alarmCfgNo) {
		try {
			return alarmCfgMap.get(alarmCfgNo);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	/**
	 * 경보조건 목록을 조회합니다.
	 * 
	 * @return 경보조건 목록
	 */
	public List<AlarmCfg> getAlarmCfgList() {
		return new ArrayList<AlarmCfg>(alarmCfgMap.values());
	}

	public AlarmCode getAlarmCodeByName(String alarmName) {
		if (alarmName == null)
			return null;

		if (isCaseSensitive == false) {
			return nameMap.get(alarmName.toLowerCase());
		} else {
			return nameMap.get(alarmName);
		}
	}

	/**
	 * 
	 * @param alcdNo
	 * @return 경보코드 설정 정보
	 */
	public AlarmCode getAlarmCodeByNo(int alcdNo) {
		return codeMap.get(alcdNo);
	}

	public List<AlarmCode> getAlarmCodeList(String moClass) {
		List<AlarmCode> list = new ArrayList<AlarmCode>();

		for (AlarmCode ac : nameMap.values()) {
			if (moClass != null && ("all".equalsIgnoreCase(moClass) || moClass.equals(ac.getTargetMoClass()))) {
				list.add(ac);
			}
		}

		return list;
	}

	public int getDefaultAlarmCfg(String moClass) {
		AlarmCfg cfg = moClassAlarmCfgMap.get(moClass);
		return cfg == null ? 0 : cfg.getAlarmCfgNo();
	}

	/**
	 * 관리대상에 설정된 경보조건번호를 찾아 제공합니다.
	 * 
	 * @param mo
	 *            관리대상
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo(long moNo) {

		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null)
			return -1;

		if (mo instanceof HasAlarmCfg) {

			int alarmCfgNo = ((HasAlarmCfg) mo).getAlarmCfgNo();

			if (alarmCfgNo <= 0) {
				if (mo.getUpperMoNo() > 0) {
					Mo node = MoApi.getApi().getMo(mo.getUpperMoNo());
					if (node != null && node instanceof HasAlarmCfg)
						alarmCfgNo = ((HasAlarmCfg) node).getAlarmCfgNo();
				}
			}

			return alarmCfgNo;

		} else {
			return -1;
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append(", alarm-code-size=" + codeMap.size());
		sb.append(", alarm-size=" + (keyMap.size() + "-" + noMap.size()));
		sb.append(", alarm-cfg-size=" + (alarmCfgMap == null ? 0 : alarmCfgMap.size()));
		sb.append(", check-ps-count=" + checkPsCount);

		if (level.contains(LOG_LEVEL.trace) && keyMap.size() > 0) {
			StringBuffer sbAlarm = new StringBuffer();
			int index = 0;
			for (Alarm alarm : getAlarmArray()) {
				if (index % 5 == 0) {
					sbAlarm.append("\n");
					sbAlarm.append(Logger.fill(" ", BasCfg.DOT_SIZE, ' '));
					sbAlarm.append(" ");
				}
				index++;
				sbAlarm.append(Logger.fill(alarm.getAlarmKey(), 15, ' '));
			}

			sb.append(sbAlarm.toString());
		}

		// StringBuffer sb2 = new StringBuffer();
		// if (countNotFoundAlarmCode > 0)
		// sb2.append("alarm-code=(" + countNotFoundAlarmCode + ")");
		// if (countNotFoundAlarmCodeWithPerf > 0)
		// sb2.append("PERF-ITEM(" + countNotFoundAlarmCodeWithPerf + ")");
		// if (countValueNull > 0)
		// sb2.append("NULL-VALUE(" + countValueNull + ")");
		//
		// if (sb2.toString().length() > 0) {
		// sb.append("NOT-FOUND-COUNT(" + sb2.toString() + ")");
		// }

		return sb.toString();
	}

	/**
	 * 경보코드가 활성화되어 있는지 확인합니다.
	 * 
	 * @param alcdNo
	 *            경보코드
	 * @return 활성화 여부
	 */
	public boolean isAlarmCodeEnable(int alcdNo) {
		AlarmCode code = codeMap.get(alcdNo);
		return code != null;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	/**
	 * 경보에 대한 해제 이벤트를 만듭니다.
	 * 
	 * @param alarm
	 *            현재 경보
	 * @return 이벤트
	 */
	public AlarmEvent makeClearEvent(Alarm alarm, AoCode.ClearReason clearReason) {

		AlarmEvent event = makeEvent(alarm.getMoNo(), alarm.getMoInstance(), alarm.getAlcdNo());

		event.setAlarmNo(alarm.getAlarmNo());
		event.setAlarmLevel(AoCode.AlarmLevel.Clear.getNo());
		event.setClearReason(clearReason);
		event.setAlarmKey(alarm.getAlarmKey());

		return event;
	}

	public AlarmEvent makeEvent(Mo mo, String moInstance, int alcdNo) {

		AlarmCode alarmCode = getAlarmCodeByNo(alcdNo);
		if (alarmCode == null) {
			return null;
		}

		AlarmEvent event = makeEvent(mo.getMoNo(), moInstance, alarmCode.getAlcdNo());
		event.setAlarmLevel(alarmCode.getAlarmLevel().getNo());
		event.setAlarmMsg(alarmCode.getAlarmMsg());

		return event;
	}

	/**
	 * 이벤트를 생성합니다.
	 * 
	 * @param mo
	 *            관리대상
	 * @param moInstance
	 *            인스턴스
	 * @param alcdNo
	 *            경보코드
	 * @param msg
	 *            경보메세지
	 * @param valueBase
	 *            비교값
	 * @param preValue
	 *            조회한 값(이전)
	 * @param value
	 *            조회한 값
	 * @param alarmLevel
	 *            경보등급
	 * @param etc
	 *            기타값
	 * @return 이벤트
	 */
	public AlarmEvent makeEvent(Mo mo, String moInstance, int alcdNo, String msg, Number valueBase, Number psValuePrev,
			Number psValueCur, int alarmLevel, Number etc) {

		AlarmCode alarmCode = getAlarmCodeByNo(alcdNo);

		// 메시지가 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if ((msg == null || msg.length() == 0) && alarmCode != null)
			msg = alarmCode.getAlarmMsg();

		// 경보등급이 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (alarmLevel < 0)
			alarmLevel = alarmCode.getAlarmLevel().getNo();

		AlarmEvent event = makeEvent(mo.getMoNo(), moInstance, alcdNo);
		event.setAlarmLevel(alarmLevel);
		event.setAlarmMsg(makeEventMsg(msg, mo, moInstance, valueBase, psValuePrev, psValueCur, etc));
		event.setCompareValue(valueBase);
		event.setPsValue(psValueCur);

		return event;
	}

	@Override
	public void onNotify(FxEvent noti) {

		if (noti instanceof Alarm) {
			setAlarm(((Alarm) noti));
		} else if (noti instanceof ReloadSignal) {
			ReloadSignal r = (ReloadSignal) noti;

			if (r.contains(ReloadSignal.RELOAD_TYPE_ALL, ReloadSignal.RELOAD_TYPE_ALARM)) {
				try {
					reload();
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}
	}

	public Alarm sendEvent(AlarmEvent event) {

		Logger.logger.debug(String.valueOf(event));

		Alarm alarmNew;
		try {
			alarmNew = doSendEvent(event);
			if (alarmNew != null) {
				setAlarm(alarmNew);
				return alarmNew;
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return null;
	}

	/**
	 * 알람을 맵에서 등록하거나 제거합니다.<br>
	 * alarm.getBeanStatus() == NotiBean.BEAN_STATUS_DELETE 인경우만 제거합니다. 그외는 추가
	 * 
	 * @param alarm
	 *            알람
	 */
	public void setAlarm(Alarm alarm) {

		if (alarm == null)
			return;

		if (alarm.getStatus() == FxEvent.STATUS.deleted) {
			keyMap.remove(alarm.getAlarmKey());
			noMap.remove(alarm.getAlarmNo());
			Logger.logger.info("removed {}", alarm);

		} else {
			boolean replace = keyMap.get(alarm.getAlarmKey()) != null;

			keyMap.put(alarm.getAlarmKey(), alarm);
			noMap.put(alarm.getAlarmNo(), alarm);
			Logger.logger.info("{}={}", replace ? "replaced" : "added", alarm);
		}

	}

	/**
	 * 
	 * @param isCaseSensitive
	 */
	public void setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}

	private void clearRepeat(Mo mo, int alcdNo) {
		repeatMap.remove(mo.getMoNo() + "." + alcdNo);
	}

	/**
	 * 경보조건번호를 조회한다. 자신이 설정되어 있지 않다면 상위에 설정된 값을 제공한다.
	 * 
	 * @param mo
	 *            MO
	 * @return 관리번호
	 */
	private int getAlarmCfgNo(Mo mo) {

		if (mo instanceof HasAlarmCfg) {

			int alarmCfgNo = ((HasAlarmCfg) mo).getAlarmCfgNo();

			if (alarmCfgNo == 0) {
				if (mo.getUpperMoNo() > 0) {
					Mo moUpper = MoApi.getApi().getMo(mo.getUpperMoNo());
					if (moUpper != null) {
						alarmCfgNo = ((HasAlarmCfg) moUpper).getAlarmCfgNo();
					}
				}
			}

			return alarmCfgNo;
		} else {
			return -1;
		}
	}

	private long getLong(Map<String, Object> para, String name, long defaultValue) {
		Object value = para.get(name);
		return value == null ? defaultValue : (value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value
				.toString()));
	}

	private int getRepeat(Mo mo, int alcdNo) {
		Integer cnt = repeatMap.get(mo.getMoNo() + "." + alcdNo);
		return cnt == null ? 0 : cnt.intValue();
	}

	private String getString(Map<String, Object> para, String name) {
		Object value = para.get(name);
		return value == null ? null : value.toString();
	}

	/**
	 * CLEAR 이벤트를 생성합니다.
	 * 
	 * @param mo
	 * @param instance
	 * @param alcdNo
	 * @param clearMemo
	 * @param value
	 * @return
	 */
	private AlarmEvent makeClearEvent(Mo mo, String instance, long alarmNo, int alcdNo, AoCode.ClearReason clearReason, Number value)
			throws Exception {

		AlarmEvent event = ObjectUtil.makeObject4Use(AlarmEvent.class);

		event.setMoNo(mo.getMoNo());
		event.setMoInstance(instance);
		event.setAlcdNo(alcdNo);
		event.setAlarmNo(alarmNo);
		event.setAlarmLevel(AoCode.AlarmLevel.Clear.getNo());
		event.setClearReason(clearReason);
		event.setMstime(System.currentTimeMillis());

		return event;
	}

	/**
	 * 입력된 내용으로 이벤트를 생성합니다.
	 * 
	 * @param thr
	 * @param mo
	 * @param instance
	 * @param alarmCode
	 * @param psCode
	 * @param valuePre
	 * @param valueCur
	 * @param hstime
	 * @param dpPerfNo
	 * @param dpValue
	 * @return
	 * @throws Exception
	 */
	private AlarmEvent makeEvent(AlarmCfg thr, Mo mo, String instance, AlarmCode alarmCode, String psCode, Number valuePre,
			Number valueCur, long hstime, int dpPerfNo, double dpValue) throws Exception {

		int alcdNo = alarmCode.getAlcdNo();

		AlarmCfgMem cfgMem;
		Alarm alarmOld;

		alarmOld = getAlarm(mo, instance, alcdNo);

		cfgMem = thr.getMatchMember(psCode, alcdNo, valuePre, valueCur, hstime, dpPerfNo, dpValue, mo);

		Logger.logger.trace("alarm-old={}, alarm-cfg-mem={}", alarmOld, cfgMem);

		// 경보조건에 매칭이되면 무조건 matchThr를 호출합니다.
		// 필요에 따라 사용할 수 임의로 처리될 수 있도록 하기 위함입니다.
		if (cfgMem != null) {
			AlarmEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCompareVal(), //
					valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.compute(valuePre, valueCur));
			event.setTreatName(cfgMem.getTreatName());
			event.setPsCode(psCode);
		}

		// 임계 비교 결과 해당 사항이 없으면 누적 카운트를 떨고, 현재 경보가 존재하면
		// 일정시간이 지난 후 시스템이 자동으로 해제하는 경보가 아닌 경우는 이벤트를 발생하여 경보를 해제한다.
		if (cfgMem == null) {
			clearRepeat(mo, alcdNo);
			if (alarmOld != null) {
				return makeClearEvent(mo, instance, alarmOld.getAlarmNo(), alcdNo, AoCode.ClearReason.AlarmCfgMemNotFound, valueCur);
			}
		}

		// 신규 발생 경보인 경우
		else if (cfgMem != null && alarmOld == null) {
			if (cfgMem.getRepeatTimes() > 1) {
				int repeat = getRepeat(mo, alcdNo);
				repeat++;
				if (repeat >= cfgMem.getRepeatTimes()) {
					AlarmEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCompareVal(), //
							valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.compute(valuePre, valueCur));
					event.setTreatName(cfgMem.getTreatName());
					event.setPsCode(psCode);
					return event;
				} else {
					setRepeat(mo, alcdNo, repeat);
				}

			} else {
				AlarmEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCompareVal(), //
						valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.compute(valuePre, valueCur));
				event.setTreatName(cfgMem.getTreatName());
				event.setPsCode(psCode);
				return event;
			}
		}

		// 경보 등급이 변경되었을 경우
		else if (cfgMem != null && alarmOld != null && cfgMem.getAlarmLevel() != alarmOld.getAlarmLevel()) {

			// 동일 경보이나 등급이 다른 경우 이전 내용을 제거하고 새롭게 보냅니다.
			AlarmEvent event = makeEvent(mo, instance, alcdNo, alarmCode.getAlarmMsg(), cfgMem.getCompareVal(), //
					valuePre, valueCur, cfgMem.getAlarmLevel(), cfgMem.compute(valuePre, valueCur));
			event.setTreatName(cfgMem.getTreatName());
			event.setPsCode(psCode);
			event.setAlarmNo(alarmOld.getAlarmNo());
			return event;

		}

		return null;

	}

	private AlarmEvent makeEvent(long moNo, String moInstance, int alcdNo) {

		AlarmEvent event;
		try {
			event = ObjectUtil.makeObject4Use(AlarmEvent.class);
		} catch (Exception e1) {
			event = new AlarmEvent();
		}

		event.setMoNo(moNo);
		event.setAlcdNo(alcdNo);
		event.setMoInstance(moInstance);
		event.setMstime(System.currentTimeMillis());

		return event;
	}

	/**
	 * 성능항목에 대한 경보조건이 없을 경우 이미 존재하는 경보조건을 없애기 위한 이벤트 생성
	 * 
	 * @param psCode
	 *            성능항목번호
	 * @param mo
	 *            관리대상
	 * @param instance
	 *            인스턴스
	 * @param valueCur
	 *            현재값
	 * @return 경보이벤트 목록
	 * @throws Exception
	 */
	private List<AlarmEvent> makeEventClearByPerf(String psCode, Mo mo, String instance, Number valueCur,
			AoCode.ClearReason clearReason) throws Exception {
		AlarmCode faultCdAlarm;
		Alarm alarm;
		List<Integer> alcdNoList = psAlcdMap.get(psCode);
		AlarmEvent event;
		List<AlarmEvent> eventList = null;

		if (alcdNoList != null) {

			for (int alcdNo : alcdNoList) {
				faultCdAlarm = getAlarmCodeByNo(alcdNo);
				if (faultCdAlarm != null) {

					alarm = getAlarm(mo, instance, alcdNo);

					if (alarm != null) {
						event = makeClearEvent(mo, instance, alarm.getAlarmNo(), alcdNo, clearReason, valueCur);
						if (eventList == null)
							eventList = new ArrayList<AlarmEvent>();
						eventList.add(event);
					}
				}
			}

			return eventList;
		}

		return null;
	}

	/**
	 * 이벤트 메시지를 만든다.<br>
	 * %value% = 조회값<br>
	 * %baseValue% = 기준값<br>
	 * %preValue% = 이전값<br>
	 * %name% = 관리대상 명<br>
	 * %loc% = 경보 발생 위치<br>
	 * %ip% = IP 주소<br>
	 * %etc% = 기타<br>
	 * 를 치환 대상입니다.
	 * 
	 * 
	 * @param orgMsg
	 *            원시 메시지
	 * @param mo
	 *            관리대상
	 * @param instance
	 *            발생위치
	 * @param baseValue
	 *            기준값
	 * @param preValue
	 *            이전 조회값
	 * @param value
	 *            조회값
	 * @return 만들어진 이벤트 메시지
	 */
	private String makeEventMsg(String orgMsg, Mo mo, String foInstance, Object baseValue, Object preValue, Object value, Object etc) {

		if (orgMsg == null)
			return mo.getMoName() + " AlarmEvent";

		String ret = orgMsg.replaceAll("%value%", toString(value));
		ret = ret.replaceAll("%valueBase%", toString(baseValue));
		ret = ret.replaceAll("%valuePre%", toString(preValue));
		ret = ret.replaceAll("%valueCur%", toString(value));
		ret = ret.replaceAll("%etc%", toString(etc));
		ret = ret.replaceAll("%foInstance%", toString(foInstance));

		// List<String> varList = ParseUtil.parseVar(orgMsg);
		// if (varList != null) {
		// Object v;
		// for (String var : varList) {
		// v = mo.get(var);
		// if (v != null) {
		// ret = ret.replaceAll("%" + var + "%", v.toString());
		// } else {
		// ret = ret.replaceAll("%" + var + "%", "");
		// }
		// }
		// }

		return ret;
	}

	private List<AlarmEvent> makeEventPs(Mo mo, String instance, String psCode, Number psPrevValue, Number psCurValue,
			long mstimePerf, int dpPerfNo, double dpValue) throws Exception {

		long psDate = FxApi.getDate(mstimePerf);
		int alarmCfgNo = getAlarmCfgNo(mo);

		AlarmCode alarmCode;
		List<AlarmEvent> eventList = null;
		AlarmEvent event;

		AlarmCfg cfg = getAlarmCfg(alarmCfgNo);

		if (cfg == null) {
			Logger.logger.trace("alarm-cfg={} not found", alarmCfgNo);
			return makeEventClearByPerf(psCode, mo, instance, psCurValue, AoCode.ClearReason.AlarmCfgNotFound);
		}

		if (cfg.getMemSize() == 0) {
			return makeEventClearByPerf(psCode, mo, instance, psCurValue, AoCode.ClearReason.AlarmCfgMemSize0);
		}

		// 성능과 관련된 경보코드가 없을 경우 이미 존재한 경보를 해제합니다.
		List<Integer> alcdNos = cfg.getAlcdNo(psCode);
		if (alcdNos == null || alcdNos.size() == 0) {
			return makeEventClearByPerf(psCode, mo, instance, psCurValue, AoCode.ClearReason.AlarmCodeNotFoundForPsCode);
		}

		for (int alcdNo : alcdNos) {

			alarmCode = getAlarmCodeByNo(alcdNo);
			if (alarmCode == null) {
				Logger.logger.trace("ALARM-CODE({}} NOT FOUND", alcdNo);
				continue;
			}

			event = makeEvent(cfg, mo, instance, alarmCode, psCode, psPrevValue, psCurValue, psDate, dpPerfNo, dpValue);

			if (event != null) {
				event.setPsDate(psDate);
				if (eventList == null)
					eventList = new ArrayList<AlarmEvent>();
				eventList.add(event);
			}
		}

		return eventList;
	}

	private void reloadAlarmCfg() throws Exception {

		List<AlarmCfg> cfgList = doSelectAlarmCfgAll(null);
		Map<Integer, AlarmCfg> map = new HashMap<Integer, AlarmCfg>();
		AlarmCfgMem mem;
		AlarmCode alarmCode;
		Map<String, AlarmCfg> moClassMap = new HashMap<String, AlarmCfg>();

		for (AlarmCfg cfg : cfgList) {
			if (cfg.getMemList() != null) {
				for (int i = cfg.getMemList().size() - 1; i >= 0; i--) {
					mem = cfg.getMemList().get(i);
					alarmCode = getAlarmCodeByNo(mem.getAlcdNo());
					if (alarmCode != null) {
						mem.setAlarmCode(alarmCode);
					} else {
						cfg.getMemList().remove(i);
						Logger.logger
								.fail("alarm-code={} not found. removed alarm-cfg-no={}", mem.getAlcdNo(), cfg.getAlarmCfgNo());
					}
				}
				map.put(cfg.getAlarmCfgNo(), cfg);
			}

			if (cfg.isBasicCfgYn()) {
				moClassMap.put(cfg.getMoClass(), cfg);
			}
		}

		alarmCfgMap = map;
		moClassAlarmCfgMap = moClassMap;

		Logger.logger.info("alarm-cfg-size={}", cfgList.size());
	}

	private void setRepeat(Mo mo, int alcdNo, int count) {
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

	protected abstract Alarm doSendEvent(AlarmEvent event);

	@Override
	protected void initApi() throws Exception {
	}

	@Override
	protected void reload() throws Exception {

		try {
			reloadAlarmCode();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		try {
			reloadAlarmCfg();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		try {
			reloadAlarm();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	private void reloadAlarmCode() throws Exception {

		List<AlarmCode> list = doSelectAlarmCodeAll();
		Map<Integer, AlarmCode> noMap = new HashMap<Integer, AlarmCode>();
		Map<String, AlarmCode> nmMap = new HashMap<String, AlarmCode>();
		Map<String, List<Integer>> psMap = new HashMap<String, List<Integer>>();
		List<Integer> entry;

		for (AlarmCode bean : list) {
			noMap.put(bean.getAlcdNo(), bean);
			if (isCaseSensitive == false) {
				nmMap.put(bean.getAlcdName().toLowerCase(), bean);
			} else {
				nmMap.put(bean.getAlcdName(), bean);
			}

			if (bean.hasPs()) {
				entry = psMap.get(bean.getPsCode());
				if (entry == null) {
					entry = new ArrayList<Integer>();
					psMap.put(bean.getPsCode(), entry);
				}
				entry.add(bean.getAlcdNo());
			}

		}

		this.codeMap = Collections.synchronizedMap(noMap);
		this.nameMap = Collections.synchronizedMap(nmMap);
		this.psAlcdMap = Collections.synchronizedMap(psMap);

		Logger.logger.debug("PS-CODE({}) ALCD({})", psAlcdMap.size(), codeMap.size());
	}

	private void reloadAlarm() throws Exception {

		Map<String, Alarm> keyMap = new HashMap<String, Alarm>();
		Map<Long, Alarm> noMap = new HashMap<Long, Alarm>();

		List<Alarm> list = doSelectCurAlarmAll(null);

		for (Alarm alarm : list) {
			keyMap.put(alarm.getAlarmKey(), alarm);
			noMap.put(alarm.getAlarmNo(), alarm);
		}

		this.keyMap = Collections.synchronizedMap(keyMap);
		this.noMap = Collections.synchronizedMap(noMap);

		Logger.logger.debug("alarm-size={} loaded", list.size());
	}
}
