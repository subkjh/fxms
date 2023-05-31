package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
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


@FxTable(name = "FX_CO_WEATHER", comment = "공통날씨테이블")
@FxIndex(name = "FX_CO_WEATHER__PK", type = INDEX_TYPE.PK, columns = {"AREA_NUM", "YMD", "HH"})
public class FX_CO_WEATHER implements Serializable {

public FX_CO_WEATHER() {
 }

@FxColumn(name = "AREA_NUM", size = 8, comment = "지역번호")
private int areaNum;


@FxColumn(name = "YMD", size = 8, comment = "년월일")
private int ymd;


@FxColumn(name = "HH", size = 2, comment = "HH")
private int hh;


@FxColumn(name = "TEMP_VAL", size = 8, nullable = true, comment = "온도값")
private double tempVal;


@FxColumn(name = "HUMI_VAL", size = 8, nullable = true, comment = "습도값")
private double humiVal;


@FxColumn(name = "WEAT_CD", size = 3, nullable = true, comment = "날씨코드")
private String weatCd;


@FxColumn(name = "PREC_VAL", size = 8, nullable = true, comment = "강수량값")
private double precVal;


@FxColumn(name = "WNDSP_VAL", size = 8, nullable = true, comment = "풍속값")
private double wndspVal;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 지역번호
* @return 지역번호
*/
public int getAreaNum() {
return areaNum;
}
/**
* 지역번호
*@param areaNum 지역번호
*/
public void setAreaNum(int areaNum) {
	this.areaNum = areaNum;
}
/**
* 년월일
* @return 년월일
*/
public int getYmd() {
return ymd;
}
/**
* 년월일
*@param ymd 년월일
*/
public void setYmd(int ymd) {
	this.ymd = ymd;
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
* 온도값
* @return 온도값
*/
public double getTempVal() {
return tempVal;
}
/**
* 온도값
*@param tempVal 온도값
*/
public void setTempVal(double tempVal) {
	this.tempVal = tempVal;
}
/**
* 습도값
* @return 습도값
*/
public double getHumiVal() {
return humiVal;
}
/**
* 습도값
*@param humiVal 습도값
*/
public void setHumiVal(double humiVal) {
	this.humiVal = humiVal;
}
/**
* 날씨코드
* @return 날씨코드
*/
public String getWeatCd() {
return weatCd;
}
/**
* 날씨코드
*@param weatCd 날씨코드
*/
public void setWeatCd(String weatCd) {
	this.weatCd = weatCd;
}
/**
* 강수량값
* @return 강수량값
*/
public double getPrecVal() {
return precVal;
}
/**
* 강수량값
*@param precVal 강수량값
*/
public void setPrecVal(double precVal) {
	this.precVal = precVal;
}
/**
* 풍속값
* @return 풍속값
*/
public double getWndspVal() {
return wndspVal;
}
/**
* 풍속값
*@param wndspVal 풍속값
*/
public void setWndspVal(double wndspVal) {
	this.wndspVal = wndspVal;
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
