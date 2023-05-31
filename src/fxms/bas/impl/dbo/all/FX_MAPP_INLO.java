package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.02.23 13:49
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_MAPP_INLO", comment = "매핑설치위치테이블")
@FxIndex(name = "FX_MAPP_INLO__PK", type = INDEX_TYPE.PK, columns = {"MNG_DIV", "MAPP_DATA"})
@FxIndex(name = "FX_MAPP_INLO__KEY1", type = INDEX_TYPE.KEY, columns = {"MNG_DIV", "MAPP_ID"})
public class FX_MAPP_INLO implements Serializable {

public FX_MAPP_INLO() {
 }

@FxColumn(name = "MNG_DIV", size = 20, comment = "관리구분")
private String mngDiv;


@FxColumn(name = "MAPP_DATA", size = 100, comment = "매핑데이터")
private String mappData;


@FxColumn(name = "MAPP_DESCR", size = 200, nullable = true, comment = "매핑설명")
private String mappDescr;


@FxColumn(name = "MAPP_ID", size = 100, comment = "매핑ID", defValue = "x")
private String mappId = "x";


@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", defValue = "0")
private Integer inloNo = 0;


@FxColumn(name = "INLO_NAME", size = 100, nullable = true, comment = "설치위치명")
private String inloName;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 관리구분
* @return 관리구분
*/
public String getMngDiv() {
return mngDiv;
}
/**
* 관리구분
*@param mngDiv 관리구분
*/
public void setMngDiv(String mngDiv) {
	this.mngDiv = mngDiv;
}
/**
* 매핑데이터
* @return 매핑데이터
*/
public String getMappData() {
return mappData;
}
/**
* 매핑데이터
*@param mappData 매핑데이터
*/
public void setMappData(String mappData) {
	this.mappData = mappData;
}
/**
* 매핑설명
* @return 매핑설명
*/
public String getMappDescr() {
return mappDescr;
}
/**
* 매핑설명
*@param mappDescr 매핑설명
*/
public void setMappDescr(String mappDescr) {
	this.mappDescr = mappDescr;
}
/**
* 매핑ID
* @return 매핑ID
*/
public String getMappId() {
return mappId;
}
/**
* 매핑ID
*@param mappId 매핑ID
*/
public void setMappId(String mappId) {
	this.mappId = mappId;
}
/**
* 설치위치번호
* @return 설치위치번호
*/
public Integer getInloNo() {
return inloNo;
}
/**
* 설치위치번호
*@param inloNo 설치위치번호
*/
public void setInloNo(Integer inloNo) {
	this.inloNo = inloNo;
}
/**
* 설치위치명
* @return 설치위치명
*/
public String getInloName() {
return inloName;
}
/**
* 설치위치명
*@param inloName 설치위치명
*/
public void setInloName(String inloName) {
	this.inloName = inloName;
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
