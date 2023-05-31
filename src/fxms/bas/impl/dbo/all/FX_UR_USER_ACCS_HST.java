package fxms.bas.impl.dbo.all;


import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
* @since 2023.04.13 17:30
* @author subkjh 
* autometic create by subkjh.dao 
*
*/


@FxTable(name = "FX_UR_USER_ACCS_HST", comment = "사용자접속이력테이블")
@FxIndex(name = "FX_UR_USER_ACCS_HST__PK", type = INDEX_TYPE.PK, columns = {"SESSION_ID"})
@FxIndex(name = "FX_UR_USER_ACCS_HST__KEY1", type = INDEX_TYPE.KEY, columns = {"LOGIN_DTM", "ACCS_ST_CD"})
@FxIndex(name = "FX_UR_USER_ACCS_HST__FK1", type = INDEX_TYPE.FK, columns = {"USER_NO"}, fkTable = "FX_UR_USER", fkColumn = "USER_NO")
public class FX_UR_USER_ACCS_HST implements Serializable {

public FX_UR_USER_ACCS_HST() {
 }

@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
private String sessionId;


@FxColumn(name = "LOGIN_DTM", size = 14, comment = "LOGIN일시")
private Long loginDtm;


@FxColumn(name = "LOGOUT_DTM", size = 14, nullable = true, comment = "LOGOUT일시")
private Long logoutDtm;


@FxColumn(name = "USER_NO", size = 9, nullable = true, comment = "사용자번호")
private Integer userNo;


@FxColumn(name = "USER_ID", size = 100, nullable = true, comment = "사용자ID")
private String userId;


@FxColumn(name = "USER_NAME", size = 50, nullable = true, comment = "사용자명")
private String userName;


@FxColumn(name = "ACCS_IP_ADDR", size = 100, nullable = true, comment = "접속IP주소")
private String accsIpAddr;


@FxColumn(name = "ACCS_ST_CD", size = 2, nullable = true, comment = "접속상태코드")
private String accsStCd;


@FxColumn(name = "ACCS_MEDIA", size = 100, nullable = true, comment = "접속매체")
private String accsMedia;


@FxColumn(name = "AUTO_LOGOUT_TIME_OUT", size = 9, comment = "자동LOGOUT타임아웃", defValue = "3600")
private Integer autoLogoutTimeOut = 3600;


@FxColumn(name = "REFRESH_TOKEN", size = 200, nullable = true, comment = "리프레쉬토큰")
private String refreshToken;


@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
private Integer regUserNo = 0;


@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
private Long regDtm;


@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
private Integer chgUserNo = 0;


@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
private Long chgDtm;


/**
* 세션ID
* @return 세션ID
*/
public String getSessionId() {
return sessionId;
}
/**
* 세션ID
*@param sessionId 세션ID
*/
public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
}
/**
* LOGIN일시
* @return LOGIN일시
*/
public Long getLoginDtm() {
return loginDtm;
}
/**
* LOGIN일시
*@param loginDtm LOGIN일시
*/
public void setLoginDtm(Long loginDtm) {
	this.loginDtm = loginDtm;
}
/**
* LOGOUT일시
* @return LOGOUT일시
*/
public Long getLogoutDtm() {
return logoutDtm;
}
/**
* LOGOUT일시
*@param logoutDtm LOGOUT일시
*/
public void setLogoutDtm(Long logoutDtm) {
	this.logoutDtm = logoutDtm;
}
/**
* 사용자번호
* @return 사용자번호
*/
public Integer getUserNo() {
return userNo;
}
/**
* 사용자번호
*@param userNo 사용자번호
*/
public void setUserNo(Integer userNo) {
	this.userNo = userNo;
}
/**
* 사용자ID
* @return 사용자ID
*/
public String getUserId() {
return userId;
}
/**
* 사용자ID
*@param userId 사용자ID
*/
public void setUserId(String userId) {
	this.userId = userId;
}
/**
* 사용자명
* @return 사용자명
*/
public String getUserName() {
return userName;
}
/**
* 사용자명
*@param userName 사용자명
*/
public void setUserName(String userName) {
	this.userName = userName;
}
/**
* 접속IP주소
* @return 접속IP주소
*/
public String getAccsIpAddr() {
return accsIpAddr;
}
/**
* 접속IP주소
*@param accsIpAddr 접속IP주소
*/
public void setAccsIpAddr(String accsIpAddr) {
	this.accsIpAddr = accsIpAddr;
}
/**
* 접속상태코드
* @return 접속상태코드
*/
public String getAccsStCd() {
return accsStCd;
}
/**
* 접속상태코드
*@param accsStCd 접속상태코드
*/
public void setAccsStCd(String accsStCd) {
	this.accsStCd = accsStCd;
}
/**
* 접속매체
* @return 접속매체
*/
public String getAccsMedia() {
return accsMedia;
}
/**
* 접속매체
*@param accsMedia 접속매체
*/
public void setAccsMedia(String accsMedia) {
	this.accsMedia = accsMedia;
}
/**
* 자동LOGOUT타임아웃
* @return 자동LOGOUT타임아웃
*/
public Integer getAutoLogoutTimeOut() {
return autoLogoutTimeOut;
}
/**
* 자동LOGOUT타임아웃
*@param autoLogoutTimeOut 자동LOGOUT타임아웃
*/
public void setAutoLogoutTimeOut(Integer autoLogoutTimeOut) {
	this.autoLogoutTimeOut = autoLogoutTimeOut;
}
/**
* 리프레쉬토큰
* @return 리프레쉬토큰
*/
public String getRefreshToken() {
return refreshToken;
}
/**
* 리프레쉬토큰
*@param refreshToken 리프레쉬토큰
*/
public void setRefreshToken(String refreshToken) {
	this.refreshToken = refreshToken;
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
