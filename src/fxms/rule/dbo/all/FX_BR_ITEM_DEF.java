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


@FxTable(name = "FX_BR_ITEM_DEF", comment = "룰정의액션테이블")
@FxIndex(name = "FX_BR_ITEM_DEF__PK", type = INDEX_TYPE.PK, columns = {"BR_ITEM_NAME"})
public class FX_BR_ITEM_DEF implements Serializable {

public FX_BR_ITEM_DEF() {
 }

@FxColumn(name = "BR_ITEM_NAME", size = 200, comment = "비즈니스룰항목명")
private String brItemName;


@FxColumn(name = "BR_ITEM_DISP_NAME", size = 100, comment = "비즈니스룰항목표시명")
private String brItemDispName;


@FxColumn(name = "BR_ITEM_DESCR", size = 240, comment = "비즈니스룰위설명")
private String brItemDescr;


@FxColumn(name = "BR_ITEM_TYPE", size = 20, comment = "비즈니스룰항목구분", defValue = "ACTION")
private String brItemType = "ACTION";


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
* 비즈니스룰항목표시명
* @return 비즈니스룰항목표시명
*/
public String getBrItemDispName() {
return brItemDispName;
}
/**
* 비즈니스룰항목표시명
*@param brItemDispName 비즈니스룰항목표시명
*/
public void setBrItemDispName(String brItemDispName) {
	this.brItemDispName = brItemDispName;
}
/**
* 비즈니스룰위설명
* @return 비즈니스룰위설명
*/
public String getBrItemDescr() {
return brItemDescr;
}
/**
* 비즈니스룰위설명
*@param brItemDescr 비즈니스룰위설명
*/
public void setBrItemDescr(String brItemDescr) {
	this.brItemDescr = brItemDescr;
}
/**
* 비즈니스룰항목구분
* @return 비즈니스룰항목구분
*/
public String getBrItemType() {
return brItemType;
}
/**
* 비즈니스룰항목구분
*@param brItemType 비즈니스룰항목구분
*/
public void setBrItemType(String brItemType) {
	this.brItemType = brItemType;
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
