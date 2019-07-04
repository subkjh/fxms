package fxms.bas.ao.vo;

import java.io.Serializable;

import fxms.bas.ao.AoCode.AlarmLevel;

public class AlarmCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5926941930719378792L;

	/** 경보코드번호 */
	private int alcdNo;
	/** 경보코드명 */
	private String alcdName;
	/** 기본경보등급 */
	private AlarmLevel alarmLevel;
	/** 경보메시지 */
	private String alarmMsg;
	/** 자동해제시간(초) */
	private int autoClearSec;
	/** 비교조건 */
	private String compareCode;
	/** 경보분류 */
	private String targetMoClass;
	/** 성능상태코드 */
	private String psCode;
	/** 경보조치명 */
	private String treatName;
	/** 서비스영향경보여부 */
	private boolean serviceAlarmYn;

	public AlarmCode(int alcdNo, String alcdName, AlarmLevel alarmLevel, String psCode, String compareCode, String alarmMsg,
			String treatName, String targetMoClass, int autoClearSec, boolean serviceAlarmYn) {

		this.alcdNo = alcdNo;
		this.alcdName = alcdName;
		this.alarmLevel = alarmLevel;
		this.psCode = psCode;
		this.compareCode = compareCode;
		this.alarmMsg = alarmMsg;
		this.treatName = treatName;
		this.targetMoClass = targetMoClass;
		this.autoClearSec = autoClearSec;
		this.serviceAlarmYn = serviceAlarmYn;
	}

	public AlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public String getAlcdName() {
		return alcdName;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public int getAutoClearSec() {
		return autoClearSec;
	}

	public String getCompareCode() {
		return compareCode;
	}

	public String getPsCode() {
		return psCode;
	}

	public String getTargetMoClass() {
		return targetMoClass;
	}

	public boolean isServiceAlarmYn() {
		return serviceAlarmYn;
	}

	public String getTreatName() {
		return treatName;
	}

	public boolean hasPs() {
		return psCode != null && psCode.length() > 0;
	}

}
