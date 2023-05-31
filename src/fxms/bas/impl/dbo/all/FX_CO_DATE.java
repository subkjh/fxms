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


@FxTable(name = "FX_CO_DATE", comment = "공통일자테이블")
@FxIndex(name = "FX_CO_DATE__PK", type = INDEX_TYPE.PK, columns = {"YMD"})
public class FX_CO_DATE implements Serializable {

public FX_CO_DATE() {
 }

@FxColumn(name = "YMD", size = 8, comment = "년월일")
private int ymd;


@FxColumn(name = "DOW_CD", size = 4, comment = "요일코드")
private String dowCd;


@FxColumn(name = "HLDAY_YN", size = 1, nullable = true, comment = "휴일여부", defValue = "N")
private boolean hldayYn = false;


@FxColumn(name = "SPECL_DOW_CD", size = 10, nullable = true, comment = "특수요일코드")
private String speclDowCd;


@FxColumn(name = "SEAN_CD", size = 3, nullable = true, comment = "계절코드")
private String seanCd;


@FxColumn(name = "YMD_DESC", size = 50, nullable = true, comment = "년월일설명")
private String ymdDesc;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


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
* 요일코드
* @return 요일코드
*/
public String getDowCd() {
return dowCd;
}
/**
* 요일코드
*@param dowCd 요일코드
*/
public void setDowCd(String dowCd) {
	this.dowCd = dowCd;
}
/**
* 휴일여부
* @return 휴일여부
*/
public boolean isHldayYn() {
return hldayYn;
}
/**
* 휴일여부
*@param hldayYn 휴일여부
*/
public void setHldayYn(boolean hldayYn) {
	this.hldayYn = hldayYn;
}
/**
* 특수요일코드
* @return 특수요일코드
*/
public String getSpeclDowCd() {
return speclDowCd;
}
/**
* 특수요일코드
*@param speclDowCd 특수요일코드
*/
public void setSpeclDowCd(String speclDowCd) {
	this.speclDowCd = speclDowCd;
}
/**
* 계절코드
* @return 계절코드
*/
public String getSeanCd() {
return seanCd;
}
/**
* 계절코드
*@param seanCd 계절코드
*/
public void setSeanCd(String seanCd) {
	this.seanCd = seanCd;
}
/**
* 년월일설명
* @return 년월일설명
*/
public String getYmdDesc() {
return ymdDesc;
}
/**
* 년월일설명
*@param ymdDesc 년월일설명
*/
public void setYmdDesc(String ymdDesc) {
	this.ymdDesc = ymdDesc;
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
