package test.bas.api;

import fxms.bas.api.UserApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.api.UserApiDfo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

public class UserApiTest {
	public static void main(String[] args) {

		UserApi.api = new UserApiDfo();

		FxCfg.DB_CONFIG = "FXMSDB";
		FxCfg.DB_PSVALUE = FxCfg.DB_CONFIG;
		FxCfg.isTest = true;
		Logger.logger.setLevel(LOG_LEVEL.trace);

//		System.out.println(CoApi.getApi().getOpCode("select-my-op-list"));

		try {
			UserApiTest test = new UserApiTest();
			test.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void test() throws Exception {
		UserApi api = UserApi.getApi();
		SessionVo session = api.login("test", "1111", "test", "test");
		System.out.println(FxmsUtil.toJson(session));

		Thread.sleep(3000);
		session = api.reissueJwt(session.getRefreshToken(), session.getAccessToken());
		System.out.println(FxmsUtil.toJson(session));

		Thread.sleep(3000);
		session = api.getSession(session.getAccessToken());
		System.out.println(FxmsUtil.toJson(session));

		Thread.sleep(3000);
		session = api.reissueJwt("test", "test", session.getAccessToken());
		System.out.println(FxmsUtil.toJson(session));

		Thread.sleep(3000);
		System.out.println(api.logout(session.getSessionId(), ACCS_ST_CD.LOGOUT_OK));

	}

}
