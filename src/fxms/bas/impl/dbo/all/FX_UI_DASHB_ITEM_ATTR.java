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


@FxTable(name = "FX_UI_DASHB_ITEM_ATTR", comment = "UI대시보드항목속성테이블")
@FxIndex(name = "FX_UI_DASHB_ITEM_ATTR__PK", type = INDEX_TYPE.PK, columns = {"DASHB_NO", "UI_NO", "ATTR_NAME"})
@FxIndex(name = "FX_UI_DASHB_ITEM_ATTR__FK1", type = INDEX_TYPE.FK, columns = {"DASHB_NO"}, fkTable = "FX_UI_DASHB", fkColumn = "DASHB_NO")
public class FX_UI_DASHB_ITEM_ATTR implements Serializable {

public FX_UI_DASHB_ITEM_ATTR() {
 }

@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호", defValue = "0")
private Integer dashbNo = 0;


@FxColumn(name = "UI_NO", size = 5, comment = "화면번호")
private Integer uiNo;


@FxColumn(name = "ATTR_NAME", size = 100, comment = "속성명")
private String attrName;


@FxColumn(name = "ATTR_VAL", size = 240, comment = "속성값")
private String attrVal;


@FxColumn(name = "ATTR_TYPE", size = 50, nullable = true, comment = "속성유형")
private String attrType;


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
* 속성명
* @return 속성명
*/
public String getAttrName() {
return attrName;
}
/**
* 속성명
*@param attrName 속성명
*/
public void setAttrName(String attrName) {
	this.attrName = attrName;
}
/**
* 속성값
* @return 속성값
*/
public String getAttrVal() {
return attrVal;
}
/**
* 속성값
*@param attrVal 속성값
*/
public void setAttrVal(String attrVal) {
	this.attrVal = attrVal;
}
/**
* 속성유형
* @return 속성유형
*/
public String getAttrType() {
return attrType;
}
/**
* 속성유형
*@param attrType 속성유형
*/
public void setAttrType(String attrType) {
	this.attrType = attrType;
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
