package fxms.bas.vo;

/**
 * 알람 이벤트<br>
 * 이벤트를 AppService가 분석하여 알람화한다.
 * 
 * @author subkjh
 *
 */
public class AlarmOccurEvent extends AlarmEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 999602443447338782L;

	/**
	 * 
	 * @param moNo      관리대상번호
	 * @param moInstance  인스턴서<br>
	 *                  null이거나 공백이면 무시됩니다.
	 * @param alarmCode 경보코드
	 * @return 관리대상번호 + _ + [ 인스턴스 + _ ] + 경보코드
	 */
	public static String getAlarmKey(long moNo, String moInstance, int alcdNo) {
		return moNo + "_" + (moInstance == null || moInstance.length() == 0 ? "" : moInstance + "_") + alcdNo;
	}

	private final int alcdNo;
	private final String moInstance;
	private final long moNo;
	
	private int alarmCfgNo;
	private String alarmKey;
	private int alarmLevel;
	private String alarmMsg;
	/** 비교 기준값 */
	private Number cmprVal;
	/** 성능 수집 일시 */
	private long psDate;
	/** 성능코드 */
	private String psId;
	/** 조회된 값 */
	private Number psVal;
	/** 후속조치코드 */
	private String fpactCd;
	private int occurCnt = 0;
	private Alarm alarm;

	public AlarmOccurEvent(long moNo, String moInstance, int alcdNo) {
		this.moInstance = moInstance;
		this.moNo = moNo;
		this.alcdNo = alcdNo;
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 
	 * @return 관리대상번호 + _ + [ 인스턴스 + _ ] + 경보코드
	 */
	public String getAlarmKey() {
		if (alarmKey != null) {
			return alarmKey;
		}
		return getAlarmKey(moNo, moInstance, alcdNo);
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public Number getCmprVal() {
		return cmprVal;
	}

	public String getFpactCd() {
		return fpactCd;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public int getOccurCnt() {
		return occurCnt;
	}

	public long getPsDate() {
		return psDate;
	}

	public String getPsId() {
		return psId;
	}

	public Number getPsVal() {
		return psVal;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public void setCmprVal(Number cmprVal) {
		this.cmprVal = cmprVal;
	}

	public void setFpactCd(String fpactCd) {
		this.fpactCd = fpactCd;
	}

	public void setOccurCnt(int occurCnt) {
		this.occurCnt = occurCnt;
	}

	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public void setPsVal(Number psVal) {
		this.psVal = psVal;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("Event[");
		if (alarm != null) {
			sb.append("no(" + alarm.getAlarmNo() + ")");
		}
		sb.append("alcd(" + alcdNo + ")");
		sb.append("mono(" + getMoNo() + ")");
		if (psId != null) {
			sb.append("psid(" + psId + ")");
			sb.append("value(" + psVal + ")");
		}
		sb.append("level(" + alarmLevel + ")");
		sb.append("]");

		return sb.toString();
	}

}