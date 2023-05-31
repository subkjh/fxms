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


@FxTable(name = "FX_CF_LINK", comment = "인터페이스연결테이블")
@FxIndex(name = "FX_CF_LN__PK", type = INDEX_TYPE.PK, columns = {"LN_ID"})
@FxIndex(name = "FX_CF_LN__KEY_SRT", type = INDEX_TYPE.KEY, columns = {"SRT_MO_NO"})
@FxIndex(name = "FX_CF_LN__KEY_END", type = INDEX_TYPE.KEY, columns = {"END_MO_NO"})
public class FX_CF_LINK implements Serializable {

public FX_CF_LINK() {
 }

@FxColumn(name = "LN_ID", size = 50, comment = "회선ID")
private String lnId;


@FxColumn(name = "LN_NAME", size = 200, nullable = true, comment = "회선명")
private String lnName;


@FxColumn(name = "LN_TYPE", size = 10, nullable = true, comment = "회선종류(코드집)")
private String lnType;


@FxColumn(name = "LN_DESC", size = 200, nullable = true, comment = "회선설명")
private String lnDesc;


@FxColumn(name = "MST_MO_NO", size = 19, nullable = true, comment = "마스터MO관리번호")
private long mstMoNo;


@FxColumn(name = "SRT_MO_NO", size = 19, nullable = true, comment = "시작MO관리번호")
private long srtMoNo;


@FxColumn(name = "END_MO_NO", size = 19, nullable = true, comment = "종단MO관리번호")
private long endMoNo;


@FxColumn(name = "MEM_SIZE", size = 9, nullable = true, comment = "구성크기", defValue = "2")
private int memSize = 2;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private long chgDtm;


/**
* 회선ID
* @return 회선ID
*/
public String getLnId() {
return lnId;
}
/**
* 회선ID
*@param lnId 회선ID
*/
public void setLnId(String lnId) {
	this.lnId = lnId;
}
/**
* 회선명
* @return 회선명
*/
public String getLnName() {
return lnName;
}
/**
* 회선명
*@param lnName 회선명
*/
public void setLnName(String lnName) {
	this.lnName = lnName;
}
/**
* 회선종류(코드집)
* @return 회선종류(코드집)
*/
public String getLnType() {
return lnType;
}
/**
* 회선종류(코드집)
*@param lnType 회선종류(코드집)
*/
public void setLnType(String lnType) {
	this.lnType = lnType;
}
/**
* 회선설명
* @return 회선설명
*/
public String getLnDesc() {
return lnDesc;
}
/**
* 회선설명
*@param lnDesc 회선설명
*/
public void setLnDesc(String lnDesc) {
	this.lnDesc = lnDesc;
}
/**
* 마스터MO관리번호
* @return 마스터MO관리번호
*/
public long getMstMoNo() {
return mstMoNo;
}
/**
* 마스터MO관리번호
*@param mstMoNo 마스터MO관리번호
*/
public void setMstMoNo(long mstMoNo) {
	this.mstMoNo = mstMoNo;
}
/**
* 시작MO관리번호
* @return 시작MO관리번호
*/
public long getSrtMoNo() {
return srtMoNo;
}
/**
* 시작MO관리번호
*@param srtMoNo 시작MO관리번호
*/
public void setSrtMoNo(long srtMoNo) {
	this.srtMoNo = srtMoNo;
}
/**
* 종단MO관리번호
* @return 종단MO관리번호
*/
public long getEndMoNo() {
return endMoNo;
}
/**
* 종단MO관리번호
*@param endMoNo 종단MO관리번호
*/
public void setEndMoNo(long endMoNo) {
	this.endMoNo = endMoNo;
}
/**
* 구성크기
* @return 구성크기
*/
public int getMemSize() {
return memSize;
}
/**
* 구성크기
*@param memSize 구성크기
*/
public void setMemSize(int memSize) {
	this.memSize = memSize;
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
