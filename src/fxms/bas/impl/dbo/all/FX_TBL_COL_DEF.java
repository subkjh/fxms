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


@FxTable(name = "FX_TBL_COL_DEF", comment = "테이블컬럼정의테이블")
@FxIndex(name = "FX_TBL_COL_DEF__PK", type = INDEX_TYPE.PK, columns = {"TBL_NAME", "COL_NAME"})
@FxIndex(name = "FX_TBL_COL_DEF__FK_NAME", type = INDEX_TYPE.FK, columns = {"TBL_NAME"}, fkTable = "FX_TBL_DEF", fkColumn = "TBL_NAME")
public class FX_TBL_COL_DEF implements Serializable {

public FX_TBL_COL_DEF() {
 }

@FxColumn(name = "TBL_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
private String tblName;


@FxColumn(name = "COL_NO", size = 9, comment = "컬럼순서", defValue = "0")
private Integer colNo = 0;


@FxColumn(name = "COL_NAME", size = 32, operator = COLUMN_OP.insert, comment = "컬럼명")
private String colName;


@FxColumn(name = "COL_TYPE_CD", size = 10, comment = "컬럼유형코드")
private String colTypeCd;


@FxColumn(name = "COL_SIZE", size = 5, comment = "컬럼크기")
private Integer colSize;


@FxColumn(name = "NULBL_YN", size = 1, comment = "NULL가능여부", defValue = "Y")
private String nulblYn = "Y";


@FxColumn(name = "COL_CMNT", size = 200, comment = "컬럼코멘트")
private String colCmnt;


@FxColumn(name = "DEF_VAL", size = 100, nullable = true, comment = "디폴트값")
private String defVal;


@FxColumn(name = "UPDBL_YN", size = 1, comment = "수정가능여부")
private String updblYn;


@FxColumn(name = "SEQ_NAME", size = 32, nullable = true, comment = "시퀀스명")
private String seqName;


@FxColumn(name = "FIELD_NAME", size = 32, nullable = true, comment = "필드명")
private String fieldName;


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
* 컬럼순서
* @return 컬럼순서
*/
public Integer getColNo() {
return colNo;
}
/**
* 컬럼순서
*@param colNo 컬럼순서
*/
public void setColNo(Integer colNo) {
	this.colNo = colNo;
}
/**
* 컬럼명
* @return 컬럼명
*/
public String getColName() {
return colName;
}
/**
* 컬럼명
*@param colName 컬럼명
*/
public void setColName(String colName) {
	this.colName = colName;
}
/**
* 컬럼유형코드
* @return 컬럼유형코드
*/
public String getColTypeCd() {
return colTypeCd;
}
/**
* 컬럼유형코드
*@param colTypeCd 컬럼유형코드
*/
public void setColTypeCd(String colTypeCd) {
	this.colTypeCd = colTypeCd;
}
/**
* 컬럼크기
* @return 컬럼크기
*/
public Integer getColSize() {
return colSize;
}
/**
* 컬럼크기
*@param colSize 컬럼크기
*/
public void setColSize(Integer colSize) {
	this.colSize = colSize;
}
/**
* NULL가능여부
* @return NULL가능여부
*/
public String isNulblYn() {
return nulblYn;
}
/**
* NULL가능여부
*@param nulblYn NULL가능여부
*/
public void setNulblYn(String nulblYn) {
	this.nulblYn = nulblYn;
}
/**
* 컬럼코멘트
* @return 컬럼코멘트
*/
public String getColCmnt() {
return colCmnt;
}
/**
* 컬럼코멘트
*@param colCmnt 컬럼코멘트
*/
public void setColCmnt(String colCmnt) {
	this.colCmnt = colCmnt;
}
/**
* 디폴트값
* @return 디폴트값
*/
public String getDefVal() {
return defVal;
}
/**
* 디폴트값
*@param defVal 디폴트값
*/
public void setDefVal(String defVal) {
	this.defVal = defVal;
}
/**
* 수정가능여부
* @return 수정가능여부
*/
public String isUpdblYn() {
return updblYn;
}
/**
* 수정가능여부
*@param updblYn 수정가능여부
*/
public void setUpdblYn(String updblYn) {
	this.updblYn = updblYn;
}
/**
* 시퀀스명
* @return 시퀀스명
*/
public String getSeqName() {
return seqName;
}
/**
* 시퀀스명
*@param seqName 시퀀스명
*/
public void setSeqName(String seqName) {
	this.seqName = seqName;
}
/**
* 필드명
* @return 필드명
*/
public String getFieldName() {
return fieldName;
}
/**
* 필드명
*@param fieldName 필드명
*/
public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
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
