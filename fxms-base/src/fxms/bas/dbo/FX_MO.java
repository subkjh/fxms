package fxms.bas.dbo;


import java.io.Serializable;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
* @since 2017.06.16 15:13
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_MO", comment = "관리대상테이블")
@FxIndex(name = "FX_MO__PK", type = INDEX_TYPE.PK, columns = {"MO_NO"})
@FxIndex(name = "FX_MO__KEY_NODE", type = INDEX_TYPE.KEY, columns = {"UPPER_MO_NO"})
@FxIndex(name = "FX_MO__KEY_MNG", type = INDEX_TYPE.KEY, columns = {"MNG_YN"})
@FxIndex(name = "FX_MO__KEY_CLS", type = INDEX_TYPE.KEY, columns = {"MO_CLASS"})
@FxIndex(name = "FX_MO__UK_CLS_NAME", type = INDEX_TYPE.UK, columns = {"UPPER_MO_NO", "MO_CLASS", "MO_NAME"})
public class FX_MO implements Serializable {

public FX_MO() {
 }

public static final String FX_SEQ_MONO  = "FX_SEQ_MONO"; 
@FxColumn(name = "MO_NO", size = 19, comment = "MO번호", sequence = "FX_SEQ_MONO")
private long moNo;


@FxColumn(name = "MO_NAME", size = 200, comment = "MO명")
private String moName;


@FxColumn(name = "MO_ANAME", size = 200, nullable = true, comment = "MO표시명")
private String moAname;


@FxColumn(name = "MO_CLASS", size = 20, comment = "MO분류 ")
private String moClass;


@FxColumn(name = "MNG_YN", size = 1, comment = "관리여부", defValue = "'Y'")
private boolean mngYn = true;


@FxColumn(name = "UPPER_MO_NO", size = 19, comment = "상위MO번호", defValue = "0")
private long upperMoNo = 0;


@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호", defValue = "-1")
private int alarmCfgNo = -1;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDate;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
private long chgDate;


@FxColumn(name = "SYNC_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
private int syncUserNo = 0;


@FxColumn(name = "SYNC_DATE", size = 14, nullable = true, comment = "동기화일시")
private long syncDate;


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
* MO명
* @return MO명
*/
public String getMoName() {
return moName;
}
/**
* MO명
*@param moName MO명
*/
public void setMoName(String moName) {
	this.moName = moName;
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
* MO분류 
* @return MO분류 
*/
public String getMoClass() {
return moClass;
}
/**
* MO분류 
*@param moClass MO분류 
*/
public void setMoClass(String moClass) {
	this.moClass = moClass;
}
/**
* 관리여부
* @return 관리여부
*/
public boolean isMngYn() {
return mngYn;
}
/**
* 관리여부
*@param mngYn 관리여부
*/
public void setMngYn(boolean mngYn) {
	this.mngYn = mngYn;
}
/**
* 상위MO번호
* @return 상위MO번호
*/
public long getUpperMoNo() {
return upperMoNo;
}
/**
* 상위MO번호
*@param upperMoNo 상위MO번호
*/
public void setUpperMoNo(long upperMoNo) {
	this.upperMoNo = upperMoNo;
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
* 등록운영자번호
* @return 등록운영자번호
*/
public int getRegUserNo() {
return regUserNo;
}
/**
* 등록운영자번호
*@param regUserNo 등록운영자번호
*/
public void setRegUserNo(int regUserNo) {
	this.regUserNo = regUserNo;
}
/**
* 등록일시
* @return 등록일시
*/
public long getRegDate() {
return regDate;
}
/**
* 등록일시
*@param regDate 등록일시
*/
public void setRegDate(long regDate) {
	this.regDate = regDate;
}
/**
* 수정운영자번호
* @return 수정운영자번호
*/
public int getChgUserNo() {
return chgUserNo;
}
/**
* 수정운영자번호
*@param chgUserNo 수정운영자번호
*/
public void setChgUserNo(int chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public long getChgDate() {
return chgDate;
}
/**
* 수정일시
*@param chgDate 수정일시
*/
public void setChgDate(long chgDate) {
	this.chgDate = chgDate;
}
/**
* 수정운영자번호
* @return 수정운영자번호
*/
public int getSyncUserNo() {
return syncUserNo;
}
/**
* 수정운영자번호
*@param syncUserNo 수정운영자번호
*/
public void setSyncUserNo(int syncUserNo) {
	this.syncUserNo = syncUserNo;
}
/**
* 동기화일시
* @return 동기화일시
*/
public long getSyncDate() {
return syncDate;
}
/**
* 동기화일시
*@param syncDate 동기화일시
*/
public void setSyncDate(long syncDate) {
	this.syncDate = syncDate;
}
}
