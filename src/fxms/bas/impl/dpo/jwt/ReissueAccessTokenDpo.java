package fxms.bas.impl.dpo.jwt;

import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.user.LogRefreshTokenDfo;
import fxms.bas.impl.dpo.user.UserGetSessionDfo;

/**
 * Java Web Token(JWT)은 클라이언트 측에서 발급받은 토큰을 서버에서 검증하여 인증하는 방식입니다.<br>
 * Refresh Token은 만료된 Access Token을 갱신하는 데 사용됩니다.<br>
 * 
 * Refresh Token을 사용하는 방법은 다음과 같습니다.<br>
 * 
 * 1. 클라이언트가 인증을 받아 Access Token과 Refresh Token을 발급받습니다.<br>
 * 2. Access Token이 만료되면 클라이언트는 서버에 Refresh Token을 보내 갱신된 Access Token을
 * 요청합니다.<br>
 * 3. 서버는 Refresh Token을 검증하고, 유효한 경우 새로운 Access Token을 발급해 클라이언트에게 응답합니다.<br>
 * 4. 클라이언트는 새로운 Access Token을 사용하여 요청을 다시 보냅니다.<br>
 * 
 * @author subkjh
 *
 */
public class ReissueAccessTokenDpo implements FxDpo<String, SessionVo> {

	public static void main(String[] args) throws Exception {
		String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODE0NDY1OTksInNlc3Npb25JZCI6IkZYTVMxMzYyMDQzNzQwMDAxIn0.WLViTxX3sxNrYmQYAnkV_CvRsEFR2NKlKwqiBU1ht1hwt6sJoHkIBrJaTzbQRMSfXDlfoBa2ZZ6OWcFtN5hOaA";
		FxFact fact = new FxFact();
		ReissueAccessTokenDpo dpo = new ReissueAccessTokenDpo();
		dpo.run(fact, refreshToken);
		System.out.println(FxmsUtil.toJson(fact));
	}

	/**
	 * 사용자ID, 엑세스토큰을 이용하여 토큰을 재발행한다.
	 * 
	 * @param userId
	 * @param hostname
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public SessionVo reissue(String userId, String hostname, String accessToken) throws Exception {

		new ValidateAccessTokenDfo().validateAccessToken(accessToken); // access 토큰 유효성 확인

		Map<String, Object> map = new ReadAccessTokenDfo().read(accessToken); // access 토큰 데이터 읽기

		String sessionId = String.valueOf(map.get("sessionId"));

		String refreshToken = new SelectRefreshTokenDfo().selectRefreshToken(sessionId); // refresh 토큰 가져오기

		return reissue(refreshToken);
	}

	/**
	 * refresh토큰을 이용하여 access토큰을 재발행한다.
	 * 
	 * @param refreshToken
	 * @return
	 * @throws Exception
	 */
	public SessionVo reissue(String refreshToken) throws Exception {

		new ValidateRefreshTokenDfo().validateRefreshToken(refreshToken); // 토큰 유효 확인

		SessionVo session = new UserGetSessionDfo().getSessionWithRefreshToken(refreshToken); // 사용자 정보 조회

		String newRefreshToken = new MakeRefreshTokenDfo().generateRefreshToken(session); // refresh 토큰 생성

		String newAccessToken = new MakeAccessTokenDfo().makeAccessToken(session); // access 토큰 생성

		session.setRefreshToken(newRefreshToken);
		session.setAccessToken(newAccessToken);

		new LogRefreshTokenDfo().log(session); // 이력 기록

		return session;
	}

	@Override
	public SessionVo run(FxFact fact, String refreshToken) throws Exception {

		SessionVo session = reissue(refreshToken);

		return session;
	}

}
