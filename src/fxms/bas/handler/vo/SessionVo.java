package fxms.bas.handler.vo;

import fxms.bas.event.FxEventImpl;
import subkjh.bas.co.user.User.USER_TYPE_CD;

/**
 * 서버용 세션데이터
 * 
 * @author subkjh
 *
 */
public class SessionVo extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7410549580262946661L;

	public static final int EXPIRE_TIME = 60 * 60 * 2; // 2시간

	private String hostname;
	private final String sessionId;
	private final int ugrpNo;
	private final int userNo;
	private final int inloNo;
	private final String userId;
	private final String userName;
	private String authority;
	private final USER_TYPE_CD userTypeCd;
	private long accessTokenExpiresIn;

	/** JSON WEB TOKEN */
	private String accessToken;
	private String refreshToken;

	public SessionVo(String sessionId, int userNo, String userId, String userName, USER_TYPE_CD userTypeCd, int ugrpNo,
			int inloNo) {

		this.userNo = userNo;
		this.sessionId = sessionId;
		this.userTypeCd = userTypeCd;
		this.ugrpNo = ugrpNo;
		this.inloNo = inloNo;
		this.userId = userId;
		this.userName = userName;

	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAuthority() {
		return authority;
	}

	public String getHostname() {
		return hostname;
	}

	public int getInloNo() {
		return inloNo;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getSessionId() {
		return sessionId;
	}

	public int getUgrpNo() {
		return ugrpNo;
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

	public USER_TYPE_CD getUserTypeCd() {
		return userTypeCd;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getAccessTokenExpiresIn() {
		return accessTokenExpiresIn;
	}

	public void setAccessTokenExpiresIn(long accessTokenExpiresIn) {
		this.accessTokenExpiresIn = accessTokenExpiresIn;
	}

}
