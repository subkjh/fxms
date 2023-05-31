package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.06.14 17:05
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_AL_STAT_HH_INLO", comment = "알람통계HH설치위치테이블")
@FxIndex(name = "FX_AL_ALARM_STAT_HH_INLO__PK", type = INDEX_TYPE.PK, columns = {"ST_DATE", "HH", "INLO_NO"})
public class FX_AL_STAT_HH_INLO implements Serializable {

public FX_AL_STAT_HH_INLO() {
 }

@FxColumn(name = "ST_DATE", size = 8, comment = "통계일자")
private int stDate;


@FxColumn(name = "HH", size = 2, comment = "HH")
private int hh;


@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호")
private int inloNo;


@FxColumn(name = "ALARM_OCCUR_CNT", size = 9, comment = "알람발생건수")
private int alarmOccurCnt;


@FxColumn(name = "ALARM_RLSE_CNT", size = 9, comment = "알람해제건수")
private int alarmRlseCnt;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 통계일자
* @return 통계일자
*/
public int getStDate() {
return stDate;
}
/**
* 통계일자
*@param stDate 통계일자
*/
public void setStDate(int stDate) {
	this.stDate = stDate;
}
/**
* HH
* @return HH
*/
public int getHh() {
return hh;
}
/**
* HH
*@param hh HH
*/
public void setHh(int hh) {
	this.hh = hh;
}
/**
* 설치위치번호
* @return 설치위치번호
*/
public int getInloNo() {
return inloNo;
}
/**
* 설치위치번호
*@param inloNo 설치위치번호
*/
public void setInloNo(int inloNo) {
	this.inloNo = inloNo;
}
/**
* 알람발생건수
* @return 알람발생건수
*/
public int getAlarmOccurCnt() {
return alarmOccurCnt;
}
/**
* 알람발생건수
*@param alarmOccurCnt 알람발생건수
*/
public void setAlarmOccurCnt(int alarmOccurCnt) {
	this.alarmOccurCnt = alarmOccurCnt;
}
/**
* 알람해제건수
* @return 알람해제건수
*/
public int getAlarmRlseCnt() {
return alarmRlseCnt;
}
/**
* 알람해제건수
*@param alarmRlseCnt 알람해제건수
*/
public void setAlarmRlseCnt(int alarmRlseCnt) {
	this.alarmRlseCnt = alarmRlseCnt;
}
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
* 수정사용자번호
* @return 수정사용자번호
*/
public int getChgUserNo() {
return chgUserNo;
}
/**
* 수정사용자번호
*@param chgUserNo 수정사용자번호
*/
public void setChgUserNo(int chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public long getChgDtm() {
return chgDtm;
}
/**
* 수정일시
*@param chgDtm 수정일시
*/
public void setChgDtm(long chgDtm) {
	this.chgDtm = chgDtm;
}
}
