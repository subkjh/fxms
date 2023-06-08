package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.mo.Moable;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class MakeAlarmEventDfo implements FxDfo<Void, AlarmOccurEvent> {

	@Override
	public AlarmOccurEvent call(FxFact fact, Void data) throws Exception {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param mo         관리대상
	 * @param moInstance 인스턴스
	 * @param alarmCode  알람코드
	 * @param alarmLevel 알람레벨
	 * @param msg        메시지
	 * @param ext        기타정보
	 * @param filter     필터
	 * @return
	 * @throws Exception
	 */
	public AlarmOccurEvent makeAlarmEvent(Moable mo, Object moInstance, AlarmCode alarmCode, ALARM_LEVEL alarmLevel,
			String msg, Map<String, Object> etcData) throws Exception {

		String strInstance = moInstance == null ? null : String.valueOf(moInstance);

		if (mo == null || alarmCode == null) {
			throw new Exception(Lang.get("MO or AlarmCode is null"));
		}

		// 메시지가 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (msg == null || msg.length() == 0)
			msg = alarmCode.getAlarmMsg();

		AlarmOccurEvent event = new AlarmOccurEvent(mo.getMoNo(), strInstance, alarmCode.getAlcdNo());
		event.setFpactCd(alarmCode.getFpactCd());

		if (etcData != null) {
			ObjectUtil.toObject(etcData, event);
		}

		// 경보등급이 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (alarmLevel != null) {
			event.setAlarmLevel(alarmLevel.getAlarmLevel());
		} else {
			event.setAlarmLevel(alarmCode.getAlarmLevel().getAlarmLevel());
		}

		// 현재 알람이 있는지 확인하고 있으면 해당 알람번호를 설정한다.
		Alarm alarm = AlarmApi.getApi().getCurAlarm(mo, strInstance, alarmCode.getAlcdNo());
		String message = makeEventMsg(msg, mo, strInstance, event.getCmprVal(), event.getPsVal(), event.getPsVal(),
				null);

		event.setAlarmMsg(message);

		if (alarm != null) {
			event.setAlarm(alarm);
		}

		return event;

	}

	public AlarmOccurEvent makeEvent(Mo mo, String moInstance, AlarmCode alarmCode, String msg, Number valueBase,
			Number psValuePrev, Number psValueCur, int alarmLevel, Number etc) {

		// 메시지가 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if ((msg == null || msg.length() == 0) && alarmCode != null)
			msg = alarmCode.getAlarmMsg();

		// 경보등급이 정의되어 있지 않으면 경보코드 기본 메세지를 사용합니다.
		if (alarmLevel < 0)
			alarmLevel = alarmCode.getAlarmLevel().getAlarmLevel();

		AlarmOccurEvent event = new AlarmOccurEvent(mo.getMoNo(), moInstance, alarmCode.getAlcdNo());
		event.setAlarmLevel(alarmLevel);
		event.setAlarmMsg(makeEventMsg(msg, mo, moInstance, valueBase, psValuePrev, psValueCur, etc));
		event.setCmprVal(valueBase);
		event.setPsVal(psValueCur);

		return event;
	}

	private String makeEventMsg(String orgMsg, Moable mo, String moInstance, Object baseValue, Object preValue,
			Object value, Object etc) {

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
