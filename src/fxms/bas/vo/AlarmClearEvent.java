package fxms.bas.vo;

import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;

/**
 * 알람 이벤트<br>
 * 이벤트를 AppService가 분석하여 알람화한다.
 * 
 * @author subkjh
 *
 */
public class AlarmClearEvent extends AlarmEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7637453930624777483L;

	private final String releaseMemo; // 해제메모
	private final ALARM_RLSE_RSN_CD alarmRlseRsnCd; // 해제사유코드
	private final long alarmNo; // 해당 알람번호
	private final int userNo; // 해제자

	public AlarmClearEvent(long alarmNo, long eventMstime, ALARM_RLSE_RSN_CD alarmRlseRsnCd, String releaseMemo,
			int userNo) {
		this.alarmNo = alarmNo;
		this.alarmRlseRsnCd = alarmRlseRsnCd;
		this.releaseMemo = releaseMemo;
		this.userNo = userNo;
		setEventMstime(eventMstime);
	}

	public long getAlarmNo() {
		return alarmNo;
	}

	public ALARM_RLSE_RSN_CD getAlarmRlseRsnCd() {
		return alarmRlseRsnCd;
	}

	public String getReleaseMemo() {
		return releaseMemo;
	}

	public int getUserNo() {
		return userNo;
	}

}