package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.03.30 15:53
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CF_MODEL", comment = "모델테이블")
@FxIndex(name = "FX_CF_MODEL__PK", type = INDEX_TYPE.PK, columns = {"MODEL_NO"})
@FxIndex(name = "FX_CF_MODEL__UK_NAME", type = INDEX_TYPE.UK, columns = {"MODEL_CL_CD", "MODEL_NAME"})
public class FX_CF_MODEL implements Serializable {

public FX_CF_MODEL() {
 }

public static final String FX_SEQ_MODELNO  = "FX_SEQ_MODELNO"; 
@FxColumn(name = "MODEL_NO", size = 9, comment = "모델번호", sequence = "FX_SEQ_MODELNO")
private Integer modelNo;


@FxColumn(name = "MODEL_NAME", size = 100, comment = "모델명")
private String modelName;


@FxColumn(name = "VENDR_NAME", size = 50, nullable = true, comment = "제조사명")
private String vendrName;


@FxColumn(name = "MODEL_CL_CD", size = 30, comment = "모델분류코드")
private String modelClCd;


@FxColumn(name = "MODEL_IDFY_VAL", size = 200, nullable = true, comment = "모델식별값")
private String modelIdfyVal;


@FxColumn(name = "MODEL_DESCR", size = 200, nullable = true, comment = "모델설명")
private String modelDescr;


@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "MO클래스 ")
private String moClass;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 모델번호
* @return 모델번호
*/
public Integer getModelNo() {
return modelNo;
}
/**
* 모델번호
*@param modelNo 모델번호
*/
public void setModelNo(Integer modelNo) {
	this.modelNo = modelNo;
}
/**
* 모델명
* @return 모델명
*/
public String getModelName() {
return modelName;
}
/**
* 모델명
*@param modelName 모델명
*/
public void setModelName(String modelName) {
	this.modelName = modelName;
}
/**
* 제조사명
* @return 제조사명
*/
public String getVendrName() {
return vendrName;
}
/**
* 제조사명
*@param vendrName 제조사명
*/
public void setVendrName(String vendrName) {
	this.vendrName = vendrName;
}
/**
* 모델분류코드
* @return 모델분류코드
*/
public String getModelClCd() {
return modelClCd;
}
/**
* 모델분류코드
*@param modelClCd 모델분류코드
*/
public void setModelClCd(String modelClCd) {
	this.modelClCd = modelClCd;
}
/**
* 모델식별값
* @return 모델식별값
*/
public String getModelIdfyVal() {
return modelIdfyVal;
}
/**
* 모델식별값
*@param modelIdfyVal 모델식별값
*/
public void setModelIdfyVal(String modelIdfyVal) {
	this.modelIdfyVal = modelIdfyVal;
}
/**
* 모델설명
* @return 모델설명
*/
public String getModelDescr() {
return modelDescr;
}
/**
* 모델설명
*@param modelDescr 모델설명
*/
public void setModelDescr(String modelDescr) {
	this.modelDescr = modelDescr;
}
/**
* MO클래스 
* @return MO클래스 
*/
public String getMoClass() {
return moClass;
}
/**
* MO클래스 
*@param moClass MO클래스 
*/
public void setMoClass(String moClass) {
	this.moClass = moClass;
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
