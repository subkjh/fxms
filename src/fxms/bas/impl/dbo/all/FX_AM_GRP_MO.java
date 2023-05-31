package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
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


@FxTable(name = "FX_AM_GRP_MO", comment = "관리그룹대상MO내역")
@FxIndex(name = "FX_AM_GRP_MO__PK", type = INDEX_TYPE.PK, columns = {"AM_GRP_NO", "MO_NO"})
public class FX_AM_GRP_MO implements Serializable {

public FX_AM_GRP_MO() {
 }

@FxColumn(name = "AM_GRP_NO", size = 9, comment = "관리그룹번호")
private int amGrpNo;


@FxColumn(name = "MO_NO", size = 19, comment = "MO번호")
private long moNo;


@FxColumn(name = "MO_DESC", size = 200, nullable = true, comment = "MO설명")
private String moDesc;


@FxColumn(name = "ENABLE_YN", size = 1, nullable = true, comment = "활성화여부", defValue = "Y")
private boolean enableYn = true;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


/**
* 관리그룹번호
* @return 관리그룹번호
*/
public int getAmGrpNo() {
return amGrpNo;
}
/**
* 관리그룹번호
*@param amGrpNo 관리그룹번호
*/
public void setAmGrpNo(int amGrpNo) {
	this.amGrpNo = amGrpNo;
}
/**
* MO번호
* @return MO번호
*/
public long getMoNo() {
return moNo;
}
/**
* MO번호
*@param moNo MO번호
*/
public void setMoNo(long moNo) {
	this.moNo = moNo;
}
/**
* MO설명
* @return MO설명
*/
public String getMoDesc() {
return moDesc;
}
/**
* MO설명
*@param moDesc MO설명
*/
public void setMoDesc(String moDesc) {
	this.moDesc = moDesc;
}
/**
* 활성화여부
* @return 활성화여부
*/
public boolean isEnableYn() {
return enableYn;
}
/**
* 활성화여부
*@param enableYn 활성화여부
*/
public void setEnableYn(boolean enableYn) {
	this.enableYn = enableYn;
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
