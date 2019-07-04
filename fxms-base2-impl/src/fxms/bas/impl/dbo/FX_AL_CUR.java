package fxms.bas.impl.dbo;

import java.io.Serializable;

import fxms.bas.mo.property.MoOwnership;
import fxms.bas.mo.property.Moable;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.20 09:46
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CUR", comment = "경보(현재)테이블")
@FxIndex(name = "FX_AL_CUR__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
@FxIndex(name = "FX_AL_CUR__KEY_MO", type = INDEX_TYPE.KEY, columns = { "MO_NO" })
public class FX_AL_CUR implements Serializable, Moable, MoOwnership {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5753261739978385832L;

	public FX_AL_CUR() {
	}

	public static final String FX_SEQ_ALARMNO = "FX_SEQ_ALARMNO";
	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호", sequence = "FX_SEQ_ALARMNO")
	private long alarmNo;

	@FxColumn(name = "ALARM_KEY", size = 100, nullable = true, comment = "경보구분고유값")
	private String alarmKey;

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
	private int alcdNo;

	@FxColumn(name = "ALCD_NAME", size = 100, nullable = true, comment = "경보명")
	private String alcdName;

	@FxColumn(name = "ALARM_CLASS", size = 50, nullable = true, comment = "경보분류")
	private String alarmClass;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "경보등급")
	private int alarmLevel;

	@FxColumn(name = "ALARM_MSG", size = 1000, nullable = true, comment = "경보메세지")
	private String alarmMsg;

	@FxColumn(name = "COMPARE_VALUE", size = 19, nullable = true, comment = "비교값")
	private double compareValue;

	@FxColumn(name = "PS_VALUE", size = 19, nullable = true, comment = "조회된값")
	private double psValue;

	@FxColumn(name = "PS_CODE", size = 20, nullable = true, comment = "상태값번호")
	private String psCode;

	@FxColumn(name = "PS_NAME", size = 50, nullable = true, comment = "상태명")
	private String psName;

	@FxColumn(name = "PS_DATE", size = 14, nullable = true, comment = "상태수집일시")
	private long psDate;

	@FxColumn(name = "MO_NO", size = 19, nullable = true, comment = "MO번호")
	private long moNo;

	@FxColumn(name = "MO_NAME", size = 200, nullable = true, comment = "MO명")
	private String moName;

	@FxColumn(name = "MO_ANAME", size = 200, nullable = true, comment = "MO명")
	private String moAname;

	@FxColumn(name = "MO_INSTANCE", size = 50, nullable = true, comment = "MO인스턴스")
	private String moInstance;

	@FxColumn(name = "MO_CLASS", size = 20, comment = "MO분류 ")
	private String moClass;

	@FxColumn(name = "UPPER_MO_NO", size = 19, nullable = true, comment = "상위MO번호")
	private long upperMoNo;

	@FxColumn(name = "UPPER_MO_NAME", size = 200, nullable = true, comment = "상위MO명")
	private String upperMoName;

	@FxColumn(name = "UPPER_MO_ANAME", size = 200, nullable = true, comment = "상위MO명")
	private String upperMoAname;

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호")
	private int inloNo;

	@FxColumn(name = "INLO_NAME", size = 200, nullable = true, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "MODEL_NO", size = 9, nullable = true, comment = "모델번호")
	private int modelNo;

	@FxColumn(name = "MODEL_NAME", size = 100, nullable = true, comment = "모델명")
	private String modelName;

	@FxColumn(name = "REG_DATE", size = 19, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "OCU_DATE", size = 19, nullable = true, comment = "발생일시")
	private long ocuDate;

	@FxColumn(name = "ACK_DATE", size = 19, nullable = true, comment = "확인일시")
	private long ackDate;

	@FxColumn(name = "ACK_USER_NO", size = 9, nullable = true, comment = "확인운용자번호")
	private int ackUserNo;

	@FxColumn(name = "REASON_REG_DATE", size = 14, nullable = true, comment = "경보원인등록일시")
	private long reasonRegDate;

	@FxColumn(name = "REASON_REG_USER_NO", size = 9, nullable = true, comment = "경보원인등록운용자번호")
	private int reasonRegUserNo;

	@FxColumn(name = "REASON_NO", size = 9, nullable = true, comment = "경보원인번호", defValue = "0")
	private int reasonNo = 0;

	@FxColumn(name = "REASON_NAME", size = 100, nullable = true, comment = "경보원인명")
	private String reasonName;

	@FxColumn(name = "REASON_MEMO", size = 200, nullable = true, comment = "경보원인메모")
	private String reasonMemo;

	@FxColumn(name = "IP_ADDR", size = 39, nullable = true, comment = "IP주소(장비)")
	private String ipAddr;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "변경일시")
	private long chgDate;

	@FxColumn(name = "TREAT_NAME", size = 50, nullable = true, comment = "경보조치코드명")
	private String treatName;

	/**
	 * 경보발생번호
	 * 
	 * @return 경보발생번호
	 */
	public long getAlarmNo() {
		return alarmNo;
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
	 * 경보구분고유값
	 * 
	 * @return 경보구분고유값
	 */
	public String getAlarmKey() {
		return alarmKey;
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
	 * 경보명
	 * 
	 * @return 경보명
	 */
	public String getAlcdName() {
		return alcdName;
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
	 * 경보분류
	 * 
	 * @return 경보분류
	 */
	public String getAlarmClass() {
		return alarmClass;
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
	 * 경보메세지
	 * 
	 * @return 경보메세지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
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
	 * 비교값
	 * 
	 * @return 비교값
	 */
	public double getCompareValue() {
		return compareValue;
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
	 * 조회된값
	 * 
	 * @return 조회된값
	 */
	public double getPsValue() {
		return psValue;
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
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
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
	 * @return 상태수집일시
	 */
	public long getPsDate() {
		return psDate;
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

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo() {
		return moNo;
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

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName() {
		return moName;
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
	 * MO인스턴스
	 * 
	 * @return MO인스턴스
	 */
	public String getMoInstance() {
		return moInstance;
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
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
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
	 * 상위MO번호
	 * 
	 * @return 상위MO번호
	 */
	public long getUpperMoNo() {
		return upperMoNo;
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

	/**
	 * 상위MO명
	 * 
	 * @return 상위MO명
	 */
	public String getUpperMoName() {
		return upperMoName;
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
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo() {
		return inloNo;
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
	 * 설치위치명
	 * 
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
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
	 * 모델번호
	 * 
	 * @return 모델번호
	 */
	public int getModelNo() {
		return modelNo;
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
	 * 모델명
	 * 
	 * @return 모델명
	 */
	public String getModelName() {
		return modelName;
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
	 * 확인운용자번호
	 * 
	 * @return 확인운용자번호
	 */
	public int getAckUserNo() {
		return ackUserNo;
	}

	public long getRegDate() {
		return regDate;
	}

	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	public long getOcuDate() {
		return ocuDate;
	}

	public void setOcuDate(long ocuDate) {
		this.ocuDate = ocuDate;
	}

	public long getAckDate() {
		return ackDate;
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
	 * 경보원인등록일시
	 * 
	 * @return 경보원인등록일시
	 */
	public long getReasonRegDate() {
		return reasonRegDate;
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
	 * @return 경보원인등록운용자번호
	 */
	public int getReasonRegUserNo() {
		return reasonRegUserNo;
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

	/**
	 * 경보원인번호
	 * 
	 * @return 경보원인번호
	 */
	public int getReasonNo() {
		return reasonNo;
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
	 * 경보원인명
	 * 
	 * @return 경보원인명
	 */
	public String getReasonName() {
		return reasonName;
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
	 * 경보원인메모
	 * 
	 * @return 경보원인메모
	 */
	public String getReasonMemo() {
		return reasonMemo;
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
	 * IP주소(장비)
	 * 
	 * @return IP주소(장비)
	 */
	public String getIpAddr() {
		return ipAddr;
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

	/**
	 * 변경일시
	 * 
	 * @return 변경일시
	 */
	public long getChgDate() {
		return chgDate;
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

	public String getPsName() {
		return psName;
	}

	public void setPsName(String psName) {
		this.psName = psName;
	}

	public String getMoAname() {
		return moAname;
	}

	public void setMoAname(String moAname) {
		this.moAname = moAname;
	}

	public String getUpperMoAname() {
		return upperMoAname;
	}

	public void setUpperMoAname(String upperMoAname) {
		this.upperMoAname = upperMoAname;
	}

}
