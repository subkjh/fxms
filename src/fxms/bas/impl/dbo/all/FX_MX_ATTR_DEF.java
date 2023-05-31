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


@FxTable(name = "FX_MX_ATTR_DEF", comment = "MO속성정의테이블")
@FxIndex(name = "FX_MX_ATTR_DEF__PK", type = INDEX_TYPE.PK, columns = {"ATTR_ID"})
@FxIndex(name = "FX_MX_ATTR_DEF__UK", type = INDEX_TYPE.UK, columns = {"TBL_NAME", "COL_NAME"})
public class FX_MX_ATTR_DEF implements Serializable {

public FX_MX_ATTR_DEF() {
 }

@FxColumn(name = "ATTR_ID", size = 30, comment = "속성ID")
private String attrId;


@FxColumn(name = "ATTR_NAME", size = 50, comment = "속성명")
private String attrName;


@FxColumn(name = "TBL_NAME", size = 32, comment = "테이블명")
private String tblName;


@FxColumn(name = "COL_NAME", size = 32, comment = "컬럼명")
private String colName;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


/**
* 속성ID
* @return 속성ID
*/
public String getAttrId() {
return attrId;
}
/**
* 속성ID
*@param attrId 속성ID
*/
public void setAttrId(String attrId) {
	this.attrId = attrId;
}
/**
* 속성명
* @return 속성명
*/
public String getAttrName() {
return attrName;
}
/**
* 속성명
*@param attrName 속성명
*/
public void setAttrName(String attrName) {
	this.attrName = attrName;
}
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
