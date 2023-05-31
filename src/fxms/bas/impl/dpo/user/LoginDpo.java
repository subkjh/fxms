package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.jwt.MakeAccessTokenDfo;
import fxms.bas.impl.dpo.jwt.MakeRefreshTokenDfo;
import subkjh.bas.co.user.exception.UserNotFoundException;

public class LoginDpo implements FxDpo<Map<String, Object>, SessionVo> {

	public static void main(String[] args) throws Exception {

		LoginDpo dpo = new LoginDpo();
		FxFact fact = new FxFact("userId", "test", "password", "1111", "host", "test", "media", "test2");
		dpo.run(fact, fact);

		SessionVo session = fact.getObject(SessionVo.class, "session");

		System.out.println(FxmsUtil.toJson(session));

	}

	public SessionVo login(String userId, String password, String ipaddr, String media)
			throws UserNotFoundException, Exception {

		SessionVo session = new LoginDfo().login(userId, password, ipaddr, media); // 계정 확인

		session.setRefreshToken(new MakeRefreshTokenDfo().generateRefreshToken(session)); // access token 발급

		session.setAccessToken(new MakeAccessTokenDfo().makeAccessToken(session)); // refresh token 발급

		new LogUserAccessDfo().log(session); // 접속 기록

		return session;
	}

	@Override
	public SessionVo run(FxFact fact, Map<String, Object> data) throws Exception {

		LoginDfo login = new LoginDfo();
		MakeRefreshTokenDfo refreshTokenMaker = new MakeRefreshTokenDfo();
		MakeAccessTokenDfo accessTokenMaker = new MakeAccessTokenDfo();
		LogUserAccessDfo logAccess = new LogUserAccessDfo();

		SessionVo session = login.call(fact, null);
		fact.put("session", session);

		session.setAccessToken(accessTokenMaker.call(fact, session));
		session.setRefreshToken(refreshTokenMaker.call(fact, session));

		logAccess.call(fact, session);

		return session;
	}

}
