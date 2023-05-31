package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.co.CoCode.ALARM_LEVEL;

/**
 * 외부 알람을 나타낸다.
 *
 * @author subkjh
 *
 */
public class ExtraAlarm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1511714863484826435L;
	/** 경보등급 */
	private ALARM_LEVEL alarmLevel;
	/** 관제ID */
	private String psId;
	/** 수집 값 */
	private Number psVal;
	/** 비교 값 */
	private Number cmprVal;
	/** 알람 키 */
	private String alarmKey;
	/** 이벤트 수신 발생시간 */
	private long recvEventMstime;
	/** 이벤트 실제 발생시간 */
	private long eventMstime;

	/**
	 * Follow-uP Action
	 */
	private String fpactCd;

	private int occurCnt = 1;

	public ExtraAlarm() {

	}

	public String getAlarmKey() {
		return alarmKey;
	}

	public ALARM_LEVEL getAlarmLevel() {
		return alarmLevel;
	}

	public Number getCmprVal() {
		return cmprVal;
	}

	public long getEventMstime() {
		return eventMstime;
	}

	public String getFpactCd() {
		return fpactCd;
	}

	public String getPsId() {
		return psId;
	}

	public Number getPsVal() {
		return psVal;
	}

	public long getRecvEventMstime() {
		return recvEventMstime;
	}

	public void initAlarmEvent(AlarmOccurEvent event) {

		if (alarmLevel != null)
			event.setAlarmLevel(alarmLevel.getAlarmLevel());

		if (psId != null) {
			event.setPsId(psId);
		}

		if (psVal != null)
			event.setPsVal(psVal);

		if (cmprVal != null)
			event.setCmprVal(cmprVal);

		if (eventMstime > 0)
			event.setEventMstime(eventMstime);

		if (fpactCd != null)
			event.setFpactCd(fpactCd);

		if (alarmKey != null) {
			event.setAlarmKey(alarmKey);
		}

		event.setOccurCnt(occurCnt);
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public void setAlarmLevel(ALARM_LEVEL alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setCmprVal(Number cmprVal) {
		this.cmprVal = cmprVal;
	}

	public void setEventMstime(long eventMstime) {
		this.eventMstime = eventMstime;
	}

	public void setFpactCd(String fpactCd) {
		this.fpactCd = fpactCd;
	}

	public void setOccurCnt(int occurCnt) {
		this.occurCnt = occurCnt;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public void setPsVal(Number psVal) {
		this.psVal = psVal;
	}

	public void setRecvEventMstime(long recvEventMstime) {
		this.recvEventMstime = recvEventMstime;
	}

	
}
