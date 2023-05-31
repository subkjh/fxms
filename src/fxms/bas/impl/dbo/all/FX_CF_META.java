package fxms.bas.impl.dbo.all;


import java.io.Serializable;

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


@FxTable(name = "FX_CF_META", comment = "메타테이블")
@FxIndex(name = "FX_CF_META__PK", type = INDEX_TYPE.PK, columns = {"META_ID"})
public class FX_CF_META implements Serializable {

public FX_CF_META() {
 }

@FxColumn(name = "META_ID", size = 10, comment = "메타ID")
private String metaId;


@FxColumn(name = "META_NAME", size = 30, comment = "메타명")
private String metaName;


@FxColumn(name = "TAB_NAME", size = 20, comment = "테이블명")
private String tabName;


@FxColumn(name = "COL_NAME", size = 32, comment = "컬럼명")
private String colName;


@FxColumn(name = "COL_DESC", size = 100, nullable = true, comment = "컬럼설명")
private String colDesc;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 메타ID
* @return 메타ID
*/
public String getMetaId() {
return metaId;
}
/**
* 메타ID
*@param metaId 메타ID
*/
public void setMetaId(String metaId) {
	this.metaId = metaId;
}
/**
* 메타명
* @return 메타명
*/
public String getMetaName() {
return metaName;
}
/**
* 메타명
*@param metaName 메타명
*/
public void setMetaName(String metaName) {
	this.metaName = metaName;
}
/**
* 테이블명
* @return 테이블명
*/
public String getTabName() {
return tabName;
}
/**
* 테이블명
*@param tabName 테이블명
*/
public void setTabName(String tabName) {
	this.tabName = tabName;
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
* 컬럼설명
* @return 컬럼설명
*/
public String getColDesc() {
return colDesc;
}
/**
* 컬럼설명
*@param colDesc 컬럼설명
*/
public void setColDesc(String colDesc) {
	this.colDesc = colDesc;
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
