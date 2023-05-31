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


@FxTable(name = "FX_UI_DIAG_LINE", comment = "다이아그램라인테이블")
@FxIndex(name = "FX_UI_DIAG_LINE__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO", "DIAG_NODE_NO"})
@FxIndex(name = "FX_UI_DIAG_LINK__FK1", type = INDEX_TYPE.FK, columns = {"DIAG_NO"}, fkTable = "FX_UI_DIAG_BAS", fkColumn = "DIAG_NO")
public class FX_UI_DIAG_LINE implements Serializable {

public FX_UI_DIAG_LINE() {
 }

@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호")
private Integer diagNo;


@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
private Integer diagNodeNo;


@FxColumn(name = "LINK_DIAG_NODE_NO1", size = 9, comment = "연결다이아그램노드번호1")
private Integer linkDiagNodeNo1;


@FxColumn(name = "LINK_DIAG_NODE_NO2", size = 9, comment = "연결다이아그램노드번호2")
private Integer linkDiagNodeNo2;


@FxColumn(name = "LINK_DIAG_NODE_JSON", size = 1000, nullable = true, comment = "연결다이아그램노드_JSON")
private String linkDiagNodeJson;


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
* 다이아그램노드번호
* @return 다이아그램노드번호
*/
public Integer getDiagNodeNo() {
return diagNodeNo;
}
/**
* 다이아그램노드번호
*@param diagNodeNo 다이아그램노드번호
*/
public void setDiagNodeNo(Integer diagNodeNo) {
	this.diagNodeNo = diagNodeNo;
}
/**
* 연결다이아그램노드번호1
* @return 연결다이아그램노드번호1
*/
public Integer getLinkDiagNodeNo1() {
return linkDiagNodeNo1;
}
/**
* 연결다이아그램노드번호1
*@param linkDiagNodeNo1 연결다이아그램노드번호1
*/
public void setLinkDiagNodeNo1(Integer linkDiagNodeNo1) {
	this.linkDiagNodeNo1 = linkDiagNodeNo1;
}
/**
* 연결다이아그램노드번호2
* @return 연결다이아그램노드번호2
*/
public Integer getLinkDiagNodeNo2() {
return linkDiagNodeNo2;
}
/**
* 연결다이아그램노드번호2
*@param linkDiagNodeNo2 연결다이아그램노드번호2
*/
public void setLinkDiagNodeNo2(Integer linkDiagNodeNo2) {
	this.linkDiagNodeNo2 = linkDiagNodeNo2;
}
/**
* 연결다이아그램노드_JSON
* @return 연결다이아그램노드_JSON
*/
public String getLinkDiagNodeJson() {
return linkDiagNodeJson;
}
/**
* 연결다이아그램노드_JSON
*@param linkDiagNodeJson 연결다이아그램노드_JSON
*/
public void setLinkDiagNodeJson(String linkDiagNodeJson) {
	this.linkDiagNodeJson = linkDiagNodeJson;
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
