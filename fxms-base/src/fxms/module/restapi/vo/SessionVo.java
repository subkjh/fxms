package fxms.module.restapi.vo;

import java.io.Serializable;

import fxms.bas.dbo.user.LoginDbo;
import subkjh.bas.user.User;

public class SessionVo extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7410549580262946661L;

	private String hostname;

	private long seqno;

	private long lastOpTime;

	private String sessionId;

	public SessionVo() {
		lastOpTime = System.currentTimeMillis();
	}

	public SessionVo(LoginDbo user, String sessionId) {

		setUserId(user.getUserId());
		setUserNo(user.getUserNo());
		setUserName(user.getUserName());
		setUserType(user.getUserType());
		setMngInloNo(user.getMngInloNo());
		setUserGroupNo(user.getUgrpNo());

		this.sessionId = sessionId;
	}

	public String getHostname() {
		return hostname;
	}

	public long getLastOpTime() {
		return lastOpTime;
	}

	public long getSeqno() {
		return seqno;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setLastOpTime(long lastOpTime) {
		this.lastOpTime = lastOpTime;
	}

	public void setSeqno(long seqno) {
		this.seqno = seqno;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
