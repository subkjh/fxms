package fxms.bas.impl.api;

import fxms.bas.api.FxApiServiceTag;
import fxms.bas.api.ServiceApi;
import fxms.bas.api.UserApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.UserService;
import fxms.bas.handler.vo.SessionVo;

/**
 * 
 * @author subkjh
 *
 */
public class UserApiService extends UserApi implements FxApiServiceTag {

	private UserService service = null;

	public UserApiService() {
		try {
			setName(FxServiceImpl.serviceName + ":UserApi");
		} catch (Exception e) {
			setName("UserApiService");
		}
	}

	private synchronized UserService getService() throws FxServiceNotFoundException, Exception {

		if (this.service == null) {
			this.service = ServiceApi.getApi().getService(UserService.class);
		}

		// ping을 해봐서 끊겼는지 확인한다.
		try {
			this.service.ping(getName());
		} catch (Exception e) {
			this.service = ServiceApi.getApi().getService(UserService.class);
		}

		return this.service;
	}

	@Override
	public SessionVo login(String userId, String password, String ipAddr, String media) throws Exception {
		return this.getService().login(userId, password, ipAddr, media);
	}

	@Override
	public boolean logout(String sessionId, ACCS_ST_CD accsStCd) throws Exception {
		return this.getService().logout(sessionId, accsStCd);
	}

	@Override
	public SessionVo getSession(String accessToken) throws Exception {
		return this.getService().getSession(accessToken);
	}

	@Override
	public SessionVo getSession(String sessionId, int userNo, String hostname) throws Exception {
		return this.getService().getSession(sessionId, userNo, hostname);
	}

	@Override
	public SessionVo reissueJwt(String userId, String hostname, String accessToken) throws Exception {
		return this.getService().reissueJwt(userId, hostname, accessToken);
	}

	@Override
	public SessionVo reissueJwt(String refreshToken, String accessToken) throws Exception {
		return this.getService().reissueJwt(refreshToken, accessToken);
	}

}
