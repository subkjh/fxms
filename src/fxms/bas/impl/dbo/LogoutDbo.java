package fxms.bas.impl.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_USER_ACCS_HST", comment = "운용자접속이력테이블")
@FxIndex(name = "FX_UR_USER_ACCS_HST__PK", type = INDEX_TYPE.PK, columns = { "SESSION_ID" })
public class LogoutDbo {

	@FxColumn(name = "LOGOUT_DTM", size = 14, nullable = true, comment = "LOGOUT일시")
	private long logoutDtm;

	@FxColumn(name = "ACCS_ST_CD", size = 1, nullable = true, comment = "접속상태코드")
	private String accsStCd;

	@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
	private String sessionId;
	
	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;


	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLogoutDtm() {
		return logoutDtm;
	}

	public void setLogoutDtm(long logoutDtm) {
		this.logoutDtm = logoutDtm;
	}

	public String getAccsStCd() {
		return accsStCd;
	}

	public void setAccsStCd(String accsStCd) {
		this.accsStCd = accsStCd;
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
