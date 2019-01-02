package com.fxms.ui.bas.code;

public class UiAlarmCfgMemVo {

	public UiAlarmCfgMemVo() {
	}

	private int alarmCfgNo;

	private int alcdNo;

	private boolean useYn = true;

	private double compareVal;

	private int repeatTimes = 1;

	private int alarmLevel;

	private String treatName;

	/**
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 경보조건번호
	 * 
	 * @param alarmCfgNo
	 *            경보조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 경보코드
	 * 
	 * @return 경보코드
	 */
	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 경보코드
	 * 
	 * @param alcdNo
	 *            경보코드
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn
	 *            사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 비교값
	 * 
	 * @return 비교값
	 */
	public double getCompareVal() {
		return compareVal;
	}

	/**
	 * 비교값
	 * 
	 * @param compareVal
	 *            비교값
	 */
	public void setCompareVal(double compareVal) {
		this.compareVal = compareVal;
	}

	/**
	 * 연속일치회수
	 * 
	 * @return 연속일치회수
	 */
	public int getRepeatTimes() {
		return repeatTimes;
	}

	/**
	 * 연속일치회수
	 * 
	 * @param repeatTimes
	 *            연속일치회수
	 */
	public void setRepeatTimes(int repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	/**
	 * 경보등급
	 * 
	 * @return 경보등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 경보등급
	 * 
	 * @param alarmLevel
	 *            경보등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 경보조치코드명
	 * 
	 * @return 경보조치코드명
	 */
	public String getTreatName() {
		return treatName;
	}

	/**
	 * 경보조치코드명
	 * 
	 * @param treatName
	 *            경보조치코드명
	 */
	public void setTreatName(String treatName) {
		this.treatName = treatName;
	}
}
