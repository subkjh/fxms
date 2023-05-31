package fxms.bas.fxo.service;

import java.rmi.RemoteException;

import fxms.bas.api.FxApi;
import fxms.bas.api.UserApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.AppServiceDao;

public class UserServiceImpl extends FxServiceImpl implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5232592706306071778L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(UserService.class.getSimpleName(), UserServiceImpl.class, args);
	}

	private long autoLogoutMstime = 0L;

	public UserServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {

		super.onInit(sb);

		FxApi.initServiceApi(UserApi.class);

	}

	@Override
	protected void onStarted() throws Exception {
		super.onStarted();
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		return notiFilter;
	}

	@Override
	public SessionVo getSession(String accessToken) throws RemoteException, Exception {

		SessionVo session = null;

		try {
			session = UserApi.getApi().getSession(accessToken);
			return session;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("accessToken={} --> {}", accessToken, session == null ? "(error)" : session.getSessionId());
		}

	}

	@Override
	public SessionVo getSession(String sessionId, int userNo, String host) throws RemoteException, Exception {

		SessionVo session = null;
		try {
			session = UserApi.getApi().getSession(sessionId, userNo, host);
			return session;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("sessionId={} userNo={} host={} --> {}", sessionId, userNo, host,
					session == null ? "(error)" : session.getAccessToken());
		}

	}

	@Override
	public SessionVo login(String userId, String password, String host, String media)
			throws RemoteException, Exception {

		if (host == null) {
			try {
				host = getClientHost();
			} catch (Exception e) {
			}
		}

		SessionVo session = null;
		try {
			session = UserApi.getApi().login(userId, password, host, media);
			return session;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("userId={} password={} host={} --> {}", userId, password, host,
					session == null ? "(error)" : session.getSessionId());
		}

	}

	@Override
	public boolean logout(String sessionId, ACCS_ST_CD accsStCd) throws RemoteException, Exception {

		Boolean ret = null;
		try {
			ret = UserApi.getApi().logout(sessionId, accsStCd);
			return ret;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("sessionId={}, accsStCd={} --> {}", sessionId, accsStCd, ret == null ? "(error)" : ret);

		}

	}

	@Override
	protected void onCycle(long mstime) {

		super.onCycle(mstime);

		try {
			if (System.currentTimeMillis() > autoLogoutMstime + 3600000L) {
				new AppServiceDao().updateAutoLogout();
				autoLogoutMstime = System.currentTimeMillis();
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		super.onNotify(noti);
	}

	@Override
	public SessionVo reissueJwt(String refreshToken, String accessToken) throws RemoteException, Exception {

		SessionVo session = null;
		try {
			session = UserApi.getApi().reissueJwt(refreshToken, accessToken);
			return session;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("refreshToken={} --> {}", refreshToken, session == null ? "(error)" : session.getAccessToken());

		}

	}

	@Override
	public SessionVo reissueJwt(String userId, String hostname, String accessToken) throws RemoteException, Exception {

		SessionVo session = null;
		try {
			session = UserApi.getApi().reissueJwt(userId, hostname, accessToken);
			return session;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("userId={}, hostname={}, accessToken={} --> {}", userId, hostname, accessToken,
					session == null ? "(error)" : session.getAccessToken());
		}

	}

}
