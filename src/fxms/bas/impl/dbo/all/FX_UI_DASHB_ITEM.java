package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.01.25 16:42
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UI_DASHB_ITEM", comment = "UI대시보드항목테이블")
@FxIndex(name = "FX_UI_DASHB_ITEM__PK", type = INDEX_TYPE.PK, columns = {"DASHB_NO", "UI_NO"})
@FxIndex(name = "FX_UI_DASHB_ITEM__FK1", type = INDEX_TYPE.FK, columns = {"DASHB_NO"}, fkTable = "FX_UI_DASHB", fkColumn = "DASHB_NO")
@FxIndex(name = "FX_UI_DASHB_ITEM__FK2", type = INDEX_TYPE.FK, columns = {"OP_ID"}, fkTable = "FX_CO_OP", fkColumn = "OP_ID")
public class FX_UI_DASHB_ITEM implements Serializable {

public FX_UI_DASHB_ITEM() {
 }

@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호")
private Integer dashbNo;


@FxColumn(name = "UI_NO", size = 5, comment = "화면번호")
private Integer uiNo;


@FxColumn(name = "OP_ID", size = 50, comment = "기능ID")
private String opId;


@FxColumn(name = "UI_TITLE", size = 100, comment = "화면타이틀")
private String uiTitle;


@FxColumn(name = "UI_STYLE", size = 20, comment = "화면스타일")
private String uiStyle;


@FxColumn(name = "UI_X", size = 9, nullable = true, comment = "화면X좌표")
private Integer uiX;


@FxColumn(name = "UI_Y", size = 9, nullable = true, comment = "화면Y좌표")
private Integer uiY;


@FxColumn(name = "UI_WIDTH", size = 9, nullable = true, comment = "화면폭")
private Integer uiWidth;


@FxColumn(name = "UI_HEIGHT", size = 9, nullable = true, comment = "화면높이")
private Integer uiHeight;


@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
private String useYn = "Y";


@FxColumn(name = "SORT_SEQ", size = 9, nullable = true, comment = "정렬순서")
private Integer sortSeq;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 대시보드번호
* @return 대시보드번호
*/
public Integer getDashbNo() {
return dashbNo;
}
/**
* 대시보드번호
*@param dashbNo 대시보드번호
*/
public void setDashbNo(Integer dashbNo) {
	this.dashbNo = dashbNo;
}
/**
* 화면번호
* @return 화면번호
*/
public Integer getUiNo() {
return uiNo;
}
/**
* 화면번호
*@param uiNo 화면번호
*/
public void setUiNo(Integer uiNo) {
	this.uiNo = uiNo;
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
* 화면타이틀
* @return 화면타이틀
*/
public String getUiTitle() {
return uiTitle;
}
/**
* 화면타이틀
*@param uiTitle 화면타이틀
*/
public void setUiTitle(String uiTitle) {
	this.uiTitle = uiTitle;
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
public Integer getUiX() {
return uiX;
}
/**
* 화면X좌표
*@param uiX 화면X좌표
*/
public void setUiX(Integer uiX) {
	this.uiX = uiX;
}
/**
* 화면Y좌표
* @return 화면Y좌표
*/
public Integer getUiY() {
return uiY;
}
/**
* 화면Y좌표
*@param uiY 화면Y좌표
*/
public void setUiY(Integer uiY) {
	this.uiY = uiY;
}
/**
* 화면폭
* @return 화면폭
*/
public Integer getUiWidth() {
return uiWidth;
}
/**
* 화면폭
*@param uiWidth 화면폭
*/
public void setUiWidth(Integer uiWidth) {
	this.uiWidth = uiWidth;
}
/**
* 화면높이
* @return 화면높이
*/
public Integer getUiHeight() {
return uiHeight;
}
/**
* 화면높이
*@param uiHeight 화면높이
*/
public void setUiHeight(Integer uiHeight) {
	this.uiHeight = uiHeight;
}
/**
* 사용여부
* @return 사용여부
*/
public String isUseYn() {
return useYn;
}
/**
* 사용여부
*@param useYn 사용여부
*/
public void setUseYn(String useYn) {
	this.useYn = useYn;
}
/**
* 정렬순서
* @return 정렬순서
*/
public Integer getSortSeq() {
return sortSeq;
}
/**
* 정렬순서
*@param sortSeq 정렬순서
*/
public void setSortSeq(Integer sortSeq) {
	this.sortSeq = sortSeq;
}
/**
* 등록사용자번호
* @return 등록사용자번호
*/
public Integer getRegUserNo() {
return regUserNo;
}
/**
* 등록사용자번호
*@param regUserNo 등록사용자번호
*/
public void setRegUserNo(Integer regUserNo) {
	this.regUserNo = regUserNo;
}
/**
* 등록일시
* @return 등록일시
*/
public Long getRegDtm() {
return regDtm;
}
/**
* 등록일시
*@param regDtm 등록일시
*/
public void setRegDtm(Long regDtm) {
	this.regDtm = regDtm;
}
/**
* 수정사용자번호
* @return 수정사용자번호
*/
public Integer getChgUserNo() {
return chgUserNo;
}
/**
* 수정사용자번호
*@param chgUserNo 수정사용자번호
*/
public void setChgUserNo(Integer chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public Long getChgDtm() {
return chgDtm;
}
/**
* 수정일시
*@param chgDtm 수정일시
*/
public void setChgDtm(Long chgDtm) {
	this.chgDtm = chgDtm;
}
}
