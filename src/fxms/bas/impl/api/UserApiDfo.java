package fxms.bas.impl.api;

import fxms.bas.api.FxApi;
import fxms.bas.api.UserApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dpo.jwt.ReissueAccessTokenDpo;
import fxms.bas.impl.dpo.user.LoginDpo;
import fxms.bas.impl.dpo.user.LogoutDpo;
import fxms.bas.impl.dpo.user.UserGetSessionDfo;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public class UserApiDfo extends UserApi {

	public UserApiDfo() {

	}

	@Override
	public SessionVo login(String userId, String password, String host, String media) throws Exception {

		LoginDpo login = new LoginDpo();
		SessionVo session = login.login(userId, password, host, media);
		return session;

	}

	@Override
	public boolean logout(String sessionId, ACCS_ST_CD accsStCd) throws Exception {
		return new LogoutDpo().logout(sessionId, accsStCd.getCode());
	}

	@Override
	public SessionVo getSession(String accessToken) throws Exception {
		return new UserGetSessionDfo().getSessionWithAccessToken(accessToken);
	}

	@Override
	public SessionVo getSession(String sessionId, int userNo, String hostname) throws Exception {

		SessionVo session = new UserGetSessionDfo().getSession(sessionId);

		if (session.getUserNo() != userNo && FxApi.isSame(hostname, session.getHostname()) == false) {
			Logger.logger.fail("session data mismatch userNo={}:{}, hostname={}:{}", session.getUserNo(), userNo,
					session.getHostname(), hostname);
			throw new Exception(Lang.get("Session information not found."));
		}

		return session;
	}

	@Override
	public SessionVo reissueJwt(String userId, String hostname, String accessToken) throws Exception {
		SessionVo session = new ReissueAccessTokenDpo().reissue(userId, hostname, accessToken);
		return session;
	}

	@Override
	public SessionVo reissueJwt(String refreshToken, String accessToken) throws Exception {
		SessionVo session = new ReissueAccessTokenDpo().reissue(refreshToken);
		return session;
	}

}
