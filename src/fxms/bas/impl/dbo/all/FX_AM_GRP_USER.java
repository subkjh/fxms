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


@FxTable(name = "FX_AM_GRP_USER", comment = "관리그룹사용자내역테이블")
@FxIndex(name = "FX_AM_GRP_USER__PK", type = INDEX_TYPE.PK, columns = {"AM_GRP_NO", "USER_NO"})
public class FX_AM_GRP_USER implements Serializable {

public FX_AM_GRP_USER() {
 }

@FxColumn(name = "AM_GRP_NO", size = 9, comment = "관리그룹번호")
private int amGrpNo;


@FxColumn(name = "AM_USER_TYPE", size = 10, comment = "사용자구분")
private String amUserType;


@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
private int userNo;


@FxColumn(name = "AM_NAME", size = 50, nullable = true, comment = "사용자명")
private String amName;


@FxColumn(name = "AM_MAIL", size = 100, nullable = true, comment = "사용자이메일")
private String amMail;


@FxColumn(name = "AM_TELNO", size = 50, nullable = true, comment = "사용자전화번호")
private String amTelno;


@FxColumn(name = "AM_DESC", size = 200, nullable = true, comment = "사용자설명")
private String amDesc;


@FxColumn(name = "EN_YN", size = 1, nullable = true, comment = "활성화여부", defValue = "Y")
private boolean enYn = true;


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
* 사용자구분
* @return 사용자구분
*/
public String getAmUserType() {
return amUserType;
}
/**
* 사용자구분
*@param amUserType 사용자구분
*/
public void setAmUserType(String amUserType) {
	this.amUserType = amUserType;
}
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
* 사용자명
* @return 사용자명
*/
public String getAmName() {
return amName;
}
/**
* 사용자명
*@param amName 사용자명
*/
public void setAmName(String amName) {
	this.amName = amName;
}
/**
* 사용자이메일
* @return 사용자이메일
*/
public String getAmMail() {
return amMail;
}
/**
* 사용자이메일
*@param amMail 사용자이메일
*/
public void setAmMail(String amMail) {
	this.amMail = amMail;
}
/**
* 사용자전화번호
* @return 사용자전화번호
*/
public String getAmTelno() {
return amTelno;
}
/**
* 사용자전화번호
*@param amTelno 사용자전화번호
*/
public void setAmTelno(String amTelno) {
	this.amTelno = amTelno;
}
/**
* 사용자설명
* @return 사용자설명
*/
public String getAmDesc() {
return amDesc;
}
/**
* 사용자설명
*@param amDesc 사용자설명
*/
public void setAmDesc(String amDesc) {
	this.amDesc = amDesc;
}
/**
* 활성화여부
* @return 활성화여부
*/
public boolean isEnYn() {
return enYn;
}
/**
* 활성화여부
*@param enYn 활성화여부
*/
public void setEnYn(boolean enYn) {
	this.enYn = enYn;
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
