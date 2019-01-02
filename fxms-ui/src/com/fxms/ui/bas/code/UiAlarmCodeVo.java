package com.fxms.ui.bas.code;

public class UiAlarmCodeVo {

	private int alcdNo;

	private String alcdName;

	private String alcdDescr;

	private String alarmMsg;

	private String targetMoClass;

	private int alarmLevel;

	private String compareCode;

	private String psCode;

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 경보코드번호
	 * 
	 * @param alcdNo
	 *            경보코드번호
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 경보코드명
	 * 
	 * @return 경보코드명
	 */
	public String getAlcdName() {
		return alcdName;
	}

	/**
	 * 경보코드명
	 * 
	 * @param alcdName
	 *            경보코드명
	 */
	public void setAlcdName(String alcdName) {
		this.alcdName = alcdName;
	}

	/**
	 * 경보설명
	 * 
	 * @return 경보설명
	 */
	public String getAlcdDescr() {
		return alcdDescr;
	}

	/**
	 * 경보설명
	 * 
	 * @param alcdDescr
	 *            경보설명
	 */
	public void setAlcdDescr(String alcdDescr) {
		this.alcdDescr = alcdDescr;
	}

	/**
	 * 경보메시지
	 * 
	 * @return 경보메시지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	/**
	 * 경보메시지
	 * 
	 * @param alarmMsg
	 *            경보메시지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public String getTargetMoClass() {
		return targetMoClass;
	}

	public void setTargetMoClass(String targetMoClass) {
		this.targetMoClass = targetMoClass;
	}

	/**
	 * 기본경보등급
	 * 
	 * @return 기본경보등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 기본경보등급
	 * 
	 * @param alarmLevel
	 *            기본경보등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 비교조건
	 * 
	 * @return 비교조건
	 */
	public String getCompareCode() {
		return compareCode;
	}

	/**
	 * 비교조건
	 * 
	 * @param compareCode
	 *            비교조건
	 */
	public void setCompareCode(String compareCode) {
		this.compareCode = compareCode;
	}

	/**
	 * 성능상태코드
	 * 
	 * @return 성능상태코드
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 성능상태코드
	 * 
	 * @param psCode
	 *            성능상태코드
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	@Override
	public String toString() {
		return alcdDescr;
	}
}
