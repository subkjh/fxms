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


@FxTable(name = "FX_UR_FAVO_GRP", comment = "관심그룹테이블")
@FxIndex(name = "FX_UR_FAVO_GRP__PK", type = INDEX_TYPE.PK, columns = {"FAVO_GRP_NO"})
public class FX_UR_FAVO_GRP implements Serializable {

public FX_UR_FAVO_GRP() {
 }

public static final String FX_SEQ_FAVO_GRP_NO  = "FX_SEQ_FAVO_GRP_NO"; 
@FxColumn(name = "FAVO_GRP_NO", size = 9, comment = "관심그룹번호", sequence = "FX_SEQ_FAVO_GRP_NO")
private int favoGrpNo;


@FxColumn(name = "OWN_UGRP_NO", size = 9, comment = "소유사용자그룹번호", defValue = "-1")
private int ownUgrpNo = -1;


@FxColumn(name = "OWN_USER_NO", size = 9, comment = "소유사용자번호", defValue = "-1")
private int ownUserNo = -1;


@FxColumn(name = "FAVO_GRP_NAME", size = 9, comment = "관심그룹명", defValue = "0")
private int favoGrpName = 0;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 관심그룹번호
* @return 관심그룹번호
*/
public int getFavoGrpNo() {
return favoGrpNo;
}
/**
* 관심그룹번호
*@param favoGrpNo 관심그룹번호
*/
public void setFavoGrpNo(int favoGrpNo) {
	this.favoGrpNo = favoGrpNo;
}
/**
* 소유사용자그룹번호
* @return 소유사용자그룹번호
*/
public int getOwnUgrpNo() {
return ownUgrpNo;
}
/**
* 소유사용자그룹번호
*@param ownUgrpNo 소유사용자그룹번호
*/
public void setOwnUgrpNo(int ownUgrpNo) {
	this.ownUgrpNo = ownUgrpNo;
}
/**
* 소유사용자번호
* @return 소유사용자번호
*/
public int getOwnUserNo() {
return ownUserNo;
}
/**
* 소유사용자번호
*@param ownUserNo 소유사용자번호
*/
public void setOwnUserNo(int ownUserNo) {
	this.ownUserNo = ownUserNo;
}
/**
* 관심그룹명
* @return 관심그룹명
*/
public int getFavoGrpName() {
return favoGrpName;
}
/**
* 관심그룹명
*@param favoGrpName 관심그룹명
*/
public void setFavoGrpName(int favoGrpName) {
	this.favoGrpName = favoGrpName;
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
* 수정사용자번호
* @return 수정사용자번호
*/
public int getChgUserNo() {
return chgUserNo;
}
/**
* 수정사용자번호
*@param chgUserNo 수정사용자번호
*/
public void setChgUserNo(int chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public long getChgDtm() {
return chgDtm;
}
/**
* 수정일시
*@param chgDtm 수정일시
*/
public void setChgDtm(long chgDtm) {
	this.chgDtm = chgDtm;
}
}
