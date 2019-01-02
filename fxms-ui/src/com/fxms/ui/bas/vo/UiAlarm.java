package com.fxms.ui.bas.vo;

public class UiAlarm {

	public enum AlarmKind {
		acked, cleared, cur;
	}

	public enum AlarmLevel {
		critical(1), major(2), minor(3), warning(4), unknown(99);

		private int level;

		private AlarmLevel(int level) {
			this.level = level;
		}

		public static AlarmLevel getAlarmLevel(int level) {
			for (AlarmLevel e : AlarmLevel.values()) {
				if (e.level == level) {
					return e;
				}
			}
			return unknown;
		}

		public int getLevel() {
			return level;
		}
	}

	private String status;

	private long alarmNo;

	private String alarmKey;

	private int alcdNo;

	private String alcdName;

	private String alarmClass;

	private int alarmLevel;

	private String alarmMsg;

	private double compareValue;

	private double psValue;

	private String psCode;

	private String psName;

	private long psDate;

	private long moNo;

	private String moName;

	private String moAname;

	private String moInstance;

	private String moClass;

	private long upperMoNo;

	private String upperMoName;

	private String upperMoAname;

	private int inloNo;

	private String inloName;

	private int modelNo;

	private String modelName;

	private long regDate;

	private long ocuDate;

	private long ackDate;

	private int ackUserNo;

	private long reasonRegDate;

	private int reasonRegUserNo;

	private int reasonNo = 0;

	private String reasonName;

	private String reasonMemo;

	private String ipAddr;

	private long chgDate;

	private String treatName;

	private int ugrpNo = 0;

	private boolean clearYn = false;

	private long clearDate;

	private int clearUserNo;

	private int clearRsnNo;

	private String clearRsnName;

	private String clearMemo;

	/** MO 전체 경보 수 */
	private int alarmCount;

