package fxms.bas.impl.dpo.user;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * Logout Data Process Object
 * 
 * @author subkjh
 *
 */
public class LogoutDpo implements FxDpo<Void, Boolean> {

	public static void main(String[] args) throws Exception {
		LogoutDpo dpo = new LogoutDpo();
		FxFact fact = new FxFact("sessionId", "FXMS1362036660001", "accsStCd", ACCS_ST_CD.LOGOUT_OK.getCode());
		dpo.run(fact, null);
	}

	@Override
	public Boolean run(FxFact fact, Void data) throws Exception {

		String sessionId = fact.getString("sessionId");
		String accsStCd = fact.getString("accsStCd");

		logout(sessionId, accsStCd);

		return true;
	}

	public boolean logout(String sessionId, String accsStCd) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("sessionId", sessionId);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FX_UR_USER_ACCS_HST hst = tran.selectOne(FX_UR_USER_ACCS_HST.class, para);
			hst.setLogoutDtm(DateUtil.getDtm());
			hst.setAccsStCd(accsStCd);
			hst.setSessionId(sessionId);
			hst.setChgDtm(DateUtil.getDtm());
			hst.setChgUserNo(hst.getUserNo());
			hst.setRefreshToken("");

			tran.updateOfClass(FX_UR_USER_ACCS_HST.class, hst);
			tran.commit();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}
}
