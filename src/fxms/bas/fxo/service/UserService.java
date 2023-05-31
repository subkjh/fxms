package fxms.bas.fxo.service;

import java.rmi.RemoteException;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.handler.vo.SessionVo;

public interface UserService extends FxService {

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
	public SessionVo login(String userId, String password, String ipAddr, String media)
			throws RemoteException, Exception;

	/**
	 * 
	 * @param sessionId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean logout(String sessionId, ACCS_ST_CD accsStCd) throws RemoteException, Exception;

	/**
	 * 
	 * @param accessToken JWT(JSON Web Token)
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SessionVo getSession(String accessToken) throws RemoteException, Exception;

	/**
	 * 
	 * @param sessionId
	 * @param userNo
	 * @param hostname
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SessionVo getSession(String sessionId, int userNo, String hostname) throws RemoteException, Exception;

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
	public SessionVo reissueJwt(String userId, String hostname, String accessToken) throws RemoteException, Exception;

	/**
	 * 
	 * @param refreshToken refresh token
	 * @param accessToken  access token
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SessionVo reissueJwt(String refreshToken, String accessToken) throws RemoteException, Exception;

}
