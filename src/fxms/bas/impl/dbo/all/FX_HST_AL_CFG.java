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


@FxTable(name = "FX_HST_AL_CFG", comment = "경보임계(설정)테이블")
@FxIndex(name = "FX_HST_AL_CFG__PK", type = INDEX_TYPE.PK, columns = {"REG_USER_NO", "REG_DTM", "ALARM_CFG_NO"})
public class FX_HST_AL_CFG implements Serializable {

public FX_HST_AL_CFG() {
 }

@FxColumn(name = "REG_USER_NO", size = 9, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, comment = "등록일시")
private long regDtm;


@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호")
private int alarmCfgNo;


@FxColumn(name = "ALARM_COND_NAME", size = 50, nullable = true, comment = "경보조건명")
private String alarmCondName;


@FxColumn(name = "ALARM_COND_DESC", size = 100, nullable = true, comment = "경보조건설명")
private String alarmCondDesc;


@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "MO클래스")
private String moClass;


@FxColumn(name = "BASIC_CFG_YN", size = 1, nullable = true, comment = "기본경보조건여부", defValue = "N")
private boolean basicCfgYn = false;


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
* 경보조건명
* @return 경보조건명
*/
public String getAlarmCondName() {
return alarmCondName;
}
/**
* 경보조건명
*@param alarmCondName 경보조건명
*/
public void setAlarmCondName(String alarmCondName) {
	this.alarmCondName = alarmCondName;
}
/**
* 경보조건설명
* @return 경보조건설명
*/
public String getAlarmCondDesc() {
return alarmCondDesc;
}
/**
* 경보조건설명
*@param alarmCondDesc 경보조건설명
*/
public void setAlarmCondDesc(String alarmCondDesc) {
	this.alarmCondDesc = alarmCondDesc;
}
/**
* MO클래스
* @return MO클래스
*/
public String getMoClass() {
return moClass;
}
/**
* MO클래스
*@param moClass MO클래스
*/
public void setMoClass(String moClass) {
	this.moClass = moClass;
}
/**
* 기본경보조건여부
* @return 기본경보조건여부
*/
public boolean isBasicCfgYn() {
return basicCfgYn;
}
/**
* 기본경보조건여부
*@param basicCfgYn 기본경보조건여부
*/
public void setBasicCfgYn(boolean basicCfgYn) {
	this.basicCfgYn = basicCfgYn;
}
}
