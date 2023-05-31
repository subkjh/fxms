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


@FxTable(name = "FX_UR_USER_PWD_CHG_HST", comment = "사용자암호변경이력테이블")
@FxIndex(name = "FX_UR_USER_PWD_CHG_HST__PK", type = INDEX_TYPE.PK, columns = {"USER_NO", "USER_PWD"})
public class FX_UR_USER_PWD_CHG_HST implements Serializable {

public FX_UR_USER_PWD_CHG_HST() {
 }

@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
private int userNo;


@FxColumn(name = "USER_PWD", size = 255, comment = "사용자암호")
private String userPwd;


@FxColumn(name = "USE_STRT_DTM", size = 14, comment = "사용시작일시")
private long useStrtDtm;


@FxColumn(name = "USE_END_DTM", size = 14, nullable = true, comment = "사용종료일시")
private long useEndDtm;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private int regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private long regDtm;


/**
* 사용자번호
* @return 사용자번호
*/
public int getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(int userNo) {
	this.userNo = userNo;
}
/**
* 사용자암호
* @return 사용자암호
*/
public String getUserPwd() {
return userPwd;
}
/**
* 사용자암호
*@param userPwd 사용자암호
*/
public void setUserPwd(String userPwd) {
	this.userPwd = userPwd;
}
/**
* 사용시작일시
* @return 사용시작일시
*/
public long getUseStrtDtm() {
return useStrtDtm;
}
/**
* 사용시작일시
*@param useStrtDtm 사용시작일시
*/
public void setUseStrtDtm(long useStrtDtm) {
	this.useStrtDtm = useStrtDtm;
}
/**
* 사용종료일시
* @return 사용종료일시
*/
public long getUseEndDtm() {
return useEndDtm;
}
/**
* 사용종료일시
*@param useEndDtm 사용종료일시
*/
public void setUseEndDtm(long useEndDtm) {
	this.useEndDtm = useEndDtm;
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
}
