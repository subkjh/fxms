package fxms.bas.impl.co;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.UserAlog;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.FX_UR_ALOG;
import fxms.bas.impl.dbo.FX_UR_USER;
import fxms.module.restapi.vo.SessionVo;

public class UserDao extends FxDaoExecutor {

	public UserDao() {
		setDatabase(DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG));
	}
	
	private static long sessionId = 0;
	private static synchronized String getNextSessionId()
	{
		if ( sessionId == 0 ) {
			sessionId = ( System.currentTimeMillis() / 1234 ) * 1000;
		}
		
		++sessionId;
		
		return "fxms-" + sessionId;
	}

	public SessionVo login(String userId, String password, String ipaddr) throws UserNotFoundException, Exception {

		String sessionId = getNextSessionId();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userId", userId);
		try {
			start();
			LoginDbo user = this.selectOne(LoginDbo.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			if (user.getUserPasswd().equals(User.encodingPassword(password)) == false) {
				throw new UserNotFoundException("password incorrect");
			}

			int ymd = FxApi.getYmd(System.currentTimeMillis());
			if ((ymd >= user.getUseSrtYmd() && ymd <= user.getUseEndYmd()) == false) {
				throw new UserNotFoundException(userId + " use expired");
			}

			// LogoutAllDbo logout = new LogoutAllDbo();
			// logout.setLogoutDate(FxApi.getDate());
			// logout.setLogStatusCode(UserAlog.TYPE_LOGOUT_TIME);
			// logout.setUserNo(user.getUserNo());
			// update(logout, FxConfDao.makePara("logStatusCode",
			// UserAlog.TYPE_LOGIN));

			FX_UR_ALOG alog = new FX_UR_ALOG();
			alog.setIpAddr(ipaddr);
			alog.setLoginDate(FxApi.getDate());
			alog.setLogoutDate(0);
			alog.setLogStatusCode(UserAlog.TYPE_LOGIN);
			alog.setSessionId(sessionId);
			alog.setUserNo(user.getUserNo());

			insertOfClass(alog.getClass(), alog);

			commit();

			return new SessionVo(sessionId, user.getUserNo());

		} catch (UserNotFoundException e) {
			Logger.logger.fail("{} {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			stop();
		}

	}

	public void changePwd(String userId, String password, String newPassword) throws UserNotFoundException, Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userId", userId);

		try {
			start();
			LoginDbo user = this.selectOne(LoginDbo.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			if (user.getUserPasswd().equals(User.encodingPassword(password)) == false) {
				throw new UserNotFoundException("password incorrect");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userNo", user.getUserNo());
			map.put("userPasswd", User.encodingPassword(newPassword));

			updateOfClass(FX_UR_USER.class, map);

			commit();

			return;
		} catch (UserNotFoundException e) {
			Logger.logger.fail("{} {}", e.getClass().getSimpleName(), e.getMessage());
			throw e;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			stop();
		}

	}

	public void writeLog(UserLogDbo dbo) {

		try {
			start();

			dbo.setOpSeqno(this.getNextVal(UserLogDbo.FX_SEQ_OPSEQNO, Long.class));

			insertOfClass(dbo.getClass(), dbo);

			commit();
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			stop();
		}

	}

	public List<OpDbo> selectOpList() throws Exception {

		try {
			start();

			return select(OpDbo.class, null);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			stop();
		}

	}
}
