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


@FxTable(name = "FX_UR_FAVO_GRP_ATTR", comment = "관심그룹속성MO테이블")
@FxIndex(name = "FX_UR_FAVO_GRP_ATTR__PK", type = INDEX_TYPE.PK, columns = {"FAVO_GRP_NO", "ATTR_NAME"})
public class FX_UR_FAVO_GRP_ATTR implements Serializable {

public FX_UR_FAVO_GRP_ATTR() {
 }

@FxColumn(name = "FAVO_GRP_NO", size = 9, comment = "관심그룹번호")
private int favoGrpNo;


@FxColumn(name = "ATTR_NAME", size = 50, comment = "속성명")
private String attrName;


@FxColumn(name = "CMPR_FUNC_CD", size = 100, nullable = true, comment = "비교함수코드")
private String cmprFuncCd;


@FxColumn(name = "CMPR_VAL", size = 100, nullable = true, comment = "비교값")
private String cmprVal;


/**
* 관심그룹번호
* @return 관심그룹번호
*/
public int getFavoGrpNo() {
return favoGrpNo;
}
/**
* 관심그룹번호
*@param favoGrpNo 관심그룹번호
*/
public void setFavoGrpNo(int favoGrpNo) {
	this.favoGrpNo = favoGrpNo;
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
