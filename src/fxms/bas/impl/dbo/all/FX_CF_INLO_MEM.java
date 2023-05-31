package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.26 10:48
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CF_INLO_MEM", comment = "설치위치구성원테이블")
@FxIndex(name = "FX_CF_INLO_CHLD__PK", type = INDEX_TYPE.PK, columns = {"INLO_NO", "LOWER_INLO_NO"})
@FxIndex(name = "FX_CF_INLO_CHLD__KEY1", type = INDEX_TYPE.KEY, columns = {"LOWER_INLO_NO"})
public class FX_CF_INLO_MEM implements Serializable {

public FX_CF_INLO_MEM() {
 }

@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호")
private int inloNo;


@FxColumn(name = "LOWER_INLO_NO", size = 9, comment = "하위설치위치번호")
private int lowerInloNo;


@FxColumn(name = "LOWER_DEPTH", size = 5, nullable = true, comment = "하위깊이", defValue = "0")
private int lowerDepth = 0;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


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
* 하위설치위치번호
* @return 하위설치위치번호
*/
public int getLowerInloNo() {
return lowerInloNo;
}
/**
* 하위설치위치번호
*@param lowerInloNo 하위설치위치번호
*/
public void setLowerInloNo(int lowerInloNo) {
	this.lowerInloNo = lowerInloNo;
}
/**
* 하위깊이
* @return 하위깊이
*/
public int getLowerDepth() {
return lowerDepth;
}
/**
* 하위깊이
*@param lowerDepth 하위깊이
*/
public void setLowerDepth(int lowerDepth) {
	this.lowerDepth = lowerDepth;
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
