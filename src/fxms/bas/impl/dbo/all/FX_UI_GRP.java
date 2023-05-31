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


@FxTable(name = "FX_UI_GRP", comment = "UI그룹테이블")
@FxIndex(name = "FY_UI_GRP__PK", type = INDEX_TYPE.PK, columns = {"USER_NO", "UI_GRP_NO"})
public class FX_UI_GRP implements Serializable {

public FX_UI_GRP() {
 }

@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
private int userNo;


@FxColumn(name = "UI_GRP_NO", size = 5, comment = "화면그룹번호")
private int uiGrpNo;


@FxColumn(name = "UI_GRP_NAME", size = 50, comment = "화면ID")
private String uiGrpName;


@FxColumn(name = "DISP_YN", size = 1, comment = "표시여부", defValue = "Y")
private boolean dispYn = true;


@FxColumn(name = "SORT_SEQ", size = 5, nullable = true, comment = "정렬순서")
private int sortSeq;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 사용자번호
* @return 사용자번호
*/
public int getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(int userNo) {
	this.userNo = userNo;
}
/**
* 화면그룹번호
* @return 화면그룹번호
*/
public int getUiGrpNo() {
return uiGrpNo;
}
/**
* 화면그룹번호
*@param uiGrpNo 화면그룹번호
*/
public void setUiGrpNo(int uiGrpNo) {
	this.uiGrpNo = uiGrpNo;
}
/**
* 화면ID
* @return 화면ID
*/
public String getUiGrpName() {
return uiGrpName;
}
/**
* 화면ID
*@param uiGrpName 화면ID
*/
public void setUiGrpName(String uiGrpName) {
	this.uiGrpName = uiGrpName;
}
/**
* 표시여부
* @return 표시여부
*/
public boolean isDispYn() {
return dispYn;
}
/**
* 표시여부
*@param dispYn 표시여부
*/
public void setDispYn(boolean dispYn) {
	this.dispYn = dispYn;
}
/**
* 정렬순서
* @return 정렬순서
*/
public int getSortSeq() {
return sortSeq;
}
/**
* 정렬순서
*@param sortSeq 정렬순서
*/
public void setSortSeq(int sortSeq) {
	this.sortSeq = sortSeq;
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
