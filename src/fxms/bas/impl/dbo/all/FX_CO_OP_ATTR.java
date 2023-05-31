package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2022.05.20 09:51
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_CO_OP_ATTR", comment = "기능속성테이블")
@FxIndex(name = "FX_CO_OP_ATTR__PK", type = INDEX_TYPE.PK, columns = {"OP_ID", "ATTR_NAME"})
@FxIndex(name = "FX_CO_OP_ATTR__FK", type = INDEX_TYPE.FK, columns = {"OP_ID"}, fkTable = "FX_CO_OP", fkColumn = "OP_ID")
public class FX_CO_OP_ATTR implements Serializable {

public FX_CO_OP_ATTR() {
 }

@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
private String opId;


@FxColumn(name = "ATTR_GRP", size = 50, comment = "속성그룹명")
private String attrGrp;


@FxColumn(name = "ATTR_UI_TXT", size = 50, comment = "속성화면명")
private String attrUiTxt;


@FxColumn(name = "ATTR_NAME", size = 50, comment = "속성명")
private String attrName;


@FxColumn(name = "ATTR_TYPE_CD", size = 20, comment = "속성유형코드")
private String attrTypeCd;


@FxColumn(name = "ATTR_DFT_VAL", size = 50, nullable = true, comment = "속성기본값")
private String attrDftVal;


@FxColumn(name = "ATTR_VAL_LIST", size = 240, nullable = true, comment = "속성값목록")
private String attrValList;


@FxColumn(name = "PRMPT_TEXT", size = 100, nullable = true, comment = "프롬프트문구")
private String prmptText;


@FxColumn(name = "READ_ONLY_YN", size = 1, nullable = true, comment = "읽기전용여부", defValue = "N")
private boolean readOnlyYn = false;


@FxColumn(name = "NULBL_YN", size = 1, nullable = true, comment = "NULL 가능여부", defValue = "Y")
private boolean nulblYn = true;


@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
private boolean useYn = true;


@FxColumn(name = "SORT_SEQ", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
private int sortSeq = 0;


@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 기능ID
* @return 기능ID
*/
public String getOpId() {
return opId;
}
/**
* 기능ID
*@param opId 기능ID
*/
public void setOpId(String opId) {
	this.opId = opId;
}
/**
* 속성그룹명
* @return 속성그룹명
*/
public String getAttrGrp() {
return attrGrp;
}
/**
* 속성그룹명
*@param attrGrp 속성그룹명
*/
public void setAttrGrp(String attrGrp) {
	this.attrGrp = attrGrp;
}
/**
* 속성화면명
* @return 속성화면명
*/
public String getAttrUiTxt() {
return attrUiTxt;
}
/**
* 속성화면명
*@param attrUiTxt 속성화면명
*/
public void setAttrUiTxt(String attrUiTxt) {
	this.attrUiTxt = attrUiTxt;
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
* 속성유형코드
* @return 속성유형코드
*/
public String getAttrTypeCd() {
return attrTypeCd;
}
/**
* 속성유형코드
*@param attrTypeCd 속성유형코드
*/
public void setAttrTypeCd(String attrTypeCd) {
	this.attrTypeCd = attrTypeCd;
}
/**
* 속성기본값
* @return 속성기본값
*/
public String getAttrDftVal() {
return attrDftVal;
}
/**
* 속성기본값
*@param attrDftVal 속성기본값
*/
public void setAttrDftVal(String attrDftVal) {
	this.attrDftVal = attrDftVal;
}
/**
* 속성값목록
* @return 속성값목록
*/
public String getAttrValList() {
return attrValList;
}
/**
* 속성값목록
*@param attrValList 속성값목록
*/
public void setAttrValList(String attrValList) {
	this.attrValList = attrValList;
}
/**
* 프롬프트문구
* @return 프롬프트문구
*/
public String getPrmptText() {
return prmptText;
}
/**
* 프롬프트문구
*@param prmptText 프롬프트문구
*/
public void setPrmptText(String prmptText) {
	this.prmptText = prmptText;
}
/**
* 읽기전용여부
* @return 읽기전용여부
*/
public boolean isReadOnlyYn() {
return readOnlyYn;
}
/**
* 읽기전용여부
*@param readOnlyYn 읽기전용여부
*/
public void setReadOnlyYn(boolean readOnlyYn) {
	this.readOnlyYn = readOnlyYn;
}
/**
* NULL 가능여부
* @return NULL 가능여부
*/
public boolean isNulblYn() {
return nulblYn;
}
/**
* NULL 가능여부
*@param nulblYn NULL 가능여부
*/
public void setNulblYn(boolean nulblYn) {
	this.nulblYn = nulblYn;
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
* 정렬순서
* @return 정렬순서
*/
public int getSortSeq() {
return sortSeq;
}
/**
* 정렬순서
*@param sortSeq 정렬순서
*/
public void setSortSeq(int sortSeq) {
	this.sortSeq = sortSeq;
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