	public int getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UiAlarm) {
			return ((UiAlarm) obj).alarmNo == alarmNo;
		}

		return super.equals(obj);
	}

	public long getAckDate() {
		return ackDate;
	}

	/**
	 * 확인운용자번호
	 * 
	 * @return 확인운용자번호
	 */
	public int getAckUserNo() {
		return ackUserNo;
	}

	/**
	 * 경보분류
	 * 
	 * @return 경보분류
	 */
	public String getAlarmClass() {
		return alarmClass;
	}

	/**
	 * 경보구분고유값
	 * 
	 * @return 경보구분고유값
	 */
	public String getAlarmKey() {
		return alarmKey;
	}

	public AlarmKind getAlarmKind() {
		if (isClearYn()) {
			return AlarmKind.cleared;
		} else if (getAckDate() > 0) {
			return AlarmKind.acked;
		} else {
			return AlarmKind.cur;
		}
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
	 * 경보메세지
	 * 
	 * @return 경보메세지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	/**
	 * 경보발생번호
	 * 
	 * @return 경보발생번호
	 */
	public long getAlarmNo() {
		return alarmNo;
	}

	/**
	 * 경보명
	 * 
	 * @return 경보명
	 */
	public String getAlcdName() {
		return alcdName;
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
	 * 변경일시
	 * 
	 * @return 변경일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	public long getClearDate() {
		return clearDate;
	}

	/**
	 * 경보해제메모
	 * 
	 * @return 경보해제메모
	 */
	public String getClearMemo() {
		return clearMemo;
	}

	/**
	 * 경보해제원인명
	 * 
	 * @return 경보해제원인명
	 */
	public String getClearRsnName() {
		return clearRsnName;
	}

	/**
	 * 경보해제원인번호
	 * 
	 * @return 경보해제원인번호
	 */
	public int getClearRsnNo() {
		return clearRsnNo;
	}

	/**
	 * 경보해제운용자번호
	 * 
	 * @return 경보해제운용자번호
	 */
	public int getClearUserNo() {
		return clearUserNo;
	}

	/**
	 * 비교값
	 * 
	 * @return 비교값
	 */
	public double getCompareValue() {
		return compareValue;
	}

	/**
	 * 설치위치명
	 * 
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
	}

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * IP주소(장비)
	 * 
	 * @return IP주소(장비)
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	public String getMoAname() {
		return moAname;
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * 모델명
	 * 
	 * @return 모델명
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * MO인스턴스
	 * 
	 * @return MO인스턴스
	 */
	public String getMoInstance() {
		return moInstance;
	}

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
	}

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
	}

	public long getOcuDate() {
		return ocuDate;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 상태수집일시
	 * 
	 * @return 상태수집일시
	 */
	public long getPsDate() {
		return psDate;
	}

	public String getPsName() {
		return psName;
	}

	/**
	 * 조회된값
	 * 
	 * @return 조회된값
	 */
	public double getPsValue() {
		return psValue;
	}

	/**
	 * 경보원인메모
	 * 
	 * @return 경보원인메모
	 */
	public String getReasonMemo() {
		return reasonMemo;
	}

	/**
	 * 경보원인명
	 * 
	 * @return 경보원인명
	 */
	public String getReasonName() {
		return reasonName;
	}

	/**
	 * 경보원인번호
	 * 
	 * @return 경보원인번호
	 */
	public int getReasonNo() {
		return reasonNo;
	}

	/**
	 * 경보원인등록일시
	 * 
	 * @return 경보원인등록일시
	 */
	public long getReasonRegDate() {
		return reasonRegDate;
	}

	/**
	 * 경보원인등록운용자번호
	 * 
	 * @return 경보원인등록운용자번호
	 */
	public int getReasonRegUserNo() {
		return reasonRegUserNo;
	}

	public long getRegDate() {
		return regDate;
	}

	public String getStatus() {
		return status;
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
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	public String getUpperMoAname() {
		return upperMoAname;
	}

	/**
	 * 상위MO명
	 * 
	 * @return 상위MO명
	 */
	public String getUpperMoName() {
		return upperMoName;
	}

	/**
	 * 상위MO번호
	 * 
	 * @return 상위MO번호
	 */
	public long getUpperMoNo() {
		return upperMoNo;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	/**
	 * 경보해제여부
	 * 
	 * @return 경보해제여부
	 */
	public boolean isClearYn() {
		return clearYn;
	}

	public void setAckDate(long ackDate) {
		this.ackDate = ackDate;
	}

	/**
	 * 확인운용자번호
	 * 
	 * @param ackUserNo
	 *            확인운용자번호
	 */
	public void setAckUserNo(int ackUserNo) {
		this.ackUserNo = ackUserNo;
	}

	/**
	 * 경보분류
	 * 
	 * @param alarmClass
	 *            경보분류
	 */
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}

	/**
	 * 경보구분고유값
	 * 
	 * @param alarmKey
	 *            경보구분고유값
	 */
	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
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
	 * 경보메세지
	 * 
	 * @param alarmMsg
	 *            경보메세지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	/**
	 * 경보발생번호
	 * 
	 * @param alarmNo
	 *            경보발생번호
	 */
	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	/**
	 * 경보명
	 * 
	 * @param alcdName
	 *            경보명
	 */
	public void setAlcdName(String alcdName) {
		this.alcdName = alcdName;
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
	 * 변경일시
	 * 
	 * @param chgDate
	 *            변경일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	public void setClearDate(long clearDate) {
		this.clearDate = clearDate;
	}

	/**
	 * 경보해제메모
	 * 
	 * @param clearMemo
	 *            경보해제메모
	 */
	public void setClearMemo(String clearMemo) {
		this.clearMemo = clearMemo;
	}

	/**
	 * 경보해제원인명
	 * 
	 * @param clearRsnName
	 *            경보해제원인명
	 */
	public void setClearRsnName(String clearRsnName) {
		this.clearRsnName = clearRsnName;
	}

	/**
	 * 경보해제원인번호
	 * 
	 * @param clearRsnNo
	 *            경보해제원인번호
	 */
	public void setClearRsnNo(int clearRsnNo) {
		this.clearRsnNo = clearRsnNo;
	}

	/**
	 * 경보해제운용자번호
	 * 
	 * @param clearUserNo
	 *            경보해제운용자번호
	 */
	public void setClearUserNo(int clearUserNo) {
		this.clearUserNo = clearUserNo;
	}

	/**
	 * 경보해제여부
	 * 
	 * @param clearYn
	 *            경보해제여부
	 */
	public void setClearYn(boolean clearYn) {
		this.clearYn = clearYn;
	}

	/**
	 * 비교값
	 * 
	 * @param compareValue
	 *            비교값
	 */
	public void setCompareValue(double compareValue) {
		this.compareValue = compareValue;
	}

	/**
	 * 설치위치명
	 * 
	 * @param inloName
	 *            설치위치명
	 */
	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo
	 *            설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * IP주소(장비)
	 * 
	 * @param ipAddr
	 *            IP주소(장비)
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public void setMoAname(String moAname) {
		this.moAname = moAname;
	}

	/**
	 * MO분류
	 * 
	 * @param moClass
	 *            MO분류
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * 모델명
	 * 
	 * @param modelName
	 *            모델명
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * 모델번호
	 * 
	 * @param modelNo
	 *            모델번호
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * MO인스턴스
	 * 
	 * @param moInstance
	 *            MO인스턴스
	 */
	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	/**
	 * MO명
	 * 
	 * @param moName
	 *            MO명
	 */
	public void setMoName(String moName) {
		this.moName = moName;
	}

	/**
	 * MO번호
	 * 
	 * @param moNo
	 *            MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setOcuDate(long ocuDate) {
		this.ocuDate = ocuDate;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCode
	 *            상태값번호
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	/**
	 * 상태수집일시
	 * 
	 * @param psDate
	 *            상태수집일시
	 */
	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	public void setPsName(String psName) {
		this.psName = psName;
	}

	/**
	 * 조회된값
	 * 
	 * @param psValue
	 *            조회된값
	 */
	public void setPsValue(double psValue) {
		this.psValue = psValue;
	}

	/**
	 * 경보원인메모
	 * 
	 * @param reasonMemo
	 *            경보원인메모
	 */
	public void setReasonMemo(String reasonMemo) {
		this.reasonMemo = reasonMemo;
	}

	/**
	 * 경보원인명
	 * 
	 * @param reasonName
	 *            경보원인명
	 */
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	/**
	 * 경보원인번호
	 * 
	 * @param reasonNo
	 *            경보원인번호
	 */
	public void setReasonNo(int reasonNo) {
		this.reasonNo = reasonNo;
	}

	/**
	 * 경보원인등록일시
	 * 
	 * @param reasonRegDate
	 *            경보원인등록일시
	 */
	public void setReasonRegDate(long reasonRegDate) {
		this.reasonRegDate = reasonRegDate;
	}

	/**
	 * 경보원인등록운용자번호
	 * 
	 * @param reasonRegUserNo
	 *            경보원인등록운용자번호
	 */
	public void setReasonRegUserNo(int reasonRegUserNo) {
		this.reasonRegUserNo = reasonRegUserNo;
	}

	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	public void setStatus(String status) {
		this.status = status;
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

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	public void setUpperMoAname(String upperMoAname) {
		this.upperMoAname = upperMoAname;
	}

	/**
	 * 상위MO명
	 * 
	 * @param upperMoName
	 *            상위MO명
	 */
	public void setUpperMoName(String upperMoName) {
		this.upperMoName = upperMoName;
	}

	/**
	 * 상위MO번호
	 * 
	 * @param upperMoNo
	 *            상위MO번호
	 */
	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append("ALARM(NO(" + getAlarmNo() + ")");
		sb.append("KEY(" + getAlarmKey() + ")");
		sb.append(")");
		sb.append(status);
		return sb.toString();
	}
}
