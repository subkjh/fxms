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


@FxTable(name = "FX_UR_TREE_ATTR", comment = "사용자트리속성MO테이블")
@FxIndex(name = "FX_UR_USER_TREE_ATTR__PK", type = INDEX_TYPE.PK, columns = {"TREE_NO", "USER_NO", "ATTR_NAME"})
public class FX_UR_TREE_ATTR implements Serializable {

public FX_UR_TREE_ATTR() {
 }

@FxColumn(name = "TREE_NO", size = 9, comment = "트리번호")
private int treeNo;


@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", defValue = "0")
private int userNo = 0;


@FxColumn(name = "ATTR_NAME", size = 50, comment = "속성명")
private String attrName;


@FxColumn(name = "CMPR_FUNC_CD", size = 20, nullable = true, comment = "비교함수코드")
private String cmprFuncCd;


@FxColumn(name = "CMPR_VAL", size = 100, nullable = true, comment = "비교값")
private String cmprVal;


/**
* 트리번호
* @return 트리번호
*/
public int getTreeNo() {
return treeNo;
}
/**
* 트리번호
*@param treeNo 트리번호
*/
public void setTreeNo(int treeNo) {
	this.treeNo = treeNo;
}
/**
* 사용자번호
* @return 사용자번호
*/
public int getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(int userNo) {
	this.userNo = userNo;
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
* 비교함수코드
* @return 비교함수코드
*/
public String getCmprFuncCd() {
return cmprFuncCd;
}
/**
* 비교함수코드
*@param cmprFuncCd 비교함수코드
*/
public void setCmprFuncCd(String cmprFuncCd) {
	this.cmprFuncCd = cmprFuncCd;
}
/**
* 비교값
* @return 비교값
*/
public String getCmprVal() {
return cmprVal;
}
/**
* 비교값
*@param cmprVal 비교값
*/
public void setCmprVal(String cmprVal) {
	this.cmprVal = cmprVal;
}
}
