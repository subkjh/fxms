package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.jwt.ReadAccessTokenDfo;
import fxms.bas.impl.dpo.jwt.ReadRefreshTokenDfo;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 세션 정보를 가져온다.
 * 
 * @author subkjh
 *
 */
public class UserGetSessionDfo implements FxDfo<String, SessionVo> {

	@Override
	public SessionVo call(FxFact fact, String refreshToken) throws Exception {

		Map<String, Object> payload = new ReadRefreshTokenDfo().call(fact, refreshToken);

		return getSession(payload.get("sessionId").toString());
	}

	public SessionVo getSessionWithRefreshToken(String refreshToken) throws NotFoundException, Exception {

		Map<String, Object> payload = new ReadRefreshTokenDfo().read(refreshToken);

		return getSession(payload.get("sessionId").toString());

	}

	public SessionVo getSessionWithAccessToken(String accessToken) throws NotFoundException, Exception {

		Map<String, Object> payload = new ReadAccessTokenDfo().read(accessToken);

		SessionVo session = getSession(payload.get("sessionId").toString());
		
		session.setAccessToken(accessToken);
		return session;
	}

	public SessionVo getSession(String sessionId) throws NotFoundException, Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			Map<String, Object> para = FxApi.makePara("sessionId", sessionId);
			FX_UR_USER_ACCS_HST hst = tran.selectOne(FX_UR_USER_ACCS_HST.class, para);
			if (hst != null) {
				para.clear();
				para.put("userNo", hst.getUserNo());
				FX_UR_USER user = tran.selectOne(FX_UR_USER.class, para);
				if (user != null) {
					SessionVo session = new SessionVo(sessionId, user.getUserNo(), user.getUserId(), user.getUserName(),
							USER_TYPE_CD.get(user.getUserTypeCd()), user.getUgrpNo(), user.getInloNo());
					session.setAuthority(user.getAuthority());
					session.setRefreshToken(hst.getRefreshToken());
					return session;
				}
			}
			throw new NotFoundException("session", sessionId);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}
}
