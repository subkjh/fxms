package fxms.bas.impl.dpo.jwt;

import fxms.bas.api.FxApi;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER_ACCS_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;

public class SelectRefreshTokenDfo implements FxDfo<SessionVo, String> {

	@Override
	public String call(FxFact fact, SessionVo session) throws Exception {

		String token = selectRefreshToken(session.getSessionId());

		return token;
	}

	public String selectRefreshToken(String sessionId) throws Exception {

		FX_UR_USER_ACCS_HST data = ClassDaoEx.SelectData(FX_UR_USER_ACCS_HST.class,
				FxApi.makePara("sessionId", sessionId));
		return data == null ? null : data.getRefreshToken();
	}
}
