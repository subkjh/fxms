package fxms.bas.impl.dbo;


import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
* @since 2017.06.16 15:13
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


@FxColumn(name = "LN_DESCR", size = 200, nullable = true, comment = "회선설명")
private String lnDescr;


@FxColumn(name = "MST_MO_NO", size = 19, nullable = true, comment = "마스터MO관리번호")
private long mstMoNo;


@FxColumn(name = "SRT_MO_NO", size = 19, nullable = true, comment = "시작MO관리번호")
private long srtMoNo;


@FxColumn(name = "END_MO_NO", size = 19, nullable = true, comment = "종단MO관리번호")
private long endMoNo;


@FxColumn(name = "MEM_SIZE", size = 9, nullable = true, comment = "구성크기", defValue = "2")
private int memSize = 2;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
private long regDate;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
private int chgUserNo = 0;


@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
private long chgDate;


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
public String getLnDescr() {
return lnDescr;
}
/**
* 회선설명
*@param lnDescr 회선설명
*/
public void setLnDescr(String lnDescr) {
	this.lnDescr = lnDescr;
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
* 등록운영자번호
* @return 등록운영자번호
*/
public int getRegUserNo() {
return regUserNo;
}
/**
* 등록운영자번호
*@param regUserNo 등록운영자번호
*/
public void setRegUserNo(int regUserNo) {
	this.regUserNo = regUserNo;
}
/**
* 등록일시
* @return 등록일시
*/
public long getRegDate() {
return regDate;
}
/**
* 등록일시
*@param regDate 등록일시
*/
public void setRegDate(long regDate) {
	this.regDate = regDate;
}
/**
* 수정운영자번호
* @return 수정운영자번호
*/
public int getChgUserNo() {
return chgUserNo;
}
/**
* 수정운영자번호
*@param chgUserNo 수정운영자번호
*/
public void setChgUserNo(int chgUserNo) {
	this.chgUserNo = chgUserNo;
}
/**
* 수정일시
* @return 수정일시
*/
public long getChgDate() {
return chgDate;
}
/**
* 수정일시
*@param chgDate 수정일시
*/
public void setChgDate(long chgDate) {
	this.chgDate = chgDate;
}
}
