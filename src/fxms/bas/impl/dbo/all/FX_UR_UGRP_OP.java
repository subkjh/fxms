package fxms.bas.impl.dbo.all;


import java.io.Serializable;

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


@FxTable(name = "FX_UR_UGRP_OP", comment = "사용자그룹기능테이블")
@FxIndex(name = "FY_UR_UGRP_OP__PK", type = INDEX_TYPE.PK, columns = {"UGRP_NO", "OP_ID"})
public class FX_UR_UGRP_OP implements Serializable {

public FX_UR_UGRP_OP() {
 }

@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호")
private int ugrpNo;


@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
private String opId;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "REG_MEMO", size = 100, nullable = true, comment = "등록사유")
private String regMemo;


/**
* 사용자그룹번호
* @return 사용자그룹번호
*/
public int getUgrpNo() {
return ugrpNo;
}
/**
* 사용자그룹번호
*@param ugrpNo 사용자그룹번호
*/
public void setUgrpNo(int ugrpNo) {
	this.ugrpNo = ugrpNo;
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
* 등록사유
* @return 등록사유
*/
public String getRegMemo() {
return regMemo;
}
/**
* 등록사유
*@param regMemo 등록사유
*/
public void setRegMemo(String regMemo) {
	this.regMemo = regMemo;
}
}
