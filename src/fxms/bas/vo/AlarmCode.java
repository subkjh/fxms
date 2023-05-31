package fxms.bas.vo;

import java.io.Serializable;

import fxms.bas.co.CoCode.ALARM_LEVEL;

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
	private ALARM_LEVEL alarmLevel;
	/** 경보메시지 */
	private String alarmMsg;
	/** 자동해제시간(초) */
	private int autoClearSec;
	/** 비교조건 */
	private String compareCode;
	/** 경보분류 */
	private String moClass;
	/** 성능상태코드 */
	private String psId;
	/** 후속조치코드 */
	private String fpactCd;
	/** 서비스영향경보여부 */
	private boolean serviceAlarmYn;

	public AlarmCode(int alcdNo, String alcdName, ALARM_LEVEL alarmLevel, String psId, String compareCode,
			String alarmMsg, String fpactCd, String moClass, int autoClearSec, boolean serviceAlarmYn) {

		this.alcdNo = alcdNo;
		this.alcdName = alcdName;
		this.alarmLevel = alarmLevel;
		this.psId = psId;
		this.compareCode = compareCode;
		this.alarmMsg = alarmMsg;
		this.fpactCd = fpactCd;
		this.moClass = moClass;
		this.autoClearSec = autoClearSec;
		this.serviceAlarmYn = serviceAlarmYn;
	}

	public ALARM_LEVEL getAlarmLevel() {
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

	public String getPsId() {
		return psId;
	}

	public String getMoClass() {
		return moClass;
	}

	public boolean isServiceAlarmYn() {
		return serviceAlarmYn;
	}

	public String getFpactCd() {
		return fpactCd;
	}

	public boolean hasPs() {
		return psId != null && psId.length() > 0;
	}

}
