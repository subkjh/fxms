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


@FxTable(name = "FX_AM_GRP", comment = "관리그룹테이블")
@FxIndex(name = "FX_AM_GRP__PK", type = INDEX_TYPE.PK, columns = {"AM_GRP_NO"})
@FxIndex(name = "FX_AM_GRP__UK", type = INDEX_TYPE.UK, columns = {"AM_GRP_NAME"})
public class FX_AM_GRP implements Serializable {

public FX_AM_GRP() {
 }

public static final String FX_SEQ_AMGRPNO  = "FX_SEQ_AMGRPNO"; 
@FxColumn(name = "AM_GRP_NO", size = 9, comment = "관리그룹번호", sequence = "FX_SEQ_AMGRPNO")
private int amGrpNo;


@FxColumn(name = "AM_GRP_NAME", size = 50, comment = "관리그룹명")
private String amGrpName;


@FxColumn(name = "AM_GRP_DESC", size = 200, nullable = true, comment = "관리그룹설명")
private String amGrpDesc;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


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
* 관리그룹명
* @return 관리그룹명
*/
public String getAmGrpName() {
return amGrpName;
}
/**
* 관리그룹명
*@param amGrpName 관리그룹명
*/
public void setAmGrpName(String amGrpName) {
	this.amGrpName = amGrpName;
}
/**
* 관리그룹설명
* @return 관리그룹설명
*/
public String getAmGrpDesc() {
return amGrpDesc;
}
/**
* 관리그룹설명
*@param amGrpDesc 관리그룹설명
*/
public void setAmGrpDesc(String amGrpDesc) {
	this.amGrpDesc = amGrpDesc;
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
