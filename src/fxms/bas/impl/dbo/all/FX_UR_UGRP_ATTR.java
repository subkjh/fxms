package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.03.14 13:09
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UR_UGRP_ATTR", comment = "사용자그룹속성테이블")
@FxIndex(name = "FX_UR_UGRP_ATTR__PK", type = INDEX_TYPE.PK, columns = {"UGRP_NO", "ATTR_NAME"})
@FxIndex(name = "FX_UR_UGRP_ATTR__FK1", type = INDEX_TYPE.FK, columns = {"UGRP_NO"}, fkTable = "FX_UR_UGRP", fkColumn = "UGRP_NO")
public class FX_UR_UGRP_ATTR implements Serializable {

public FX_UR_UGRP_ATTR() {
 }

@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호")
private Integer ugrpNo;


@FxColumn(name = "ATTR_NAME", size = 100, comment = "속성명")
private String attrName;


@FxColumn(name = "ATTR_VAL", size = 240, comment = "속성값")
private String attrVal;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 사용자그룹번호
* @return 사용자그룹번호
*/
public Integer getUgrpNo() {
return ugrpNo;
}
/**
* 사용자그룹번호
*@param ugrpNo 사용자그룹번호
*/
public void setUgrpNo(Integer ugrpNo) {
	this.ugrpNo = ugrpNo;
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
