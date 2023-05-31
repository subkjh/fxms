package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.02 18:01
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UR_USER_REL_UGRP", comment = "사용자연관그룹테이블")
@FxIndex(name = "FX_UR_USER_REL_UGRP__PK", type = INDEX_TYPE.PK, columns = {"USER_NO", "UGRP_NO"})
public class FX_UR_USER_REL_UGRP implements Serializable {

public FX_UR_USER_REL_UGRP() {
 }

@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
private int userNo;


@FxColumn(name = "UGRP_NO", size = 9, comment = "사용자그룹번호")
private int ugrpNo;


@FxColumn(name = "DFT_UGRP_YN", size = 1, comment = "기본사용자그룹여부")
private boolean dftUgrpYn;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


/**
* 사용자번호
* @return 사용자번호
*/
public int getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(int userNo) {
	this.userNo = userNo;
}
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
* 기본사용자그룹여부
* @return 기본사용자그룹여부
*/
public boolean isDftUgrpYn() {
return dftUgrpYn;
}
/**
* 기본사용자그룹여부
*@param dftUgrpYn 기본사용자그룹여부
*/
public void setDftUgrpYn(boolean dftUgrpYn) {
	this.dftUgrpYn = dftUgrpYn;
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
}
