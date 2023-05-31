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


@FxTable(name = "FX_CF_VAR", comment = "코드(시간)테이블")
@FxIndex(name = "FX_CF_VAR__PK", type = INDEX_TYPE.PK, columns = {"VAR_NAME"})
@FxIndex(name = "FX_CF_VAR__UK", type = INDEX_TYPE.UK, columns = {"VAR_DISP_NAME"})
public class FX_CF_VAR implements Serializable {

public FX_CF_VAR() {
 }

@FxColumn(name = "VAR_GRP_NAME", size = 50, nullable = true, comment = "변수그룹명")
private String varGrpName;


@FxColumn(name = "VAR_NAME", size = 50, comment = "변수명")
private String varName;


@FxColumn(name = "VAR_DISP_NAME", size = 100, nullable = true, comment = "변수표시명")
private String varDispName;


@FxColumn(name = "VAR_DESC", size = 200, nullable = true, comment = "변수설명")
private String varDesc;


@FxColumn(name = "VAR_TYPE_CD", size = 1, nullable = true, comment = "변수유형코드")
private String varTypeCd;


@FxColumn(name = "VAR_TYPE_VAL", size = 200, nullable = true, comment = "변수유형값")
private String varTypeVal;


@FxColumn(name = "VAR_VAL", size = 100, nullable = true, comment = "변수설정값")
private String varVal;


@FxColumn(name = "VAR_MEMO", size = 100, nullable = true, comment = "변수메모사항")
private String varMemo;


@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
private boolean useYn = true;


@FxColumn(name = "EDTBL_YN", size = 1, nullable = true, comment = "편집여부", defValue = "Y")
private boolean edtblYn = true;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호")
private int regUserNo;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시", defValue = "0")
private long chgDtm = 0L;


/**
* 변수그룹명
* @return 변수그룹명
*/
public String getVarGrpName() {
return varGrpName;
}
/**
* 변수그룹명
*@param varGrpName 변수그룹명
*/
public void setVarGrpName(String varGrpName) {
	this.varGrpName = varGrpName;
}
/**
* 변수명
* @return 변수명
*/
public String getVarName() {
return varName;
}
/**
* 변수명
*@param varName 변수명
*/
public void setVarName(String varName) {
	this.varName = varName;
}
/**
* 변수표시명
* @return 변수표시명
*/
public String getVarDispName() {
return varDispName;
}
/**
* 변수표시명
*@param varDispName 변수표시명
*/
public void setVarDispName(String varDispName) {
	this.varDispName = varDispName;
}
/**
* 변수설명
* @return 변수설명
*/
public String getVarDesc() {
return varDesc;
}
/**
* 변수설명
*@param varDesc 변수설명
*/
public void setVarDesc(String varDesc) {
	this.varDesc = varDesc;
}
/**
* 변수유형코드
* @return 변수유형코드
*/
public String getVarTypeCd() {
return varTypeCd;
}
/**
* 변수유형코드
*@param varTypeCd 변수유형코드
*/
public void setVarTypeCd(String varTypeCd) {
	this.varTypeCd = varTypeCd;
}
/**
* 변수유형값
* @return 변수유형값
*/
public String getVarTypeVal() {
return varTypeVal;
}
/**
* 변수유형값
*@param varTypeVal 변수유형값
*/
public void setVarTypeVal(String varTypeVal) {
	this.varTypeVal = varTypeVal;
}
/**
* 변수설정값
* @return 변수설정값
*/
public String getVarVal() {
return varVal;
}
/**
* 변수설정값
*@param varVal 변수설정값
*/
public void setVarVal(String varVal) {
	this.varVal = varVal;
}
/**
* 변수메모사항
* @return 변수메모사항
*/
public String getVarMemo() {
return varMemo;
}
/**
* 변수메모사항
*@param varMemo 변수메모사항
*/
public void setVarMemo(String varMemo) {
	this.varMemo = varMemo;
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
* 편집여부
* @return 편집여부
*/
public boolean isEdtblYn() {
return edtblYn;
}
/**
* 편집여부
*@param edtblYn 편집여부
*/
public void setEdtblYn(boolean edtblYn) {
	this.edtblYn = edtblYn;
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
