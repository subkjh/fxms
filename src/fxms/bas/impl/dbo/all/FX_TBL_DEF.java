package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.12.22 09:34
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_TBL_DEF", comment = "테이블정의테이블")
@FxIndex(name = "FX_TBL_DEF__PK", type = INDEX_TYPE.PK, columns = {"TBL_NAME"})
public class FX_TBL_DEF implements Serializable {

public FX_TBL_DEF() {
 }

@FxColumn(name = "TBL_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
private String tblName;


@FxColumn(name = "TBL_CMNT", size = 200, comment = "테이블코멘트")
private String tblCmnt;


@FxColumn(name = "RESEV_YN", size = 1, comment = "예약여부", defValue = "N")
private String resevYn = "N";


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 테이블명
* @return 테이블명
*/
public String getTblName() {
return tblName;
}
/**
* 테이블명
*@param tblName 테이블명
*/
public void setTblName(String tblName) {
	this.tblName = tblName;
}
/**
* 테이블코멘트
* @return 테이블코멘트
*/
public String getTblCmnt() {
return tblCmnt;
}
/**
* 테이블코멘트
*@param tblCmnt 테이블코멘트
*/
public void setTblCmnt(String tblCmnt) {
	this.tblCmnt = tblCmnt;
}
/**
* 예약여부
* @return 예약여부
*/
public String isResevYn() {
return resevYn;
}
/**
* 예약여부
*@param resevYn 예약여부
*/
public void setResevYn(String resevYn) {
	this.resevYn = resevYn;
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
