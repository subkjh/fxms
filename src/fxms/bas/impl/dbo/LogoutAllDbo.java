package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_ALOG", comment = "운용자접속이력테이블")
@FxIndex(name = "FX_UR_ALOG__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
public class LogoutAllDbo {

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private int userNo;

	@FxColumn(name = "LOGOUT_DATE", size = 14, nullable = true, comment = "로그아웃일시")
	private long logoutDate;

	@FxColumn(name = "LOG_STATUS_CODE", size = 1, nullable = true, comment = "로그상태코드")
	private int logStatusCode;

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public long getLogoutDate() {
		return logoutDate;
	}

	public void setLogoutDate(long logoutDate) {
		this.logoutDate = logoutDate;
	}

	public int getLogStatusCode() {
		return logStatusCode;
	}

	public void setLogStatusCode(int logStatusCode) {
		this.logStatusCode = logStatusCode;
	}
	
	
}
