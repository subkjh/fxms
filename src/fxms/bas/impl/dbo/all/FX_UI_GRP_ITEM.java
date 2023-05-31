package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.20 09:51
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UI_GRP_ITEM", comment = "UI그룹항목테이블")
@FxIndex(name = "FX_UI_GRP_ITEM__PK", type = INDEX_TYPE.PK, columns = {"USER_NO", "UI_GRP_NO", "UI_NO"})
public class FX_UI_GRP_ITEM implements Serializable {

public FX_UI_GRP_ITEM() {
 }

@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
private int userNo;


@FxColumn(name = "UI_GRP_NO", size = 5, comment = "화면그룹번호")
private int uiGrpNo;


@FxColumn(name = "UI_NO", size = 5, comment = "화면번호")
private int uiNo;


@FxColumn(name = "UI_TTL", size = 100, comment = "화면타이틀")
private String uiTtl;


@FxColumn(name = "UI_STYLE", size = 20, comment = "화면스타일")
private String uiStyle;


@FxColumn(name = "UI_X", size = 9, nullable = true, comment = "화면X좌표")
private int uiX;


@FxColumn(name = "UI_Y", size = 9, nullable = true, comment = "화면Y좌표")
private int uiY;


@FxColumn(name = "UI_WDTH", size = 9, nullable = true, comment = "화면폭")
private int uiWdth;


@FxColumn(name = "UI_HGHT", size = 9, nullable = true, comment = "화면높이")
private int uiHght;


@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
private String opId;


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
* 화면번호
* @return 화면번호
*/
public int getUiNo() {
return uiNo;
}
/**
* 화면번호
*@param uiNo 화면번호
*/
public void setUiNo(int uiNo) {
	this.uiNo = uiNo;
}
/**
* 화면타이틀
* @return 화면타이틀
*/
public String getUiTtl() {
return uiTtl;
}
/**
* 화면타이틀
*@param uiTtl 화면타이틀
*/
public void setUiTtl(String uiTtl) {
	this.uiTtl = uiTtl;
}
/**
* 화면스타일
* @return 화면스타일
*/
public String getUiStyle() {
return uiStyle;
}
/**
* 화면스타일
*@param uiStyle 화면스타일
*/
public void setUiStyle(String uiStyle) {
	this.uiStyle = uiStyle;
}
/**
* 화면X좌표
* @return 화면X좌표
*/
public int getUiX() {
return uiX;
}
/**
* 화면X좌표
*@param uiX 화면X좌표
*/
public void setUiX(int uiX) {
	this.uiX = uiX;
}
/**
* 화면Y좌표
* @return 화면Y좌표
*/
public int getUiY() {
return uiY;
}
/**
* 화면Y좌표
*@param uiY 화면Y좌표
*/
public void setUiY(int uiY) {
	this.uiY = uiY;
}
/**
* 화면폭
* @return 화면폭
*/
public int getUiWdth() {
return uiWdth;
}
/**
* 화면폭
*@param uiWdth 화면폭
*/
public void setUiWdth(int uiWdth) {
	this.uiWdth = uiWdth;
}
/**
* 화면높이
* @return 화면높이
*/
public int getUiHght() {
return uiHght;
}
/**
* 화면높이
*@param uiHght 화면높이
*/
public void setUiHght(int uiHght) {
	this.uiHght = uiHght;
}
/**
* 기능ID
* @return 기능ID
*/
public String getOpId() {
return opId;
}
/**
* 기능ID
*@param opId 기능ID
*/
public void setOpId(String opId) {
	this.opId = opId;
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
