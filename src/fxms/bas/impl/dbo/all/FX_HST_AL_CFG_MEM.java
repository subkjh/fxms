package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.02 18:01
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_HST_AL_CFG_MEM", comment = "경보임계(설정항목)테이블")
@FxIndex(name = "FX_HST_AL_CFG_MEM__KEY", type = INDEX_TYPE.KEY, columns = {"REG_USER_NO", "REG_DTM", "ALARM_CFG_NO"})
public class FX_HST_AL_CFG_MEM implements Serializable {

public FX_HST_AL_CFG_MEM() {
 }

@FxColumn(name = "REG_USER_NO", size = 9, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, comment = "등록일시")
private long regDtm;


@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호")
private int alarmCfgNo;


@FxColumn(name = "VERIFIER_VAL", size = 10, comment = "검증자값", defValue = "all")
private String verifierVal = "all";


@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드")
private int alcdNo;


@FxColumn(name = "ALARM_LEVEL", size = 2, comment = "경보등급")
private int alarmLevel;


@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
private boolean useYn = true;


@FxColumn(name = "COMPARE_VAL", size = 20, nullable = true, comment = "비교값")
private double compareVal;


@FxColumn(name = "REPEAT_TIMES", size = 2, nullable = true, comment = "연속일치회수", defValue = "1")
private int repeatTimes = 1;


@FxColumn(name = "VERIFIER_JAVA_CLASS", size = 100, nullable = true, comment = "검증자자바클래스")
private String verifierJavaClass;


@FxColumn(name = "TREAT_NAME", size = 100, nullable = true, comment = "경보조치코드명")
private String treatName;


/**
* 등록사용자번호
* @return 등록사용자번호
*/
public int getRegUserNo() {
return regUserNo;
}
/**
* 등록사용자번호
*@param regUserNo 등록사용자번호
*/
public void setRegUserNo(int regUserNo) {
	this.regUserNo = regUserNo;
}
/**
* 등록일시
* @return 등록일시
*/
public long getRegDtm() {
return regDtm;
}
/**
* 등록일시
*@param regDtm 등록일시
*/
public void setRegDtm(long regDtm) {
	this.regDtm = regDtm;
}
/**
* 경보조건번호
* @return 경보조건번호
*/
public int getAlarmCfgNo() {
return alarmCfgNo;
}
/**
* 경보조건번호
*@param alarmCfgNo 경보조건번호
*/
public void setAlarmCfgNo(int alarmCfgNo) {
	this.alarmCfgNo = alarmCfgNo;
}
/**
* 검증자값
* @return 검증자값
*/
public String getVerifierVal() {
return verifierVal;
}
/**
* 검증자값
*@param verifierVal 검증자값
*/
public void setVerifierVal(String verifierVal) {
	this.verifierVal = verifierVal;
}
/**
* 경보코드
* @return 경보코드
*/
public int getAlcdNo() {
return alcdNo;
}
/**
* 경보코드
*@param alcdNo 경보코드
*/
public void setAlcdNo(int alcdNo) {
	this.alcdNo = alcdNo;
}
/**
* 경보등급
* @return 경보등급
*/
public int getAlarmLevel() {
return alarmLevel;
}
/**
* 경보등급
*@param alarmLevel 경보등급
*/
public void setAlarmLevel(int alarmLevel) {
	this.alarmLevel = alarmLevel;
}
/**
* 사용여부
* @return 사용여부
*/
public boolean isUseYn() {
return useYn;
}
/**
* 사용여부
*@param useYn 사용여부
*/
public void setUseYn(boolean useYn) {
	this.useYn = useYn;
}
/**
* 비교값
* @return 비교값
*/
public double getCompareVal() {
return compareVal;
}
/**
* 비교값
*@param compareVal 비교값
*/
public void setCompareVal(double compareVal) {
	this.compareVal = compareVal;
}
/**
* 연속일치회수
* @return 연속일치회수
*/
public int getRepeatTimes() {
return repeatTimes;
}
/**
* 연속일치회수
*@param repeatTimes 연속일치회수
*/
public void setRepeatTimes(int repeatTimes) {
	this.repeatTimes = repeatTimes;
}
/**
* 검증자자바클래스
* @return 검증자자바클래스
*/
public String getVerifierJavaClass() {
return verifierJavaClass;
}
/**
* 검증자자바클래스
*@param verifierJavaClass 검증자자바클래스
*/
public void setVerifierJavaClass(String verifierJavaClass) {
	this.verifierJavaClass = verifierJavaClass;
}
/**
* 경보조치코드명
* @return 경보조치코드명
*/
public String getTreatName() {
return treatName;
}
/**
* 경보조치코드명
*@param treatName 경보조치코드명
*/
public void setTreatName(String treatName) {
	this.treatName = treatName;
}
}
