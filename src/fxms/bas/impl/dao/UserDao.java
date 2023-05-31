package fxms.bas.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.LoginDbo;
import fxms.bas.impl.dbo.OpDbo;
import fxms.bas.impl.dbo.UserLogDbo;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class UserDao extends ClassDao {

	public UserDao() {
		setDatabase(DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG));
	}

	private static long sessionId = 0;

	private static synchronized String getNextSessionId() {
		if (sessionId == 0) {
			sessionId = (System.currentTimeMillis() / 12345) * 10000;
		}

		++sessionId;

		return "FXMS" + sessionId;
	}

	public SessionVo login(String userId, String password, String ipaddr, String media)
			throws UserNotFoundException, Exception {

		String sessionId = getNextSessionId();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userId", userId);
		try {
			start();
			LoginDbo user = this.selectOne(LoginDbo.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			if (user.isUseYn() == false) {
				throw new UserNotFoundException("ID is disabled");
			}

			if (user.getUserPwd().equals(User.encodingPassword(password)) == false) {
				throw new UserNotFoundException("password incorrect");
			}

			int ymd = DateUtil.getYmd(System.currentTimeMillis());
			if ((ymd >= user.getUseStrtDate() && ymd <= user.getUseEndDate()) == false) {
				throw new UserNotFoundException(userId + " use expired");
			}

			// LogoutAllDbo logout = new LogoutAllDbo();
			// logout.setLogoutDate(FxApi.getDate());
			// logout.setLogStatusCode(UserAlog.TYPE_LOGOUT_TIME);
			// logout.setUserNo(user.getUserNo());
			// update(logout, FxConfDao.makePara("logStatusCode",
			// UserAlog.TYPE_LOGIN));

			FX_UR_USER_ACCS_HST alog = new FX_UR_USER_ACCS_HST();
			alog.setAccsIpAddr(ipaddr);
			alog.setLoginDtm(DateUtil.getDtm());
			alog.setLogoutDtm(0L);
			alog.setAccsStCd(ACCS_ST_CD.LOGIN.getCode());
			alog.setSessionId(sessionId);
			alog.setAccsMedia(media);
			alog.setUserNo(user.getUserNo());
			alog.setUserId(user.getUserId());
			alog.setUserName(user.getUserName());
			alog.setAutoLogoutTimeOut(SessionVo.EXPIRE_TIME);
			alog.setRegDtm(DateUtil.getDtm());
			alog.setRegUserNo(user.getUserNo());

			insertOfClass(alog.getClass(), alog);

			commit();

			SessionVo session = new SessionVo(sessionId, user.getUserNo(), user.getUserId(), user.getUserName(),
					USER_TYPE_CD.get(user.getUserTypeCd()), user.getUgrpNo(), user.getInloNo());
			session.setAuthority(user.getAuthority());
			return session;

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

			if (user.getUserPwd().equals(User.encodingPassword(password)) == false) {
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

			dbo.setOpSeqNo(this.getNextVal(UserLogDbo.FX_SEQ_OPSEQNO, Long.class));

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
