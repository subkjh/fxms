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


@FxTable(name = "FX_CO_TERM", comment = "FX용어테이블")
@FxIndex(name = "FX_CO_TERM__PK", type = INDEX_TYPE.PK, columns = {"TERM_NAME", "TERM_CLASS_NAME"})
public class FX_CO_TERM implements Serializable {

public FX_CO_TERM() {
 }

@FxColumn(name = "TERM_NAME", size = 250, comment = "용어명")
private String termName;


@FxColumn(name = "TERM_CLASS_NAME", size = 250, comment = "용어분류명")
private String termClassName;


@FxColumn(name = "TERM_DESC", size = 2000, comment = "용어설명")
private String termDesc;


@FxColumn(name = "TERM_ALIAS", size = 250, nullable = true, comment = "용어별칭명")
private String termAlias;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시", defValue = "0")
private long chgDtm = 0L;


/**
* 용어명
* @return 용어명
*/
public String getTermName() {
return termName;
}
/**
* 용어명
*@param termName 용어명
*/
public void setTermName(String termName) {
	this.termName = termName;
}
/**
* 용어분류명
* @return 용어분류명
*/
public String getTermClassName() {
return termClassName;
}
/**
* 용어분류명
*@param termClassName 용어분류명
*/
public void setTermClassName(String termClassName) {
	this.termClassName = termClassName;
}
/**
* 용어설명
* @return 용어설명
*/
public String getTermDesc() {
return termDesc;
}
/**
* 용어설명
*@param termDesc 용어설명
*/
public void setTermDesc(String termDesc) {
	this.termDesc = termDesc;
}
/**
* 용어별칭명
* @return 용어별칭명
*/
public String getTermAlias() {
return termAlias;
}
/**
* 용어별칭명
*@param termAlias 용어별칭명
*/
public void setTermAlias(String termAlias) {
	this.termAlias = termAlias;
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
