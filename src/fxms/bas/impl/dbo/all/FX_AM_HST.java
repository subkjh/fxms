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


@FxTable(name = "FX_AM_HST", comment = "관리그룹행위이력")
@FxIndex(name = "FX_AM_HST__PK", type = INDEX_TYPE.PK, columns = {"AM_GRP_NO", "USER_NO", "MO_NO", "ALARM_NO"})
@FxIndex(name = "FX_AM_HST__KEY", type = INDEX_TYPE.KEY, columns = {"REG_DTM"})
public class FX_AM_HST implements Serializable {

public FX_AM_HST() {
 }

@FxColumn(name = "AM_GRP_NO", size = 9, comment = "관리그룹번호")
private int amGrpNo;


@FxColumn(name = "AM_GRP_NAME", size = 50, comment = "관리그룹명")
private String amGrpName;


@FxColumn(name = "USER_NO", size = 19, comment = "사용자번호")
private long userNo;


@FxColumn(name = "AM_NAME", size = 50, nullable = true, comment = "사용자명")
private String amName;


@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
private long moNo;


@FxColumn(name = "MO_ANAME", size = 200, nullable = true, comment = "MO표시명")
private String moAname;


@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호")
private long alarmNo;


@FxColumn(name = "REG_DTM", size = 14, comment = "등록일시")
private long regDtm;


@FxColumn(name = "TREAT_NAME", size = 100, nullable = true, comment = "경보조치코드명")
private String treatName;


@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
private int alcdNo;


@FxColumn(name = "ALCD_NAME", size = 200, comment = "경보코드명")
private String alcdName;


@FxColumn(name = "CLEAR_YN", size = 1, comment = "해제여부", defValue = "N")
private boolean clearYn = false;


@FxColumn(name = "ERR_YN", size = 1, comment = "오류여부", defValue = "N")
private boolean errYn = false;


@FxColumn(name = "ERR_MSG", size = 1000, nullable = true, comment = "오류메시지")
private String errMsg;


/**
* 관리그룹번호
* @return 관리그룹번호
*/
public int getAmGrpNo() {
return amGrpNo;
}
/**
* 관리그룹번호
*@param amGrpNo 관리그룹번호
*/
public void setAmGrpNo(int amGrpNo) {
	this.amGrpNo = amGrpNo;
}
/**
* 관리그룹명
* @return 관리그룹명
*/
public String getAmGrpName() {
return amGrpName;
}
/**
* 관리그룹명
*@param amGrpName 관리그룹명
*/
public void setAmGrpName(String amGrpName) {
	this.amGrpName = amGrpName;
}
/**
* 사용자번호
* @return 사용자번호
*/
public long getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(long userNo) {
	this.userNo = userNo;
}
/**
* 사용자명
* @return 사용자명
*/
public String getAmName() {
return amName;
}
/**
* 사용자명
*@param amName 사용자명
*/
public void setAmName(String amName) {
	this.amName = amName;
}
/**
* MO번호
* @return MO번호
*/
public long getMoNo() {
return moNo;
}
/**
* MO번호
*@param moNo MO번호
*/
public void setMoNo(long moNo) {
	this.moNo = moNo;
}
/**
* MO표시명
* @return MO표시명
*/
public String getMoAname() {
return moAname;
}
/**
* MO표시명
*@param moAname MO표시명
*/
public void setMoAname(String moAname) {
	this.moAname = moAname;
}
/**
* 경보발생번호
* @return 경보발생번호
*/
public long getAlarmNo() {
return alarmNo;
}
/**
* 경보발생번호
*@param alarmNo 경보발생번호
*/
public void setAlarmNo(long alarmNo) {
	this.alarmNo = alarmNo;
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
/**
* 경보코드번호
* @return 경보코드번호
*/
public int getAlcdNo() {
return alcdNo;
}
/**
* 경보코드번호
*@param alcdNo 경보코드번호
*/
public void setAlcdNo(int alcdNo) {
	this.alcdNo = alcdNo;
}
/**
* 경보코드명
* @return 경보코드명
*/
public String getAlcdName() {
return alcdName;
}
/**
* 경보코드명
*@param alcdName 경보코드명
*/
public void setAlcdName(String alcdName) {
	this.alcdName = alcdName;
}
/**
* 해제여부
* @return 해제여부
*/
public boolean isClearYn() {
return clearYn;
}
/**
* 해제여부
*@param clearYn 해제여부
*/
public void setClearYn(boolean clearYn) {
	this.clearYn = clearYn;
}
/**
* 오류여부
* @return 오류여부
*/
public boolean isErrYn() {
return errYn;
}
/**
* 오류여부
*@param errYn 오류여부
*/
public void setErrYn(boolean errYn) {
	this.errYn = errYn;
}
/**
* 오류메시지
* @return 오류메시지
*/
public String getErrMsg() {
return errMsg;
}
/**
* 오류메시지
*@param errMsg 오류메시지
*/
public void setErrMsg(String errMsg) {
	this.errMsg = errMsg;
}
}
