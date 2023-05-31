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


@FxTable(name = "FX_UI_DIAG_NODE", comment = "다이아그램NODE테이블")
@FxIndex(name = "FX_UI_DIAG_NODE__PK", type = INDEX_TYPE.PK, columns = {"DIAG_NO", "DIAG_NODE_NO"})
@FxIndex(name = "FX_UI_DIAG_NODE__FK1", type = INDEX_TYPE.FK, columns = {"DIAG_NO"}, fkTable = "FX_UI_DIAG_BAS", fkColumn = "DIAG_NO")
public class FX_UI_DIAG_NODE implements Serializable {

public FX_UI_DIAG_NODE() {
 }

@FxColumn(name = "DIAG_NO", size = 9, comment = "다이아그램번호")
private Integer diagNo;


@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
private Integer diagNodeNo;


@FxColumn(name = "DIAG_NODE_KEY", size = 50, comment = "다아아그램노드키")
private String diagNodeKey;


@FxColumn(name = "DIAG_NODE_TYPE_CD", size = 30, comment = "다이아그램노드유형")
private String diagNodeTypeCd;


@FxColumn(name = "DIAG_NODE_TEXT", size = 100, nullable = true, comment = "다아아그램노드문구")
private String diagNodeText;


@FxColumn(name = "DIAG_NODE_X", size = 9, comment = "다이아그램노드X좌표")
private Integer diagNodeX;


@FxColumn(name = "DIAG_NODE_Y", size = 9, comment = "다이아그램노드Y좌표")
private Integer diagNodeY;


@FxColumn(name = "DIAG_NODE_WIDTH", size = 9, comment = "다이아그램노드넓이")
private Integer diagNodeWidth;


@FxColumn(name = "DIAG_NODE_HEIGHT", size = 9, comment = "다이아그램노드높이")
private Integer diagNodeHeight;


@FxColumn(name = "DIAG_NODE_ANGLE", size = 3, nullable = true, comment = "다이아그램노드각도", defValue = "0")
private Integer diagNodeAngle = 0;


@FxColumn(name = "DIAG_NODE_JSON", size = 1000, nullable = true, comment = "다이아그램노드_JSON")
private String diagNodeJson;


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
* 다아아그램노드키
* @return 다아아그램노드키
*/
public String getDiagNodeKey() {
return diagNodeKey;
}
/**
* 다아아그램노드키
*@param diagNodeKey 다아아그램노드키
*/
public void setDiagNodeKey(String diagNodeKey) {
	this.diagNodeKey = diagNodeKey;
}
/**
* 다이아그램노드유형
* @return 다이아그램노드유형
*/
public String getDiagNodeTypeCd() {
return diagNodeTypeCd;
}
/**
* 다이아그램노드유형
*@param diagNodeTypeCd 다이아그램노드유형
*/
public void setDiagNodeTypeCd(String diagNodeTypeCd) {
	this.diagNodeTypeCd = diagNodeTypeCd;
}
/**
* 다아아그램노드문구
* @return 다아아그램노드문구
*/
public String getDiagNodeText() {
return diagNodeText;
}
/**
* 다아아그램노드문구
*@param diagNodeText 다아아그램노드문구
*/
public void setDiagNodeText(String diagNodeText) {
	this.diagNodeText = diagNodeText;
}
/**
* 다이아그램노드X좌표
* @return 다이아그램노드X좌표
*/
public Integer getDiagNodeX() {
return diagNodeX;
}
/**
* 다이아그램노드X좌표
*@param diagNodeX 다이아그램노드X좌표
*/
public void setDiagNodeX(Integer diagNodeX) {
	this.diagNodeX = diagNodeX;
}
/**
* 다이아그램노드Y좌표
* @return 다이아그램노드Y좌표
*/
public Integer getDiagNodeY() {
return diagNodeY;
}
/**
* 다이아그램노드Y좌표
*@param diagNodeY 다이아그램노드Y좌표
*/
public void setDiagNodeY(Integer diagNodeY) {
	this.diagNodeY = diagNodeY;
}
/**
* 다이아그램노드넓이
* @return 다이아그램노드넓이
*/
public Integer getDiagNodeWidth() {
return diagNodeWidth;
}
/**
* 다이아그램노드넓이
*@param diagNodeWidth 다이아그램노드넓이
*/
public void setDiagNodeWidth(Integer diagNodeWidth) {
	this.diagNodeWidth = diagNodeWidth;
}
/**
* 다이아그램노드높이
* @return 다이아그램노드높이
*/
public Integer getDiagNodeHeight() {
return diagNodeHeight;
}
/**
* 다이아그램노드높이
*@param diagNodeHeight 다이아그램노드높이
*/
public void setDiagNodeHeight(Integer diagNodeHeight) {
	this.diagNodeHeight = diagNodeHeight;
}
/**
* 다이아그램노드각도
* @return 다이아그램노드각도
*/
public Integer getDiagNodeAngle() {
return diagNodeAngle;
}
/**
* 다이아그램노드각도
*@param diagNodeAngle 다이아그램노드각도
*/
public void setDiagNodeAngle(Integer diagNodeAngle) {
	this.diagNodeAngle = diagNodeAngle;
}
/**
* 다이아그램노드_JSON
* @return 다이아그램노드_JSON
*/
public String getDiagNodeJson() {
return diagNodeJson;
}
/**
* 다이아그램노드_JSON
*@param diagNodeJson 다이아그램노드_JSON
*/
public void setDiagNodeJson(String diagNodeJson) {
	this.diagNodeJson = diagNodeJson;
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
