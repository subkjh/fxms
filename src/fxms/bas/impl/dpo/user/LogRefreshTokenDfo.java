package fxms.bas.impl.dpo.user;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * Refresh Token을 수정한다.
 * 
 * @author subkjh
 *
 */
public class LogRefreshTokenDfo implements FxDfo<SessionVo, Boolean> {

	@Override
	public Boolean call(FxFact fact, SessionVo session) throws Exception {

		log(session);

		return true;
	}

	public void log(SessionVo session) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("sessionId", session.getSessionId());

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FX_UR_USER_ACCS_HST hst = tran.selectOne(FX_UR_USER_ACCS_HST.class, para);
			hst.setChgDtm(DateUtil.getDtm());
			hst.setChgUserNo(hst.getUserNo());
			hst.setRefreshToken(session.getRefreshToken());

			tran.updateOfClass(FX_UR_USER_ACCS_HST.class, hst);
			tran.commit();

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

}
