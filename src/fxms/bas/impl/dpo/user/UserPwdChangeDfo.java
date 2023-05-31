package fxms.bas.impl.dpo.user;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dbo.all.FX_UR_USER_PWD_CHG_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 운용자의 암호를 변경하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserPwdChangeDfo implements FxDfo<User, Boolean> {

	@Override
	public Boolean call(FxFact fact, User user) throws Exception {
		String newPwd = fact.getString("newPwd");
		String newPwdChk = fact.getString("newPwdChk");

		changePwd(user, newPwd, newPwdChk);

		return true;
	}

	/**
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @param newPasswdChk
	 * @return
	 * @throws Exception
	 */
	public boolean changePwd(User user, String newPasswd, String newPasswdChk) throws Exception {

		if (newPasswd.equals(newPasswdChk) == false) {
			throw new Exception("입력한 새로운 암호를 다시 확인하세요.");
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_UR_USER old = tran.selectOne(FX_UR_USER.class, FxApi.makePara("userNo", user.getUserNo()));
			old.setUserPwd(User.encodingPassword(newPasswd));
			tran.updateOfClass(FX_UR_USER.class, old);

			FX_UR_USER_PWD_CHG_HST plog = new FX_UR_USER_PWD_CHG_HST();
			plog.setRegDtm(DateUtil.getDtm());
			plog.setRegUserNo(user.getUserNo());
			plog.setUseEndDtm(0);
			plog.setUserNo(user.getUserNo());
			plog.setUserPwd(User.encodingPassword(newPasswd));
			plog.setUseStrtDtm(DateUtil.getDtm());

			tran.insertOfClass(FX_UR_USER_PWD_CHG_HST.class, plog);

			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}
