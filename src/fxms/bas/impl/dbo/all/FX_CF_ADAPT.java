package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.02.08 14:54
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CF_ADAPT", comment = "아답터기본테이블")
@FxIndex(name = "FX_CF_ADAPT__PK", type = INDEX_TYPE.PK, columns = {"ADAPT_NAME"})
public class FX_CF_ADAPT implements Serializable {

public FX_CF_ADAPT() {
 }

@FxColumn(name = "ADAPT_NAME", size = 200, comment = "아답터명")
private String adaptName;


@FxColumn(name = "ADAPT_DESCR", size = 200, comment = "아답터설명")
private String adaptDescr;


@FxColumn(name = "FXSVC_NAME", size = 50, nullable = true, comment = "FX서비스명", defValue = "all")
private String fxsvcName = "all";


@FxColumn(name = "POLL_CYCLE", size = 7, nullable = true, comment = "폴링주기", defValue = "0")
private Integer pollCycle = 0;


@FxColumn(name = "PARA_JSON", size = 1000, nullable = true, comment = "인자JSON")
private String paraJson;


@FxColumn(name = "MO_JSON", size = 1000, nullable = true, comment = "관리대상JSON")
private String moJson;


@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
private String useYn = "Y";


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 아답터명
* @return 아답터명
*/
public String getAdaptName() {
return adaptName;
}
/**
* 아답터명
*@param adaptName 아답터명
*/
public void setAdaptName(String adaptName) {
	this.adaptName = adaptName;
}
/**
* 아답터설명
* @return 아답터설명
*/
public String getAdaptDescr() {
return adaptDescr;
}
/**
* 아답터설명
*@param adaptDescr 아답터설명
*/
public void setAdaptDescr(String adaptDescr) {
	this.adaptDescr = adaptDescr;
}
/**
* FX서비스명
* @return FX서비스명
*/
public String getFxsvcName() {
return fxsvcName;
}
/**
* FX서비스명
*@param fxsvcName FX서비스명
*/
public void setFxsvcName(String fxsvcName) {
	this.fxsvcName = fxsvcName;
}
/**
* 폴링주기
* @return 폴링주기
*/
public Integer getPollCycle() {
return pollCycle;
}
/**
* 폴링주기
*@param pollCycle 폴링주기
*/
public void setPollCycle(Integer pollCycle) {
	this.pollCycle = pollCycle;
}
/**
* 인자JSON
* @return 인자JSON
*/
public String getParaJson() {
return paraJson;
}
/**
* 인자JSON
*@param paraJson 인자JSON
*/
public void setParaJson(String paraJson) {
	this.paraJson = paraJson;
}
/**
* 관리대상JSON
* @return 관리대상JSON
*/
public String getMoJson() {
return moJson;
}
/**
* 관리대상JSON
*@param moJson 관리대상JSON
*/
public void setMoJson(String moJson) {
	this.moJson = moJson;
}
/**
* 사용여부
* @return 사용여부
*/
public String isUseYn() {
return useYn;
}
/**
* 사용여부
*@param useYn 사용여부
*/
public void setUseYn(String useYn) {
	this.useYn = useYn;
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
