package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class UserDisableDfo implements FxDfo<String, Boolean> {

	public static void main(String[] args) {
		UserDisableDfo dfo = new UserDisableDfo();
		try {
			dfo.disable(111, "kkk");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, String data) throws Exception {
		int userNo = fact.getUserNo();
		return disable(userNo, data);
	}

	public boolean disable(int userNo, String userId) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> para = FxApi.makePara("userNo", userNo, "userId", userId, "useYn", "Y");
			FX_UR_USER user = tran.selectOne(FX_UR_USER.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			user.setUseYn(false);
			user.setUseEndDate(DateUtil.getYmd(System.currentTimeMillis() - 86400000L));
			user.setChgDtm(DateUtil.getDtm());

			tran.updateOfClass(FX_UR_USER.class, user);

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