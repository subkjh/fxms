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


@FxTable(name = "FX_UI_DASHB", comment = "UI대시보드테이블")
@FxIndex(name = "FX_UI_DASHB__PK", type = INDEX_TYPE.PK, columns = {"DASHB_NO"})
@FxIndex(name = "FX_UI_DASHB__FK1", type = INDEX_TYPE.FK, columns = {"DASHB_VIEW_OP_ID"}, fkTable = "FX_CO_OP", fkColumn = "OP_ID")
@FxIndex(name = "FX_UI_DASHB__FK2", type = INDEX_TYPE.FK, columns = {"DASHB_EDIT_OP_ID"}, fkTable = "FX_CO_OP", fkColumn = "OP_ID")
public class FX_UI_DASHB implements Serializable {

public FX_UI_DASHB() {
 }

public static final String FX_SEQ_DASHBNO  = "FX_SEQ_DASHBNO"; 
@FxColumn(name = "DASHB_NO", size = 9, comment = "대시보드번호", defValue = "-1", sequence = "FX_SEQ_DASHBNO")
private Integer dashbNo = -1;


@FxColumn(name = "DASHB_NAME", size = 100, comment = "대시보드명")
private String dashbName;


@FxColumn(name = "DASHB_DESCR", size = 200, nullable = true, comment = "대시보드설명")
private String dashbDescr;


@FxColumn(name = "DASHB_VIEW_OP_ID", size = 50, comment = "대시보드조회기능ID")
private String dashbViewOpId;


@FxColumn(name = "DASHB_EDIT_OP_ID", size = 50, comment = "대시보드편집기능ID")
private String dashbEditOpId;


@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "N")
private String useYn = "N";


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
* 대시보드명
* @return 대시보드명
*/
public String getDashbName() {
return dashbName;
}
/**
* 대시보드명
*@param dashbName 대시보드명
*/
public void setDashbName(String dashbName) {
	this.dashbName = dashbName;
}
/**
* 대시보드설명
* @return 대시보드설명
*/
public String getDashbDescr() {
return dashbDescr;
}
/**
* 대시보드설명
*@param dashbDescr 대시보드설명
*/
public void setDashbDescr(String dashbDescr) {
	this.dashbDescr = dashbDescr;
}
/**
* 대시보드조회기능ID
* @return 대시보드조회기능ID
*/
public String getDashbViewOpId() {
return dashbViewOpId;
}
/**
* 대시보드조회기능ID
*@param dashbViewOpId 대시보드조회기능ID
*/
public void setDashbViewOpId(String dashbViewOpId) {
	this.dashbViewOpId = dashbViewOpId;
}
/**
* 대시보드편집기능ID
* @return 대시보드편집기능ID
*/
public String getDashbEditOpId() {
return dashbEditOpId;
}
/**
* 대시보드편집기능ID
*@param dashbEditOpId 대시보드편집기능ID
*/
public void setDashbEditOpId(String dashbEditOpId) {
	this.dashbEditOpId = dashbEditOpId;
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
