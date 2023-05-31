package fxms.bas.impl.dpo.user;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 사용자 로그인 정보를 기록하는 함수
 * 
 * @author subkjh
 *
 */
public class LogUserAccessDfo implements FxDfo<SessionVo, Boolean> {

	@Override
	public Boolean call(FxFact fact, SessionVo session) throws Exception {

		log(session);

		return true;
	}

	public void log(SessionVo session) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FX_UR_USER_ACCS_HST alog = new FX_UR_USER_ACCS_HST();
			alog.setAccsIpAddr(session.getHostname());
			alog.setLoginDtm(DateUtil.getDtm());
			alog.setLogoutDtm(0L);
			alog.setAccsStCd(ACCS_ST_CD.LOGIN.getCode());
			alog.setSessionId(session.getSessionId());
			alog.setAccsMedia("-");
			alog.setUserNo(session.getUserNo());
			alog.setUserId(session.getUserId());
			alog.setUserName(session.getUserName());
			alog.setAutoLogoutTimeOut(SessionVo.EXPIRE_TIME);
			alog.setRegDtm(DateUtil.getDtm());
			alog.setRegUserNo(session.getUserNo());
			alog.setRefreshToken(session.getRefreshToken());

			tran.insertOfClass(FX_UR_USER_ACCS_HST.class, alog);

			tran.commit();

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

}
