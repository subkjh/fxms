package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.mo.Moable;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmOccurEvent;
import subkjh.bas.co.log.Logger;

/**
 * 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다.<br>
 * 시스템 시작하고 이후 데이터만 보관한다.<br>
 * 저장소에 이전 데이터를 읽어오지 않는다.<br>
 */
public class AlarmMap {

	private static AlarmMap map = null;

	public static AlarmMap getInstance() {

		if (map == null) {
			map = new AlarmMap();
		}

		return map;
	}

	private final Map<String, Alarm> keyMap;
	private final Map<Long, Alarm> noMap;

	private AlarmMap() {
		this.keyMap = new HashMap<>();
		this.noMap = new HashMap<>();
	}

	public void load() throws Exception {

		List<Alarm> list = new AlarmCurSelectDfo().selectAlarmCur(null);

		Map<String, Alarm> keyMap = new HashMap<String, Alarm>();
		Map<Long, Alarm> noMap = new HashMap<Long, Alarm>();

		for (Alarm alarm : list) {
			keyMap.put(alarm.getAlarmKey(), alarm);
			noMap.put(alarm.getAlarmNo(), alarm);
		}

		synchronized (this.keyMap) {
			this.keyMap.clear();
			this.keyMap.putAll(keyMap);
		}
		synchronized (this.noMap) {
			this.noMap.clear();
			this.noMap.putAll(noMap);
		}

		Logger.logger.info(
				Logger.makeString("loaded.alarm.size", "key=" + this.keyMap.size() + ", no=" + this.noMap.size()));
	}

	public int size() {
		synchronized (this.keyMap) {
			return this.keyMap.size();
		}
	}

	/**
	 * 알람번호를 이용하여 알람을 제공합니다.
	 * 
	 * @param alarmNo 알람번호
	 * @return 알람
	 */
	public Alarm getAlarm(long alarmNo) {
		synchronized (this.noMap) {
			return this.noMap.get(alarmNo);
		}
	}

	/**
	 * 메모리에 가지고 있는 경보 정보를 제공합니다.
	 * 
	 * @param mo       관리MO
	 * @param instance 인스턴스
	 * @param alcdNo   경보코드
	 * @return 경보
	 */
	public Alarm getAlarm(Moable mo, String instance, int alcdNo) {

		if (mo == null) {
			return null;
		}

		return getAlarm4Key(AlarmOccurEvent.getAlarmKey(mo.getMoNo(), instance, alcdNo));
	}

	/**
	 * 경보키를 이용하여 현재 경보 내용을 제공합니다.
	 * 
	 * @param alarmKey 경보키
	 * @return 현재경보
	 */
	public Alarm getAlarm4Key(String alarmKey) {
		synchronized (this.keyMap) {
			return this.keyMap.get(alarmKey);
		}
	}

	/**
	 * 알람을 맵에서 등록하거나 제거합니다.<br>
	 * alarm.getBeanStatus() == NotiBean.BEAN_STATUS_DELETE 인경우만 제거합니다. 그외는 추가
	 * 
	 * @param alarm 알람
	 */
	public synchronized void setAlarm(Alarm alarm) {

		if (alarm == null)
			return;

		synchronized (this.keyMap) {
			if (alarm.isCleared()) {
				Alarm removeAlarm = keyMap.remove(alarm.getAlarmKey());
				Logger.logger.info("alarm removed : {}", removeAlarm == null ? "(notfound)" : removeAlarm);

			} else {
				boolean replace = keyMap.get(alarm.getAlarmKey()) != null;
				keyMap.put(alarm.getAlarmKey(), alarm);
				Logger.logger.info("alarm {} : {}", replace ? "replaced" : "added", alarm);
			}
		}

		synchronized (this.noMap) {
			if (alarm.isCleared()) {
				noMap.remove(alarm.getAlarmNo());
			} else {
				noMap.put(alarm.getAlarmNo(), alarm);
			}
		}
	}
}
