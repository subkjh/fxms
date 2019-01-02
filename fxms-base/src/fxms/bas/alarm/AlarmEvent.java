package fxms.bas.alarm;

import java.io.Serializable;
import java.util.Map;

import fxms.bas.alarm.AoCode.ClearReason;

public class AlarmEvent implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 999602443447338782L;

	/**
	 * 
	 * @param moNo
	 *            관리대상번호
	 * @param instance
	 *            인스턴서<br>
	 *            null이거나 공백이면 무시됩니다.
	 * @param alarmCode
	 *            경보코드
	 * @return 관리대상번호 + _ + [ 인스턴스 + _ ] + 경보코드
	 */
	public static String getAlarmKey(long moNo, String moInstance, int alcdNo) {
		return moNo + "_" + (moInstance == null || moInstance.length() == 0 ? "" : moInstance + "_") + alcdNo;
	}

	/** 성능 수집 일시 */
	private long psDate;
	/** 이벤트 발생 시간 */
	private long mstime;
	/** 성능코드 */
	private String psCode;
	/** 조회된 값 */
	private Number psValue;
	/** 비교 기준값 */
	private Number compareValue;
	private Map<String, Object> map;
	private int alarmLevel;
	private long moNo;
	private String moInstance;
	private long upperMoNo;
	private String alarmMsg;
	private int alcdNo;
	private long alarmNo;
	private String treatName;
	private String alarmKey;
	private ClearReason clearReason;

	public ClearReason getClearReason() {
		return clearReason;
	}

	public void setClearReason(ClearReason clearReason) {
		this.clearReason = clearReason;
	}

	public AlarmEvent() {

	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
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

	public long getAlarmNo() {
		return alarmNo;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public Number getCompareValue() {
		return compareValue;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public String getMoInstance() {
		return moInstance;
	}

	public long getMoNo() {
		return moNo;
	}

	public long getMstime() {
		return mstime;
	}

	public String getPsCode() {
		return psCode;
	}

	public long getPsDate() {
		return psDate;
	}

	public Number getPsValue() {
		return psValue;
	}

	public String getTreatName() {
		return treatName;
	}

	public long getUpperMoNo() {
		return upperMoNo;
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

	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	public void setCompareValue(Number compareValue) {
		this.compareValue = compareValue;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setMstime(long mstime) {
		this.mstime = mstime;
	}

	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	public void setPsValue(Number psValue) {
		this.psValue = psValue;
	}

	public void setTreatName(String treatName) {
		this.treatName = treatName;
	}

	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("Event[");
		if (alarmNo > 0) {
			sb.append("alarm-no(" + alarmNo + ")");
		}
		sb.append("alarm-code(" + alcdNo + ")");
		sb.append("mo-no(" + getMoNo() + ")");
		if (psCode != null) {
			sb.append("ps-code(" + psCode + ")");
			sb.append("ps-value(" + psValue + ")");
		}
		sb.append("alarm-level(" + alarmLevel + ")");
		sb.append("]");

		return sb.toString();
	}
}