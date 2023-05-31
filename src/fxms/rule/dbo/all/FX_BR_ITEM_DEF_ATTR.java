package fxms.rule.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.01.26 16:58
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_BR_ITEM_DEF_ATTR", comment = "룰정의액션테이블")
@FxIndex(name = "FX_BR_ITEM_DEF_ATTR__PK", type = INDEX_TYPE.PK, columns = {"BR_ITEM_NAME", "ATTR_NAME"})
@FxIndex(name = "FX_BR_ITEM_DEF_ATTR__FK1", type = INDEX_TYPE.FK, columns = {"BR_ITEM_NAME"}, fkTable = "FX_BR_ITEM_DEF", fkColumn = "BR_ITEM_NAME")
public class FX_BR_ITEM_DEF_ATTR implements Serializable {

public FX_BR_ITEM_DEF_ATTR() {
 }

@FxColumn(name = "BR_ITEM_NAME", size = 200, comment = "비즈니스룰항목명")
private String brItemName;


@FxColumn(name = "ATTR_NAME", size = 100, comment = "속성명")
private String attrName;


@FxColumn(name = "ATTR_FIELD_NAME", size = 100, comment = "속성필드명")
private String attrFieldName;


@FxColumn(name = "ATTR_TYPE", size = 20, comment = "속성종류", defValue = "String")
private String attrType = "String";


@FxColumn(name = "ATTR_DESCR", size = 400, nullable = true, comment = "속성설명")
private String attrDescr;


@FxColumn(name = "MANDT_YN", size = 1, comment = "필수여부", defValue = "Y")
private String mandtYn = "Y";


@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
private String useYn = "Y";


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 비즈니스룰항목명
* @return 비즈니스룰항목명
*/
public String getBrItemName() {
return brItemName;
}
/**
* 비즈니스룰항목명
*@param brItemName 비즈니스룰항목명
*/
public void setBrItemName(String brItemName) {
	this.brItemName = brItemName;
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
* 속성필드명
* @return 속성필드명
*/
public String getAttrFieldName() {
return attrFieldName;
}
/**
* 속성필드명
*@param attrFieldName 속성필드명
*/
public void setAttrFieldName(String attrFieldName) {
	this.attrFieldName = attrFieldName;
}
/**
* 속성종류
* @return 속성종류
*/
public String getAttrType() {
return attrType;
}
/**
* 속성종류
*@param attrType 속성종류
*/
public void setAttrType(String attrType) {
	this.attrType = attrType;
}
/**
* 속성설명
* @return 속성설명
*/
public String getAttrDescr() {
return attrDescr;
}
/**
* 속성설명
*@param attrDescr 속성설명
*/
public void setAttrDescr(String attrDescr) {
	this.attrDescr = attrDescr;
}
/**
* 필수여부
* @return 필수여부
*/
public String isMandtYn() {
return mandtYn;
}
/**
* 필수여부
*@param mandtYn 필수여부
*/
public void setMandtYn(String mandtYn) {
	this.mandtYn = mandtYn;
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
