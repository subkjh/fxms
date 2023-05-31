package fxms.bas.fxo.cron.vo;

import fxms.bas.co.CoCode.ALARM_LEVEL;

/**
 * 최근 수집일시 및 알람 발생 조건 데이터
 * 
 * @author subkjh
 *
 */
public class CollectInfoVo {

	private long moNo;
	private String moName;
	private int pollCycle;
	private int alarmCfgNo;
	private int alcdNo;
	private String criDtm;
	private String majDtm;
	private String minDtm;
	private String warDtm;
	private String collDtm;

	public long getMoNo() {
		return moNo;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public String getMoName() {
		return moName;
	}

	public void setMoName(String moName) {
		this.moName = moName;
	}

	public int getPollCycle() {
		return pollCycle;
	}

	public void setPollCycle(int pollCycle) {
		this.pollCycle = pollCycle;
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	public String getCriDtm() {
		return criDtm;
	}

	public void setCriDtm(String criDtm) {
		this.criDtm = criDtm;
	}

	public String getMajDtm() {
		return majDtm;
	}

	public void setMajDtm(String majDtm) {
		this.majDtm = majDtm;
	}

	public String getMinDtm() {
		return minDtm;
	}

	public void setMinDtm(String minDtm) {
		this.minDtm = minDtm;
	}

	public String getWarDtm() {
		return warDtm;
	}

	public void setWarDtm(String warDtm) {
		this.warDtm = warDtm;
	}

	public String getCollDtm() {
		return collDtm;
	}

	public void setCollDtm(String collDtm) {
		this.collDtm = collDtm;
	}

	public ALARM_LEVEL getAlarmLevel() {
		if (collDtm == null) {
			return ALARM_LEVEL.Critical;
		} else if (criDtm != null && criDtm.compareTo(collDtm) > 0) {
			return ALARM_LEVEL.Critical;
		} else if (majDtm != null && majDtm.compareTo(collDtm) > 0) {
			return ALARM_LEVEL.Major;
		} else if (minDtm != null && minDtm.compareTo(collDtm) > 0) {
			return ALARM_LEVEL.Minor;
		} else if (warDtm != null && warDtm.compareTo(collDtm) > 0) {
			return ALARM_LEVEL.Warning;
		}

		return ALARM_LEVEL.Clear;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

}
