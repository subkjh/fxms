package fxms.bas.impl.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_ALOG", comment = "운용자접속이력테이블")
@FxIndex(name = "FX_UR_ALOG__PK", type = INDEX_TYPE.PK, columns = { "SESSION_ID" })
public class FX_UR_ALOG implements Serializable {

	public FX_UR_ALOG() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
	private String sessionId;

	@FxColumn(name = "LOGIN_DATE", size = 14, comment = "로그인일시")
	private long loginDate;

	@FxColumn(name = "LOGOUT_DATE", size = 14, nullable = true, comment = "로그아웃일시")
	private long logoutDate;

	@FxColumn(name = "IP_ADDR", size = 39, nullable = true, comment = "접속IP주소")
	private String ipAddr;

	@FxColumn(name = "LOG_STATUS_CODE", size = 1, nullable = true, comment = "로그상태코드")
	private int logStatusCode;

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 세션ID
	 * 
	 * @return 세션ID
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 세션ID
	 * 
	 * @param sessionId
	 *            세션ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 로그인일시
	 * 
	 * @return 로그인일시
	 */
	public long getLoginDate() {
		return loginDate;
	}

	/**
	 * 로그인일시
	 * 
	 * @param loginDate
	 *            로그인일시
	 */
	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
	}

	public long getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(long logoutDate) {
		this.logoutDate = logoutDate;
	}

	/**
	 * 접속IP주소
	 * 
	 * @return 접속IP주소
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * 접속IP주소
	 * 
	 * @param ipAddr
	 *            접속IP주소
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public int getLogStatusCode() {
		return logStatusCode;
	}

	public void setLogStatusCode(int logStatusCode) {
		this.logStatusCode = logStatusCode;
	}


}
