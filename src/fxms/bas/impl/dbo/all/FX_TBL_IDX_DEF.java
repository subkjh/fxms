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


@FxTable(name = "FX_TBL_IDX_DEF", comment = "테이블인덱스정의테이블")
@FxIndex(name = "FX_TBL_IDX_DEF__PK", type = INDEX_TYPE.PK, columns = {"TBL_NAME", "IDX_NAME"})
@FxIndex(name = "FX_TBL_IDX_DEF__FK_NAME", type = INDEX_TYPE.FK, columns = {"TBL_NAME"}, fkTable = "FX_TBL_DEF", fkColumn = "TBL_NAME")
public class FX_TBL_IDX_DEF implements Serializable {

public FX_TBL_IDX_DEF() {
 }

@FxColumn(name = "TBL_NAME", size = 20, operator = COLUMN_OP.insert, comment = "테이블명")
private String tblName;


@FxColumn(name = "IDX_NAME", size = 32, operator = COLUMN_OP.insert, comment = "인덱스명")
private String idxName;


@FxColumn(name = "IDX_TYPE_CD", size = 3, comment = "인덱스유형코드")
private String idxTypeCd;


@FxColumn(name = "COL_NAME_LIST", size = 1000, comment = "컬럼명목록")
private String colNameList;


@FxColumn(name = "FK_TBL_NAME", size = 20, nullable = true, comment = "FK테이블명", defValue = "Y")
private String fkTblName = "Y";


@FxColumn(name = "FK_COL_NAME_LIST", size = 200, nullable = true, comment = "FK컬럼명목록")
private String fkColNameList;


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
* 인덱스명
* @return 인덱스명
*/
public String getIdxName() {
return idxName;
}
/**
* 인덱스명
*@param idxName 인덱스명
*/
public void setIdxName(String idxName) {
	this.idxName = idxName;
}
/**
* 인덱스유형코드
* @return 인덱스유형코드
*/
public String getIdxTypeCd() {
return idxTypeCd;
}
/**
* 인덱스유형코드
*@param idxTypeCd 인덱스유형코드
*/
public void setIdxTypeCd(String idxTypeCd) {
	this.idxTypeCd = idxTypeCd;
}
/**
* 컬럼명목록
* @return 컬럼명목록
*/
public String getColNameList() {
return colNameList;
}
/**
* 컬럼명목록
*@param colNameList 컬럼명목록
*/
public void setColNameList(String colNameList) {
	this.colNameList = colNameList;
}
/**
* FK테이블명
* @return FK테이블명
*/
public String getFkTblName() {
return fkTblName;
}
/**
* FK테이블명
*@param fkTblName FK테이블명
*/
public void setFkTblName(String fkTblName) {
	this.fkTblName = fkTblName;
}
/**
* FK컬럼명목록
* @return FK컬럼명목록
*/
public String getFkColNameList() {
return fkColNameList;
}
/**
* FK컬럼명목록
*@param fkColNameList FK컬럼명목록
*/
public void setFkColNameList(String fkColNameList) {
	this.fkColNameList = fkColNameList;
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
