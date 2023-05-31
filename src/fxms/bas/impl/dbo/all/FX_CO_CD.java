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


@FxTable(name = "FX_CO_CD", comment = "코드테이블")
@FxIndex(name = "FX_CO_CD__PK", type = INDEX_TYPE.PK, columns = {"CD_CLASS", "CD_CODE"})
public class FX_CO_CD implements Serializable {

public FX_CO_CD() {
 }

@FxColumn(name = "CD_CLASS", size = 30, comment = "코드분류")
private String cdClass;


@FxColumn(name = "CD_CODE", size = 30, comment = "코드")
private String cdCode;


@FxColumn(name = "CD_NAME", size = 50, nullable = true, comment = "코드명")
private String cdName;


@FxColumn(name = "CD_DESC", size = 200, nullable = true, comment = "설명")
private String cdDesc;


@FxColumn(name = "VAL1", size = 1000, nullable = true, comment = "값1")
private String val1;


@FxColumn(name = "VAL2", size = 1000, nullable = true, comment = "값2")
private String val2;


@FxColumn(name = "VAL3", size = 1000, nullable = true, comment = "값3")
private String val3;


@FxColumn(name = "VAL4", size = 1000, nullable = true, comment = "값4")
private String val4;


@FxColumn(name = "VAL5", size = 1000, nullable = true, comment = "값5")
private String val5;


@FxColumn(name = "VAL6", size = 1000, nullable = true, comment = "값6")
private String val6;


@FxColumn(name = "SORT_SEQ", size = 5, nullable = true, comment = "정렬순서")
private int sortSeq;


@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부")
private boolean useYn;


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
* 코드
* @return 코드
*/
public String getCdCode() {
return cdCode;
}
/**
* 코드
*@param cdCode 코드
*/
public void setCdCode(String cdCode) {
	this.cdCode = cdCode;
}
/**
* 코드명
* @return 코드명
*/
public String getCdName() {
return cdName;
}
/**
* 코드명
*@param cdName 코드명
*/
public void setCdName(String cdName) {
	this.cdName = cdName;
}
/**
* 설명
* @return 설명
*/
public String getCdDesc() {
return cdDesc;
}
/**
* 설명
*@param cdDesc 설명
*/
public void setCdDesc(String cdDesc) {
	this.cdDesc = cdDesc;
}
/**
* 값1
* @return 값1
*/
public String getVal1() {
return val1;
}
/**
* 값1
*@param val1 값1
*/
public void setVal1(String val1) {
	this.val1 = val1;
}
/**
* 값2
* @return 값2
*/
public String getVal2() {
return val2;
}
/**
* 값2
*@param val2 값2
*/
public void setVal2(String val2) {
	this.val2 = val2;
}
/**
* 값3
* @return 값3
*/
public String getVal3() {
return val3;
}
/**
* 값3
*@param val3 값3
*/
public void setVal3(String val3) {
	this.val3 = val3;
}
/**
* 값4
* @return 값4
*/
public String getVal4() {
return val4;
}
/**
* 값4
*@param val4 값4
*/
public void setVal4(String val4) {
	this.val4 = val4;
}
/**
* 값5
* @return 값5
*/
public String getVal5() {
return val5;
}
/**
* 값5
*@param val5 값5
*/
public void setVal5(String val5) {
	this.val5 = val5;
}
/**
* 값6
* @return 값6
*/
public String getVal6() {
return val6;
}
/**
* 값6
*@param val6 값6
*/
public void setVal6(String val6) {
	this.val6 = val6;
}
/**
* 정렬순서
* @return 정렬순서
*/
public int getSortSeq() {
return sortSeq;
}
/**
* 정렬순서
*@param sortSeq 정렬순서
*/
public void setSortSeq(int sortSeq) {
	this.sortSeq = sortSeq;
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
