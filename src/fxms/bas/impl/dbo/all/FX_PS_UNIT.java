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


@FxTable(name = "FX_PS_UNIT", comment = "성능단위테이블")
@FxIndex(name = "FX_PS_UNIT__PK", type = INDEX_TYPE.PK, columns = {"PS_UNIT"})
public class FX_PS_UNIT implements Serializable {

public FX_PS_UNIT() {
 }

@FxColumn(name = "PS_UNIT", size = 10, comment = "성능단위")
private String psUnit;


@FxColumn(name = "UNIT_NAME", size = 50, comment = "단위명")
private String unitName;


@FxColumn(name = "DISP_TEXT", size = 20, comment = "표시문구")
private String dispText;


@FxColumn(name = "UNIT_DESC", size = 20, nullable = true, comment = "단위설명")
private String unitDesc;


@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
private boolean useYn = true;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
private long chgDtm;


/**
* 성능단위
* @return 성능단위
*/
public String getPsUnit() {
return psUnit;
}
/**
* 성능단위
*@param psUnit 성능단위
*/
public void setPsUnit(String psUnit) {
	this.psUnit = psUnit;
}
/**
* 단위명
* @return 단위명
*/
public String getUnitName() {
return unitName;
}
/**
* 단위명
*@param unitName 단위명
*/
public void setUnitName(String unitName) {
	this.unitName = unitName;
}
/**
* 표시문구
* @return 표시문구
*/
public String getDispText() {
return dispText;
}
/**
* 표시문구
*@param dispText 표시문구
*/
public void setDispText(String dispText) {
	this.dispText = dispText;
}
/**
* 단위설명
* @return 단위설명
*/
public String getUnitDesc() {
return unitDesc;
}
/**
* 단위설명
*@param unitDesc 단위설명
*/
public void setUnitDesc(String unitDesc) {
	this.unitDesc = unitDesc;
}
/**
* 사용여부
* @return 사용여부
*/
public boolean isUseYn() {
return useYn;
}
/**
* 사용여부
*@param useYn 사용여부
*/
public void setUseYn(boolean useYn) {
	this.useYn = useYn;
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
