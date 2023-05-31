package fxms.bas.api;

import java.rmi.RemoteException;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.log.LOG_LEVEL;

/**
 * UserService에 대응하는 API
 * 
 * @author subkjh
 *
 */
public abstract class UserApi extends FxApi {

	public static UserApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static UserApi getApi() {
		if (api != null)
			return api;

		api = makeApi(UserApi.class);

		return api;
	}

	public UserApi() {

	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {
		if (type == ReloadType.All || type == ReloadType.User) {
		}
	}

	/**
	 * 로그인 계정을 이용하여 로그인 요청함.
	 * 
	 * @param userId
	 * @param password
	 * @param ipAddr
	 * @param media
	 * @return 세션ID
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SessionVo login(String userId, String password, String host, String media) throws Exception;

	/**
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract boolean logout(String sessionId, ACCS_ST_CD accsStCd) throws Exception;

	/**
	 * 
	 * @param accessToken JWT(JSON Web Token)
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SessionVo getSession(String accessToken) throws Exception;

	/**
	 * 
	 * @param hostname
	 * @param userNo
	 * @param sessionId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SessionVo getSession(String sessionId, int userNo, String hostname) throws Exception;

	/**
	 * 토큰을 갱신한다.
	 * 
	 * @param userId   접속계정
	 * @param hostname 접속주소
	 * @param jwt      JWT
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SessionVo reissueJwt(String userId, String hostname, String accessToken) throws Exception;

	/**
	 * 
	 * @param refreshToken refresh토큰
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SessionVo reissueJwt(String refreshToken, String accessToken) throws Exception;

	@Override
	public String getState(LOG_LEVEL arg0) {
		return "";
	}

}
