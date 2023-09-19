package fxms.bas.impl.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import fxms.bas.api.UserApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.FxResponse.HttpStatusCode;
import fxms.bas.handler.vo.SessionVo;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 로그인 전용 HANDLER<br>
 * 
 * @author subkjh
 *
 */
public class LoginHandler extends BaseHandler {

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}

	@Override
	protected byte[] onProcess(HttpExchange he, InetSocketAddress client, String mapping, String body)
			throws Exception {

		Logger.logger.info("body={}", body);
		
		Map<String, Object> map = FxmsUtil.toMapFromJson(body);
		String hostname = client.getHostName();
		Map<String, Object> datas = new HashMap<String, Object>();
		SessionVo session;

		try {

			if ("reissue".equals(mapping)) {

				Object userId = map.get("userId");
				Object refreshToken = map.get("refreshToken");
				Object accessToken = map.get("accessToken");

				Logger.logger.info("update-token userId={}, hostname={}, accessToken={}", userId, hostname,
						accessToken);

				if (userId != null && accessToken != null) {
					session = UserApi.getApi().reissueJwt(userId.toString(), hostname, accessToken.toString());
				} else if (refreshToken != null && accessToken != null) {
					session = UserApi.getApi().reissueJwt(refreshToken.toString(), accessToken.toString());
				} else {
					throw new Exception(Lang.get("Insufficient information to reissue.")); // 재발행 할 정보가 부족합니다.
				}

			} else {

				String userId = getString(map, "userId", "email");
				String userPwd = getString(map, "userPwd", "password");

				// 로그인 확인
				session = UserApi.getApi().login(userId, userPwd, hostname, "ui");
			}

			// 브라우저를 통해서 처리하기 위해 넣어줌
			he.getResponseHeaders().add("Set-Cookie", "FXMS-TOKEN=" + session.getAccessToken());
			he.getResponseHeaders().add("Authorization", "Bearer " + session.getAccessToken());
			datas.put("jwt", session.getAccessToken());
			datas.put("grantType", "bearer");
			datas.put("accessToken", session.getAccessToken());
			datas.put("refreshToken", session.getRefreshToken());
			datas.put("accessTokenExpiresIn", session.getAccessTokenExpiresIn());

			return getResult(HttpStatusCode.OK, null, datas);
			
		} catch (UserNotFoundException e) {
			Logger.logger.error(e);
			return getResult(HttpStatusCode.Unauthorized, e.getMessage(), null);
		} catch (NotFoundException e) {
			Logger.logger.error(e);
			return getResult(HttpStatusCode.Forbidden, e.getMessage(), null);
		} catch (Exception e) {
			Logger.logger.error(e);
			return getResult(HttpStatusCode.InternalServerError, e.getMessage(), null);
		}

	}
}
