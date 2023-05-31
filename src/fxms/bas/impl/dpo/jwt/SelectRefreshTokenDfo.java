package fxms.bas.impl.dpo.jwt;

import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class SelectRefreshTokenDfo implements FxDfo<SessionVo, String> {

	@Override
	public String call(FxFact fact, SessionVo session) throws Exception {

		String token = selectRefreshToken(session.getSessionId());

		return token;
	}

	public String selectRefreshToken(String sessionId) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			List<FX_UR_USER_ACCS_HST> list = tran.select(FX_UR_USER_ACCS_HST.class,
					FxApi.makePara("sessionId", sessionId));
			if (list.size() == 1) {
				return list.get(0).getRefreshToken();
			}
			return null;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}
}
