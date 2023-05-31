package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.01.30 15:31
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UI_DIAG_BAS", comment = "다이아그램기본테이블")
@FxIndex(name = "FX_UI_DIAG_BAS__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO"})
public class FX_UI_DIAG_BAS implements Serializable {

public FX_UI_DIAG_BAS() {
 }

public static final String FX_SEQ_DIAGNO  = "FX_SEQ_DIAGNO"; 
@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호", sequence = "FX_SEQ_DIAGNO")
private Integer diagNo = 0;


@FxColumn(name = "DIAG_TITLE", size = 50, comment = "다이아그램제목")
private String diagTitle;


@FxColumn(name = "OP_ID", size = 50, comment = "기능ID")
private String opId;


@FxColumn(name = "OWNER_USER_NO", size = 9, comment = "소유사용자번호", defValue = "0")
private Integer ownerUserNo = 0;


@FxColumn(name = "SHR_UGRP_NO", size = 9, comment = "공유운영자그룹번호", defValue = "0")
private Integer shrUgrpNo = 0;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 다이아그램번호
* @return 다이아그램번호
*/
public Integer getDiagNo() {
return diagNo;
}
/**
* 다이아그램번호
*@param diagNo 다이아그램번호
*/
public void setDiagNo(Integer diagNo) {
	this.diagNo = diagNo;
}
/**
* 다이아그램제목
* @return 다이아그램제목
*/
public String getDiagTitle() {
return diagTitle;
}
/**
* 다이아그램제목
*@param diagTitle 다이아그램제목
*/
public void setDiagTitle(String diagTitle) {
	this.diagTitle = diagTitle;
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
* 소유사용자번호
* @return 소유사용자번호
*/
public Integer getOwnerUserNo() {
return ownerUserNo;
}
/**
* 소유사용자번호
*@param ownerUserNo 소유사용자번호
*/
public void setOwnerUserNo(Integer ownerUserNo) {
	this.ownerUserNo = ownerUserNo;
}
/**
* 공유운영자그룹번호
* @return 공유운영자그룹번호
*/
public Integer getShrUgrpNo() {
return shrUgrpNo;
}
/**
* 공유운영자그룹번호
*@param shrUgrpNo 공유운영자그룹번호
*/
public void setShrUgrpNo(Integer shrUgrpNo) {
	this.shrUgrpNo = shrUgrpNo;
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
