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


@FxTable(name = "FX_UR_TREE_BAS", comment = "트리기본테이블")
@FxIndex(name = "FX_UR_TREE__PK", type = INDEX_TYPE.PK, columns = {"TREE_NO"})
public class FX_UR_TREE_BAS implements Serializable {

public FX_UR_TREE_BAS() {
 }

public static final String FX_SEQ_TREENO  = "FX_SEQ_TREENO"; 
@FxColumn(name = "TREE_NO", size = 9, comment = "트리번호", sequence = "FX_SEQ_TREENO")
private int treeNo;


@FxColumn(name = "UPPER_TREE_NO", size = 9, comment = "상위트리번호", defValue = "0")
private int upperTreeNo = 0;


@FxColumn(name = "TREE_NAME", size = 50, comment = "트리명")
private String treeName;


@FxColumn(name = "TREE_DESC", size = 100, nullable = true, comment = "트리설명")
private String treeDesc;


@FxColumn(name = "TARGT_MO_CLASS", size = 20, comment = "대상MO클래스 ")
private String targtMoClass;


@FxColumn(name = "INLO_CL_CD", size = 30, comment = "설치위치분류코드")
private String inloClCd;


@FxColumn(name = "OWNER_UGRP_NO", size = 9, comment = "소유사용자그룹번호", defValue = "-1")
private int ownerUgrpNo = -1;


@FxColumn(name = "OWNER_USER_NO", size = 9, comment = "소유사용자번호", defValue = "-1")
private int ownerUserNo = -1;


@FxColumn(name = "ETC_MO_TREE_NAME", size = 50, nullable = true, comment = "기타MO트리명")
private String etcMoTreeName;


@FxColumn(name = "SORT_SEQ", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
private int sortSeq = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일자")
private long chgDtm;


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
* 상위트리번호
* @return 상위트리번호
*/
public int getUpperTreeNo() {
return upperTreeNo;
}
/**
* 상위트리번호
*@param upperTreeNo 상위트리번호
*/
public void setUpperTreeNo(int upperTreeNo) {
	this.upperTreeNo = upperTreeNo;
}
/**
* 트리명
* @return 트리명
*/
public String getTreeName() {
return treeName;
}
/**
* 트리명
*@param treeName 트리명
*/
public void setTreeName(String treeName) {
	this.treeName = treeName;
}
/**
* 트리설명
* @return 트리설명
*/
public String getTreeDesc() {
return treeDesc;
}
/**
* 트리설명
*@param treeDesc 트리설명
*/
public void setTreeDesc(String treeDesc) {
	this.treeDesc = treeDesc;
}
/**
* 대상MO클래스 
* @return 대상MO클래스 
*/
public String getTargtMoClass() {
return targtMoClass;
}
/**
* 대상MO클래스 
*@param targtMoClass 대상MO클래스 
*/
public void setTargtMoClass(String targtMoClass) {
	this.targtMoClass = targtMoClass;
}
/**
* 설치위치분류코드
* @return 설치위치분류코드
*/
public String getInloClCd() {
return inloClCd;
}
/**
* 설치위치분류코드
*@param inloClCd 설치위치분류코드
*/
public void setInloClCd(String inloClCd) {
	this.inloClCd = inloClCd;
}
/**
* 소유사용자그룹번호
* @return 소유사용자그룹번호
*/
public int getOwnerUgrpNo() {
return ownerUgrpNo;
}
/**
* 소유사용자그룹번호
*@param ownerUgrpNo 소유사용자그룹번호
*/
public void setOwnerUgrpNo(int ownerUgrpNo) {
	this.ownerUgrpNo = ownerUgrpNo;
}
/**
* 소유사용자번호
* @return 소유사용자번호
*/
public int getOwnerUserNo() {
return ownerUserNo;
}
/**
* 소유사용자번호
*@param ownerUserNo 소유사용자번호
*/
public void setOwnerUserNo(int ownerUserNo) {
	this.ownerUserNo = ownerUserNo;
}
/**
* 기타MO트리명
* @return 기타MO트리명
*/
public String getEtcMoTreeName() {
return etcMoTreeName;
}
/**
* 기타MO트리명
*@param etcMoTreeName 기타MO트리명
*/
public void setEtcMoTreeName(String etcMoTreeName) {
	this.etcMoTreeName = etcMoTreeName;
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
* 수정일자
* @return 수정일자
*/
public long getChgDtm() {
return chgDtm;
}
/**
* 수정일자
*@param chgDtm 수정일자
*/
public void setChgDtm(long chgDtm) {
	this.chgDtm = chgDtm;
}
}
