package fxms.module.restapi.handler.func;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.dbo.user.FX_UR_PLOG;
import fxms.bas.dbo.user.FX_UR_USER;
import fxms.bas.dbo.user.LoginDbo;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;
import subkjh.bas.user.exception.UserNotFoundException;

/**
 * 운용자의 암호를 변경하는 함수
 * @author SUBKJH-DEV
 *
 */
public class UserPwdChange {

	/**
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @param newPasswdChk
	 * @return
	 * @throws Exception
	 */
	public LoginDbo change(String userId, String oldPasswd, String newPasswd, String newPasswdChk) throws Exception {

		if (newPasswd.equals(newPasswdChk) == false) {
			throw new Exception("입력한 새로운 암호를 다시 확인하세요.");
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("userId", userId);

			LoginDbo user = tran.selectOne(LoginDbo.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			if (user.getUserPasswd().equals(User.encodingPassword(oldPasswd)) == false) {
				throw new UserNotFoundException("현재 암호가 틀립니다.");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userNo", user.getUserNo());
			map.put("userPasswd", User.encodingPassword(newPasswd));
			tran.updateOfClass(FX_UR_USER.class, map);

			FX_UR_PLOG plog = new FX_UR_PLOG();
			plog.setRegDate(FxApi.getDate(System.currentTimeMillis()));
			plog.setRegUserNo(user.getUserNo());
			plog.setUseEndDate(0);
			plog.setUserNo(user.getUserNo());
			plog.setUserPasswd(User.encodingPassword(newPasswd));
			plog.setUseSrtDate(FxApi.getDate(System.currentTimeMillis()));

			tran.insert(plog);

			tran.commit();

			return user;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}
