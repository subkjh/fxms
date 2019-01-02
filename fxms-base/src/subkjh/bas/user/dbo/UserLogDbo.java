package subkjh.bas.user.dbo;

import fxms.bas.dbo.user.FX_UR_LOG;

public class UserLogDbo extends FX_UR_LOG {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8059584176443977483L;

	public UserLogDbo() {

	}

	public UserLogDbo(int userNo, String sessionId, int opNo, String opName) {
		setUserNo(userNo);
		setSessionId(sessionId);
		setOpNo(opNo);
		setOpName(opName);
	}

}
