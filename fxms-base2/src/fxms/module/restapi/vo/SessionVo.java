package fxms.module.restapi.vo;

import java.io.Serializable;

import subkjh.bas.co.user.User.USER_TYPE;

public class SessionVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7410549580262946661L;

	private String hostname;

	private long seqno;

	private long lastOpTime;

	private String sessionId;

	private int userNo;

	private int mngInloNo;
	private String userId;
	private String userName;
	private USER_TYPE userType;

	public SessionVo() {
		lastOpTime = System.currentTimeMillis();
	}

	public SessionVo(String sessionId, int userNo) {

		this.userNo = userNo;
		this.sessionId = sessionId;
	}

	public String getHostname() {
		return hostname;
	}

	public long getLastOpTime() {
		return lastOpTime;
	}

	public int getMngInloNo() {
		return mngInloNo;
	}

	public long getSeqno() {
		return seqno;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getUserNo() {
		return userNo;
	}

	public USER_TYPE getUserType() {
		return userType;
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
