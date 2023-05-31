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


@FxTable(name = "FX_CO_ABBR", comment = "FX약어테이블")
@FxIndex(name = "FX_CO_ABBR__PK", type = INDEX_TYPE.PK, columns = {"ABBR_NAME", "ABBR_COL"})
public class FX_CO_ABBR implements Serializable {

public FX_CO_ABBR() {
 }

@FxColumn(name = "ABBR_NAME", size = 50, comment = "약어명")
private String abbrName;


@FxColumn(name = "ABBR_COL", size = 10, comment = "약어컬럼")
private String abbrCol;


@FxColumn(name = "ABBR_ALL_NAME", size = 100, comment = "약어전체명")
private String abbrAllName;


@FxColumn(name = "ABBR_DESC", size = 250, nullable = true, comment = "약어설명")
private String abbrDesc;


@FxColumn(name = "ABBR_DATA_TYPE", size = 20, nullable = true, comment = "약어데이터유형")
private String abbrDataType;


@FxColumn(name = "ABBR_DATA_SIZE", size = 7, nullable = true, comment = "약어데이터크기")
private double abbrDataSize;


@FxColumn(name = "NULBL_YN", size = 1, comment = "NULL가능여부", defValue = "Y")
private boolean nulblYn = true;


@FxColumn(name = "DFT_VAL", size = 100, nullable = true, comment = "디폴트값")
private String dftVal;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시", defValue = "0")
private long chgDtm = 0L;


/**
* 약어명
* @return 약어명
*/
public String getAbbrName() {
return abbrName;
}
/**
* 약어명
*@param abbrName 약어명
*/
public void setAbbrName(String abbrName) {
	this.abbrName = abbrName;
}
/**
* 약어컬럼
* @return 약어컬럼
*/
public String getAbbrCol() {
return abbrCol;
}
/**
* 약어컬럼
*@param abbrCol 약어컬럼
*/
public void setAbbrCol(String abbrCol) {
	this.abbrCol = abbrCol;
}
/**
* 약어전체명
* @return 약어전체명
*/
public String getAbbrAllName() {
return abbrAllName;
}
/**
* 약어전체명
*@param abbrAllName 약어전체명
*/
public void setAbbrAllName(String abbrAllName) {
	this.abbrAllName = abbrAllName;
}
/**
* 약어설명
* @return 약어설명
*/
public String getAbbrDesc() {
return abbrDesc;
}
/**
* 약어설명
*@param abbrDesc 약어설명
*/
public void setAbbrDesc(String abbrDesc) {
	this.abbrDesc = abbrDesc;
}
/**
* 약어데이터유형
* @return 약어데이터유형
*/
public String getAbbrDataType() {
return abbrDataType;
}
/**
* 약어데이터유형
*@param abbrDataType 약어데이터유형
*/
public void setAbbrDataType(String abbrDataType) {
	this.abbrDataType = abbrDataType;
}
/**
* 약어데이터크기
* @return 약어데이터크기
*/
public double getAbbrDataSize() {
return abbrDataSize;
}
/**
* 약어데이터크기
*@param abbrDataSize 약어데이터크기
*/
public void setAbbrDataSize(double abbrDataSize) {
	this.abbrDataSize = abbrDataSize;
}
/**
* NULL가능여부
* @return NULL가능여부
*/
public boolean isNulblYn() {
return nulblYn;
}
/**
* NULL가능여부
*@param nulblYn NULL가능여부
*/
public void setNulblYn(boolean nulblYn) {
	this.nulblYn = nulblYn;
}
/**
* 디폴트값
* @return 디폴트값
*/
public String getDftVal() {
return dftVal;
}
/**
* 디폴트값
*@param dftVal 디폴트값
*/
public void setDftVal(String dftVal) {
	this.dftVal = dftVal;
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
