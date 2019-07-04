package fxms.bas.impl.co;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_UR_ALOG", comment = "운용자접속이력테이블")
@FxIndex(name = "FX_UR_ALOG__PK", type = INDEX_TYPE.PK, columns = { "SESSION_ID" })
public class LogoutDbo {

	@FxColumn(name = "LOGOUT_DATE", size = 14, nullable = true, comment = "로그아웃일시")
	private long logoutDate;

	@FxColumn(name = "LOG_STATUS_CODE", size = 1, nullable = true, comment = "로그상태코드")
	private int logStatusCode;

	@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
	private String sessionId;


	public long getLogoutDate() {
		return logoutDate;
	}


	public int getLogStatusCode() {
		return logStatusCode;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setLogoutDate(long logoutDate) {
		this.logoutDate = logoutDate;
	}

	public void setLogStatusCode(int logStatusCode) {
		this.logStatusCode = logStatusCode;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
