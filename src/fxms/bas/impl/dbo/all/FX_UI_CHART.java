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


@FxTable(name = "FX_UI_CHART", comment = "UI챠트테이블")
@FxIndex(name = "FX_UI_CHRT__PK", type = INDEX_TYPE.PK, columns = {"CHRT_ID"})
public class FX_UI_CHART implements Serializable {

public FX_UI_CHART() {
 }

@FxColumn(name = "CHRT_ID", size = 30, comment = "챠트ID")
private String chrtId;


@FxColumn(name = "CHRT_NAME", size = 50, comment = "챠트명")
private String chrtName;


@FxColumn(name = "MO_CLASS", size = 20, comment = "MO클래스 ")
private String moClass;


@FxColumn(name = "MO_TYPE", size = 30, comment = "MO유형")
private String moType;


@FxColumn(name = "PS_ID_LIST", size = 200, comment = "성능ID목록")
private String psIdList;


@FxColumn(name = "PS_DATA_CD", size = 10, comment = "성능데이터코드")
private String psDataCd;


@FxColumn(name = "DATA_RNG_CD", size = 10, comment = "데이터범위코드")
private String dataRngCd;


@FxColumn(name = "DATA_RNG_VAL", size = 5, comment = "데이터범위값")
private int dataRngVal;


@FxColumn(name = "CHRT_CRE_CLASS", size = 200, nullable = true, comment = "챠트생성CLASS")
private String chrtCreClass;


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
* 챠트ID
* @return 챠트ID
*/
public String getChrtId() {
return chrtId;
}
/**
* 챠트ID
*@param chrtId 챠트ID
*/
public void setChrtId(String chrtId) {
	this.chrtId = chrtId;
}
/**
* 챠트명
* @return 챠트명
*/
public String getChrtName() {
return chrtName;
}
/**
* 챠트명
*@param chrtName 챠트명
*/
public void setChrtName(String chrtName) {
	this.chrtName = chrtName;
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
* MO유형
* @return MO유형
*/
public String getMoType() {
return moType;
}
/**
* MO유형
*@param moType MO유형
*/
public void setMoType(String moType) {
	this.moType = moType;
}
/**
* 성능ID목록
* @return 성능ID목록
*/
public String getPsIdList() {
return psIdList;
}
/**
* 성능ID목록
*@param psIdList 성능ID목록
*/
public void setPsIdList(String psIdList) {
	this.psIdList = psIdList;
}
/**
* 성능데이터코드
* @return 성능데이터코드
*/
public String getPsDataCd() {
return psDataCd;
}
/**
* 성능데이터코드
*@param psDataCd 성능데이터코드
*/
public void setPsDataCd(String psDataCd) {
	this.psDataCd = psDataCd;
}
/**
* 데이터범위코드
* @return 데이터범위코드
*/
public String getDataRngCd() {
return dataRngCd;
}
/**
* 데이터범위코드
*@param dataRngCd 데이터범위코드
*/
public void setDataRngCd(String dataRngCd) {
	this.dataRngCd = dataRngCd;
}
/**
* 데이터범위값
* @return 데이터범위값
*/
public int getDataRngVal() {
return dataRngVal;
}
/**
* 데이터범위값
*@param dataRngVal 데이터범위값
*/
public void setDataRngVal(int dataRngVal) {
	this.dataRngVal = dataRngVal;
}
/**
* 챠트생성CLASS
* @return 챠트생성CLASS
*/
public String getChrtCreClass() {
return chrtCreClass;
}
/**
* 챠트생성CLASS
*@param chrtCreClass 챠트생성CLASS
*/
public void setChrtCreClass(String chrtCreClass) {
	this.chrtCreClass = chrtCreClass;
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
