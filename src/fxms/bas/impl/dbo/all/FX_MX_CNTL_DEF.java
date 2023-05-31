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


@FxTable(name = "FX_MX_CNTL_DEF", comment = "MO제어정의테이블")
@FxIndex(name = "FX_MX_CNTL_DEF__PK", type = INDEX_TYPE.PK, columns = {"CNTL_ID"})
public class FX_MX_CNTL_DEF implements Serializable {

public FX_MX_CNTL_DEF() {
 }

@FxColumn(name = "CNTL_ID", size = 20, comment = "제어ID")
private String cntlId;


@FxColumn(name = "CNTL_NAME", size = 30, comment = "제어명")
private String cntlName;


@FxColumn(name = "MO_CLASS", size = 20, comment = "연관MO클래스 ")
private String moClass;


@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
private boolean useYn = true;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 제어ID
* @return 제어ID
*/
public String getCntlId() {
return cntlId;
}
/**
* 제어ID
*@param cntlId 제어ID
*/
public void setCntlId(String cntlId) {
	this.cntlId = cntlId;
}
/**
* 제어명
* @return 제어명
*/
public String getCntlName() {
return cntlName;
}
/**
* 제어명
*@param cntlName 제어명
*/
public void setCntlName(String cntlName) {
	this.cntlName = cntlName;
}
/**
* 연관MO클래스 
* @return 연관MO클래스 
*/
public String getMoClass() {
return moClass;
}
/**
* 연관MO클래스 
*@param moClass 연관MO클래스 
*/
public void setMoClass(String moClass) {
	this.moClass = moClass;
}
/**
* 사용여부
* @return 사용여부
*/
public boolean isUseYn() {
return useYn;
}
/**
* 사용여부
*@param useYn 사용여부
*/
public void setUseYn(boolean useYn) {
	this.useYn = useYn;
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
