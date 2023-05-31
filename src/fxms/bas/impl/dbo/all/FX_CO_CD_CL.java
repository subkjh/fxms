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


@FxTable(name = "FX_CO_CD_CL", comment = "코드분류테이블")
@FxIndex(name = "FX_CO_CD__PK", type = INDEX_TYPE.PK, columns = {"CD_CLASS"})
public class FX_CO_CD_CL implements Serializable {

public FX_CO_CD_CL() {
 }

@FxColumn(name = "CD_CLASS", size = 30, comment = "코드분류")
private String cdClass;


@FxColumn(name = "CD_CLASS_NAME", size = 50, comment = "코드분류명")
private String cdClassName;


@FxColumn(name = "CD_CLASS_DESC", size = 200, nullable = true, comment = "코드분류설명")
private String cdClassDesc;


@FxColumn(name = "VAL1_USG", size = 200, nullable = true, comment = "값1쓰임새")
private String val1Usg;


@FxColumn(name = "VAL2_USG", size = 200, nullable = true, comment = "값2쓰임새")
private String val2Usg;


@FxColumn(name = "VAL3_USG", size = 200, nullable = true, comment = "값3쓰임새")
private String val3Usg;


@FxColumn(name = "VAL4_USG", size = 200, nullable = true, comment = "값4쓰임새")
private String val4Usg;


@FxColumn(name = "VAL5_USG", size = 200, nullable = true, comment = "값5쓰임새")
private String val5Usg;


@FxColumn(name = "VAL6_USG", size = 200, nullable = true, comment = "값6쓰임새")
private String val6Usg;


@FxColumn(name = "EDTBL_YN", size = 1, comment = "편집가능여부")
private boolean edtblYn;


@FxColumn(name = "CHK_QRY", size = 1000, nullable = true, comment = "사용중여부 확인 쿼리")
private String chkQry;


@FxColumn(name = "FILL_QRY", size = 1000, nullable = true, comment = "채울쿼리")
private String fillQry;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 코드분류
* @return 코드분류
*/
public String getCdClass() {
return cdClass;
}
/**
* 코드분류
*@param cdClass 코드분류
*/
public void setCdClass(String cdClass) {
	this.cdClass = cdClass;
}
/**
* 코드분류명
* @return 코드분류명
*/
public String getCdClassName() {
return cdClassName;
}
/**
* 코드분류명
*@param cdClassName 코드분류명
*/
public void setCdClassName(String cdClassName) {
	this.cdClassName = cdClassName;
}
/**
* 코드분류설명
* @return 코드분류설명
*/
public String getCdClassDesc() {
return cdClassDesc;
}
/**
* 코드분류설명
*@param cdClassDesc 코드분류설명
*/
public void setCdClassDesc(String cdClassDesc) {
	this.cdClassDesc = cdClassDesc;
}
/**
* 값1쓰임새
* @return 값1쓰임새
*/
public String getVal1Usg() {
return val1Usg;
}
/**
* 값1쓰임새
*@param val1Usg 값1쓰임새
*/
public void setVal1Usg(String val1Usg) {
	this.val1Usg = val1Usg;
}
/**
* 값2쓰임새
* @return 값2쓰임새
*/
public String getVal2Usg() {
return val2Usg;
}
/**
* 값2쓰임새
*@param val2Usg 값2쓰임새
*/
public void setVal2Usg(String val2Usg) {
	this.val2Usg = val2Usg;
}
/**
* 값3쓰임새
* @return 값3쓰임새
*/
public String getVal3Usg() {
return val3Usg;
}
/**
* 값3쓰임새
*@param val3Usg 값3쓰임새
*/
public void setVal3Usg(String val3Usg) {
	this.val3Usg = val3Usg;
}
/**
* 값4쓰임새
* @return 값4쓰임새
*/
public String getVal4Usg() {
return val4Usg;
}
/**
* 값4쓰임새
*@param val4Usg 값4쓰임새
*/
public void setVal4Usg(String val4Usg) {
	this.val4Usg = val4Usg;
}
/**
* 값5쓰임새
* @return 값5쓰임새
*/
public String getVal5Usg() {
return val5Usg;
}
/**
* 값5쓰임새
*@param val5Usg 값5쓰임새
*/
public void setVal5Usg(String val5Usg) {
	this.val5Usg = val5Usg;
}
/**
* 값6쓰임새
* @return 값6쓰임새
*/
public String getVal6Usg() {
return val6Usg;
}
/**
* 값6쓰임새
*@param val6Usg 값6쓰임새
*/
public void setVal6Usg(String val6Usg) {
	this.val6Usg = val6Usg;
}
/**
* 편집가능여부
* @return 편집가능여부
*/
public boolean isEdtblYn() {
return edtblYn;
}
/**
* 편집가능여부
*@param edtblYn 편집가능여부
*/
public void setEdtblYn(boolean edtblYn) {
	this.edtblYn = edtblYn;
}
/**
* 사용중여부 확인 쿼리
* @return 사용중여부 확인 쿼리
*/
public String getChkQry() {
return chkQry;
}
/**
* 사용중여부 확인 쿼리
*@param chkQry 사용중여부 확인 쿼리
*/
public void setChkQry(String chkQry) {
	this.chkQry = chkQry;
}
/**
* 채울쿼리
* @return 채울쿼리
*/
public String getFillQry() {
return fillQry;
}
/**
* 채울쿼리
*@param fillQry 채울쿼리
*/
public void setFillQry(String fillQry) {
	this.fillQry = fillQry;
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
