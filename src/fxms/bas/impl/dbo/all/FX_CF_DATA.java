package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.04.12 14:21
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CF_DATA", comment = "데이터테이블")
@FxIndex(name = "FX_CF_DATA__PK", type = INDEX_TYPE.PK, columns = {"DATA_CLASS", "DATA_TYPE", "DATA_VALUE"})
public class FX_CF_DATA implements Serializable {

public FX_CF_DATA() {
 }

@FxColumn(name = "DATA_CLASS", size = 100, comment = "데이터분류")
private String dataClass;


@FxColumn(name = "DATA_TYPE", size = 100, comment = "데이터유형")
private String dataType;


@FxColumn(name = "DATA_VALUE", size = 400, comment = "데이터값")
private String dataValue;


@FxColumn(name = "DATA_NAME1", size = 100, comment = "데이터명1")
private String dataName1;


@FxColumn(name = "DATA_NAME2", size = 100, nullable = true, comment = "데이터명2")
private String dataName2;


@FxColumn(name = "DATA_NAME3", size = 100, nullable = true, comment = "데이터명3")
private String dataName3;


@FxColumn(name = "DATA_NAME4", size = 100, nullable = true, comment = "데이터명4")
private String dataName4;


@FxColumn(name = "DATA_NAME5", size = 100, nullable = true, comment = "데이터명5")
private String dataName5;


@FxColumn(name = "DATA_NAME6", size = 100, nullable = true, comment = "데이터명6")
private String dataName6;


@FxColumn(name = "SORT_SEQ", size = 5, nullable = true, comment = "정렬순서")
private Integer sortSeq;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 데이터분류
* @return 데이터분류
*/
public String getDataClass() {
return dataClass;
}
/**
* 데이터분류
*@param dataClass 데이터분류
*/
public void setDataClass(String dataClass) {
	this.dataClass = dataClass;
}
/**
* 데이터유형
* @return 데이터유형
*/
public String getDataType() {
return dataType;
}
/**
* 데이터유형
*@param dataType 데이터유형
*/
public void setDataType(String dataType) {
	this.dataType = dataType;
}
/**
* 데이터값
* @return 데이터값
*/
public String getDataValue() {
return dataValue;
}
/**
* 데이터값
*@param dataValue 데이터값
*/
public void setDataValue(String dataValue) {
	this.dataValue = dataValue;
}
/**
* 데이터명1
* @return 데이터명1
*/
public String getDataName1() {
return dataName1;
}
/**
* 데이터명1
*@param dataName1 데이터명1
*/
public void setDataName1(String dataName1) {
	this.dataName1 = dataName1;
}
/**
* 데이터명2
* @return 데이터명2
*/
public String getDataName2() {
return dataName2;
}
/**
* 데이터명2
*@param dataName2 데이터명2
*/
public void setDataName2(String dataName2) {
	this.dataName2 = dataName2;
}
/**
* 데이터명3
* @return 데이터명3
*/
public String getDataName3() {
return dataName3;
}
/**
* 데이터명3
*@param dataName3 데이터명3
*/
public void setDataName3(String dataName3) {
	this.dataName3 = dataName3;
}
/**
* 데이터명4
* @return 데이터명4
*/
public String getDataName4() {
return dataName4;
}
/**
* 데이터명4
*@param dataName4 데이터명4
*/
public void setDataName4(String dataName4) {
	this.dataName4 = dataName4;
}
/**
* 데이터명5
* @return 데이터명5
*/
public String getDataName5() {
return dataName5;
}
/**
* 데이터명5
*@param dataName5 데이터명5
*/
public void setDataName5(String dataName5) {
	this.dataName5 = dataName5;
}
/**
* 데이터명6
* @return 데이터명6
*/
public String getDataName6() {
return dataName6;
}
/**
* 데이터명6
*@param dataName6 데이터명6
*/
public void setDataName6(String dataName6) {
	this.dataName6 = dataName6;
}
/**
* 정렬순서
* @return 정렬순서
*/
public Integer getSortSeq() {
return sortSeq;
}
/**
* 정렬순서
*@param sortSeq 정렬순서
*/
public void setSortSeq(Integer sortSeq) {
	this.sortSeq = sortSeq;
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
